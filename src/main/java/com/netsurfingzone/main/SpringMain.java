package com.netsurfingzone.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@ComponentScan(basePackages = "com.netsurfingzone.*")
public class SpringMain {

	@Autowired
    private static JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringMain.class, args);
		jdbcTemplate.execute("CREATE TABLE CN_LOG_ERROR(" + "AccountName VARCHAR(255), Status NUMERIC(3), Message VARCHAR(255), API_Name VARCHAR(255), Created_At VARCHAR(255))");
	}
}
