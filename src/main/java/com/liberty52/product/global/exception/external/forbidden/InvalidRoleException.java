package com.liberty52.product.global.exception.external.forbidden;

import com.liberty52.product.global.exception.external.forbidden.NotYourResourceException;

public class InvalidRoleException extends NotYourResourceException {
    public InvalidRoleException(String role) {
        super("Role", role);
    }
}