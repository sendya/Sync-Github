package com.loacg.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

/**
 * Project: Sync-Github
 * Author: Sendya <18x@loacg.com>
 * Time: 11/22/2016 11:44 PM
 */
@Configuration
@ConditionalOnClass(AppEnv.class)
public class WebInitializer {

    @Autowired
    private AppEnv appEnv;

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        if (appEnv.isProxyEnable()) {
            SocketAddress address = new InetSocketAddress(appEnv.getProxyHost(), appEnv.getProxyPort());
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, address);
            httpRequestFactory.setProxy(proxy);
        }
        return new RestTemplate(httpRequestFactory);
    }
}
