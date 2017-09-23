package com.cpapijanni.password.response;

/**
 * Enum Class Contains all success and error codes
 *
 */
public enum ResponseCode {
	
	ALPHANUMERIC_ERROR("Password must consist of a mixture of lowercase letters and numerical digits only, with at least one of each."),
	SEQUENCE_ERROR("Password must not contain any sequence of characters immediately followed by the same sequence."),
	LENGTH_ERROR("Password must be between 5 and 12 characters in length."),
	SUCCESS("Valid Password.");
	
	private String code;
	
	ResponseCode(String code){
		this.code = code;
	}
	
	public String getCode(){
		return code;
	}
}
