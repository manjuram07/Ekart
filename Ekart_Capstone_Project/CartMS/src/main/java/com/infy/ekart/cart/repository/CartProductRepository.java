package com.infy.ekart.cart.repository;



import org.springframework.data.repository.CrudRepository;

import com.infy.ekart.cart.entity.CartProduct;


public interface CartProductRepository extends CrudRepository<CartProduct, Integer> {

}
