package com.flapkap.vendingmachine;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the VendingMachine application.
 *
 * @author Mahmoud Shtayeh
 */
@SpringBootApplication
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VendingMachineApplication {
	/**
	 * The main method serves as the entry point for the VendingMachine application.
	 * It initiates the SpringApplication to start the application.
	 *
	 * @param args command-line arguments passed to the application
	 */
	public static void main(final String[] args) {
		SpringApplication.run(VendingMachineApplication.class, args);
	}
}