package com.example.suivi_projet.exceptions;



public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}