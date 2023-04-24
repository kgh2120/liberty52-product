package com.liberty52.product.global.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class BeanConfig {

    /**
     * PortOne API를 호출하는 WebClient.
     */
    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(
                        client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000) //miliseconds
                                .doOnConnected(
                                        conn -> conn.addHandlerLast(new ReadTimeoutHandler(5))  //sec
                                                .addHandlerLast(new WriteTimeoutHandler(60)) //sec
                                )
                );

        return WebClient.builder()
                .baseUrl("https://api.iamport.kr")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set(HttpHeaders.CONTENT_TYPE, "application/json");
                    httpHeaders.set(HttpHeaders.ACCEPT_CHARSET, "UTF-8");
                    httpHeaders.set(HttpHeaders.ACCEPT, "*/*");
                })
                .build();
    }

}
