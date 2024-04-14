package com.anon.ecom.item.exceptions;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(Long itemId) {
        super("Item not found with ID: " + itemId);
    }
}