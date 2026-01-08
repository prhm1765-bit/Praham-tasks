package com.CustomerRegi.repository;

import com.CustomerRegi.model.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerAddressRepo extends JpaRepository<CustomerAddress, Integer> {
}
