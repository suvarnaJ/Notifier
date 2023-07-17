package com.netsurfingzone.controller;

import com.google.gson.Gson;
import com.netsurfingzone.dto.ErrorLogs;
import com.netsurfingzone.payload.ErrorResponse;
import com.netsurfingzone.payload.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1.0")
@CrossOrigin("*")
public class MainController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/data/fetchData/errorLogs")
    public ResponseEntity<?> findErrorLogs(@RequestParam(required = true) String to,@RequestParam(required = true) String from){
        ResponseEntity<?> response;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        ArrayList<Date> dates = new ArrayList<Date>();
        ArrayList<String> filterDates = new ArrayList<>();
        Gson gson = new Gson();
        try{
            //jdbcTemplate.execute("CREATE TABLE CN_LOG_ERROR(" + "AccountName VARCHAR(255), Status NUMERIC(3), Message VARCHAR(255), API_Name VARCHAR(255), Created_At datetime default CURRENT_TIMESTAMP)");
            //jdbcTemplate.execute("DROP TABLE CN_LOG_ERROR");
            //jdbcTemplate.execute("DELETE FROM CN_LOG_ERROR");
            if(from.equals("") || to.equals("")){
                response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,"Field can't be null");
            }else {
                Object[] error_logs = jdbcTemplate.queryForList("select * from CN_LOG_ERROR").toArray();
                List<ErrorLogs> newErrorLogs =new ArrayList<>();
                for(int i=0;i<error_logs.length;i++){
                    String s = gson.toJson(error_logs[i]);
                    ErrorLogs gsonErrorLogs = gson.fromJson(s, ErrorLogs.class);
                    newErrorLogs.add(gsonErrorLogs);
                }

                Date toDate = simpleDateFormat.parse(to);
                Date fromDate = simpleDateFormat.parse(from);
                simpleDateFormat.format(toDate);
                simpleDateFormat.format(fromDate);
                for(int i  = 0;i<newErrorLogs.size();i++){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date date = dateFormat.parse(String.valueOf(newErrorLogs.get(i).getCreatedAt()));
                    Date filterDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(simpleDateFormat.format(date));
                    dates.add(filterDate);
                }
                ArrayList<?> filterDate = (ArrayList<?>) dates.stream().filter(date -> date.after(fromDate) && date.before(toDate) || date.equals(fromDate) || date.equals(toDate)).collect(Collectors.toList());
                if(filterDate.size()==0){
                    response = SuccessResponse.successHandler(HttpStatus.OK,false,"Successfully data fetched",filterDate);
                    return response;
                }else {
                    for(int i=0;i<filterDate.size();i++){
                        filterDates.add(i,simpleDateFormat.format(filterDate.get(i)));
                    }
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    newErrorLogs = newErrorLogs.stream().filter(item -> {
                        try {
                            return filterDates.contains(simpleDateFormat.format(dateFormat.parse(String.valueOf(item.getCreatedAt()))));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return false;
                    }).collect(Collectors.toList());
                    response = SuccessResponse.successHandler(HttpStatus.OK, false, "Successfully data fetched",newErrorLogs);
                }
                return response;
            }
        }catch (Exception ex){
            System.out.println(ex);
            response = ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ex.getMessage());
        }
        return response;
    }

    @GetMapping("fetchData/errorLogs1")
    public ResponseEntity<?> findErrorLogs(){
        try{
            jdbcTemplate.execute("CREATE TABLE CN_LOG_ERROR(" + "AccountName VARCHAR(255), Status NUMERIC(3), Message VARCHAR(255), API_Name VARCHAR(255), Created_At VARCHAR(255))");
            //connectingToDB.Execute("DROP TABLE CN_LOG_ERROR");
            //connectingToDB.Execute("DELETE FROM CN_LOG_ERROR");
            List<Map<String,Object>> data = jdbcTemplate.queryForList("select * from CN_LOG_ERROR");
            return SuccessResponse.successHandler(HttpStatus.OK, false, "Successfully operation performed", data);
        }catch (Exception ex){
            return ErrorResponse.errorHandler(HttpStatus.BAD_REQUEST,true,ex.getMessage());
        }
    }

}
