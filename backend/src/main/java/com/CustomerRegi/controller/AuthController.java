package com.CustomerRegi.controller;

import com.CustomerRegi.dto.LoginRequestDTO;
import com.CustomerRegi.dto.LoginResponseDTO;
import com.CustomerRegi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer/login")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping
	public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
		String token = authService.login(loginRequestDTO);
		return ResponseEntity.ok(new LoginResponseDTO(token));
	}

}
