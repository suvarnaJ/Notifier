package com.java.centralizedNotificationBackend.exceptions;

import com.java.centralizedNotificationBackend.payload.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptions {

    @ExceptionHandler(JwtExpiredException.class)
    public ResponseEntity<?> JwtExpiredException(JwtExpiredException ex){
        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> HttpMessageNotReadableException(HttpMessageNotReadableException ex){
        ResponseEntity<?> response;
        if(ex.getMessage().startsWith("Required request body is missing")){
            response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Please enter a valid payload");
            return response;
        }else if(ex.getMessage().startsWith("JSON parse error")){
            response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Please enter a valid payload");
            return response;
        }else{
            response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ex.getMessage());
            return response;
        }
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> MissingServletRequestParameterException(MissingServletRequestParameterException ex){
        ResponseEntity<?> response;
        if(ex.getMessage().startsWith("Required request parameter")) {
            System.out.println(ex.getMessage());
            response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Please enter a valid parameter");
            return response;
        }else{
            response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ex.getMessage());
            return response;
        }
    }
}
