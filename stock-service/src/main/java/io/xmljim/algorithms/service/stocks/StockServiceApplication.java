package io.xmljim.algorithms.service.stocks;

import io.xmljim.algorithms.service.stocks.client.CPIClient;
import io.xmljim.algorithms.services.entity.cpi.CPIComputedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
@Slf4j
public class StockServiceApplication implements CommandLineRunner {

	@Autowired
	CPIClient client;

	public static void main(String[] args) {
		SpringApplication.run(StockServiceApplication.class, args);



	}

	public void run(String... args) {
		log.info("Starting Stock Service");
		CPIComputedResponse response = client.getInflationMultiplier(2020, 1990);

		log.info("Run Feign Client: {}", response);
		log.info("Run Feign Client Result: {}", response.getResults().get(0).getData());
	}

}
