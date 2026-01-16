package com.CustomerRegi.controller;

import com.CustomerRegi.enums.AddressType;
import com.CustomerRegi.enums.Gender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.CustomerRegi.constants.ApiPathConstants.*;

@RestController
@RequestMapping(ENUM)
public class EnumController {

	@GetMapping(GENDERS)
	public Gender[] getGenders() {
		return Gender.values();
	}

	@GetMapping(ADDRESSTYPE)
	public AddressType[] getAddressTypes() {
		return AddressType.values();
	}

}
