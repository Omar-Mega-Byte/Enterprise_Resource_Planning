package com.example.Enterprise_Resource_Planning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EnterpriseResourcePlanningApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnterpriseResourcePlanningApplication.class, args);
	}

}
