package com.infy.ekart.customer.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.infy.ekart.customer.dto.CartProductDTO;
import com.infy.ekart.customer.dto.CustomerCartDTO;
import com.infy.ekart.customer.dto.CustomerDTO;
import com.infy.ekart.customer.dto.ProductDTO;
import com.infy.ekart.customer.exception.EKartCustomerException;
import com.infy.ekart.customer.service.CustomerService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

@CrossOrigin
@RestController
@RequestMapping(value = "/customer-api")
@Validated
public class CustomerAPI {

    @Autowired
    private CustomerService customerService;

    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;

    @Autowired
    private Environment environment;

    static Log logger = LogFactory.getLog(CustomerAPI.class);

    @PostMapping(value = "/login")
    public ResponseEntity<CustomerDTO> authenticateCustomer(@Valid @RequestBody CustomerDTO customerDTO)
            throws EKartCustomerException {

        logger.info("CUSTOMER TRYING TO LOGIN, VALIDATING CREDENTIALS. CUSTOMER EMAIL ID: " + customerDTO.getEmailId());
        CustomerDTO customerDTOFromDB = customerService.authenticateCustomer(customerDTO.getEmailId(),
                customerDTO.getPassword());
        logger.info("CUSTOMER LOGIN SUCCESS, CUSTOMER EMAIL : " + customerDTOFromDB.getEmailId());
        return new ResponseEntity<>(customerDTOFromDB, HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody CustomerDTO customerDTO)
            throws EKartCustomerException {

        logger.info("CUSTOMER TRYING TO REGISTER. CUSTOMER EMAIL ID: " + customerDTO.getEmailId());
        String registeredWithEmailID = customerService.registerNewCustomer(customerDTO);
        registeredWithEmailID = environment.getProperty("CustomerAPI.CUSTOMER_REGISTRATION_SUCCESS")
                + registeredWithEmailID;
        return new ResponseEntity<>(registeredWithEmailID, HttpStatus.OK);
    }

    @PutMapping(value = "/customer/{customerEmailId:.+}/address/")
    public ResponseEntity<String> updateShippingAddress(
            @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") String customerEmailId,
            @RequestBody String address) throws EKartCustomerException {

        customerService.updateShippingAddress(customerEmailId, address);
        String modificationSuccessMsg = environment.getProperty("CustomerAPI.UPDATE_ADDRESS_SUCCESS");
        return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);
    }

    @DeleteMapping(value = "/customer/{customerEmailId:.+}")
    public ResponseEntity<String> deleteShippingAddress(
            @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId)
            throws EKartCustomerException {

        customerService.deleteShippingAddress(customerEmailId);
        String modificationSuccessMsg = environment.getProperty("CustomerAPI.CUSTOMER_ADDRESS_DELETED_SUCCESS");
        return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);
    }

    @PostMapping(value = "/customercarts/add-product")
    public ResponseEntity<String> addProductToCart(@Valid @RequestBody CustomerCartDTO customerCartDTO)
            throws EKartCustomerException {

        customerService.getCustomerByEmailId(customerCartDTO.getCustomerEmailId());

        for (CartProductDTO cartProductDTO : customerCartDTO.getCartProducts()) {
            String productUrl = "http://ProductMS/Ekart/product-api/product/" + cartProductDTO.getProduct().getProductId();
            ResponseEntity<ProductDTO> productResponse = restTemplate.getForEntity(productUrl, ProductDTO.class);

            if (!productResponse.getStatusCode().is2xxSuccessful()) {
                throw new EKartCustomerException("ProductAPI.PRODUCT_NOT_FOUND");
            }
        }

        String cartUrl = "http://CartMS/Ekart/customercart-api/products";
        ResponseEntity<String> productAddedToCartMessage = restTemplate.postForEntity(cartUrl, customerCartDTO, String.class);

        return productAddedToCartMessage;
    }
}
