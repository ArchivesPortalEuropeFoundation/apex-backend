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

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import org.xml.sax.SAXParseException;

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.ead.xml.AbstractParser;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.vo.Ead;
import eu.archivesportaleurope.harvester.util.StreamUtil;
import eu.archivesportaleurope.xml.ApeXMLConstants;

public class IntegrateMETSParser extends AbstractParser {

	private static Logger LOGGER = Logger.getLogger(IntegrateMETSParser.class);
	protected static final QName DAO_ELEMENT = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "dao");

	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
		String error = integrateMets(new File("/home/bverhoef/eadfiles/1.11.06.11-with-METS.xml"), new File(
				"/home/bverhoef/eadfiles/1.11.06.11-APE-with-dao.xml"));
		if (error == null){
			LOGGER.info("Merging done in: " + (System.currentTimeMillis() - startTime));
		}else {
			LOGGER.error("Merging failed:\n" +error);
		}
	}

	public static void mergeMETSinEAD(Ead ead) {
		long startTime = System.currentTimeMillis();
		XmlType xmlType = XmlType.getContentType(ead);
		LOGGER.info("Ead " + ead.getEadid() + "(" + xmlType.getName() + "): Start merging METS");
		File inputEadFile = new File(APEnetUtilities.getConfig().getRepoDirPath() + ead.getPath());
		File outputEadFile = new File(inputEadFile.getParentFile(), "TEMP_OUTPUT_" + System.currentTimeMillis() + "_"
				+ inputEadFile.getName());
		String error = integrateMets(inputEadFile, outputEadFile);
		if (error == null){
			LOGGER.info("Merging done in: " + (System.currentTimeMillis() - startTime));
		}else {
			LOGGER.error("Merging failed:\n" +error);
		}
	}

	private static String integrateMets(File inputFile, File outputFile) {
		String error = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(inputFile);
			XMLStreamReader xmlReader = StreamUtil.getXMLStreamReader(fileInputStream);
			FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
			XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(fileOutputStream,
					ApeXMLConstants.UTF_8);
			boolean isMetsDAO = false;
			MetsHttpClient httpClient = new MetsHttpClient();
			for (int event = xmlReader.next(); error == null && event != XMLStreamConstants.END_DOCUMENT; event = xmlReader
					.next()) {
				if (event == XMLStreamConstants.START_ELEMENT) {
					QName element = xmlReader.getName();
					isMetsDAO = DAO_ELEMENT.equals(element)
							&& "METS".equalsIgnoreCase(xmlReader.getAttributeValue(ApeXMLConstants.XLINK_NAMESPACE,
									"role"));
					if (isMetsDAO) {
						error = integrateDaoWithMets(xmlReader, xmlWriter, httpClient,outputFile.getParentFile());
					}
					if (!isMetsDAO) {
						writeStartElement(xmlReader, xmlWriter);
					}
				} else if (event == XMLStreamConstants.CHARACTERS) {
					if (!isMetsDAO) {
						writeCharacters(xmlReader, xmlWriter);
					}
				} else if (event == XMLStreamConstants.CDATA) {
					if (!isMetsDAO) {
						writeCData(xmlReader, xmlWriter);
					}
				} else if (event == XMLStreamConstants.END_ELEMENT) {
					if (!DAO_ELEMENT.equals(xmlReader.getName()) || !isMetsDAO) {
						writeEndElement(xmlReader, xmlWriter);
					}
					if (isMetsDAO) {
						isMetsDAO = false;
					}
				}
			}
			httpClient.close();
			xmlWriter.writeEndDocument();
			xmlWriter.flush();
			xmlWriter.close();
			if (error != null) {
				ContentUtils.deleteFile(outputFile, false);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return error;
	}

	private static String integrateDaoWithMets(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter,
			MetsHttpClient httpClient, File dir ) {
		QName element = xmlReader.getName();
		String href = xmlReader.getAttributeValue(ApeXMLConstants.XLINK_NAMESPACE, "href");
		String filename = "METS_TEMP_FILE_"+System.currentTimeMillis()+".xml";
		File metsFile = new File(dir, filename);		
		try {
			/*
			 * write to file
			 */
			LOGGER.debug("Downloading: " + href);
			CloseableHttpResponse closeableHttpResponse = httpClient.get(href);
			InputStream response = httpClient.getResponseInputStream(closeableHttpResponse);
			FileOutputStream outputStream = new FileOutputStream(metsFile);
			IOUtils.copy(response, outputStream);
			closeableHttpResponse.close();
			LOGGER.debug("Merging: " + href);
			List<DaoInfo> daoInfos = METSParser.parse(metsFile);
			for (int i = 0; i < daoInfos.size(); i++) {
				DaoInfo daoInfo = daoInfos.get(i);
				xmlWriter.writeStartElement(element.getPrefix(), element.getLocalPart(), element.getNamespaceURI());
				if (daoInfo.getReference() != null) {
					xmlWriter.writeAttribute(ApeXMLConstants.XLINK_NAMESPACE, "title", daoInfo.getLabel());
				}
				if (daoInfo.getReference().getRole() != null) {
					xmlWriter.writeAttribute(ApeXMLConstants.XLINK_NAMESPACE, "role", daoInfo.getReference().getRole());
				}
				xmlWriter.writeAttribute(ApeXMLConstants.XLINK_NAMESPACE, "href", daoInfo.getReference().getHref());
				xmlWriter.writeEndElement();
				xmlWriter.writeCharacters("\n");
				if (daoInfo.getThumbnail() != null) {
					xmlWriter.writeStartElement(element.getPrefix(), element.getLocalPart(), element.getNamespaceURI());
					if (daoInfo.getThumbnail() != null) {
						xmlWriter.writeAttribute(ApeXMLConstants.XLINK_NAMESPACE, "title", "thumbnail");
					}
					xmlWriter.writeAttribute(ApeXMLConstants.XLINK_NAMESPACE, "href", daoInfo.getThumbnail().getHref());
					xmlWriter.writeEndElement();
					xmlWriter.writeCharacters("\n");
				}
			}

		} catch (SAXParseException exception) {
			StringBuilder error = new StringBuilder();
			error.append("<span class=\"validation-error\"><b>METS file (" + href+ ") :</b><br/>");
			error.append("l.").append(exception.getLineNumber()).append(" c.")
				.append(exception.getColumnNumber()).append(": ").append(exception.getMessage())
				.append("</span>").append("<br />");			
			return error.toString();
		}catch (Exception e) {
			StringBuilder error = new StringBuilder();
			error.append("<span class=\"validation-error\"><b>METS file (" + href+ ") :</b><br/>");
			error.append(e.getMessage());
			return error.toString();
		}finally {
			metsFile.delete();
		}
		return null;

	}

}
