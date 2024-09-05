package org.maevgal.registration_bot;

import org.springframework.boot.SpringApplication;

public class TestRegistrationBotApplication {

	public static void main(String[] args) {
		SpringApplication.from(RegistrationBotApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
