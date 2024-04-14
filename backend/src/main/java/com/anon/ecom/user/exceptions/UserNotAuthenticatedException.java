package com.anon.ecom.user.exceptions;

public class UserNotAuthenticatedException extends RuntimeException {

    public UserNotAuthenticatedException(String message) {
        super(message);
    }

    public UserNotAuthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }
}