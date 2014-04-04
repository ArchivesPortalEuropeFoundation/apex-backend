package eu.archivesportaleurope.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ApeUtilTest {

	@Test
	public void testEncodeRepositoryCode() {
		assertEquals("NA_HI", ApeUtil.encodeRepositoryCode("NA/HI"));
	}

	@Test
	public void testDecodeRepositoryCode() {
		assertEquals("NA/HI", ApeUtil.decodeRepositoryCode("NA_HI"));
	}

	@Test
	public void testEncodeSpecialCharacters() {
		assertEquals("_COLON__COMP__SLASH__BSLASH__PERCENT__ATCHAR__AMP__DOLLAR__LRDBRKT__RRDBRKT__EXCLMARK__TILDE_", ApeUtil.encodeSpecialCharacters(":=/\\%@&$()!~"));
	}

	@Test
	public void testDecodeSpecialCharacters() {
		assertEquals(":=/\\%@&$()!~", ApeUtil.decodeSpecialCharacters("_COLON__COMP__SLASH__BSLASH__PERCENT__ATCHAR__AMP__DOLLAR__LRDBRKT__RRDBRKT__EXCLMARK__TILDE_") );
	}

}
