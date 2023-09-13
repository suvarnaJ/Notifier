package com.netsurfingzone.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.security.Security;

@SpringBootApplication
@ComponentScan(basePackages = "com.netsurfingzone.*")
public class SpringMain {
	public static void main(String[] args) {
		Security.setProperty("jdk.tls.disabledAlgorithms","SSLv3, RC4, DES, MD5withRSA, DH keySize < 1024,EC keySize < 224, 3DES_EDE_CBC, anon, NULL");
		SpringApplication.run(SpringMain.class, args);
	}
}
