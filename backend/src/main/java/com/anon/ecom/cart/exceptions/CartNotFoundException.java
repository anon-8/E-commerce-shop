package com.anon.ecom.cart.exceptions;

public class CartNotFoundException extends RuntimeException{

    public CartNotFoundException(String message) {
        super(message);
    }

    public CartNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
