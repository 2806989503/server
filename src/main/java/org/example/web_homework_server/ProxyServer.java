package org.example.web_homework_server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.example.web_homework_server.config.ProxyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.netty.bootstrap.Bootstrap;
import javax.annotation.PostConstruct;

@Component
public class ProxyServer {

    @Autowired
    private ProxyConfig proxyConfig;

    @PostConstruct
    public void start() throws InterruptedException {
        startServer(proxyConfig.getHostA(), proxyConfig.getPortA(), proxyConfig.getHostB(), proxyConfig.getPortB());
        startServer(proxyConfig.getHostB(), proxyConfig.getPortB(), proxyConfig.getHostA(), proxyConfig.getPortA());
    }

    private void startServer(String listenHost, int listenPort, String forwardHost, int forwardPort) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1); // 限制boss线程数
        EventLoopGroup workerGroup = new NioEventLoopGroup(2); // 限制worker线程数

        try {
            new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new ProxyHandler(forwardHost, forwardPort));
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 100) // 添加连接队列限制
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .bind(listenPort)
                .sync()
                .channel()
                .closeFuture()
                .addListener(future -> {
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                });
        } catch (InterruptedException e) {
            bossGroup.shutdownGracefully(); // 异常时释放资源
            workerGroup.shutdownGracefully();
            throw new RuntimeException(e);
        }
    }

    @ChannelHandler.Sharable
    static class ProxyHandler extends ChannelInboundHandlerAdapter {
        private final String forwardHost;
        private final int forwardPort;

        public ProxyHandler(String forwardHost, int forwardPort) {
            this.forwardHost = forwardHost;
            this.forwardPort = forwardPort;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(ctx.channel().eventLoop())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new ForwardHandler(ctx.channel()));
                    }
                });

            bootstrap.connect(forwardHost, forwardPort)
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        future.channel().attr(AttributeKey.valueOf("sourceChannel")).set(ctx.channel());
                        ctx.channel().attr(AttributeKey.valueOf("targetChannel")).set(future.channel());
                    } else {
                        ctx.close();
                    }
                });
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            Channel target = ctx.channel().attr(AttributeKey.<Channel>valueOf("targetChannel")).get();
            if (target != null) {
                target.close();
            }
            ctx.close();
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            Channel target = ctx.channel().attr(AttributeKey.<Channel>valueOf("targetChannel")).get();
            if (target != null && target.isActive()) {
                target.writeAndFlush(msg);
            }
        }
    }

    static class ForwardHandler extends ChannelInboundHandlerAdapter {
        private final Channel sourceChannel;

        public ForwardHandler(Channel sourceChannel) {
            this.sourceChannel = sourceChannel;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            sourceChannel.writeAndFlush(msg);
        }
    }
}
