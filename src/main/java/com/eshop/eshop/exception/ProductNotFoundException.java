package com.eshop.eshop.exception;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String exc) {
        super(exc);
    }
}
