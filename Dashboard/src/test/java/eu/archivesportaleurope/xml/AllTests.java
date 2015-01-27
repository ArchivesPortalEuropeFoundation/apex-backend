package eu.archivesportaleurope.xml;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import eu.archivesportaleurope.xml.util.ApeUtilTest;
import eu.archivesportaleurope.xml.xpath.AbstractXpathReaderTest;
import eu.archivesportaleurope.xml.xpath.handler.AttributeXpathHandlerTest;
import eu.archivesportaleurope.xml.xpath.handler.CountXpathHandlerTest;
import eu.archivesportaleurope.xml.xpath.handler.NestedXpathHandlerTest;
import eu.archivesportaleurope.xml.xpath.handler.TextXpathHandlerTest;
import eu.archivesportaleurope.xml.xpath.handler.XpathQueryTest;

@RunWith(Suite.class)
@SuiteClasses({ XmlParserTest.class, AbstractXpathReaderTest.class, XpathQueryTest.class, CountXpathHandlerTest.class, TextXpathHandlerTest.class, AttributeXpathHandlerTest.class, ApeUtilTest.class, NestedXpathHandlerTest.class})
public class AllTests {

}
