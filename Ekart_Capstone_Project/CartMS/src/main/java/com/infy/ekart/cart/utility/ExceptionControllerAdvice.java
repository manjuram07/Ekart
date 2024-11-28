package com.infy.ekart.cart.utility;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import com.infy.ekart.cart.exception.EKartCustomerCartException;


@RestControllerAdvice
public class ExceptionControllerAdvice {
	@Autowired
	Environment environment;

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorInfo> generalExceptionHandler(Exception exception) {
		ErrorInfo error = new ErrorInfo();
		error.setErrorMessage(environment.getProperty("General.EXCEPTION_MESSAGE"));
		error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler({RestClientException.class, HttpClientErrorException.class})
	public ResponseEntity<ErrorInfo> restClientException(RestClientException exception) {
		ErrorInfo error = new ErrorInfo();
		String errorMessage = exception.getMessage();
		if(! errorMessage.equals("Something went wrong, please check the log."))
		{
		errorMessage=errorMessage.substring(errorMessage.indexOf('{')+1, errorMessage.indexOf('}'));
		errorMessage =errorMessage.split(",")[0].split(":")[1];
		errorMessage= errorMessage.substring(errorMessage.indexOf('"')+1, errorMessage.lastIndexOf('"'));

		}
		error.setErrorMessage(errorMessage);
		error.setErrorCode(HttpStatus.BAD_REQUEST.value());
		error.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EKartCustomerCartException.class)
	public ResponseEntity<ErrorInfo> ekartExceptionHandler(EKartCustomerCartException exception) {
		ErrorInfo error = new ErrorInfo();
		error.setErrorMessage(environment.getProperty(exception.getMessage()));
		error.setTimestamp(LocalDateTime.now());
		error.setErrorCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class, ConstraintViolationException.class })
	public ResponseEntity<ErrorInfo> exceptionHandler(Exception exception) {
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		String errorMsg = "";
		if (exception instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException exception1 = (MethodArgumentNotValidException) exception;
			errorMsg = exception1.getBindingResult().getAllErrors().stream().map(x -> x.getDefaultMessage())
					.collect(Collectors.joining(", "));
		} else {
			ConstraintViolationException exception1 = (ConstraintViolationException) exception;
			errorMsg = exception1.getConstraintViolations().stream().map(x -> x.getMessage())
					.collect(Collectors.joining(", "));
		}
		errorInfo.setErrorMessage(errorMsg);
		errorInfo.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}


}
