package com.example.usersessiondataworkflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    scanBasePackages = {"com.example.system", "com.example.usersessiondataworkflow"})
public class UserSessionDataWorkflowApplication {
  public static void main(String[] args) {
    SpringApplication.run(UserSessionDataWorkflowApplication.class, args);
  }
}
