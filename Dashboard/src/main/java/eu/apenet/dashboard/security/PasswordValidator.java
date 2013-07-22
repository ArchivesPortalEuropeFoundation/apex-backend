package eu.apenet.dashboard.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.apenet.dashboard.security.PasswordValidator.ValidationResult.ValidationResultType;

/**
 * Validate according to the rules of windows passwords
 * @author bastiaan
 *
 */
public final class PasswordValidator {
	private static final Logger LOGGER = Logger.getLogger(PasswordValidator.class);
	private static final Pattern DIGIT_PATTERN = Pattern.compile("\\p{Digit}");
	private static final Pattern LOWER_CASE_PATTERN = Pattern.compile("\\p{Ll}");
	private static final Pattern UPPER_CASE_PATTERN = Pattern.compile("\\p{Lu}");
	//~!@#$%^&*_-+=`|\(){}[]:;"'<>,.?/
	private static final Pattern SPECIAL_CHARS_PATTERN = Pattern.compile("[~!@#$%\\^&\\*_-]|[+=`\\|\\\\()\\[\\]{}:;\"'<>,.?/]");
	/**
	 * Validates the password
	 * @param password
	 * @return
	 */
	public static ValidationResult validate(String password){
		List<ValidationResultType> requirementsPassed = new ArrayList<ValidationResultType>();

		if (StringUtils.isNotBlank(password)){
			String passwordTemp = password.trim();
			/*
			 * requirement password length of minimum of 7
			 */
			if (passwordTemp.length() >= 7){
				requirementsPassed.add(ValidationResultType.LONG_ENOUGH);
				
			}
			/*
			 * requirement of digits
			 */
			if (DIGIT_PATTERN.matcher(password).find()){
				requirementsPassed.add(ValidationResultType.CONTAIN_DIGITS);
			}
			/*
			 * requirement of lowercase
			 */
			if (LOWER_CASE_PATTERN.matcher(password).find()){
				requirementsPassed.add(ValidationResultType.LOWER_CASE);
			}
			/*
			 * requirement of uppercase
			 */
			if (UPPER_CASE_PATTERN.matcher(password).find()){
				requirementsPassed.add(ValidationResultType.UPPER_CASE);
			}
			/*
			 * requirement of special characters
			 */
			if (SPECIAL_CHARS_PATTERN.matcher(password).find()){
				requirementsPassed.add(ValidationResultType.SPECIAL_CHARS);
			}
		}
		return new ValidationResult(requirementsPassed);
	}
	public static class ValidationResult {
		public enum ValidationResultType {
			LONG_ENOUGH("password.tooShort"), CONTAIN_DIGITS("password.nodigits"), LOWER_CASE("password.lowercase"), UPPER_CASE("password.uppercase"), SPECIAL_CHARS("password.specialchars");
			private String errorKey;
			private ValidationResultType(String errorKey){
				this.errorKey = errorKey;
			}
			public String getErrorKey() {
				return errorKey;
			}
			
		}

		private List<ValidationResultType> requirementsPassed;
		private boolean valid;

		protected ValidationResult(List<ValidationResultType> requirementsPassed) {
			this.requirementsPassed = requirementsPassed;
			this.valid = requirementsPassed.size() >= 4 && requirementsPassed.contains(ValidationResultType.LONG_ENOUGH);
		}
		public boolean isTooShort(){
			return !requirementsPassed.contains(ValidationResultType.LONG_ENOUGH);
		}

		public List<ValidationResultType> getRequirementsPassed() {
			return Collections.unmodifiableList(requirementsPassed);
		}

		public boolean isValid() {
			return valid;
		}



	}
}
