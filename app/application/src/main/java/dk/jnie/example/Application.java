package dk.jnie.example;

import io.micronaut.runtime.Micronaut;

/**
 * Main application entry point for Micronaut.
 * 
 * This demonstrates that multi-module architecture is framework-independent.
 * The same module structure works with Micronaut as it did with Spring Boot.
 */
public class Application {

	public static void main(String[] args) {
		Micronaut.run(Application.class, args);
	}

}