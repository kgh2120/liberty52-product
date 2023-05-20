package com.liberty52.product.global.adapter.portone.webclient;

import com.liberty52.product.global.adapter.DallEApiClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DallEApiClientTest {
    @Autowired
    DallEApiClient client;
    @Test
    void generateImage() {
        DallEApiClient.Dto.Response response = client.generateImage("Imagine a beautiful landscape with vibrant colors and serene atmosphere.",
                1,
                DallEApiClient.Dto.Request.Size.S256,
                DallEApiClient.Dto.Request.Format.URL,
                "user");
        assertNotNull(response);
    }

}