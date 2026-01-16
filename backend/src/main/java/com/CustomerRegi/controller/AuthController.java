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

import static com.CustomerRegi.constants.ApiPathConstants.CUSTOMER;
import static com.CustomerRegi.constants.ApiPathConstants.LOGIN;

@RestController
@RequestMapping(CUSTOMER)
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping(LOGIN)
	public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
		String token = authService.login(loginRequestDTO);
		return ResponseEntity.ok(new LoginResponseDTO(token));
	}

}
