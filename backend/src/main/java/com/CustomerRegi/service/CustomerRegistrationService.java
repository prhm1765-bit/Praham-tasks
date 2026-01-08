package com.CustomerRegi.service;

import com.CustomerRegi.dto.CustomerReqDTO;
import com.CustomerRegi.dto.CustomerResDTO;

import java.util.List;

public interface CustomerRegistrationService {

	List<CustomerResDTO> findAll();

	CustomerResDTO getById(int id);

	CustomerResDTO saveOrUpdate(CustomerReqDTO dto);

	void delete(int id);

}
