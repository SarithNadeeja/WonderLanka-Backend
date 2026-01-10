package com.wanderlanka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class WonderlankaApplication {

	public static void main(String[] args) {
		SpringApplication.run(WonderlankaApplication.class, args);
	}

}
