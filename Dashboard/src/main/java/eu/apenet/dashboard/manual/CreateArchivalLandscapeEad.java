package eu.apenet.dashboard.manual;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.indexing.AbstractParser;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.HoldingsGuide;
import javanet.staxutils.IndentingXMLStreamWriter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Set;

/**
 * User: Yoann Moranville
 * Date: 05/09/2012
 *
 * @author Yoann Moranville
 */
public class CreateArchivalLandscapeEad {
    private static final Logger LOG = Logger.getLogger(CreateArchivalLandscapeEad.class);

    private XMLStreamWriter writer;

    public CreateArchivalLandscapeEad(StringWriter eadArchivalLandscapeWriter) throws XMLStreamException {
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(eadArchivalLandscapeWriter);
        this.writer = new IndentingXMLStreamWriter(xmlStreamWriter);
    }

    public void addInsideEad(Country country) throws XMLStreamException {
        addElement(new QName(AbstractParser.APENET_EAD, "c"), "level", "fonds", "encodinganalog", "3.1.4");
        addElement(new QName(AbstractParser.APENET_EAD, "did"));
        addElement(new QName(AbstractParser.APENET_EAD, "unitid"), "encodinganalog", "3.1.1");
        writer.writeCharacters(""+country.getId());
        writer.writeEndElement();
        addElement(new QName(AbstractParser.APENET_EAD, "unittitle"), "encodinganalog", "3.1.2");
        writer.writeCharacters(StringUtils.capitalize(StringUtils.lowerCase(country.getCname())));
        writer.writeEndElement();
        writer.writeEndElement();
    }

    public void addInsideEad(ArchivalInstitution archivalInstitution) throws XMLStreamException {
        addElement(new QName(AbstractParser.APENET_EAD, "c"), "level", "series", "encodinganalog", "3.1.4");
        addElement(new QName(AbstractParser.APENET_EAD, "did"));
        addElement(new QName(AbstractParser.APENET_EAD, "unitid"), "encodinganalog", "3.1.1");
        writer.writeCharacters(""+archivalInstitution.getAiId());
        writer.writeEndElement();
        addElement(new QName(AbstractParser.APENET_EAD, "unittitle"), "encodinganalog", "3.1.2");

        writer.writeCharacters(archivalInstitution.getAiname());
        writer.writeEndElement();
        if(StringUtils.isNotEmpty(archivalInstitution.getEagPath())) {
            addElement(new QName(AbstractParser.APENET_EAD, "repository"));
            addElement(new QName(AbstractParser.APENET_EAD, "extref"), "xlink:href", APEnetUtilities.getDashboardConfig().getRepoDirPath() + archivalInstitution.getEagPath(), "xlink:title", archivalInstitution.getAiname());
            writer.writeEndElement();
            writer.writeEndElement();
        }
        writer.writeEndElement();

        Set<HoldingsGuide> holdingsGuides = archivalInstitution.getHoldingsGuides();
        if(!holdingsGuides.isEmpty()) {
            addElement(new QName(AbstractParser.APENET_EAD, "otherfindaid"), "encodinganalog", "3.4.5");
            for(HoldingsGuide holdingsGuide : holdingsGuides) {
                addElement(new QName(AbstractParser.APENET_EAD, "p"));
                addElement(new QName(AbstractParser.APENET_EAD, "extref"), "xlink:href", holdingsGuide.getEadid());
                writer.writeEndElement();
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    public void createEadContentData(String title, String identifier, String countrycode, String mainagencycode) throws XMLStreamException, IOException {
        writer.writeStartDocument();

        QName qName = new QName(AbstractParser.APENET_EAD, "ead");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeDefaultNamespace(AbstractParser.APENET_EAD);
        writer.writeNamespace("xlink", AbstractParser.XLINK);
        writer.writeNamespace("xsi", AbstractParser.XSI);
        writer.writeAttribute("audience", "external");

        addElement(new QName(AbstractParser.APENET_EAD, "eadheader"), "countryencoding", "iso3166-1", "dateencoding", "iso8601", "langencoding", "iso639-2b", "repositoryencoding", "iso15511", "scriptencoding", "iso15924", "relatedencoding", "MARC21");

        addElement(new QName(AbstractParser.APENET_EAD, "eadid"));
        if(StringUtils.isNotEmpty(countrycode))
            writer.writeAttribute("countrycode", countrycode);
        if(StringUtils.isNotEmpty(mainagencycode))
            writer.writeAttribute("mainagencycode", mainagencycode);
        writer.writeCharacters(identifier);
        writer.writeEndElement();

        addElement(new QName(AbstractParser.APENET_EAD, "filedesc"));
        addElement(new QName(AbstractParser.APENET_EAD, "titlestmt"));
        addElement(new QName(AbstractParser.APENET_EAD, "titleproper"));
        writer.writeCharacters(title);
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement(); //end eadheader

        addElement(new QName(AbstractParser.APENET_EAD, "archdesc"), "level", "fonds", "type", "archival_landscape", "encodinganalog", "3.1.4", "relatedencoding", "ISAD(G)v2");

        addElement(new QName(AbstractParser.APENET_EAD, "did"));

        addElement(new QName(AbstractParser.APENET_EAD, "unitid"), "encodinganalog", "3.1.1");
        writer.writeCharacters(identifier);
        writer.writeEndElement();

        addElement(new QName(AbstractParser.APENET_EAD, "unittitle"), "encodinganalog", "3.1.2");
        writer.writeCharacters(title);
        writer.writeEndElement();

        writer.writeEndElement(); //end did

        addElement(new QName(AbstractParser.APENET_EAD, "dsc"));
    }

    public void closeEndFile() throws XMLStreamException {
        writer.writeEndElement(); //end dsc

        writer.writeEndElement(); //end archdesc
        writer.writeEndElement(); //end ead

        writer.writeEndDocument();
        writer.close();
    }

    public void addElement(QName element, String... attributes) throws XMLStreamException {
        writer.writeStartElement(element.getPrefix(), element.getLocalPart(), element.getNamespaceURI());
        for(int i = 0; i < attributes.length; i++) {
            writer.writeAttribute(attributes[i], attributes[++i]);
        }
    }

    public void writeEndElement() throws XMLStreamException {
        writer.writeEndElement();
    }

}
