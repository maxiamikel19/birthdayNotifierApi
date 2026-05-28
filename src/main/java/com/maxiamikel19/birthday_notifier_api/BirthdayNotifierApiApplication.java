package com.maxiamikel19.birthday_notifier_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BirthdayNotifierApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BirthdayNotifierApiApplication.class, args);
	}

}
