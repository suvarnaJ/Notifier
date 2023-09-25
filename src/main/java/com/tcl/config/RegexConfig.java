package com.tcl.config;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexConfig {

    String emailRegex = "^(.+)@(.+)$";
    String accountNameRegex = "^[a-zA-Z\\s]*$";
    String eventNameRegex = "^[a-zA-Z-_\\s]*$";
    String AESRegex = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$";

    public Boolean validateEmail(String email){
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public Boolean validateAccountName(String accountName){
        Pattern pattern = Pattern.compile(accountNameRegex);
        Matcher matcher = pattern.matcher(accountName);
        return matcher.matches();
    }

    public boolean isBase64(String input){
        Pattern pattern = Pattern.compile(AESRegex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public Boolean validateEventName(String eventName){
        Pattern pattern = Pattern.compile(eventNameRegex);
        Matcher matcher = pattern.matcher(eventName);
        return matcher.matches();
    }


}
