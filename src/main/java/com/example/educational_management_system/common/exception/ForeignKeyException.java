package com.example.educational_management_system.common.exception;


public class ForeignKeyException extends RuntimeException{
    public ForeignKeyException(String message) {
        super(message);
    }
}
