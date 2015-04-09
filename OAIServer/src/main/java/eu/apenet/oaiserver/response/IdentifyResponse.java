package eu.apenet.oaiserver.response;

import java.util.Date;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import eu.apenet.oaiserver.config.Configuration;
import eu.apenet.oaiserver.config.dao.MetadataObjectDAOFront;
import eu.apenet.oaiserver.config.ape.dao.impl.MetadataObjectDAOFrontImpl;
import eu.apenet.oaiserver.util.OAIUtils;

public class IdentifyResponse extends AbstractResponse {
    private static String REPOSITORY_NAME;
    private static String ADMIN_EMAIL;
    private static String DELETED_RECORD_MANNER;
    private static String GRANULARITY;
    private static String PROTOCOL_VERSION;
    private static String COMPRESSION;

    static {
        REPOSITORY_NAME = Configuration.REPOSITORY_NAME;
        ADMIN_EMAIL = Configuration.ADMIN_EMAIL;
        DELETED_RECORD_MANNER = Configuration.DELETED_RECORD_MANNER;
        GRANULARITY = Configuration.GRANULARITY;
        PROTOCOL_VERSION = Configuration.PROTOCOL_VERSION;
        COMPRESSION = Configuration.COMPRESSION;
    }

	@Override
	protected void generateResponseInternal(XMLStreamWriterHolder writer, Map<String, String> params) throws XMLStreamException {
        MetadataObjectDAOFront metadataObjectDAOFront = new MetadataObjectDAOFrontImpl();
		writer.writeStartElement("Identify");
		writer.writeTextElement("repositoryName",REPOSITORY_NAME );
		writer.writeTextElement("baseURL",params.get(AbstractResponse.REQUEST_URL) );
		writer.writeTextElement("protocolVersion",PROTOCOL_VERSION );
		writer.writeTextElement("adminEmail",ADMIN_EMAIL);
		Date earliestDate = metadataObjectDAOFront.getTheEarliestDatestamp();
		if(earliestDate == null){
			earliestDate = new Date();
		}
		writer.writeTextElement("earliestDatestamp",OAIUtils.parseDateToISO8601(earliestDate));
		writer.writeTextElement("deletedRecord",DELETED_RECORD_MANNER);
		writer.writeTextElement("granularity",GRANULARITY);
		writer.writeTextElement("compression",COMPRESSION);
		writer.closeElement();
	}

}
