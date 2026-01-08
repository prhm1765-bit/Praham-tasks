package com.CustomerRegi.model;

import com.CustomerRegi.enums.AddressType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CustomerAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "address")
	@NotEmpty(message = "address can not be null")
	private String address;

	@Column(name = "addresstype")
	@NotNull(message = "Address type cannpot be null")
	@Enumerated(EnumType.STRING)
	private AddressType addresstype;

	@ManyToOne
	@JoinColumn(name = "cutomer_id", nullable = false)
	@JsonIgnore
	private Customer customer;

}
