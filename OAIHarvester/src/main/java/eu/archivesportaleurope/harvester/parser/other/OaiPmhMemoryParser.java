package eu.archivesportaleurope.harvester.parser.other;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

/**
 * User: Yoann Moranville
 * Date: 16/10/2013
 *
 * @author Yoann Moranville
 */
public abstract class OaiPmhMemoryParser {
    protected static final Logger LOG = Logger.getLogger(OaiPmhMemoryParser.class);
    protected static final String UTF8 = "UTF-8";
    protected static final String OAI_PMH = "http://www.openarchives.org/OAI/2.0/";

    protected static final QName METADATA_FORMAT = new QName(OAI_PMH, "metadataFormat");
    protected static final QName SET = new QName(OAI_PMH, "set");

    protected static final QName RESUMPTION_TOKEN = new QName(OAI_PMH, "resumptionToken");
    protected static final QName ERROR = new QName(OAI_PMH, "error");

    public abstract OaiPmhElements parse(XMLStreamReader xmlReader) throws XMLStreamException;
}
