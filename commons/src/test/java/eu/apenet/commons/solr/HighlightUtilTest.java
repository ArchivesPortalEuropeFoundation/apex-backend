package eu.apenet.commons.solr;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.apenet.commons.utils.APEnetUtilities;

public class HighlightUtilTest {

    private static final String HTML_CONTENT = "19de de EEUW van 1815-1950, 10.123";
    private static final String HTML_CONTENT_SPECIAL = "19de de eeuwse provinciën met nog wat.";
    private HighlightUtil highlightUtil;

    @Before
    public void initStopwords() {
        highlightUtil = HighlightUtil.getInstance("classpath:stopwords.txt");
    }

    @Test
    public void testConvertSearchTermToWordsDefault() {
        List<String> words = highlightUtil.convertSearchTermToWords(HTML_CONTENT, HighlightType.DEFAULT);
        Assert.assertEquals(6, words.size());
        Assert.assertEquals("19de", words.get(0));
        Assert.assertEquals("eeuw", words.get(1));
        Assert.assertEquals("1815", words.get(2));
        Assert.assertEquals("1950", words.get(3));
        Assert.assertEquals("10", words.get(4));
        Assert.assertEquals("123", words.get(5));
    }

    @Test
    public void testConvertSearchTermToWordsUnitid() {
        List<String> words = highlightUtil.convertSearchTermToWords(HTML_CONTENT, HighlightType.UNITID);
        Assert.assertEquals(6, words.size());
        Assert.assertEquals("19de", words.get(0));
        Assert.assertEquals("de", words.get(1));
        Assert.assertEquals("eeuw", words.get(2));
        Assert.assertEquals("van", words.get(3));
        Assert.assertEquals("1815-1950,", words.get(4));
        Assert.assertEquals("10.123", words.get(5));
    }

    @Test
    public void testHighlightDefault() {
        Assert.assertEquals(HTML_CONTENT, highlightUtil.highlight("19", HTML_CONTENT, HighlightType.DEFAULT));
        Assert.assertEquals("<em>19de</em> de EEUW van 1815-1950, 10.123", highlightUtil.highlight("19de", HTML_CONTENT, HighlightType.DEFAULT));
        Assert.assertEquals("<em>19de</em> de EEUW van 1815-1950, 10.123", highlightUtil.highlight("19de van", HTML_CONTENT, HighlightType.DEFAULT));
        Assert.assertEquals("<em>19de</em> de EEUW van 1815-1950, 10.123", highlightUtil.highlight("19DE van", HTML_CONTENT, HighlightType.DEFAULT));
        Assert.assertEquals("<em>19de</em> de EEUW van 1815-1950, 10.123", highlightUtil.highlight("19DE van", HTML_CONTENT, HighlightType.DEFAULT));
        Assert.assertEquals("19de de EEUW van <em>1815</em>-1950, 10.<em>123</em>", highlightUtil.highlight("123-1815", HTML_CONTENT, HighlightType.DEFAULT));
    }

    @Test
    public void testHighlightUnitid() {
        Assert.assertEquals(HTML_CONTENT, HighlightUtil.getInstance("classpath:stopwords.txt").highlight("19", HTML_CONTENT, HighlightType.DEFAULT));
        Assert.assertEquals("<em>19de</em> de EEUW van 1815-1950, 10.123", highlightUtil.highlight("19de", HTML_CONTENT, HighlightType.UNITID));
        Assert.assertEquals("<em>19de</em> de EEUW <em>van</em> 1815-1950, 10.123", highlightUtil.highlight("19de van", HTML_CONTENT, HighlightType.UNITID));
        Assert.assertEquals("<em>19de</em> de EEUW <em>van</em> 1815-1950, 10.123", highlightUtil.highlight("19DE van", HTML_CONTENT, HighlightType.UNITID));
        Assert.assertEquals("<em>19de</em> de EEUW <em>van</em> 1815-1950, 10.123", highlightUtil.highlight("19DE van", HTML_CONTENT, HighlightType.UNITID));
        Assert.assertEquals("19de de EEUW van 1815-1950, 10.123", highlightUtil.highlight("123-1815", HTML_CONTENT, HighlightType.UNITID));
        Assert.assertEquals("19de de EEUW van 1815-1950, 10.123", highlightUtil.highlight("1815", HTML_CONTENT, HighlightType.UNITID));
        Assert.assertEquals("19de de EEUW van 1815-1950, <em>10.123</em>", highlightUtil.highlight("10.123", HTML_CONTENT, HighlightType.UNITID));
        Assert.assertEquals("19de de EEUW van <em>1815-1950,</em> <em>10.123</em>", highlightUtil.highlight("1815-1950, 10.123", HTML_CONTENT, HighlightType.UNITID));
    }

    @Test
    public void testHighlightSpecialCharacters() {
        Assert.assertEquals(HTML_CONTENT_SPECIAL, highlightUtil.highlight("19", HTML_CONTENT_SPECIAL, HighlightType.DEFAULT));
        Assert.assertEquals("19de de eeuwse <em>provinciën</em> met nog wat.", highlightUtil.highlight("provinciën", HTML_CONTENT_SPECIAL, HighlightType.DEFAULT));
    }

    @Test
    public void testHighlightStopwordInWord() {
        Assert.assertEquals("Mairie de <em>Clichy</em>-<em>La</em>-<em>Garenne</em>", highlightUtil.highlight("de Clichy-La-Garenne", "Mairie de Clichy-La-Garenne", HighlightType.DEFAULT));
    }

}
