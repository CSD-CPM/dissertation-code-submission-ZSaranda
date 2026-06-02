package com.example.gosti_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.gosti_backend.repository")
@EntityScan(basePackages = "com.example.gosti_backend.model")
public class GostiBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(GostiBackendApplication.class, args);
	}
}
