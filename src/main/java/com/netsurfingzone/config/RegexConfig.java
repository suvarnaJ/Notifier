package com.netsurfingzone.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexConfig {

    String emailRegex = "^(.+)@(.+)$";
    String accountNameRegex = "^[a-zA-Z\\s]*$";

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

}
