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

	@Column(name = "password", updatable = false,length = 100)
	private String password;

	@Column(name = "email", unique = true)
	@Email
	@NotBlank(message = "Email cannot be null")
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false)
	private Role role;

}
