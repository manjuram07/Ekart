package com.infy.ekart.payment.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

public class OrderDTO {
	@NotNull(message = "{transaction.orderid.notpresent}")
	private Integer orderId;
	private String customerEmailId;
	private LocalDateTime dateOfOrder;
	private Double totalPrice;
	private Double discount;
	private String orderStatus;
	private String paymentThrough;

	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getCustomerEmailId() {
		return customerEmailId;
	}
	public void setCustomerEmailId(String customerEmailId) {
		this.customerEmailId = customerEmailId;
	}
	
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	
	public LocalDateTime getDateOfOrder() {
		return dateOfOrder;
	}
	public void setDateOfOrder(LocalDateTime dateOfOrder) {
		this.dateOfOrder = dateOfOrder;
	}
	
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public String getPaymentThrough() {
		return paymentThrough;
	}
	public void setPaymentThrough(String paymentThrough) {
		this.paymentThrough = paymentThrough;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	


}
