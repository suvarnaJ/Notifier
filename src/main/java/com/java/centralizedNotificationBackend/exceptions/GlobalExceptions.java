package com.java.centralizedNotificationBackend.exceptions;

import com.java.centralizedNotificationBackend.payload.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
<<<<<<< HEAD
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
=======
>>>>>>> 72b8fa0a0f1bdbda5e0a7e524b62ba09b8ca264b
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptions {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> ExpiredJwtException(ExpiredJwtException ex){
        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ex.getMessage());
    }
<<<<<<< HEAD

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
=======
>>>>>>> 72b8fa0a0f1bdbda5e0a7e524b62ba09b8ca264b
}
