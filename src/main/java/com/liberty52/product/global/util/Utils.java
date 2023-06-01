package com.liberty52.product.global.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Utils {

    public static DateTimeFormatter DATE_FORMAT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter DATE_FORMAT_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static LocalDateTime convertUnixToLocalDateTime(long unixTime) {
        return LocalDateTime.ofEpochSecond(unixTime, 0, ZoneOffset.UTC);
    }

    public static class OrderNumberBuilder {
        public static String createOrderNum() {
            Calendar cal = Calendar.getInstance();

            return new StringBuilder()
                    .append(delimGen(cal.get(Calendar.YEAR)))
                    .append(appendZeroOrNot(cal.get(Calendar.MONTH) + 1))
                    .append(appendZeroOrNot(cal.get(Calendar.DATE)))
                    .append(appendZeroOrNot(cal.get(Calendar.HOUR_OF_DAY)))
                    .append(appendZeroOrNot(cal.get(Calendar.MINUTE)))
                    .append(appendZeroOrNot(cal.get(Calendar.SECOND)))
                    .append(appendZeroOrMs(cal.get(Calendar.MILLISECOND)))
                    .toString();
        }

        private static String delimGen(int year) {
            return String.valueOf(year).substring(2, 4);
        }

        private static String appendZeroOrNot(int num) {
            return num < 10 ? "0"+num : String.valueOf(num);
        }

        private static String appendZeroOrMs(int ms) {
            if (ms < 100) {
                return ms < 10 ? "00"+ms : "0"+ms;
            }
            return String.valueOf(ms);
        }
    }
}
