package com.flightbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlightSystemApplication {

	public static void main(String[] args) {
		//System.out.println("DB_PASSWORD = " + System.getenv("DB_PASSWORD"));// Checking for db password
		SpringApplication.run(FlightSystemApplication.class, args);

	}

}
