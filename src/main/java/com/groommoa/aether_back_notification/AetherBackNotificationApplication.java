package com.groommoa.aether_back_notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class AetherBackNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AetherBackNotificationApplication.class, args);
	}

}
