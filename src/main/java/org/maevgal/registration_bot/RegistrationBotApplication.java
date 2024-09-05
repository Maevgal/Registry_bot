package org.maevgal.registration_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@EnableJdbcRepositories(basePackages = "org.maevgal.registration_bot.repository")
public class RegistrationBotApplication {

	public static void main(String[] args) {

		SpringApplication.run(RegistrationBotApplication.class, args);
	}

}
