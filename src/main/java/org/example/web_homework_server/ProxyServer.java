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
        startServer(proxyConfig.getPortA(), proxyConfig.getPortB());
        startServer(proxyConfig.getPortB(), proxyConfig.getPortA());
    }

    private void startServer(int listenPort, int forwardPort) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new ProxyHandler(forwardPort));
                    }
                })
                .bind(listenPort)
                .sync()
                .channel()
                .closeFuture()
                .addListener(future -> {
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @ChannelHandler.Sharable
    static class ProxyHandler extends ChannelInboundHandlerAdapter {
        private final int forwardPort;

        public ProxyHandler(int forwardPort) {
            this.forwardPort = forwardPort;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            // 创建目标端口连接
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(ctx.channel().eventLoop())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new ForwardHandler(ctx.channel()));
                    }
                });

            bootstrap.connect("localhost", forwardPort)
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
