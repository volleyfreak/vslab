package com.example.categorymanagement.exception;

public class CategoryNotFoundException extends RuntimeException{

    public CategoryNotFoundException(int id) {
        super("Could not find category " + id);
    }
}
