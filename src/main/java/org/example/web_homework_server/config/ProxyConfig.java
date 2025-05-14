package org.example.web_homework_server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProxyConfig {
    @Value("${proxy.portA}")
    private int portA;

    @Value("${proxy.portB}")
    private int portB;

    public int getPortA() {
        return portA;
    }

    public int getPortB() {
        return portB;
    }
}