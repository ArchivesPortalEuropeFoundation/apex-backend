package eu.apenet.dashboard.services.ead.xml.stream.mets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;

import eu.apenet.dashboard.services.ead.xml.AbstractParser;
import eu.archivesportaleurope.harvester.util.StreamUtil;
import eu.archivesportaleurope.xml.ApeXMLConstants;

public class IntegrateMETSParser extends AbstractParser {

	private static Logger LOGGER = Logger.getLogger(IntegrateMETSParser.class);
	protected static final QName DAO_ELEMENT = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "dao");

	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
		integrateMets(new File("/home/bverhoef/eadfiles/1.11.06.11-APE.xml"), new File(
				"/home/bverhoef/eadfiles/1.11.06.11-APE-with-dao.xml"));
		LOGGER.info("Merging done in: " + (System.currentTimeMillis()- startTime));
	}

	public static void integrateMets(File inputFile, File outputFile) throws Exception {
		FileInputStream fileInputStream = new FileInputStream(inputFile);
		XMLStreamReader xmlReader = StreamUtil.getXMLStreamReader(fileInputStream);
		FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
		XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(fileOutputStream,
				ApeXMLConstants.UTF_8);
		boolean isNotDaoWithMetsLink = true;
		MetsHttpClient httpClient = new MetsHttpClient();
		for (int event = xmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
			if (event == XMLStreamConstants.START_ELEMENT) {
				isNotDaoWithMetsLink = integrateDaoWithMets(xmlReader, xmlWriter, httpClient);
				if (isNotDaoWithMetsLink) {
					writeStartElement(xmlReader, xmlWriter);
				}
			} else if (event == XMLStreamConstants.CHARACTERS) {
				if (isNotDaoWithMetsLink) {
					writeCharacters(xmlReader, xmlWriter);
				}
			} else if (event == XMLStreamConstants.CDATA) {
				if (isNotDaoWithMetsLink) {
					writeCData(xmlReader, xmlWriter);
				}
			} else if (event == XMLStreamConstants.END_ELEMENT) {
				if (!DAO_ELEMENT.equals(xmlReader.getName()) || isNotDaoWithMetsLink) {
					writeEndElement(xmlReader, xmlWriter);
				}
				if (!isNotDaoWithMetsLink) {
					isNotDaoWithMetsLink = true;
				}
			}
		}
		httpClient.close();
		xmlWriter.writeEndDocument();
		xmlWriter.flush();
		xmlWriter.close();

	}

	private static boolean integrateDaoWithMets(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter,
			MetsHttpClient httpClient) {
		QName element = xmlReader.getName();
		boolean isDAO = DAO_ELEMENT.equals(element);
		if (isDAO) {
			String role = xmlReader.getAttributeValue(ApeXMLConstants.XLINK_NAMESPACE, "role");
			String href = xmlReader.getAttributeValue(ApeXMLConstants.XLINK_NAMESPACE, "href");
			if ("METS".equalsIgnoreCase(role)) {
				LOGGER.info("Downloading: " + href);
				try {
					CloseableHttpResponse closeableHttpResponse = httpClient.get(href);
					InputStream response = httpClient.getResponseInputStream(closeableHttpResponse);
					LOGGER.info("Merging: " + href);
					List<DaoInfo> daoInfos = METSParser.parse(response);
					for (int i = 0; i < daoInfos.size();i++) {
						DaoInfo daoInfo = daoInfos.get(i);
						xmlWriter.writeStartElement(element.getPrefix(), element.getLocalPart(),
								element.getNamespaceURI());
						if (daoInfo.getReference() != null) {
							xmlWriter.writeAttribute(ApeXMLConstants.XLINK_NAMESPACE, "title", daoInfo.getLabel());
						}
						if (daoInfo.getReference().getRole() != null) {
							xmlWriter.writeAttribute(ApeXMLConstants.XLINK_NAMESPACE, "role", daoInfo.getReference()
									.getRole());
						}
						xmlWriter.writeAttribute(ApeXMLConstants.XLINK_NAMESPACE, "href", daoInfo.getReference()
								.getHref());
						xmlWriter.writeEndElement();
						if (daoInfo.getThumbnail() != null) {
							xmlWriter.writeStartElement(element.getPrefix(), element.getLocalPart(),
									element.getNamespaceURI());
							if (daoInfo.getThumbnail() != null) {
								xmlWriter.writeAttribute(ApeXMLConstants.XLINK_NAMESPACE, "title", "thumbnail");
							}
							xmlWriter.writeAttribute(ApeXMLConstants.XLINK_NAMESPACE, "href", daoInfo.getThumbnail()
									.getHref());
							xmlWriter.writeEndElement();
						}
					}
					closeableHttpResponse.close();
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
		return !isDAO;
	}

//	private static FileInputStream getFileInputStream(String path) throws FileNotFoundException, XMLStreamException {
//		File file = new File(APEnetUtilities.getConfig().getRepoDirPath() + path);
//		return new FileInputStream(file);
//	}
//
//	private static XMLStreamReader getXMLReader(FileInputStream fileInputStream) throws FileNotFoundException,
//			XMLStreamException {
//		XMLInputFactory inputFactory = (XMLInputFactory) XMLInputFactory.newInstance();
//		return (XMLStreamReader) inputFactory.createXMLStreamReader(fileInputStream, ApeXMLConstants.UTF_8);
//	}

}
