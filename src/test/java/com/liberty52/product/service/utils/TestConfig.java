package com.liberty52.product.service.utils;

import com.liberty52.product.global.adapter.AuthClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
public class TestConfig {


    @Bean
    public AuthClient authClient(){
        return new MockAuthClient();
    }

}
