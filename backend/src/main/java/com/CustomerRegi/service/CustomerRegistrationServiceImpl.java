package com.CustomerRegi.service;

import com.CustomerRegi.dto.CustomerReqDTO;
import com.CustomerRegi.dto.CustomerResDTO;
import com.CustomerRegi.enums.Role;
import com.CustomerRegi.exception.EmailAlreadyExistsException;
import com.CustomerRegi.mapper.CustomerMapper;
import com.CustomerRegi.model.Customer;
import com.CustomerRegi.repository.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerRegistrationServiceImpl implements CustomerRegistrationService {

	private final CustomerRepo customerRepo;
	private final CustomerMapper customerMapper;
	private final PasswordEncoder bCryptPasswordEncoder;

	@Override
	@Transactional
	public CustomerResDTO saveOrUpdate(CustomerReqDTO dto) {

		Customer customer = customerMapper.toEntity(dto);

		if (customer.getId() == null) {
			customer.setRole(Role.CUSTOMER);
		}

		//To store password in database in hash encoded form
		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			customer.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
		}
		//To check email exist or not while registering and while updating
		if ( dto.getId() == null && customerRepo.existsByEmailAndIdNot(dto.getEmail(), dto.getId()) ||
			dto.getId() != null && customerRepo.existsByEmailAndIdNot(dto.getEmail(), dto.getId())) {
				throw new EmailAlreadyExistsException("Email already exists");
		}
		Customer saved = customerRepo.save(customer);
		return customerMapper.toDTO(saved);
	}

	@Override
	public List<CustomerResDTO> findAll() {
		List<Customer> customers = customerRepo.findAll();
		List<CustomerResDTO> customerResDTOS = new ArrayList<>();
		for (Customer customer : customers) {
			customerResDTOS.add(customerMapper.toDTO(customer));
		}
		return customerResDTOS;
	}

	@Override
	public CustomerResDTO getById(int id) {
		Customer customer = customerRepo.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
		return customerMapper.toDTO(customer);
	}

	@Override
	public void delete(int id) {
		customerRepo.deleteById(id);
	}

}
