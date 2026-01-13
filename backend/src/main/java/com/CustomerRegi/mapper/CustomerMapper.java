package com.CustomerRegi.mapper;

import com.CustomerRegi.dto.CustomerReportDTO;
import com.CustomerRegi.dto.CustomerReqDTO;
import com.CustomerRegi.dto.CustomerResDTO;
import com.CustomerRegi.model.Customer;
import com.CustomerRegi.model.CustomerAddress;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

	Customer toEntity(CustomerReqDTO dto);

	CustomerResDTO toDTO(Customer customer);

	CustomerReportDTO toReportDTO(Customer customer);

	@AfterMapping
	default void linkCustomer(@MappingTarget Customer customer) {
	if (customer.getAddress() != null) {
	for (CustomerAddress address : customer.getAddress()) {
		address.setCustomer(customer);
			}
		}
	}

}
