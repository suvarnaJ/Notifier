package com.tcl.exception;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.azure.core.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonParseException;
import com.tcl.config.Constant;
import com.tcl.payload.ErrorResponse;
import com.tcl.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    Date date = new Date();

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {

        String strDate = formatter.format(date);
        logger.info("Request method not supported");
        jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Request method not supported', '" + Constant.API_Name.GLOBAL_ERROR + "', '"+strDate+"')");
        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid method");
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<?> JsonParseException(JsonParseException ex, WebRequest request) {

        String strDate = formatter.format(date);
        logger.info("Invalid payload");
        jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Invalid payload', '" + Constant.API_Name.GLOBAL_ERROR + "', '"+strDate+"')");
        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, "Invalid payload");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> MissingServletRequestParameterException(MissingServletRequestParameterException ex, WebRequest request) {

        String strDate = formatter.format(date);
        logger.info("Required request parameter is not present");
        jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Required request parameter is not present', '" + Constant.API_Name.GLOBAL_ERROR + "', '"+strDate+"')");
        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<?> MissingServletRequestPartException(MissingServletRequestPartException ex, WebRequest request) {

        String strDate = formatter.format(date);
        logger.info("Required request part is not present");
        jdbcTemplate.execute("insert into CN_LOG_ERROR (AccountName, Status, Message, API_Name, Created_At) values ('null', 400, 'Required request part is not present', '" + Constant.API_Name.GLOBAL_ERROR + "', '"+strDate+"')");
        return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST, true, ex.getMessage());
    }
}
