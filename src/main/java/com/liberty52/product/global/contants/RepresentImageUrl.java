package com.liberty52.product.global.contants;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class RepresentImageUrl {

    private final Environment env;
    public static String LIBERTY52_FRAME_REPRESENTATIVE_URL;
    public RepresentImageUrl(Environment env) {
        this.env = env;
        LIBERTY52_FRAME_REPRESENTATIVE_URL = env.getProperty("product.representative-url.liberty52-frame");
    }
}
