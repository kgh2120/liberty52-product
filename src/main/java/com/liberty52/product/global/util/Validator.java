package com.liberty52.product.global.util;

import com.liberty52.product.global.contants.RoleConstants;
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
}
