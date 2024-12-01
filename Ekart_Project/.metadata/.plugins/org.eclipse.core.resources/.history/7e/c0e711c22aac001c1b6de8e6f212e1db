package com.infy.ekart.customer.api;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.infy.ekart.customer.dto.CartProductDTO;
import com.infy.ekart.customer.dto.OrderDTO;
import com.infy.ekart.customer.dto.OrderStatus;
import com.infy.ekart.customer.dto.OrderedProductDTO;
import com.infy.ekart.customer.dto.PaymentThrough;
import com.infy.ekart.customer.dto.ProductDTO;
import com.infy.ekart.customer.exception.EKartCustomerException;
import com.infy.ekart.customer.service.OrderService;

@CrossOrigin
@RestController
@RequestMapping(value = "/customerorder-api")
@Validated
public class OrderAPI {

	@Autowired
	private OrderService orderService;

	@Autowired
	private Environment environment;

	@Autowired
	private RestTemplate template;

	// This method will receives order details with customerEmailId , dateOfDelivery
	// and paymentThrough
	// Fetch the cart-products of the given customer by calling appropriate CartMS
	// API
	// Remove all the products in the cart for the given customer by calling
	// appropriate CartMS API
	// For each cart-product populate a ordered-product object (finally you will get
	// List<OrderedProductDTO>)
	// Update the ordered-product list of order
	// Save the order details in the DB by calling placeOrder() method of
	// OrderService
	@PostMapping(value = "/place-order")
	public ResponseEntity<String> placeOrder(@Valid @RequestBody OrderDTO order) throws EKartCustomerException {
		//write your logic here

		ResponseEntity<CartProductDTO[]> cartProductDTOsResponse = template.getForEntity(
				"http://localhost:3335/Ekart/customercart-api/customer/" + order.getCustomerEmailId() + "/products",
				CartProductDTO[].class);
		// We are calling the Cart API using hard-coded URI
		// Replace this call with the appropriate MS name
		// CartMS is not an upscaled one (available in 1 number) , still load-balanced
		// rest template should be used here to make the call
		// Don't create and autowire a normal rest template because load balanced
		// template is already in config file

		CartProductDTO[] cartProductDTOs = cartProductDTOsResponse.getBody();
		template.delete(
				"http://localhost:3335/Ekart/customercart-api/customer/" + order.getCustomerEmailId() + "/products");
		// We are calling the Cart API using hard-coded URI
		// Replace this call with the appropriate MS name
		// CartMS is not an upscaled one (available in 1 number) , still load-balanced
		// rest template should be used here to make the call
		// Don't create and autowire a normal rest template because load balanced
		// template is already in config file

		List<OrderedProductDTO> orderedProductDTOs = new ArrayList<>();

		for (CartProductDTO cartProductDTO : cartProductDTOs) {
			OrderedProductDTO orderedProductDTO = new OrderedProductDTO();
			orderedProductDTO.setProduct(cartProductDTO.getProduct());
			orderedProductDTO.setQuantity(cartProductDTO.getQuantity());
			orderedProductDTOs.add(orderedProductDTO);
		}
		order.setOrderedProducts(orderedProductDTOs);

		Integer orderId = orderService.placeOrder(order);
		String modificationSuccessMsg = environment.getProperty("OrderAPI.ORDERED_PLACE_SUCCESSFULLY");

		return new ResponseEntity<>(modificationSuccessMsg + orderId, HttpStatus.CREATED);

	}

	// Fetch the order details (OrderDTO) by calling getOrderDetails() method of
	// OrderService
	// Fetched order details includes
	// orderId,orderDate,discount,orderStatus,List<OrderedProduct>, etc
	// Every OrderedProductDTO object has ProductDTO Object which has
	// only productId
	// Iterate over the List<OrderedProductDTO> , call the appropriate API of
	// ProductMS by passing productId(available in ProductDTO of OrderedProductDTO)
	// to get the Product details
	// Update the product details of OrderedProductDTO with the fetched ProductDTO
	// received in previous step
	// Return the order details
	@GetMapping(value = "order/{orderId}")
	public ResponseEntity<OrderDTO> getOrderDetails(
			@NotNull(message = "{orderId.absent}") @PathVariable Integer orderId) throws EKartCustomerException {
		OrderDTO orderDTO = orderService.getOrderDetails(orderId);
		for (OrderedProductDTO orderedProductDTO : orderDTO.getOrderedProducts()) {

			ResponseEntity<ProductDTO> productResponse = template.getForEntity(
					"http://localhost:3334/Ekart/product-api/product/" + orderedProductDTO.getProduct().getProductId(),
					ProductDTO.class);
			// We are calling the Product API using hard-coded URI
			// Replace this call with the appropriate MS name
			// ProductMS is upscaled (available in 2 numbers). Hence, use load balanced
			// template to make call to the Product API

			orderedProductDTO.setProduct(productResponse.getBody());

		}

		return new ResponseEntity<>(orderDTO, HttpStatus.OK);

	}

	// Fetch all the orders (List<OrderDTO>) placed by the customer by calling
	// findOrdersByCustomerEmailId() method of OrderService
	// Every fetched order details will includes
	// orderId,orderDate,discount,orderStatus,List<OrderedProduct>, etc
	// Every OrderedProductDTO object will have ProductDTO Object which in turns
	// have only productId
	// Iterate over the List<OrderedProductDTO> , call the appropriate API of
	// ProductMS by passing productId(available in ProductDTO of OrderedProductDTO)
	// to get the Product details
	// Update the product details of OrderedProductDTO with the fetched ProductDTO
	// in previous step
	// Return the list of order details
	@GetMapping(value = "customer/{customerEmailId}/orders")
	public ResponseEntity<List<OrderDTO>> getOrdersOfCustomer(
			@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable String customerEmailId)
			throws EKartCustomerException {
		List<OrderDTO> orderDTOs = orderService.findOrdersByCustomerEmailId(customerEmailId);
		for (OrderDTO orderDTO : orderDTOs) {
			for (OrderedProductDTO orderedProductDTO : orderDTO.getOrderedProducts()) {

				ResponseEntity<ProductDTO> productResponse = template
						.getForEntity("http://localhost:3334/Ekart/product-api/product/"
								+ orderedProductDTO.getProduct().getProductId(), ProductDTO.class);
				// We are calling the Product API using hard-coded URI
				// Replace this call with the appropriate MS name
				// ProductMS is upscaled (available in 2 numbers). Hence, use load balanced
				// template to make call to the Product API

				orderedProductDTO.setProduct(productResponse.getBody());

			}

		}
		return new ResponseEntity<>(orderDTOs, HttpStatus.OK);

	}

	@PutMapping(value = "order/{orderId}/update/order-status")
	public void updateOrderAfterPayment(@NotNull(message = "{orderId.absent}") @PathVariable Integer orderId,
			@RequestBody String transactionStatus) throws EKartCustomerException {
		if (transactionStatus.equals("TRANSACTION_SUCCESS")) {
			orderService.updateOrderStatus(orderId, OrderStatus.CONFIRMED);
			OrderDTO orderDTO = orderService.getOrderDetails(orderId);
			for (OrderedProductDTO orderedProductDTO : orderDTO.getOrderedProducts()) {

				template.put("http://localhost:3335/Ekart/product-api/update/"
						+ orderedProductDTO.getProduct().getProductId(), orderedProductDTO.getQuantity());
				// We are calling the Product API using hard-coded URI
				// ProductMS is upscaled (available in 2 numbers). Hence, use load balanced
				// template to make call to the Product API

			}

		} else {
			orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED);
		}
	}

	@PutMapping(value = "order/{orderId}/update/payment-through")
	public void updatePaymentOption(@NotNull(message = "{orderId.absent}") @PathVariable Integer orderId,
			@RequestBody String paymentThrough) throws EKartCustomerException {
		if (paymentThrough.equalsIgnoreCase("DEBIT_CARD")) {
			orderService.updatePaymentThrough(orderId, PaymentThrough.DEBIT_CARD);
		} else {

			orderService.updatePaymentThrough(orderId, PaymentThrough.CREDIT_CARD);
		}
	}

}
