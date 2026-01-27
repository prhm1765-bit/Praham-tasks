package com.CustomerRegi.service;

import com.CustomerRegi.dto.CustomerReqDTO;
import com.CustomerRegi.dto.CustomerResDTO;
import com.CustomerRegi.enums.Role;
import com.CustomerRegi.mapper.CustomerMapper;
import com.CustomerRegi.model.Customer;
import com.CustomerRegi.model.Tenant;
import com.CustomerRegi.repository.CustomerRepo;
import com.CustomerRegi.repository.TenantRepo;
import com.CustomerRegi.tenant.TenantContext;
import com.CustomerRegi.tenant.TenantProvisioningService;
import lombok.RequiredArgsConstructor;
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
	private final TenantService tenantService;

	/**
	 * @param dto is Customer Request DTO
	 * {@inheritDoc}
	 * @return it is returning customer response DTO
	 * */
//	@Override
//	public CustomerResDTO saveOrUpdate(CustomerReqDTO dto) {
//		Customer customer = customerMapper.toEntity(dto);
//		if (customer.getId() == null) {
//			customer.setRole(Role.CUSTOMER);
//			String tenantId = UUID.randomUUID().toString().replace("-", "");
//			// Create tenant database + metadata
//			tenantProvisioningService.registerTenant(tenantId, dto.getCompanyCode());
//			// Assign tenantId to customer
//			customer.setTenantId(tenantId);
//			TenantContext.setTenant(tenantId);
//			try {
//				//To store password in database in hash encoded form
//				if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
//					customer.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
//				}
//				Customer saved = customerRepo.save(customer);
//				return customerMapper.toDTO(saved);
//			} finally {
//				//  IMPORTANT: clear after DB operation
//				TenantContext.clear();
//			}
//		}
//		Customer existingCustomer = customerRepo.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Customer not found.."));
//		boolean emailChanged = !existingCustomer.getEmail().equals(dto.getEmail());
//		String tenantId = existingCustomer.getTenantId();
//		customer.setTenantId(tenantId);
//		Customer saved = customerRepo.save(customer);
//		TenantContext.clear();
//		tenantService.updateTenantFromCustomer(saved, tenantId);
//		CustomerResDTO response = customerMapper.toDTO(saved);
//		response.setReLoginRequired(emailChanged);
//		return response;
//	}

	@Override
	public CustomerResDTO saveOrUpdate(CustomerReqDTO dto) {

		Customer customer = customerMapper.toEntity(dto);

		if (customer.getId() == null) {
			customer.setRole(Role.CUSTOMER);
			String tenantId = UUID.randomUUID().toString().replace("-", "");
<<<<<<< Updated upstream
<<<<<<< Updated upstream
			// Create tenant database + metadata
			tenantProvisioningService.registerTenant(tenantId, dto.getEmail(), dto.getPassword());
			// Assign tenantId to customer
=======
			TenantContext.clear();
			tenantProvisioningService.registerTenant(tenantId, dto.getCompanyCode());
>>>>>>> Stashed changes
=======
			TenantContext.clear();
			tenantProvisioningService.registerTenant(tenantId, dto.getCompanyCode());
>>>>>>> Stashed changes
			customer.setTenantId(tenantId);
		}

		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			customer.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
		}
		boolean emailChanged = false;
		if (dto.getId() != null) {
			Customer existingCustomer = customerRepo.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Customer not found.."));
			customer.setTenantId(existingCustomer.getTenantId());
			emailChanged = !existingCustomer.getEmail().equals(dto.getEmail());
		}

		TenantContext.setTenant(customer.getTenantId());
		try {
			Customer saved = customerRepo.save(customer);
			TenantContext.clear();
			tenantService.updateTenantFromCustomer(saved, customer.getTenantId());
			CustomerResDTO response = customerMapper.toDTO(saved);
			response.setReLoginRequired(emailChanged);
			return response;
		} finally {
			TenantContext.clear();
		}
	}


	/**
	 * {@inheritDoc}
	 * @return it is returning list of customer response DTO
	 * */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<CustomerResDTO> findAll() {
		// Global uniqueness by email
		Map<String, CustomerResDTO> uniqueByEmail = new LinkedHashMap<>();
		// MASTER DB
		TenantContext.clear();
		List<Tenant> tenants = tenantRepo.findAll();
		CustomerRegistrationServiceImpl proxy = applicationContext.getBean(CustomerRegistrationServiceImpl.class);
		for (Tenant tenant : tenants) {
			TenantContext.setTenant(tenant.getTenantId());
			List<CustomerResDTO> tenantCustomers = proxy.fetchCustomersFromTenant();
			try {
				for (CustomerResDTO dto : tenantCustomers) {
					// OPTION 2: global uniqueness rule
					uniqueByEmail.putIfAbsent(dto.getEmail(), dto);
				}
			} finally {
				TenantContext.clear();
			}
		}
		return new ArrayList<>(uniqueByEmail.values());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	protected List<CustomerResDTO> fetchCustomersFromTenant() {
			List<Customer> customers = customerRepo.findAll();
			return customerMapper.toDTOList(customers);
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
		Customer customer = customerRepo.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
		String tenantId = customer.getTenantId();
		// Delete customer row (tenant DB)
		customerRepo.delete(customer);
		//  Clear tenant context → switch to MASTER DB
		TenantContext.clear();
		// Delete tenant metadata + database
		tenantProvisioningService.deleteTenantDatabase(tenantId);
	}

}
