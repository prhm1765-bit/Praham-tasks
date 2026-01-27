package com.CustomerRegi.dto;

import com.CustomerRegi.enums.Gender;
import com.CustomerRegi.model.CustomerAddress;
import com.CustomerRegi.validation.OnCreate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CustomerReqDTO {

	private Integer id;

	@NotBlank(message = "First Name can not b null cannot be null")
	private String firstName;

	@NotBlank(message = "Last Name can not be null")
	private String lastName;

	@NotBlank(message = "Mobile Number can not be null")
	@Size(min = 10, max = 10, message = "Mobile number must be 10 digits")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
	private String mobilenumber;

	@NotNull(message = "Dob can not be null")
	@Past(message = "Date of birth must be in the past")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate dob;

	@NotNull(message = "Gender can not be null")
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@NotBlank(groups = OnCreate.class, message = "password can not be null")
	private String password;

	private String companyCode;

	@Email
	@NotBlank(message = "Email cannot be null")
	private String email;

	@NotEmpty(message = "addresses can not be null")
	@Valid
	private List<CustomerAddress> address;

}
