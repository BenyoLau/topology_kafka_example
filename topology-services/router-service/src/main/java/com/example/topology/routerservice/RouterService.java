package com.example.topology.routerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.system", "com.example.topology.routerservice"})
public class RouterService {

	public static void main(String[] args) {
		SpringApplication.run(RouterService.class, args);
	}

}
