package com.java.centralizedNotificationBackend.exceptions;

import com.java.centralizedNotificationBackend.payload.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptions {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> ExpiredJwtException(ExpiredJwtException ex){
        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ex.getMessage());
    }
}
