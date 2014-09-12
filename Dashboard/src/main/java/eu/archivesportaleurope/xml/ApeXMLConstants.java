package eu.archivesportaleurope.xml;

import javax.xml.namespace.QName;

public class ApeXMLConstants {
	public static final String UTF_8 = "utf-8";
	public static final String APE_EAD_NAMESPACE = "urn:isbn:1-931666-22-9";
	public static final String APE_EAG_NAMESPACE = "http://www.archivesportaleurope.net/Portal/profiles/eag_2012/";
	public static final String APE_EAC_CPF_NAMESPACE = "urn:isbn:1-931666-33-4";
	public static final String XLINK_NAMESPACE = "http://www.w3.org/1999/xlink";
	public static final String XSI_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String METS_NAMESPACE = "http://www.loc.gov/METS/";
	public static final String XLINK_PREFIX = "xlink";
	public static final String XSI_PREFIX = "xsi";
	public static final String APE_EAD_LOCATION = "http://schemas.archivesportaleurope.net/profiles/apeEAD_XSD1.0.xsd";
	public static final String XLINK_LOCATION = "http://www.loc.gov/standards/xlink/xlink.xsd";
	public static final String SCHEMA_LOCATION = "schemaLocation";


	public static final QName EAD_ELEMENT = new QName(APE_EAD_NAMESPACE, "ead");
	public static final QName C_ELEMENT = new QName(APE_EAD_NAMESPACE, "c");
}
