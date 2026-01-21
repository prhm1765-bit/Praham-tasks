package com.CustomerRegi.service;

import com.CustomerRegi.dto.CustomerReqDTO;
import com.CustomerRegi.dto.CustomerResDTO;
import com.CustomerRegi.enums.Role;
import com.CustomerRegi.exception.EmailAlreadyExistsException;
import com.CustomerRegi.mapper.CustomerMapper;
import com.CustomerRegi.model.Customer;
import com.CustomerRegi.model.Tenant;
import com.CustomerRegi.repository.CustomerRepo;
import com.CustomerRegi.repository.TenantRepo;
import com.CustomerRegi.tenant.TenantContext;
import com.CustomerRegi.tenant.TenantProvisioningService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerRegistrationServiceImpl implements CustomerRegistrationService {

	private final CustomerRepo customerRepo;
	private final CustomerMapper customerMapper;
	private final PasswordEncoder bCryptPasswordEncoder;
	private final TenantProvisioningService tenantProvisioningService;
	private final TenantRepo tenantRepo;
	private final ApplicationContext applicationContext;


	/**
	 * @param dto is Customer Request DTO
	 * {@inheritDoc}
	 * @return it is returning customer response DTO
	 * */
	@Override
	public CustomerResDTO saveOrUpdate(CustomerReqDTO dto) {

		Customer customer = customerMapper.toEntity(dto);

		if (customer.getId() == null) {
			customer.setRole(Role.CUSTOMER);

			String tenantId = UUID.randomUUID().toString().replace("-", "");
			// Create tenant database + metadata
			tenantProvisioningService.registerTenant(tenantId, dto.getEmail(), dto.getPassword());
			// Assign tenantId to customer
			customer.setTenantId(tenantId);

			TenantContext.setTenant(tenantId);
			try {
				Customer saved = customerRepo.save(customer);
				return customerMapper.toDTO(saved);
			} finally {
				//  IMPORTANT: clear after DB operation
				TenantContext.clear();
			}
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

	/**
	 * {@inheritDoc}
	 * @return it is returning list of customer response DTO
	 * */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<CustomerResDTO> findAll() {
//		List<Customer> customers = customerRepo.findAll();
//		List<CustomerResDTO> customerResDTOS = customerMapper.toDTOList(customers);
//		return customerResDTOS;


//		List<CustomerResDTO> result = new ArrayList<>();
//		// MASTER DB
//		TenantContext.clear();
//		List<Tenant> tenants = tenantRepo.findAll();
//		CustomerRegistrationServiceImpl proxy = applicationContext.getBean(CustomerRegistrationServiceImpl.class);
//		for (Tenant tenant : tenants) {
//			result.addAll(proxy.fetchCustomersFromTenant(tenant.getTenantId()));
//		}
//		return result;


		// Global uniqueness by email
		Map<String, CustomerResDTO> uniqueByEmail = new LinkedHashMap<>();

		// MASTER DB
		TenantContext.clear();
		List<Tenant> tenants = tenantRepo.findAll();

		CustomerRegistrationServiceImpl proxy =
				applicationContext.getBean(CustomerRegistrationServiceImpl.class);

		for (Tenant tenant : tenants) {
			List<CustomerResDTO> tenantCustomers =
					proxy.fetchCustomersFromTenant(tenant.getTenantId());

			for (CustomerResDTO dto : tenantCustomers) {
				// OPTION 2: global uniqueness rule
				uniqueByEmail.putIfAbsent(dto.getEmail(), dto);
			}
		}

		return new ArrayList<>(uniqueByEmail.values());
	}

	@Transactional(
			propagation = Propagation.REQUIRES_NEW

	)
	protected List<CustomerResDTO> fetchCustomersFromTenant(String tenantId) {

		TenantContext.setTenant(tenantId);
		try {
			List<Customer> customers = customerRepo.findAll();
			return customerMapper.toDTOList(customers);
		} finally {
			TenantContext.clear();
		}
	}


	/**
	 * {@inheritDoc}
	 * @return it is returning customer response DTO
	 * */
	@Override
	@Transactional(readOnly = true)
	public CustomerResDTO getById(int id) {
		Customer customer = customerRepo.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
		return customerMapper.toDTO(customer);
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public void delete(int id) {
		customerRepo.deleteById(id);
	}

}
