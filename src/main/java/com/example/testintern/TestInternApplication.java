package com.example.testintern;

import com.example.testintern.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class TestInternApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestInternApplication.class, args);
	}

}
