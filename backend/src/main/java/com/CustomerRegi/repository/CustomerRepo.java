package com.CustomerRegi.repository;

import com.CustomerRegi.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Integer> {

	boolean existsByEmailAndIdNot(String email, Integer id);

	Optional<Customer> findByEmail(String email);

}

