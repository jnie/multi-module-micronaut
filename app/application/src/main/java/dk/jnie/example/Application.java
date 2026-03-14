package dk.jnie.example;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
	info = @Info(
		title = "Multi-Module Micronaut API",
		version = "1.0.0",
		description = "Clean Architecture API demonstrating hexagonal architecture patterns",
		contact = @Contact(name = "API Support"),
		license = @License(name = "Apache 2.0")
	)
)
public class Application {

	public static void main(String[] args) {
		Micronaut.run(Application.class, args);
	}

}