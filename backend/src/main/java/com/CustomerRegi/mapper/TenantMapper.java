package com.CustomerRegi.mapper;

import com.CustomerRegi.dto.CustomerResDTO;
import com.CustomerRegi.dto.TenantResDTO;
import com.CustomerRegi.model.Customer;
import com.CustomerRegi.model.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TenantMapper {

	/**
	 * @param customer source object from which data is copied
	 * @param tenant   target tenant object which will be updated
	 *
	 * This method updates tenant fields using customer data.
	 * Tenant id is ignored to avoid accidental overwrite.
	 */
	@Mapping(target = "id", ignore = true)
	void updateTenantFromCustomer(Customer customer, @MappingTarget Tenant tenant);

	List<TenantResDTO> toDTOList(List<Tenant> tenants);
}
