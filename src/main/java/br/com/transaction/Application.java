package br.com.transaction;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Transaction", version = "2.0", description = "Order Manager Transaction"))
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


}
