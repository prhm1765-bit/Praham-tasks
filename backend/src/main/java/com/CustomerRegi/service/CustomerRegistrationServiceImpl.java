package com.CustomerRegi.service;

import com.CustomerRegi.dto.CustomerReqDTO;
import com.CustomerRegi.dto.CustomerResDTO;
import com.CustomerRegi.dto.TenantResDTO;
import com.CustomerRegi.enums.Role;
import com.CustomerRegi.mapper.CustomerMapper;
import com.CustomerRegi.mapper.TenantMapper;
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
	private final TenantMapper tenantMapper;
	private final TenantService tenantService;
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
			TenantContext.clear();
			tenantProvisioningService.registerTenant(tenantId, dto.getCompanyCode(), dto.getEmail(), dto.getMobilenumber());
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

//	/**
//	 * @param dto is Customer Request DTO
//	 * {@inheritDoc}
//	 * @return it is returning customer response DTO
//	 * */
//	@Override
//	public CustomerResDTO saveOrUpdate(CustomerReqDTO dto) {
//
//		Customer customer = customerMapper.toEntity(dto);
//		String tenantId = null;
//
//		if (customer.getId() == null) {
//			customer.setRole(Role.CUSTOMER);
//			tenantId = UUID.randomUUID().toString().replace("-", "");
//			customer.setTenantId(tenantId);
//		}
//
//		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
//			customer.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
//		}
//		boolean emailChanged = false;
//		if (dto.getId() != null) {
//			Customer existingCustomer = customerRepo.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Customer not found.."));
//			customer.setTenantId(existingCustomer.getTenantId());
//			emailChanged = !existingCustomer.getEmail().equals(dto.getEmail());
//		}
//
//		TenantContext.setTenant(customer.getTenantId());
//		try {
//			Customer saved = customerRepo.save(customer);
//			TenantContext.clear();
//			if (tenantId != null) {
//				tenantProvisioningService.registerTenant(tenantId, dto.getCompanyCode(), saved.getEmail(), saved.getMobilenumber());
//			}
//			tenantService.updateTenantFromCustomer(saved, customer.getTenantId());
//			CustomerResDTO response = customerMapper.toDTO(saved);
//			response.setReLoginRequired(emailChanged);
//			return response;
//		} finally {
//			TenantContext.clear();
//		}
//	}

	/**
	 * {@inheritDoc}
	 * @return it is returning list of customer response DTO
	 * */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<CustomerResDTO> findAllCustomer() {
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

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<TenantResDTO> findAll() {
		TenantContext.clear();
		List<Tenant> tenants = tenantRepo.findAll();
		return tenantMapper.toDTOList(tenants);
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
