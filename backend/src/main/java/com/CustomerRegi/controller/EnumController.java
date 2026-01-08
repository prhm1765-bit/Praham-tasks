package com.CustomerRegi.controller;

import com.CustomerRegi.enums.AddressType;
import com.CustomerRegi.enums.Gender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnumController {

	@GetMapping("/api/enums/genders")
	public Gender[] getGenders() {
		return Gender.values();
	}

	@GetMapping("/api/enums/address-types")
	public AddressType[] getAddressTypes() {
		return AddressType.values();
	}

}
