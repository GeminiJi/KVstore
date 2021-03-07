package com.dynamo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com")
public class DynamoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DynamoApplication.class, args);
	}

}
