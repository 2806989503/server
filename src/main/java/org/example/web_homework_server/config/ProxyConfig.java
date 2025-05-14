package org.example.web_homework_server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProxyConfig {
    @Value("${proxy.hostA}")
    private String hostA;
    
    @Value("${proxy.hostB}")
    private String hostB;
    
    @Value("${proxy.portA}")
    private int portA;

    @Value("${proxy.portB}")
    private int portB;

    public String getHostA() {
        return hostA;
    }

    public String getHostB() {
        return hostB;
    }

    public int getPortA() {
        return portA;
    }

    public int getPortB() {
        return portB;
    }
}