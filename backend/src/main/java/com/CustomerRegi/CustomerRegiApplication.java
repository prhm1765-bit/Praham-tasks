package com.CustomerRegi;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CustomerRegiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CustomerRegiApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(CustomerRegiApplication.class);
	}

	@Autowired
	private BCryptPasswordEncoder encoder;

	@PostConstruct
	public void printAdminPassword() {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>   "+encoder.encode("Praham123"));
	}


}
