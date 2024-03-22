package com.anon.ecom.exeptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String usernameOrEmail) {
        super("User not found with username or email: " + usernameOrEmail);
    }
}