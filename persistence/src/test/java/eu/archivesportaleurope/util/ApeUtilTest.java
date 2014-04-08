package eu.archivesportaleurope.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ApeUtilTest {

	private static final String INPUT_ENCODED = "_COLON__ASTERISK__COMP__SLASH__BSLASH__LSQBRKT__RSQBRKT__PLUS__PERCENT__ATCHAR__AMP__DOLLAR__HASH__CFLEX__LRDBRKT__RRDBRKT__EXCLMARK__TILDE_";
	private static final String INPUT = ":*=/\\[]+%@&$#^()!~";

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

		assertEquals(INPUT_ENCODED, ApeUtil.encodeSpecialCharacters(INPUT));
	}

	@Test
	public void testDecodeSpecialCharacters() {
		assertEquals(INPUT, ApeUtil.decodeSpecialCharacters(INPUT_ENCODED) );
	}

}
