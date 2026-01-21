package com.CustomerRegi.repository;

import com.CustomerRegi.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepo extends JpaRepository<Tenant, Long> {

	Optional<Tenant> findByTenantId(String tenantId);

	Optional<Tenant> findByEmail(String email);

	boolean existsByEmail(String email);

}
