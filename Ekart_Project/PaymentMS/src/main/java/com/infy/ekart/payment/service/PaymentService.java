package com.infy.ekart.payment.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.infy.ekart.payment.dto.CardDTO;
import com.infy.ekart.payment.dto.TransactionDTO;
import com.infy.ekart.payment.exception.EKartPaymentException;
import com.infy.ekart.payment.exception.PayOrderFallbackException;

public interface PaymentService {

	Integer addCustomerCard(String customerEmailId, CardDTO cardDTO) throws EKartPaymentException, NoSuchAlgorithmException;
	void updateCustomerCard(CardDTO cardDTO) throws EKartPaymentException, NoSuchAlgorithmException;
	void deleteCustomerCard(String customerEmailId, Integer cardId) throws EKartPaymentException;
	CardDTO getCard(Integer cardId) throws EKartPaymentException;
	List<CardDTO> getCustomerCardOfCardType(String customerEmailId , String cardType) throws EKartPaymentException;
	Integer addTransaction (TransactionDTO transactionDTO) throws EKartPaymentException, PayOrderFallbackException ;
	TransactionDTO authenticatePayment(String customerEmailId , TransactionDTO transactionDTO) throws EKartPaymentException , NoSuchAlgorithmException;
}
