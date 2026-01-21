package com.CustomerRegi.controller;

import com.CustomerRegi.dto.CustomerReqDTO;
import com.CustomerRegi.dto.CustomerResDTO;
import com.CustomerRegi.enums.Role;
import com.CustomerRegi.model.Customer;
import com.CustomerRegi.repository.CustomerRepo;
import com.CustomerRegi.security.SecurityUtils;
import com.CustomerRegi.service.CustomerRegistrationService;
import com.CustomerRegi.validation.OnCreate;
import com.CustomerRegi.validation.OnUpdate;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static com.CustomerRegi.constants.ApiPathConstants.CUSTOMER;
import static com.CustomerRegi.constants.ApiPathConstants.SIGNUP;

@RestController
@AllArgsConstructor
@RequestMapping(CUSTOMER)
public class CustomerRegistrationController {

	private final CustomerRegistrationService customerRegistrationService;
	private final CustomerRepo customerRepo;

	@PostMapping(SIGNUP)
	public ResponseEntity<CustomerResDTO> save(@Validated(OnCreate.class) @RequestBody CustomerReqDTO customerReqDTO) {
		return ResponseEntity.ok(customerRegistrationService.saveOrUpdate(customerReqDTO));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<CustomerResDTO>> getAll() {
		return ResponseEntity.ok(customerRegistrationService.findAll());
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
	@GetMapping("/{id}")
	public ResponseEntity<CustomerResDTO> getById(@PathVariable int id) {
		Customer loggedIn = SecurityUtils.currentCustomer();
		Customer tenantTableCustomer = customerRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Customer not found by id"));

		// Admin can see details of customers
		if (loggedIn.getRole() == Role.ADMIN) {
			return ResponseEntity.ok(customerRegistrationService.getById(id));
		}

		// customer can see only their details
		if (!loggedIn.getEmail().equals(tenantTableCustomer.getEmail())) {
			throw new AccessDeniedException(
					"You are not allowed to view another person's profile."
			);
		}
		return ResponseEntity.ok(customerRegistrationService.getById(id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable int id) {
		Customer loggedIn = SecurityUtils.currentCustomer();
		Customer tenantTableCustomer = customerRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Customer not found by id in delate"));
		//restricting admin to delete admin accounts
		if (loggedIn.getRole() == Role.ADMIN && loggedIn.getId().equals(id)) {
			throw new AccessDeniedException("Admin account cannot be deleted.");
		}

		// Customer can delete only his own account
		if (loggedIn.getRole() == Role.CUSTOMER && !loggedIn.getEmail().equals(tenantTableCustomer.getEmail())) {
			throw new AccessDeniedException("You are not allowed to delete another person's account.");
		}
		customerRegistrationService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping
	public ResponseEntity<CustomerResDTO> edit(@Validated(OnUpdate.class) @RequestBody CustomerReqDTO dto) {
		Customer loggedIn = SecurityUtils.currentCustomer();
		Customer tenantTableCustomer = customerRepo.findById(dto.getId())
				.orElseThrow(() -> new RuntimeException("Customer not found by id in edit"));

		// Admin is not allowed to update customer details
		if (loggedIn.getRole() == Role.ADMIN) {
			throw new AccessDeniedException("Admins are not allowed to edit customer profiles.");
		}

		// Customer can update only his own profile
		if (!loggedIn.getEmail().equals(tenantTableCustomer.getEmail())) {
			throw new AccessDeniedException("You can update only your own profile.");
		}
		return ResponseEntity.ok(customerRegistrationService.saveOrUpdate(dto));
	}

}




