package io.xmljim.algorithms.service.retirement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RetirementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetirementServiceApplication.class, args);
	}

}
