package com.CustomerRegi.model;

import com.CustomerRegi.enums.Gender;
import com.CustomerRegi.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "firstname")
	@NotBlank(message = "First Name can not b null cannot be null")
	private String firstName;

	@Column(name = "lastname")
	@NotBlank(message = "Last Name can not be null")
	private String lastName;

	@Column(name = "mobilenumber")
	@NotBlank(message = "Mobile Number can not be null")
	@Size(min = 10, max = 10, message = "Mobile number must be 10 digits")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
	private String mobilenumber;

	@Column(name = "dob")
	@NotNull(message = "Dob can not be null")
	@Past(message = "Date of birth must be in the past")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate dob;

	@Column(name = "gender")
	@NotNull(message = "Gender can not be null")
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(name = "password", updatable = false,length = 100)
	private String password;

	@Column(name = "email", unique = true)
	@Email
	@NotBlank(message = "Email cannot be null")
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false)
	private Role role;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	@NotEmpty(message = "Address cannot be null")
	private List<CustomerAddress> address;

	//Using JPA lifecycle methods to store proper values which will execute before saving and before updating
	@PrePersist
	@PreUpdate
	private void normalizeAndValidate() {
		//Email trim, lowercase, and no space
		if (this.email != null) {
			this.email = this.email.trim().toLowerCase();

			if (this.email.contains(" ")) {
				throw new IllegalArgumentException(
						"email should not contain spaces."
				);
			}
		}

		//First name trim, lowercase, and no space
		if (this.firstName != null) {
			this.firstName = this.firstName.trim();

			if (this.firstName.contains(" ")) {
				throw new IllegalArgumentException(
						"First name should not contain spaces."
				);
			}
		}

		//Last name trim, lowercase, and no space
		if (this.lastName != null) {
			this.lastName = this.lastName.trim();

			if (this.lastName.contains(" ")) {
				throw new IllegalArgumentException(
						"Last name should not contain spaces."
				);
			}
		}

		//Mobile number trim and no space
		if (this.mobilenumber != null) {
			this.mobilenumber = this.mobilenumber.trim();
		}
		if (this.mobilenumber.contains(" ")) {
			throw new IllegalArgumentException(
					"Mobile number should not contain spaces."
			);
		}
	}

}
