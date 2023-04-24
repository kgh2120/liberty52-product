package com.liberty52.product.global.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Utils {
    public static LocalDateTime convertUnixToLocalDateTime(long unixTime) {
        return LocalDateTime.ofEpochSecond(unixTime, 0, ZoneOffset.UTC);
    }
}
