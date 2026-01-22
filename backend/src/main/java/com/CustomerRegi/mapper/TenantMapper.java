package com.CustomerRegi.mapper;

import com.CustomerRegi.model.Customer;
import com.CustomerRegi.model.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TenantMapper {

	@Mapping(target = "id", ignore = true)
	void updateTenantFromCustomer(Customer customer, @MappingTarget Tenant tenant);

}
