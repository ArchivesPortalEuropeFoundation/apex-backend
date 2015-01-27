package eu.archivesportaleurope.xml;

import javax.xml.namespace.QName;

public class TestConstants {
	public static final String TEST_NAMESPACE = "http://archivesportaleurope.net/schemas/test";
	public static final String XLINK_NAMESPACE="http://www.w3.org/1999/xlink";
	public static final QName EAD = new QName(TestConstants.TEST_NAMESPACE, "ead");
	public static final QName ARCHDESC = new QName(TestConstants.TEST_NAMESPACE, "archdesc");
	public static final QName C = new QName(TestConstants.TEST_NAMESPACE, "c");
	public static final QName DID = new QName(TestConstants.TEST_NAMESPACE, "did");
	public static final QName UNITID = new QName(TestConstants.TEST_NAMESPACE, "unitid");
	public static final QName UNITTITLE = new QName(TestConstants.TEST_NAMESPACE, "unittitle");
	public static final QName OTHER = new QName(TestConstants.TEST_NAMESPACE, "other");
	public static final QName DAO = new QName(TestConstants.TEST_NAMESPACE, "dao");
}
