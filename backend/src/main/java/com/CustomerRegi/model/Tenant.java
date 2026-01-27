package com.CustomerRegi.model;

import com.CustomerRegi.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

}
