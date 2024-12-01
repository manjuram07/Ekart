package com.infy.ekart.cart.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="EK_CUSTOMER_CART")
public class CustomerCart {
	
	@Id
	@Column(name="CART_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer cartId;
	
	
	@Column(name="CUSTOMER_EMAIL_ID")
	private String customerEmailId;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name ="cartId")
    private Set<CartProduct> cartProducts;
	
	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public String getCustomerEmailId() {
		return customerEmailId;
	}

	public void setCustomerEmailId(String customerEmailId) {
		this.customerEmailId = customerEmailId;
	}

	public Set<CartProduct> getCartProducts() {
		return cartProducts;
	}

	public void setCartProducts(Set<CartProduct> cartProducts) {
		this.cartProducts = cartProducts;
	}



	

}
