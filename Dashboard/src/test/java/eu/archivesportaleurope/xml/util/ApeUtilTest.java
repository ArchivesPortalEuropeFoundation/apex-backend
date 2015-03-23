package eu.archivesportaleurope.xml.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import eu.archivesportaleurope.xml.ApeXmlUtil;

public class ApeUtilTest {

	private static final String SEPARATOR = "|";

	@Test
	public void testRemoveUnusedCharacters() {
		Assert.assertNull(ApeXmlUtil.removeUnusedCharacters(null));
		Assert.assertEquals(" ", ApeXmlUtil.removeUnusedCharacters("\t"));
		Assert.assertEquals(" ", ApeXmlUtil.removeUnusedCharacters("\t\t"));
		Assert.assertEquals(" ", ApeXmlUtil.removeUnusedCharacters(" "));
		Assert.assertEquals(" ", ApeXmlUtil.removeUnusedCharacters("  "));
		Assert.assertEquals("", ApeXmlUtil.removeUnusedCharacters("\n"));
		Assert.assertEquals("", ApeXmlUtil.removeUnusedCharacters("\n\n"));
		Assert.assertEquals("", ApeXmlUtil.removeUnusedCharacters("\r"));
		Assert.assertEquals("", ApeXmlUtil.removeUnusedCharacters("\r\r"));
		Assert.assertEquals(" ", ApeXmlUtil.removeUnusedCharacters("\t \n\r"));
	}

	@Test
	public void testConvertToString() {
		Assert.assertNull(ApeXmlUtil.convertToString(null, 0, SEPARATOR));
		List<String> emptyList = new ArrayList<String>();
		Assert.assertNull(ApeXmlUtil.convertToString(emptyList, 0, SEPARATOR));
		Assert.assertNull(ApeXmlUtil.convertToString(emptyList, 1, SEPARATOR));
		List<String> list = new ArrayList<String>();
		list.add("test");
		Assert.assertEquals("test|", ApeXmlUtil.convertToString(list, 0, SEPARATOR));
		Assert.assertNull(ApeXmlUtil.convertToString(list, 1, SEPARATOR));
		list.add("test2");
		list.add("test3");
		Assert.assertEquals("test|test2|test3|", ApeXmlUtil.convertToString(list, 0, SEPARATOR));
		Assert.assertEquals("test test2 test3", ApeXmlUtil.convertToString(list, 0, ApeXmlUtil.WHITE_SPACE));
	}

	@Test
	public void testConvertEmptyStringToNull() {
		Assert.assertNull(ApeXmlUtil.convertEmptyStringToNull(null));
		Assert.assertNull(ApeXmlUtil.convertEmptyStringToNull(""));
		Assert.assertNull(ApeXmlUtil.convertEmptyStringToNull(" "));
		Assert.assertNull(ApeXmlUtil.convertEmptyStringToNull(" \t\n\r"));
		Assert.assertEquals("hi", ApeXmlUtil.convertEmptyStringToNull(" hi "));
		
	}

}
