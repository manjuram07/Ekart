package com.infy.ekart.payment.repository;



import org.springframework.data.repository.CrudRepository;

import com.infy.ekart.payment.entity.Transaction;

public interface TransactionRepository   extends CrudRepository<Transaction, Integer> {

}
