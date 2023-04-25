package com.java.centralizedNotificationBackend.payload;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class SuccessResponse {

    public static ResponseEntity<?> successHandler(HttpStatus status,boolean error,String message,Object data){
        Map map = new HashMap<String,Object>();
        map.put("status",status);
        map.put("error",error);
        map.put("message",message);
        map.put("data",data);
        return new ResponseEntity<Object>(map,status);
    }
}
