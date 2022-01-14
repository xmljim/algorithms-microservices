package io.xml.algorithms.services.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer

public class ConfigServerApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(ConfigServerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

	@Override
	public void run(final String... args) throws Exception {

	}
}
