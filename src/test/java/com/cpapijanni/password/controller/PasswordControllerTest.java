package com.cpapijanni.password.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.cpapijanni.password.PasswordApplicationTests;
import com.cpapijanni.password.response.ResponseCode;
import com.cpapijanni.password.validator.PasswordValidator;

public class PasswordControllerTest extends PasswordApplicationTests{

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@MockBean
	private PasswordValidator passwordValidator;
	
	private MockMvc mockMvc;
	
	@Before
	public void setUp(){
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	public void testValidateWithSuccess(){
		String password = "abc123";
		when(passwordValidator.validate(password)).thenReturn(new ArrayList<>());
		try {
			mockMvc.perform(post("/password/validate").content(password))
			.andExpect(status().isOk())
			.andExpect(content().string(ResponseCode.SUCCESS.getCode()));
		} catch (Exception e) {
			Assert.fail("Exception while executing the test");
		}
	}
	
	@Test
	public void testValidateWithError(){
			List<String> list = new ArrayList<>();
			list.add(ResponseCode.LENGTH_ERROR.getCode());
			when(passwordValidator.validate("ab")).thenReturn(list);
			try {
				mockMvc.perform(post("/password/validate").content("ab"))
				.andExpect(status().isBadRequest());
			} catch (Exception e) {
				Assert.fail("Exception while executing the test");
			}
	}
	
	@Test
	public void testValidateWithoutRequestBody(){
		try {
			mockMvc.perform(post("/password/validate"))
			.andExpect(status().isBadRequest());
		} catch (Exception e) {
			Assert.fail("Exception while executing the test");
		}
	}
}
