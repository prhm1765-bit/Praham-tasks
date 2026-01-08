package com.CustomerRegi.mapper;

import com.CustomerRegi.dto.CustomerReqDTO;
import com.CustomerRegi.dto.CustomerResDTO;
import com.CustomerRegi.model.Customer;
import com.CustomerRegi.model.CustomerAddress;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

	Customer toEntity(CustomerReqDTO dto);

	CustomerResDTO toDTO(Customer customer);

	@AfterMapping
	default void linkCustomer(@MappingTarget Customer customer) {
	if (customer.getAddress() != null) {
	for (CustomerAddress address : customer.getAddress()) {
		address.setCustomer(customer);
			}
		}
	}

}
