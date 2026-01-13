package com.CustomerRegi.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CustomerReportDTO {

	private Integer id;
	private String firstName;
	private String lastName;
	private String mobilenumber;
	private String email;
	private String gender;
	private LocalDate dob;

}
