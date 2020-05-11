package eu.apenet.oaiserver.response;

import java.util.Date;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import eu.apenet.oaiserver.util.OAIUtils;
import eu.apenet.persistence.factory.DAOFactory;

public class IdentifyResponse extends AbstractResponse {


	private static final String REPOSITORY_NAME = "Archives Portal Europe OAI-PMH Repository";
	private static final String ADMIN_EMAIL = "info@archivesportaleurope.net";
	private static final String DELETED_RECORD_MANNER = "transient";
	private static final String GRANULARITY = "YYYY-MM-DDThh:mm:ssZ";
	private static final String PROTOCOL_VERSION = "2.0";
	private static final String COMPRESSION = "gzip";


	@Override
	protected void generateResponseInternal(XMLStreamWriterHolder writer, Map<String, String> params) throws XMLStreamException {
		writer.writeStartElement("Identify");
		writer.writeTextElement("repositoryName",REPOSITORY_NAME );
		writer.writeTextElement("baseURL",params.get(AbstractResponse.REQUEST_URL) );
		writer.writeTextElement("protocolVersion",PROTOCOL_VERSION );
		writer.writeTextElement("adminEmail",ADMIN_EMAIL);
		Date earliestDate = DAOFactory.instance().getEseDAO().getTheEarliestDatestamp();
		if(earliestDate==null){
			earliestDate = new Date();
		}
		writer.writeTextElement("earliestDatestamp",OAIUtils.parseDateToISO8601(earliestDate));
		writer.writeTextElement("deletedRecord",DELETED_RECORD_MANNER);
		writer.writeTextElement("granularity",GRANULARITY);
		writer.writeTextElement("compression",COMPRESSION);
		writer.closeElement();
		
		
	}

}
