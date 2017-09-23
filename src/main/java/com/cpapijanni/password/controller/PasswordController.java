package com.cpapijanni.password.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cpapijanni.password.response.ResponseCode;
import com.cpapijanni.password.validator.PasswordValidator;

@RestController
@RequestMapping(value = "/password")
public class PasswordController {

	private static final Logger LOG = LoggerFactory.getLogger(PasswordController.class);

	@Autowired
	private PasswordValidator passwordValidator;

	/**
	 * This method will validate the given password and returns success or error message with appropriate HttpStatus code.
	 * 
	 * @param password
	 * @return
	 */
	@PostMapping(value = "/validate")
	public ResponseEntity<String> validatePassword(@RequestBody String password) {
		LOG.info("Starting the password validaton. ");
		
		ResponseEntity<String> responseEntity = null;
		
		List<String> errorList = passwordValidator.validate(password);
		if(errorList != null && !errorList.isEmpty()) {
			responseEntity = new ResponseEntity<String>(buildErrorMessage(errorList), HttpStatus.BAD_REQUEST);
		}else {
			responseEntity = new ResponseEntity<>(ResponseCode.SUCCESS.getCode(), HttpStatus.OK);
		}
		
		LOG.info("Password validation completed. ");
		return responseEntity;
	}
	
	/**
	 * This method will build the actual error message based given list
	 * 
	 * @param errorList
	 * @return {@link String}
	 */
	private String buildErrorMessage(List<String> errorList) {
		StringBuilder errorMsgBuilder = new StringBuilder();
		for (String errorMsg : errorList) {
			errorMsgBuilder.append(errorMsg);
			errorMsgBuilder.append(" \n");
		}
		String errorMsg = errorMsgBuilder.toString();
		LOG.info("Error Message ----> " + errorMsg);
		return errorMsg;
	}
}
