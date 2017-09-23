package com.cpapijanni.password;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication
public class PasswordApplication {

	public static void main(String[] args) {
		SpringApplication.run(PasswordApplication.class, args);
	}

	@Bean
	public JSONArray jsonArray() throws IOException {
		String content = IOUtils.toString(new ClassPathResource("password-validation-rules.json").getInputStream());
		return new JSONArray(content);
	}

}
