package eu.apenet.dashboard.services.eag.xml.stream;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import eu.apenet.commons.solr.SolrValues;
import eu.apenet.dashboard.services.AbstractSolrPublisher;
import eu.apenet.dashboard.services.eag.xml.stream.publish.EagPublishData;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.archivesportaleurope.xml.ApeXMLConstants;
import eu.archivesportaleurope.xml.ApeXmlUtil;
import eu.archivesportaleurope.xml.xpath.AbstractXpathReader;
import eu.archivesportaleurope.xml.xpath.handler.StringXpathHandler;
import eu.archivesportaleurope.xml.xpath.handler.TextMapXpathHandler;
import eu.archivesportaleurope.xml.xpath.handler.TextXpathHandler;

public class EagXpathReader extends AbstractXpathReader<EagPublishData> {
	private static final String COUNTRY_PREFIX = "country.";
	/*
	 * unitids
	 */
	private StringXpathHandler otherNamesHandler;
	private StringXpathHandler repositoryNameHandler;
	private StringXpathHandler repositoryTypeHandler;
	private StringXpathHandler historyHandler;
	private StringXpathHandler holdingsHandler;
	private TextMapXpathHandler locationHandler;
	private TextXpathHandler actingMaintenanceForHandler;
	private TextXpathHandler municipalityPostalcodeHandler;	
	private static Map<String, Set<String>> countryResourceBundles;
	static {
		countryResourceBundles = new HashMap<String, Set<String>>();
		Map<String, ResourceBundle> resourceBundlesMap = getResourceBundles("i18n/ApplicationResources");
		ResourceBundle enBundle = resourceBundlesMap.get("en");
		Set<String> countryKeys =  new HashSet<String>();
		Enumeration<String> keys = enBundle.getKeys();
		while (keys.hasMoreElements()){
			String key = keys.nextElement();
			if (key.startsWith(COUNTRY_PREFIX)){
				countryKeys.add(key);
				countryResourceBundles.put(key, new HashSet<String>());
			}
		}
		for (Map.Entry<String, ResourceBundle> entry: resourceBundlesMap.entrySet()){
			ResourceBundle resourceBundle = entry.getValue();
			for (String countryKey :countryKeys){
				String value = resourceBundle.getString(countryKey);
				if (StringUtils.isNotBlank(value)){
					countryResourceBundles.get(countryKey).add(value);
				}
			}
		}

	}
	
	


	@Override
	protected void internalInit() throws Exception {
		/*
			 * name
			 */
		otherNamesHandler = new TextXpathHandler(ApeXMLConstants.APE_EAG_NAMESPACE, new String[] { "eag", "archguide","identity", "autform | parform | nonpreform" });
		repositoryNameHandler = new TextXpathHandler(ApeXMLConstants.APE_EAG_NAMESPACE, new String[] { "eag", "archguide","desc", "repositories", "repository", "repositoryName" }); 
		repositoryTypeHandler = new TextXpathHandler(ApeXMLConstants.APE_EAG_NAMESPACE, new String[] { "eag", "archguide","identity", "repositoryType"}); 
		historyHandler = new TextXpathHandler(ApeXMLConstants.APE_EAG_NAMESPACE, new String[] { "eag", "archguide","desc", "repositories", "repository", "repositorhist", "descriptiveNote" });
		historyHandler.setAllTextBelow(true);
		holdingsHandler = new TextXpathHandler(ApeXMLConstants.APE_EAG_NAMESPACE, new String[] { "eag", "archguide","desc", "repositories", "repository", "holdings", "descriptiveNote" }); 
		holdingsHandler.setAllTextBelow(true);
		locationHandler =  new TextMapXpathHandler(ApeXMLConstants.APE_EAG_NAMESPACE, new String[] { "eag", "archguide","desc", "repositories", "repository", "location" }); 
		locationHandler.setAttribute("localType", "visitors address", false);
		locationHandler.setAllTextBelow(true);
		locationHandler.setOnlyFirst(true);
		actingMaintenanceForHandler = new TextXpathHandler(ApeXMLConstants.APE_EAG_NAMESPACE, new String[] { "eag", "archguide","desc", "repositories", "repository", "holdings", "actingMaintenanceForGroup", "actingMaintenanceFor", "placeEntry" }); 
		municipalityPostalcodeHandler = new TextXpathHandler(ApeXMLConstants.APE_EAG_NAMESPACE, new String[] { "eag", "archguide","desc", "repositories", "repository", "location", "municipalityPostalcode" });
		getXpathHandlers().add(otherNamesHandler);
		getXpathHandlers().add(repositoryNameHandler);
		getXpathHandlers().add(repositoryTypeHandler);
		getXpathHandlers().add(historyHandler);
		getXpathHandlers().add(holdingsHandler);
		getXpathHandlers().add(locationHandler);
		getXpathHandlers().add(actingMaintenanceForHandler);
		getXpathHandlers().add(municipalityPostalcodeHandler);
	}
	@Override
	public EagPublishData getData() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void fillData(EagPublishData publishData, ArchivalInstitution archivalInstitution) {
		Set<String> otherNames = otherNamesHandler.getResultSet();
		otherNames.remove(archivalInstitution.getAiname());
		publishData.setOtherNames(otherNames);
		Set<String> repositories = repositoryNameHandler.getResultSet();
		repositories.remove(archivalInstitution.getAiname());
		publishData.setRepositories(repositories);
		publishData.setRepositoryTypes(repositoryTypeHandler.getResultSet());
		StringBuilder other = new StringBuilder();
		add(other, holdingsHandler.getResultAsStringWithWhitespace());
		add(other, historyHandler.getResultAsStringWithWhitespace());
		if (locationHandler.getResults().size() > 0){
			String address = TextMapXpathHandler.getResultAsStringWithWhitespace(locationHandler.getResults().get(0), new String[] {"street", "municipalityPostalcode"},", ");
			publishData.getAddress().add(address);
		}
		publishData.setPlaces(municipalityPostalcodeHandler.getResultSet());
		publishData.getPlaces().addAll(actingMaintenanceForHandler.getResultSet());

		publishData.setOther(other.toString());
	
		ArchivalInstitution ai = archivalInstitution.getParent();
		List<ArchivalInstitution> ais = new ArrayList<ArchivalInstitution>();
		while (ai != null) {
			ais.add(ai);
			ai = ai.getParent();
		}
		for (int i = ais.size() - 1; i >= 0; i--) {
			ArchivalInstitution currentAi = ais.get(i);
			publishData.getAiGroups().add(currentAi.getAiname());
			publishData.getAiGroupFacets().add(currentAi.getAiname() + AbstractSolrPublisher.COLON + SolrValues.TYPE_GROUP + AbstractSolrPublisher.COLON  + currentAi.getAiId());
			publishData.getAiGroupIds().add(currentAi.getAiId());
		}
		Set<String> countries = countryResourceBundles.get(COUNTRY_PREFIX + archivalInstitution.getCountry().getEncodedCname().toLowerCase());
		if (countries == null){
			publishData.getCountries().add(archivalInstitution.getCountry().getCname());
		}else {
			publishData.setCountries(countries);
		}
		
	}
	private void add(StringBuilder other, String item){
		if (StringUtils.isNotBlank(item)){
			other.append(ApeXmlUtil.WHITE_SPACE + item);
		}
	}
	public static Map<String, ResourceBundle> getResourceBundles(String baseName) {

		Map<String, ResourceBundle> resourceBundles = new HashMap<String, ResourceBundle>();

		  for (String isoLanguage: Locale.getISOLanguages()) {
		    try {
		    	URL url = EagXpathReader.class.getClassLoader().getResource(baseName + "_"+isoLanguage+".properties");
		    	if (url != null){
		    		resourceBundles.put(isoLanguage, ResourceBundle.getBundle(baseName, new Locale(isoLanguage)));
		    	}
		    } catch (MissingResourceException ex) {
		      // ...
		    }
		  }

		  return resourceBundles;
		}
}
