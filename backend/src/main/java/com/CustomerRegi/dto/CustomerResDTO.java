package com.CustomerRegi.dto;

import com.CustomerRegi.enums.Gender;
import com.CustomerRegi.model.CustomerAddress;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CustomerResDTO {

	private int id;

	private String firstName;

	private String lastName;

	private String mobilenumber;

	private LocalDate dob;

	private String email;

	private List<CustomerAddress> address;

	private Gender gender;

}
