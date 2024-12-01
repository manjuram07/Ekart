package com.infy.ekart.customer.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.infy.ekart.customer.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, String> {

	List<Customer> findByPhoneNumber(String phoneNumber);

}
