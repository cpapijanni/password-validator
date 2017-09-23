package com.cpapijanni.password.validator;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.cpapijanni.password.response.ResponseCode;

@Component
public class PasswordValidator {

	private static final String PASSWORD = "password";
	private static final String EXPRESSION = "expression";
	private static final String MESSAGE = "message";

	@Autowired
	private JSONArray validationRules;

	/**
	 * This method will validate the password based on the set of rules.
	 * 
	 * @param password
	 *            - Password to validate
	 * @return {@link List}
	 */
	public List<String> validate(String password) {
		List<String> errorList = new ArrayList<>();

		if (password != null) {

			for (int i = 0; i < validationRules.length(); i++)
				evaluate(validationRules.getJSONObject(i), password, errorList);

		} else {
			errorList.add(ResponseCode.LENGTH_ERROR.getCode());
		}
		return errorList;
	}

	/**
	 * This method will evaluate the given password using SpEL
	 * 
	 * @param rule
	 * @param password
	 * @param errorList
	 */
	private void evaluate(JSONObject rule, String password, List<String> errorList) {

		EvaluationContext context = new StandardEvaluationContext();
		context.setVariable(PASSWORD, password);

		ExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression(rule.getString(EXPRESSION));

		boolean result = expression.getValue(context, Boolean.class);
		if (result)
			errorList.add(rule.getString(MESSAGE));
	}

}
