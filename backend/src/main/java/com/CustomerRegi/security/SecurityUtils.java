package com.CustomerRegi.security;

import com.CustomerRegi.model.Customer;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

	private SecurityUtils() {}

	/**
	 * Method to get the data of Currently logged in customer
	 * @return Current logged in customer
	 * */
	public static Customer currentCustomer() {
		Authentication auth = SecurityContextHolder
			.getContext()
			.getAuthentication();

		if (auth == null || !(auth.getPrincipal() instanceof Customer)) {
			throw new AccessDeniedException("Unauthenticated");
		}

		return (Customer) auth.getPrincipal();
	}

}
