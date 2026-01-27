package com.CustomerRegi.service;

import com.CustomerRegi.mapper.TenantMapper;
import com.CustomerRegi.model.Customer;
import com.CustomerRegi.model.Tenant;
import com.CustomerRegi.repository.TenantRepo;
import com.CustomerRegi.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TenantService {

	private final TenantRepo tenantRepo;
	private final TenantMapper tenantMapper;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateTenantFromCustomer(Customer saved, String tenantId) {
		TenantContext.clear(); // MASTER DB only
		Tenant tenant = tenantRepo.findByTenantId(tenantId).orElseThrow(() -> new RuntimeException("Tenant not found"));
		tenantMapper.updateTenantFromCustomer(saved, tenant);
		tenantRepo.save(tenant);
	}
}
