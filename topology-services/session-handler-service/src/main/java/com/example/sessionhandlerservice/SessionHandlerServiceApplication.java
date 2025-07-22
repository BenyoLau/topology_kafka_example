package com.example.sessionhandlerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.system", "com.example.sessionhandlerservice"})
public class SessionHandlerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SessionHandlerServiceApplication.class, args);
    }

}
