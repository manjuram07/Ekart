package com.infy.ekart.customer.service;

import java.util.List;

import com.infy.ekart.customer.dto.OrderDTO;
import com.infy.ekart.customer.dto.OrderStatus;
import com.infy.ekart.customer.dto.PaymentThrough;
import com.infy.ekart.customer.exception.EKartCustomerException;

public interface OrderService {
	
	 Integer placeOrder(OrderDTO orderDTO) throws EKartCustomerException;
	 OrderDTO getOrderDetails (Integer orderId) throws EKartCustomerException;
	 List<OrderDTO> findOrdersByCustomerEmailId(String emailId) throws EKartCustomerException;
	 void updateOrderStatus( Integer orderId , OrderStatus orderStatus) throws EKartCustomerException;
	 void updatePaymentThrough( Integer orderId , PaymentThrough paymentThrough) throws EKartCustomerException;
	 
}
