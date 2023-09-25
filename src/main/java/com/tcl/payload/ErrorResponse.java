package com.tcl.payload;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponse {

    public static ResponseEntity<?> errorHandler(HttpStatus status,boolean error,String message){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("status",status.value());
        map.put("error",error);
        map.put("message",message);
        return new ResponseEntity<Object>(map,status);
    }
}
