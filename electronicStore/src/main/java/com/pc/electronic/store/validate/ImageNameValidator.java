package com.pc.electronic.store.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageNameValidator implements ConstraintValidator<ImageNameValid, String>{
	
	private org.slf4j.Logger logger=LoggerFactory.getLogger(ImageNameValidator.class);

	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		logger.info("Message from isValid: {},",value);
		
		if(value.isBlank()) {
			return false;
		} else {
			return true;
		}
	}
}
