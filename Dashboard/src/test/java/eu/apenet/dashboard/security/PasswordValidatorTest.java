package eu.apenet.dashboard.security;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import eu.apenet.dashboard.security.PasswordValidator.ValidationResult;
import eu.apenet.dashboard.security.PasswordValidator.ValidationResult.ValidationResultType;

public class PasswordValidatorTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testTooShortPassword() {
		Assert.assertFalse(contains(PasswordValidator.validate("hello"), ValidationResultType.LONG_ENOUGH));
		Assert.assertTrue(contains(PasswordValidator.validate("hellost"), ValidationResultType.LONG_ENOUGH));
	}
	
	@Test
	public void testContainsPassword() {
		Assert.assertFalse(contains(PasswordValidator.validate("hello"), ValidationResultType.CONTAIN_DIGITS));
		Assert.assertTrue(contains(PasswordValidator.validate("hello1"),  ValidationResultType.CONTAIN_DIGITS));
		Assert.assertTrue(contains(PasswordValidator.validate("hellos2"), ValidationResultType.LONG_ENOUGH, ValidationResultType.CONTAIN_DIGITS));
	}
	@Test
	public void testContainsSpecialCharacters() {
		//~!@#$%^&*_-+=`|\(){}[]:;"'<>,.?/
		Assert.assertTrue(contains(PasswordValidator.validate("~"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("!"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("@"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("#"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("$"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("%"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("^"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("&"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("*"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("_"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("-"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("+"), ValidationResultType.SPECIAL_CHARS));
		
		Assert.assertTrue(contains(PasswordValidator.validate("="), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("`"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("|"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("\\"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("("), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate(")"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("{"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("}"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("["), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("]"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate(":"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate(";"), ValidationResultType.SPECIAL_CHARS));
		
		Assert.assertTrue(contains(PasswordValidator.validate("\""), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("'"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("<"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate(">"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate(","), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("."), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("?"), ValidationResultType.SPECIAL_CHARS));
		Assert.assertTrue(contains(PasswordValidator.validate("/"), ValidationResultType.SPECIAL_CHARS));

	}
	private boolean contains(ValidationResult validationResult, ValidationResultType... types){
		boolean contains = true ;
		for (ValidationResultType type: types){
			contains = contains && validationResult.getRequirementsPassed().contains(type);
		}
		return contains;
	}

}
