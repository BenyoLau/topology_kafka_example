package com.example.topology;

import org.springframework.boot.SpringApplication;

public class TestTopologyApplication {

	public static void main(String[] args) {
		SpringApplication.from(TopologyApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
