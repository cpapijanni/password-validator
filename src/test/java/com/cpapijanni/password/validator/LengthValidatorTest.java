package com.cpapijanni.password.validator;

import org.junit.Assert;
import org.junit.Test;

import com.cpapijanni.password.validator.LengthValidator;

public class LengthValidatorTest {

	@Test
	public void testPasswordwith4Characters() {
		Assert.assertTrue(LengthValidator.validate("1234"));
	}
	
	@Test
	public void testPasswordwith5Characters() {
		Assert.assertFalse(LengthValidator.validate("1234e"));
	}
	
	@Test
	public void testPasswordwith11Characters() {
		Assert.assertFalse(LengthValidator.validate("abcdeabcde1"));
	}
	
	@Test
	public void testPasswordwith12Characters() {
		Assert.assertFalse(LengthValidator.validate("abcdeabcde12"));
	}
	
	@Test
	public void testPasswordwith13Characters() {
		Assert.assertTrue(LengthValidator.validate("abcdeabcde123"));
	}
	
	@Test
	public void testPasswordwithNull() {
		Assert.assertFalse(LengthValidator.validate(null));
	}

}
