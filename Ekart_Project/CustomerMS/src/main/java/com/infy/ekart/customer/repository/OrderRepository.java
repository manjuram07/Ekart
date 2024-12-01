package com.infy.ekart.customer.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.infy.ekart.customer.entity.Order;

public interface OrderRepository extends CrudRepository<Order, Integer> {

List<Order> findByCustomerEmailId(String customerEmailId);

}
