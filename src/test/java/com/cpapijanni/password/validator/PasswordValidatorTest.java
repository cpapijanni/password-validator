package com.cpapijanni.password.validator;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.hasItem;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import com.cpapijanni.password.PasswordApplicationTests;
import com.cpapijanni.password.response.ResponseCode;
import com.cpapijanni.password.validator.PasswordValidator;

public class PasswordValidatorTest extends PasswordApplicationTests{

	private PasswordValidator passwordValidator;
	
	@Before
	public void init() {
		passwordValidator = new PasswordValidator();
		try {
			Field field = PasswordValidator.class.getDeclaredField("validationRules");
			field.setAccessible(true);
			field.set(passwordValidator, getJSONArray());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			Assert.fail("Exception while executing the test");
		}
	}
	
	private JSONArray getJSONArray() {
		JSONArray jsonArray = null;
		try {
			String content = IOUtils.toString(new ClassPathResource("password-validation-rules.json").getInputStream());
			jsonArray = new JSONArray(content);
		} catch (JSONException | IOException e) {
			Assert.fail("Exception while executing the test");
		}
		return jsonArray;
	}

	@Test
	public void testContainsOnlyLowercaseLetters() {
		List<String> result = passwordValidator.validate("abcde");
		assertTrue(result.contains(ResponseCode.ALPHANUMERIC_ERROR.getCode()));
	}

	@Test
	public void testContainsUppercaseLetters() {
		List<String> result = passwordValidator.validate("Abcde");
		assertThat(result, hasItem(ResponseCode.ALPHANUMERIC_ERROR.getCode()));
	}

	@Test
	public void testContainsBothLetterAndDigit() {
		List<String> result = passwordValidator.validate("a0");
		assertTrue(result.contains(ResponseCode.LENGTH_ERROR.getCode()));
	}

	@Test
	public void testContainsBothDigitAndLetter() {
		List<String> result = passwordValidator.validate("0a");
		assertTrue(result.contains(ResponseCode.LENGTH_ERROR.getCode()));
	}

	@Test
	public void testContainsOnlyLetters() {
		List<String> result = passwordValidator.validate("a");
		assertThat(result, hasItem(ResponseCode.ALPHANUMERIC_ERROR.getCode()));
	}

	@Test
	public void testContainsOnlyDigits() {
		List<String> result = passwordValidator.validate("0");
		assertThat(result, hasItem(ResponseCode.ALPHANUMERIC_ERROR.getCode()));
	}

	@Test
	public void testSize5orMore() {
		List<String> result = passwordValidator.validate("12345");
		assertTrue(result.contains(ResponseCode.ALPHANUMERIC_ERROR.getCode()));
	}

	@Test
	public void testSizeLessThan5() {
		List<String> result = passwordValidator.validate("1234");
		assertThat(result, hasItem(ResponseCode.ALPHANUMERIC_ERROR.getCode()));
	}

	@Test
	public void testSize12orLess() {
		List<String> result = passwordValidator.validate("123456789112");
		assertFalse(result.contains(ResponseCode.LENGTH_ERROR.getCode()));
	}

	@Test
	public void testSizeMoreThan12() {
		List<String> result = passwordValidator.validate("1234567891123");
		assertThat(result, hasItem(ResponseCode.LENGTH_ERROR.getCode()));
	}

	@Test
	public void testSequenceNotViolated() {
		List<String> result = passwordValidator.validate("abcde12345");
		assertFalse(result.contains(ResponseCode.ALPHANUMERIC_ERROR.getCode()));
	}

	@Test
	public void testSequenceRepeatLetters() {
		List<String> result = passwordValidator.validate("abab");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void testSequenceRepeatSingleLetter() {
		List<String> result = passwordValidator.validate("aa");
		assertTrue(result.contains(ResponseCode.LENGTH_ERROR.getCode()));
	}

	@Test
	public void testSequenceRepeatLettersAndDigits() {
		List<String> result = passwordValidator.validate("ab1ab1");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void testSequenceRepeatLettersNotAtFront() {
		List<String> result = passwordValidator.validate("prefixabab");
		assertThat(result, hasItem(ResponseCode.ALPHANUMERIC_ERROR.getCode()));
	}

	@Test
	public void testSequenceRepeatLettersNotAtBack() {
		List<String> result = passwordValidator.validate("ababpostfix");
		assertThat(result, hasItem(ResponseCode.ALPHANUMERIC_ERROR.getCode()));
	}

	@Test
	public void nullPasswordShouldBeInvalid() {
		List<String> result = passwordValidator.validate(null);
		assertThat(result, hasItem(ResponseCode.LENGTH_ERROR.getCode()));
	}

	@Test
	public void emptyPasswordShouldBeInvalid() {
		List<String> result = passwordValidator.validate("");
		assertThat(result, hasItem(ResponseCode.LENGTH_ERROR.getCode()));
	}

	@Test
	public void whiteSpacePasswordWithLengthOfFiveShouldBeInvalid() {
		List<String> result = passwordValidator.validate("     ");
		assertThat(result, hasItem(ResponseCode.ALPHANUMERIC_ERROR.getCode()));
	}

	@Test
	public void lowercaseMixedCharacterWithLengthOfFourShouldBeInvalid() {
		List<String> result = passwordValidator.validate("123a");
		assertThat(result, hasItem(ResponseCode.LENGTH_ERROR.getCode()));
	}

	@Test
	public void lowercaseMixedCharacterWithLengthOf13ShouldBeInvalid() {
		List<String> result = passwordValidator.validate("123456789abcd");
		assertThat(result, hasItem(ResponseCode.LENGTH_ERROR.getCode()));
	}

	@Test
	public void passwordWithUppercaseShouldBeInvalid() {
		List<String> result = passwordValidator.validate("123aB");
		assertThat(result, hasItem(ResponseCode.ALPHANUMERIC_ERROR.getCode()));
	}

	@Test
	public void passwordWithoutANumberShouldBeInvalid() {
		List<String> result = passwordValidator.validate("abcde");
		assertThat(result, hasItem(ResponseCode.ALPHANUMERIC_ERROR.getCode()));
	}

	@Test
	public void passwordWithoutALetterShouldBeInvalid() {
		List<String> result = passwordValidator.validate("12345");
		assertThat(result, hasItem(ResponseCode.ALPHANUMERIC_ERROR.getCode()));
	}

	@Test
	public void passwordWithNonAlphanumericCharacterAtTheEndShouldBeInvalid() {
		List<String> result = passwordValidator.validate("12345a!");
		assertThat(result, hasItem(ResponseCode.ALPHANUMERIC_ERROR.getCode()));
	}

	@Test
	public void passwordWithNonAlphanumericCharacterAtTheBeginningShouldBeInvalid() {
		List<String> result = passwordValidator.validate("!12345a");
		assertThat(result, hasItem(ResponseCode.ALPHANUMERIC_ERROR.getCode()));
	}

	@Test
	public void passwordWithNonAlphanumericCharacterInTheMiddleShouldBeInvalid() {
		List<String> result = passwordValidator.validate("123!45a");
		assertThat(result, hasItem(ResponseCode.ALPHANUMERIC_ERROR.getCode()));
	}

	@Test
	public void passwordWithThreeConsecutiveLettersShouldBeInvalid() {
		List<String> result = passwordValidator.validate("12aaa345");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void passwordWithThreeConsecutiveLetterAtBeginningShouldBeInvalid() {
		List<String> result = passwordValidator.validate("aaa123");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void passwordWithThreeConsecutiveLetterAtEndShouldBeInvalid() {
		List<String> result = passwordValidator.validate("12345aaa");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void passwordWithThreeConsecutiveNumbersShouldBeInvalid() {
		List<String> result = passwordValidator.validate("12a33345");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void passwordWithFourConsecutiveNumbersShouldBeInvalid() {
		List<String> result = passwordValidator.validate("444412345");
		assertThat(result, hasItem(ResponseCode.ALPHANUMERIC_ERROR.getCode()));
	}

	@Test
	public void passwordWithConsecutivePatternOfLengthTwoShouldBeInvalid() {
		List<String> result = passwordValidator.validate("abab1");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void passwordWithConsecutivePatternOfLengthTwoAtEndShouldBeInvalid() {
		List<String> result = passwordValidator.validate("1abab");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void passwordWithConsecutivePatternOfLengthTwoAtEndOf12CharacterPasswordShouldBeInvalid() {
		List<String> result = passwordValidator.validate("12345678abab");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void passwordWithConsecutivePatternOfLengthThreeShouldBeInvalid() {
		List<String> result = passwordValidator.validate("ab1ab1");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void passwordWithConsecutivePatternOfLengthThreeAtBeginningShouldBeInvalid() {
		List<String> result = passwordValidator.validate("abcabc1234");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void passwordWithConsecutivePatternOfLengthFourShouldBeInvalid() {
		List<String> result = passwordValidator.validate("abc1abc1");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void passwordWithConsecutivePatternOfLengthFourAtBeginningShouldBeInvalid() {
		List<String> result = passwordValidator.validate("abcdabcd1234");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void passwordWithConsecutivePatternOfLengthFourAtEndShouldBeInvalid() {
		List<String> result = passwordValidator.validate("1234abcdabcd");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void passwordWithConsecutivePatternOfLengthFiveAtEndShouldBeInvalid() {
		List<String> result = passwordValidator.validate("zyabcd1abcd1");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void passwordWithConsecutivePatternOfLengthFiveShouldBeInvalid() {
		List<String> result = passwordValidator.validate("abcd1abcd1");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void passwordWithConsecutivePatternOfLengthSixShouldBeInvalid() {
		List<String> result = passwordValidator.validate("abcd12abcd12");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void passwordBananaShouldBeInvalid() {
		List<String> result = passwordValidator.validate("bananas123");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void passwordMississippiShouldBeInvalid() {
		List<String> result = passwordValidator.validate("mississippi8");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void password1310213102aShouldBeInvalid() {
		List<String> result = passwordValidator.validate("1310213102a");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void password124123123qsShouldBeInvalid() {
		List<String> result = passwordValidator.validate("124123123qs");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void password123123qsShouldBeInvalid() {
		List<String> result = passwordValidator.validate("123123qs");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void passwordWithConsecutiveLetterShouldNotBeValid() {
		List<String> result = passwordValidator.validate("12aa345");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void passwordWithConsecutiveLetterAtBeginningShouldNotBeValid() {
		List<String> result = passwordValidator.validate("aa123");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void passwordWithConsecutiveLetterAtEndShouldNotBeValid() {
		List<String> result = passwordValidator.validate("12345aa");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void passwordWithConsecutiveNumberShouldBeValid() {
		List<String> result = passwordValidator.validate("12a3345");
		assertThat(result, hasItem(ResponseCode.SEQUENCE_ERROR.getCode()));
	}

	@Test
	public void lowercaseMixedCharacterPasswordWithLengthOfFiveShouldBeValid() {
		List<String> result = passwordValidator.validate("1234a");
		assertTrue(result.isEmpty());
	}

	@Test
	public void lowercaseMixedCharacterWithLengthOf12ShouldBeValid() {
		List<String> result = passwordValidator.validate("123456789abc");
		assertTrue(result.isEmpty());
	}

	@Test
	public void lowercaseMixedCharacterWithMultipleNonConsecutiveAsShouldBeValid() {
		List<String> result = passwordValidator.validate("a2a4a6a8a9ba");
		assertTrue(result.isEmpty());
	}

	@Test
	public void lowercaseMixedCharacterWithMultipleNonConsecutiveTwosShouldBeValid() {
		List<String> result = passwordValidator.validate("a2b2c2d2e2fg");
		assertTrue(result.isEmpty());
	}

	@Test
	public void lowercaseMixedCharacterWithMultipleNonConsecutiveThreesShouldBeValid() {
		List<String> result = passwordValidator.validate("ab3cd3ef3gh3");
		assertTrue(result.isEmpty());
	}

	@Test
	public void lowercaseMixedCharacterWithMultipleABCsShouldBeValid() {
		List<String> result = passwordValidator.validate("abc3abc4abc5");
		assertTrue(result.isEmpty());
	}

	@Test
	public void password1acabcdghabcShouldBeValid() {
		List<String> result = passwordValidator.validate("1acabcdghabc");
		assertTrue(result.isEmpty());
	}

	@Test
	public void password123qs123ShouldBeValid() {
		List<String> result = passwordValidator.validate("123qs123");
		assertTrue(result.isEmpty());
	}
}
