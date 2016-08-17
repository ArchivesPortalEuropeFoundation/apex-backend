package eu.apenet.commons.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * User: Yoann Moranville Date: 3/28/11
 *
 * @author Yoann Moranville
 */
public class APEnetUtilitiesTest {

    @Test
    public void testEncodingString() {
        String stringToTest = "abs23dw/dw";
        Assert.assertEquals("abs23dw%2Fdw", APEnetUtilities.encodeString(stringToTest));

        stringToTest = "abs23dw\"dw";
        Assert.assertEquals("abs23dw%22dw", APEnetUtilities.encodeString(stringToTest));

        stringToTest = "abs23dw\\dw";
        Assert.assertEquals("abs23dw%5Cdw", APEnetUtilities.encodeString(stringToTest));

        stringToTest = "abs23dw%dw";
        Assert.assertEquals("abs23dw%25dw", APEnetUtilities.encodeString(stringToTest));

        stringToTest = "abs23dw&dw";
        Assert.assertEquals("abs23dw%26dw", APEnetUtilities.encodeString(stringToTest));

        stringToTest = "abs23dw:dw";
        Assert.assertEquals("abs23dw%3Adw", APEnetUtilities.encodeString(stringToTest));
    }

}
