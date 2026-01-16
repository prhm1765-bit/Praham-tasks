package com.CustomerRegi.service;

import com.CustomerRegi.dto.LoginRequestDTO;
import com.CustomerRegi.model.Customer;
import com.CustomerRegi.repository.CustomerRepo;
import com.CustomerRegi.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthSeriveImpl implements AuthService {

	private final CustomerRepo customerRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	/**
	 * @param loginRequestDTO is Customer LogIn Request DTO
	 * {@inheritDoc}
	 * @return it is returning JWT token as String
	 * */
	public String login(LoginRequestDTO loginRequestDTO) {
		Customer customer = customerRepo
			.findByEmail(loginRequestDTO.getEmail())
			.orElseThrow(() -> new RuntimeException("User not found"));

		if (!passwordEncoder.matches(loginRequestDTO.getPassword(), customer.getPassword())) {
			throw new RuntimeException("Invalid credentials");
		}
		return jwtService.generateToken(customer.getEmail(),  customer.getRole().name(), customer.getId());
	}

	}

