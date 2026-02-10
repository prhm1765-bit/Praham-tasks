package com.CustomerRegi.service;

import com.CustomerRegi.dto.CustomerReqDTO;
import com.CustomerRegi.dto.CustomerResDTO;
import com.CustomerRegi.dto.TenantResDTO;

import java.util.List;

public interface CustomerRegistrationService {

	/**
	 * @return it is returning list of Tenant response DTO
	 * */
	List<TenantResDTO> findAll();

	/**
	 * @return it is returning list of customer response DTO
	 * */
	List<CustomerResDTO> findAllCustomer();

	/**
	 * @return it is returning customer response DTO
	 * */
	CustomerResDTO getById(int id);

	/**
	 * @param dto is Customer Request DTO
	 * @return it is returning customer response DTO
	 * */
	CustomerResDTO saveOrUpdate(CustomerReqDTO dto);

	void delete(int id);

}
