package com.ewsv3.ews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class EwsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EwsApplication.class, args);
	}

}
