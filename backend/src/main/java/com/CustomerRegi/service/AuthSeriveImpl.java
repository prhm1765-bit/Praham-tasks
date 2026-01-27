package com.CustomerRegi.service;

import com.CustomerRegi.dto.LoginRequestDTO;
import com.CustomerRegi.model.Customer;
import com.CustomerRegi.model.Tenant;
import com.CustomerRegi.repository.CustomerRepo;
import com.CustomerRegi.repository.TenantRepo;
import com.CustomerRegi.security.JwtService;
import com.CustomerRegi.tenant.TenantContext;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthSeriveImpl implements AuthService {

	private final CustomerRepo customerRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final TenantRepo tenantRepo;

	/**
	 * @param loginRequestDTO is Customer LogIn Request DTO
	 * {@inheritDoc}
	 * @return it is returning JWT token as String
	 *
	 */
	public String login(LoginRequestDTO loginRequestDTO) {
		Tenant tenant = tenantRepo.findByEmail(loginRequestDTO.getEmail()).orElseThrow(() -> new RuntimeException("User Tenant not found"));
		if (!passwordEncoder.matches(loginRequestDTO.getPassword(), tenant.getPassword())) {
			throw new RuntimeException("Invalid credentials");
		}
		TenantContext.setTenant(tenant.getTenantId());
		try {
			Customer customer = customerRepo.findByEmail(loginRequestDTO.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
<<<<<<< Updated upstream
			return jwtService.generateToken(tenant.getEmail(), tenant.getRole().name(), tenant.getId(), tenant.getTenantId(), customer.getId());
=======
			if (!passwordEncoder.matches(loginRequestDTO.getPassword(), customer.getPassword())) {
				throw new RuntimeException("Invalid credentials");
			}
			return jwtService.generateToken(customer.getEmail(), customer.getRole().name(), tenant.getTenantId(), customer.getId());
>>>>>>> Stashed changes
		} finally {
			TenantContext.clear();
		}
	}

}
