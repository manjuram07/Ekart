package com.infy.ekart.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.ekart.product.dto.ProductDTO;
import com.infy.ekart.product.entity.Product;
import com.infy.ekart.product.exception.EKartProductException;
import com.infy.ekart.product.repository.ProductRepository;

@Service(value = "customerProductService")
@Transactional
public class CustomerProductServiceImpl implements CustomerProductService {
	@Autowired
	private ProductRepository productRepository;

	//This method will retrieve list of all the products from database
	@Override
	public List<ProductDTO> getAllProducts() throws EKartProductException {
		//retrieving list of product from repository
		Iterable<Product> products = productRepository.findAll();
		List<ProductDTO> productDTOs = new ArrayList<>();
		products.forEach(product -> {
			ProductDTO productDTO = new ProductDTO();
			productDTO.setBrand(product.getBrand());
			productDTO.setCategory(product.getCategory());
			productDTO.setDescription(product.getDescription());
			productDTO.setName(product.getName());
			productDTO.setPrice(product.getPrice());
			productDTO.setProductId(product.getProductId());
			productDTO.setAvailableQuantity(product.getAvailableQuantity());
			
			productDTOs.add(productDTO);
		});

		return productDTOs;
	}

	@Override
	public ProductDTO getProductById(Integer productId) throws EKartProductException {
		
		Optional<Product> productOp = productRepository.findById(productId);
		Product product =productOp.orElseThrow(() -> new EKartProductException("ProductService.PRODUCT_NOT_AVAILABLE"));
		
		ProductDTO productDTO = new ProductDTO();
		productDTO.setBrand(product.getBrand());
		productDTO.setCategory(product.getCategory());
		productDTO.setDescription(product.getDescription());
		productDTO.setName(product.getName());
		productDTO.setPrice(product.getPrice());
		productDTO.setProductId(product.getProductId());
		productDTO.setAvailableQuantity(product.getAvailableQuantity());

		return productDTO;
	}
	
	@Override
	public void reduceAvailableQuantity(Integer productId, Integer quantity) throws EKartProductException {
		Optional<Product> productOp = productRepository.findById(productId);
		Product product =productOp.orElseThrow(() -> new EKartProductException("ProductService.PRODUCT_NOT_AVAILABLE"));
		product.setAvailableQuantity(product.getAvailableQuantity()-quantity);
	}
}
