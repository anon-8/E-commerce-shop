package com.anon.ecom.user.exeptions;

public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException() {
        super("User is not authenticated");
    }
}