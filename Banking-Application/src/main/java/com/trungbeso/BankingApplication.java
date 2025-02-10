package com.trungbeso;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
	  info = @Info(
			 title = "Trungbeso's Banking Application",
		    description = "Backend Restfull API projects",
		    version = "SNAPSHOT-1.0.0",
		    contact = @Contact(
					name = "Trungbeso",
			      email = "trungbeso@gmail.com",
			      url = "https://github.com/trungbeso"
		    ),
		    license = @License(
					name = "Trungbeso",
			      url = "https://github.com/trungbeso"
		    )
	  ),
	  externalDocs = @ExternalDocumentation(
			 description = "Java - Bank App Documentation",
		    url = "https://github.com/trungbeso"

	  )
)
public class BankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}

}
