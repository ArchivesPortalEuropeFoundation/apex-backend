package eu.apenet.dashboard.services.ead.xml.stream.mets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import org.xml.sax.SAXParseException;

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.ead.xml.AbstractParser;
import eu.apenet.persistence.vo.Ead;
import eu.archivesportaleurope.harvester.util.StreamUtil;
import eu.archivesportaleurope.xml.ApeXMLConstants;

public class IntegrateMETSParser extends AbstractParser {

    private static Logger LOGGER = Logger.getLogger(IntegrateMETSParser.class);
    protected static final QName DAO_ELEMENT = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "dao");
    protected static final QName DID_ELEMENT = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "did");

    public static String mergeMETSinEAD(Ead ead) throws Exception {
        XmlType xmlType = XmlType.getContentType(ead);
        LOGGER.info("Ead " + ead.getEadid() + "(" + xmlType.getName() + "): Start merging METS");
        File inputEadFile = new File(APEnetUtilities.getConfig().getRepoDirPath() + ead.getPath());
        File outputEadFile = new File(inputEadFile.getParentFile(), "TEMP_OUTPUT_" + System.currentTimeMillis() + "_"
                + inputEadFile.getName());
        return integrateMets(inputEadFile, outputEadFile);
    }

    private static String integrateMets(File inputFile, File outputFile) throws Exception {
        String error = null;
        MetsHttpClient httpClient = new MetsHttpClient();
        FileInputStream fileInputStream = new FileInputStream(inputFile);
        XMLStreamReader xmlReader = StreamUtil.getXMLStreamReader(fileInputStream);
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(fileOutputStream,
                ApeXMLConstants.UTF_8);
        boolean isMetsDAO = false;
        MetsInfo metsInfo = null;

        for (int event = xmlReader.next(); error == null && event != XMLStreamConstants.END_DOCUMENT; event = xmlReader
                .next()) {
            if (event == XMLStreamConstants.START_ELEMENT) {
                QName element = xmlReader.getName();
                isMetsDAO = DAO_ELEMENT.equals(element)
                        && "METS"
                        .equalsIgnoreCase(xmlReader.getAttributeValue(ApeXMLConstants.XLINK_NAMESPACE, "role"));
                if (isMetsDAO) {
                    try {
                        metsInfo = integrateDaoWithMets(xmlReader, xmlWriter, httpClient, outputFile.getParentFile());
                    } catch (METSException e) {
                        error = e.getMessage();
                    }
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
                if (metsInfo != null && DID_ELEMENT.equals(xmlReader.getName())) {
                    writeRightsToEad(xmlReader, xmlWriter, metsInfo);
                    metsInfo = null;
                }
            }
        }
        xmlWriter.writeEndDocument();
        httpClient.close();
        xmlWriter.flush();
        xmlWriter.close();
        xmlReader.close();
        if (error == null) {
            inputFile.delete();
            outputFile.renameTo(inputFile);
        } else {
            outputFile.delete();
        }
        return error;
    }

    private static void writeRightsToEad(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter, MetsInfo metsInfo) throws XMLStreamException {
        QName element = xmlReader.getName();
        if (metsInfo.containRightsInfo()) {
            xmlWriter.writeCharacters("\n");
            xmlWriter.writeStartElement(element.getPrefix(), "userestrict", element.getNamespaceURI());
            xmlWriter.writeAttribute("encodinganalog", "rts:rightscategory");
            xmlWriter.writeAttribute("type", "dao");
            String href = null;
            String description = null;
            if ("PUBLIC DOMAIN".equals(metsInfo.getRightsCategory())) {
                href = "http://creativecommons.org/publicdomain/mark/1.0/";
                description = "Public Domain Mark";
            } else if ("LICENSED".equals(metsInfo.getRightsCategory())) {
                href = "http://rightsstatements.org/vocab/NoC-OKLR/1.0/";
                description = "No Copyright - Other Known Legal Restrictions";
            } else if ("CONTRACTUAL".equals(metsInfo.getRightsCategory())) {
                href = "http://rightsstatements.org/vocab/NoC-OKLR/1.0/";
                description = "No Copyright - Other Known Legal Restrictions";
            } else if ("COPYRIGHTED".equals(metsInfo.getRightsCategory())) {
                if ("SA".equals(metsInfo.getRightsConstraint())) {
                    href = "http://creativecommons.org/licenses/by-sa/4.0/";
                    description = "Creative Commons Attribution, ShareAlike";
                } else if ("ND".equals(metsInfo.getRightsConstraint())) {
                    href = "http://creativecommons.org/licenses/by-nd/4.0/";
                    description = "Creative Commons Attribution, No Derivatives";
                } else if ("NC".equals(metsInfo.getRightsConstraint())) {
                    href = "http://creativecommons.org/licenses/by-nc/4.0/";
                    description = "Creative Commons Attribution, Non-Commercial";
                } else if ("NC-SA".equals(metsInfo.getRightsConstraint())) {
                    href = "http://creativecommons.org/licenses/by-nc-sa/4.0/";
                    description = "Creative Commons Attribution, Non-Commercial, ShareAlike";
                } else if ("NC-ND".equals(metsInfo.getRightsConstraint())) {
                    href = "http://creativecommons.org/licenses/by-nc-nd/4.0/";
                    description = "Creative Commons Attribution, Non-Commercial, No Derivatives";
                } else {
                    href = "http://creativecommons.org/licenses/by/4.0/";
                    description = "Creative Commons Attribution";
                }
            } else {
                if ("INC".equals(metsInfo.getRightsOtherCategory())) {
                    href = "http://rightsstatements.org/vocab/InC/1.0/";
                    description = "In Copyright";
                } else if ("INC-EDU".equals(metsInfo.getRightsOtherCategory())) {
                    href = "http://rightsstatements.org/vocab/InC-EDU/1.0/";
                    description = "In Copyright - Educational Use Permitted";
                } else if ("INC-EU-OW".equals(metsInfo.getRightsOtherCategory())) {
                    href = "http://rightsstatements.org/vocab/InC-OW-EU/1.0/";
                    description = "In Copyright - EU Orphan Work";
                } else if ("CC0".equals(metsInfo.getRightsOtherCategory())) {
                    href = "http://creativecommons.org/publicdomain/zero/1.0/";
                    description = "Creative Commons CC0 Public Domain Dedication";
                } else if ("NOC-NC".equals(metsInfo.getRightsOtherCategory())) {
                    href = "http://rightsstatements.org/vocab/NoC-NC/1.0/";
                    description = "No Copyright - Non-Commercial Use Only ";
                } else if ("NOC-OKLR".equals(metsInfo.getRightsOtherCategory())) {
                    href = "http://rightsstatements.org/vocab/NoC-OKLR/1.0/";
                    description = "No Copyright - Other Known Legal Restrictions";
                } else if ("CNE".equals(metsInfo.getRightsOtherCategory())) {
                    href = "http://rightsstatements.org/vocab/CNE/1.0/";
                    description = "Copyright Not Evaluated";
                }
            }

            if (href != null && description != null) {
                xmlWriter.writeStartElement("p");
                xmlWriter.writeStartElement("extref");
                xmlWriter.writeAttribute(ApeXMLConstants.XLINK_NAMESPACE, "href", href);
                xmlWriter.writeCharacters(description);
                xmlWriter.writeEndElement();
                xmlWriter.writeEndElement();
            }
            if (StringUtils.isNotBlank(metsInfo.getRightsHolder())) {
                xmlWriter.writeStartElement("p");
                xmlWriter.writeCharacters(metsInfo.getRightsHolder());
                xmlWriter.writeEndElement();
            }
            if (StringUtils.isNotBlank(metsInfo.getRightsComments())) {
                xmlWriter.writeStartElement("p");
                xmlWriter.writeCharacters(metsInfo.getRightsComments());
                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();
        }
    }

    private static MetsInfo integrateDaoWithMets(XMLStreamReader xmlReader, XMLStreamWriter xmlWriter,
            MetsHttpClient httpClient, File dir) throws METSException {
        QName element = xmlReader.getName();
        String href = xmlReader.getAttributeValue(ApeXMLConstants.XLINK_NAMESPACE, "href");
        String filename = "METS_TEMP_FILE_" + System.currentTimeMillis() + ".xml";
        File metsFile = new File(dir, filename);
        /*
         * downloading file
         */
        LOGGER.debug("Downloading: " + href);
        CloseableHttpResponse closeableHttpResponse = null;
        InputStream response = null;
        FileOutputStream outputStream = null;
        try {
            closeableHttpResponse = httpClient.get(href);
            response = httpClient.getResponseInputStream(closeableHttpResponse);
            outputStream = new FileOutputStream(metsFile);
            IOUtils.copy(response, outputStream);
        } catch (Exception e) {
            if (metsFile.exists()) {
                metsFile.delete();
            }
            StringBuilder error = new StringBuilder();
            error.append("<span class=\"validation-error\"><b>METS file (" + href + ") :</b><br/>");
            error.append(e.getMessage());
            throw new METSException(error.toString());
        } finally {
            try {
                if (closeableHttpResponse != null) {
                    closeableHttpResponse.close();
                }
                if (response != null) {
                    response.close();
                }
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (Exception e) {
            }
        }
        MetsInfo metsInfo = null;
        try {
            /*
             * write to file
             */

            LOGGER.debug("Merging: " + href);
            metsInfo = METSParser.parse(metsFile);
            for (int i = 0; i < metsInfo.getDaoInfos().size(); i++) {
                DaoInfo daoInfo = metsInfo.getDaoInfos().get(i);
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
            error.append("<span class=\"validation-error\"><b>METS file (" + href + ") :</b><br/>");
            error.append("l.").append(exception.getLineNumber()).append(" c.").append(exception.getColumnNumber())
                    .append(": ").append(exception.getMessage()).append("</span>").append("<br />");
            throw new METSException(error.toString());
        } catch (Exception e) {
            StringBuilder error = new StringBuilder();
            error.append("<span class=\"validation-error\"><b>METS file (" + href + ") :</b><br/>");
            error.append(e.getMessage());
            throw new METSException(error.toString());
        } finally {
            metsFile.delete();
        }
        if (metsFile.exists()) {
            metsFile.delete();
        }
        return metsInfo;

    }

}
