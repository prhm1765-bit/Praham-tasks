package com.CustomerRegi.dto;

import com.CustomerRegi.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TenantResDTO {

	private Long id;
	private String tenantId;
	private String dbName;
	private String status;
	private String companyCode;
	private Role role;
	private String email;
	@Size(min = 10, max = 10, message = "Mobile number must be 10 digits")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
	private String mobilenumber;

}
