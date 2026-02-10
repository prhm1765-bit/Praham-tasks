package com.CustomerRegi.model;

import com.CustomerRegi.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "tenants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String tenantId;

	@Column(nullable = false, unique = true)
	private String dbName;

	private String status;

	@Column(name = "comapny_code", unique = true, updatable = false)
	private String companyCode;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false)
	private Role role;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "mobilenumber")
	@Size(min = 10, max = 10, message = "Mobile number must be 10 digits")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
	private String mobilenumber;

}
