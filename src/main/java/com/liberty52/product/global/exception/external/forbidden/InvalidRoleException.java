package com.liberty52.product.global.exception.external.forbidden;


public class InvalidRoleException extends NotYourResourceException {
    public InvalidRoleException(String role) {
        super("Role", role);
    }
}
