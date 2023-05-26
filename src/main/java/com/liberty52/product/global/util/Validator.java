package com.liberty52.product.global.util;

import com.liberty52.product.global.constants.RoleConstants;
import com.liberty52.product.global.exception.external.forbidden.InvalidRoleException;

import java.util.Objects;

public class Validator {

    public static void isAdmin(String target) {
        if (isNotEquals(RoleConstants.ADMIN, target)) {
            throw new InvalidRoleException(target);
        }
    }

    private static boolean isNotEquals(Object origin, Object target) {
        return !Objects.equals(origin, target);
    }

    public static boolean areNullOrBlank(String... args) {
        for (String arg : args) {
            if (arg == null || arg.isBlank()) {
                return true;
            }
        }
        return false;
    }
}
