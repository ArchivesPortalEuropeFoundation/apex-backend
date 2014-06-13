package eu.apenet.dashboard.services.eag.xml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;

import eu.apenet.dashboard.services.eag.xml.stream.publish.EagPublishData;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.archivesportaleurope.xml.ApeXMLConstants;
import eu.archivesportaleurope.xml.xpath.AttributeXpathHandler;
import eu.archivesportaleurope.xml.xpath.StringXpathHandler;
import eu.archivesportaleurope.xml.xpath.TextXpathHandler;
import eu.archivesportaleurope.xml.xpath.XmlStreamHandler;

public class EagPublishDataFiller {
	//private static final Logger LOGGER = Logger.getLogger(EadPublishDataFiller.class);
	/*
	 * unitids
	 */
	private StringXpathHandler otherNamesHandler;
	private StringXpathHandler repositoryNameHandler;
	private StringXpathHandler repositoryTypeHandler;
	private StringXpathHandler historyHandler;
	private StringXpathHandler holdingsHandler;
	private StringXpathHandler locationHandler;
	private AttributeXpathHandler languageHandler;
	private List<XmlStreamHandler> eagHandlers = new ArrayList<XmlStreamHandler>();
	public EagPublishDataFiller() {
			/*
			 * name
			 */
		otherNamesHandler = new TextXpathHandler(ApeXMLConstants.APE_EAG_NAMESPACE, new String[] { "eag", "archguide","identity", "autform | parform | nonpreform" });
		repositoryNameHandler = new TextXpathHandler(ApeXMLConstants.APE_EAG_NAMESPACE, new String[] { "eag", "archguide","desc", "repositories", "repository", "repositoryName" }); 
		repositoryTypeHandler = new TextXpathHandler(ApeXMLConstants.APE_EAG_NAMESPACE, new String[] { "eag", "archguide","identity", "repositoryType"}); 
		languageHandler = new AttributeXpathHandler(ApeXMLConstants.APE_EAG_NAMESPACE, new String[] { "eag", "control", "languageDeclarations", "languageDeclaration", "language" }, "languageCode");
		historyHandler = new TextXpathHandler(ApeXMLConstants.APE_EAG_NAMESPACE, new String[] { "eag", "archguide","desc", "repositories", "repository", "repositorhist" });
		historyHandler.setAllTextBelow(true);
		holdingsHandler = new TextXpathHandler(ApeXMLConstants.APE_EAG_NAMESPACE, new String[] { "eag", "archguide","desc", "repositories", "repository", "holdings" }); 
		holdingsHandler.setAllTextBelow(true);
		locationHandler =  new TextXpathHandler(ApeXMLConstants.APE_EAG_NAMESPACE, new String[] { "eag", "archguide","desc", "repositories", "repository", "location", "street | municipalityPostalcode | country" }); 
		eagHandlers.add(otherNamesHandler);
		eagHandlers.add(repositoryNameHandler);
		eagHandlers.add(repositoryTypeHandler);
		eagHandlers.add(historyHandler);
		eagHandlers.add(holdingsHandler);
		eagHandlers.add(languageHandler);
		eagHandlers.add(locationHandler);
	}

	public void processCharacters(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
			for (XmlStreamHandler handler : eagHandlers) {
				handler.processCharacters(xpathPosition, xmlReader);
			}


	}

	public void processStartElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
			for (XmlStreamHandler handler : eagHandlers) {
				handler.processStartElement(xpathPosition, xmlReader);
			}

	}

	public void processEndElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
			for (XmlStreamHandler handler : eagHandlers) {
				handler.processEndElement(xpathPosition, xmlReader);
			}

	}


	public void fillData(EagPublishData publishData, ArchivalInstitution archivalInstitution) {
		Set<String> otherNames = otherNamesHandler.getResultSet();
		otherNames.remove(archivalInstitution.getAiname());
		Set<String> repositories = repositoryNameHandler.getResultSet();
		repositories.remove(archivalInstitution.getAiname());
		publishData.setRepositories(repositories);
		publishData.setRepositoryTypes(repositoryTypeHandler.getResultSet());
		publishData.setDescription(locationHandler.getResultAsStringWithWhitespace() + StringXpathHandler.WHITE_SPACE + holdingsHandler.getResultAsStringWithWhitespace());
		publishData.setOther(historyHandler.getResultAsStringWithWhitespace());
		publishData.setLanguage(languageHandler.getResultAsStringWithWhitespace());
	}
	private void add(StringBuilder other, String item){
		if (StringUtils.isNotBlank(item)){
			other.append(StringXpathHandler.WHITE_SPACE + item);
		}
	}
}
