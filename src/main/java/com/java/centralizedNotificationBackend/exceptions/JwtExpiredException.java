package com.java.centralizedNotificationBackend.exceptions;

public class JwtExpiredException extends RuntimeException {

    public JwtExpiredException(String message){
        super(message);
    }
}
