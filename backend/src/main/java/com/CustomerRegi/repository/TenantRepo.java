package com.CustomerRegi.repository;

import com.CustomerRegi.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepo extends JpaRepository<Tenant, Long> {

	Optional<Tenant> findByTenantId(String tenantId);

<<<<<<< Updated upstream
<<<<<<< Updated upstream
	Optional<Tenant> findByEmail(String email);
=======
=======
>>>>>>> Stashed changes
	Optional<Tenant> findByCompanyCode(String companyCode);
>>>>>>> Stashed changes

}
