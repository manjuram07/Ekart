package com.infy.ekart.payment.exception;

public class PayOrderFallbackException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PayOrderFallbackException(String mssg)
	{
		super(mssg);
	}
}
