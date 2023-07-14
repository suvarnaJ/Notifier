package com.netsurfingzone.exception;

import java.util.Date;

import com.azure.core.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonParseException;
import com.netsurfingzone.payload.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid method");
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<?> JsonParseException(JsonParseException ex, WebRequest request) {
        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid payload");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> MissingServletRequestParameterException(MissingServletRequestParameterException ex, WebRequest request) {
        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<?> MissingServletRequestParameterException(MissingServletRequestPartException ex, WebRequest request) {
        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, ex.getMessage());
    }
}
