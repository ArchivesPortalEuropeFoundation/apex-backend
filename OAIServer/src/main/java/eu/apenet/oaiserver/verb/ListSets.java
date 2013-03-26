package eu.apenet.oaiserver.verb;

import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.apenet.oaiserver.util.OAIResponse;
import eu.apenet.oaiserver.util.OAIUtils;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
@Deprecated
public class ListSets extends OAIVerb{
	private String resumptionToken;
	private InputStream inputStream;
	private Logger LOG = Logger.getLogger(ListSets.class);
	
	public ListSets(Map<String, String> params) {
		this.resumptionToken = params.get("resumptionToken");
	}
	
	public void setResumptionToken(String resumptionToken) {
		this.resumptionToken = resumptionToken;
	}

	public InputStream getInputStream(){
		return this.inputStream;
	}
	
	public boolean execute(){
		Map<String,String> map = new HashMap<String, String>();
		try{
			if(this.resumptionToken!=null && !this.resumptionToken.isEmpty()){
				map.put("resumptionToken",this.resumptionToken);
			}
			this.inputStream = OAIResponse.getVerbInfo("ListSets",map,this.getUrl());
			if(this.inputStream!=null){
				return true;
			}
			Document emptyDocument = OAIResponse.getVerbResponse("ListSets",map,this.getUrl());
			this.inputStream = OAIUtils.getInputStreamOfDocument(OAIUtils.getResponseError(emptyDocument,"badArgument"));
		}catch(Exception e){
			LOG.error("Params are NOT right in URL: ",e.getCause());
		}
		return false;
	}
	
	/**
	 * Method which builds ListSets node with all sets that are in a List<String>,
	 * in a future if the setName is activated this param has to been a Set|List<Set>
	 * and the Set has two attributes, name and id. If not, there should be extracted
	 * of Finding Aid (set and finding_aid name or finding_aid.eadid && ese.dao_identification  
	 * 
	 * @param eSets
	 * @param doc
	 * @return Document
	 * @throws DOMException
	 * @throws ParserConfigurationException
	 * @throws ParseException
	 */
	public static Document buildListSetResponse(List<String> eSets,Document doc,Map<String,String> arguments) throws DOMException, ParserConfigurationException, ParseException{
		Node verbNode = doc.createElementNS(OAIResponse.NAMESPACE, "ListSets");
		Iterator<String> iteratorESets = eSets.iterator();
		Integer counter = OAIResponse.LIMIT_PER_RESPONSE;
		if(iteratorESets.hasNext()){
			do{
				Node set = doc.createElementNS(OAIResponse.NAMESPACE, "set");
				String textContent = iteratorESets.next();
				String institutionName = null;
				Integer aiId = null;
				String countryName = null;
				String couIsoname = null;
				Node setSpec = doc.createElementNS(OAIResponse.NAMESPACE, "setSpec");
				setSpec.setTextContent(textContent);
				set.appendChild(setSpec);
				if(!textContent.isEmpty() && textContent.contains(":")){
					doc = appendVirtualSets(doc,verbNode,textContent);
					Node setName = doc.createElementNS(OAIResponse.NAMESPACE, "setName");
					aiId = Integer.valueOf(textContent.substring(textContent.lastIndexOf(":")+1));
					ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().findById(aiId);
					if (archivalInstitution != null) {
						institutionName = archivalInstitution.getAiname();
					}
					couIsoname = textContent.substring(0,textContent.indexOf(":"));
					Country country = DAOFactory.instance().getCountryDAO().getCountries(couIsoname).get(0); 
					if (country != null) {
						countryName = country.getCname();
					}
					setName.setTextContent(institutionName);
					set.appendChild(setName);
				}
				if(institutionName!=null && countryName!=null){
					Node setDescription = doc.createElementNS(OAIResponse.NAMESPACE,"setDescription");
					Element dc = doc.createElementNS(OAIUtils.OAIDC_SCHEMA_LOCATION, "oai_dc:dc");
					dc.setAttribute("xmlns:dc", OAIUtils.DC_METADATA_NAMESPACE);
					dc.setAttribute("xmlns:xsi", OAIUtils.XMLNS_XSI);
					dc.setAttribute("xsi:schemaLocation", OAIUtils.OAIDC_SCHEMA_LOCATION + " " + OAIUtils.OAIDC_SCHEMA_LOCATION_FILE);
					Element dcTitle = doc.createElementNS(OAIUtils.DC_METADATA_NAMESPACE, "dc:title");
					dcTitle.setTextContent("ESE Records for "+institutionName+", "+countryName);
					dc.appendChild(dcTitle);
					Node dcCreator = doc.createElementNS(OAIUtils.DC_METADATA_NAMESPACE, "dc:creator");
					dcCreator.setTextContent(OAIResponse.CREATOR);
					dc.appendChild(dcCreator);
					Element dcDescription = doc.createElementNS(OAIUtils.DC_METADATA_NAMESPACE, "dc:description");
					dcDescription.setTextContent("This set contains the ESE records created via "+OAIResponse.CREATOR+" by "+institutionName+", "+countryName);
					dc.appendChild(dcDescription);
					Node dcContributor = doc.createElementNS(OAIUtils.DC_METADATA_NAMESPACE, "dc:contributor");
					dcContributor.setTextContent(institutionName);
					dc.appendChild(dcContributor);
					setDescription.appendChild(dc);
					set.appendChild(setDescription);
				}
				verbNode.appendChild(set);
				counter--;
			}while(counter>0 && iteratorESets.hasNext());
			doc.getDocumentElement().appendChild(verbNode);
			if(iteratorESets.hasNext()){
				doc = OAIUtils.buildResumptionToken(doc,arguments,OAIResponse.LIMIT_PER_RESPONSE.toString());
			}
		}else{
			String resumptionToken = arguments.get("resumptionToken");
			if(resumptionToken!=null && resumptionToken.contains("/") && resumptionToken.length()>5){
				doc = OAIUtils.getResponseError(doc,"badResumptionToken");
			}else{
				doc = OAIUtils.getResponseError(doc,"noSetHierarchy");
			}
		}
		return doc;
	}
	
	/**
	 * This method is a very temporal solution to apply OAI recommendations,
	 * related to ticket #309. In a near future it should be changed. First look forward
	 * if virtual target set has been appended, next apply this target set to the document. 
	 * 
	 * @param doc
	 * @param eset
	 * @return Document
	 */
	private static Document appendVirtualSets(Document doc,Node verbNode,String eset) {
		String temporalHierarchy = eset.substring(0,eset.lastIndexOf(":")); //Remove the lastest institution
		String[] hierarchy = temporalHierarchy.split(":");
		String hierarchyName = null;
		for(int i=0;i<hierarchy.length;i++){
			String target = hierarchy[i];
			if (i==0) {
				// The node is a country
				Country country = DAOFactory.instance().getCountryDAO().getCountries(target).get(0);
				if (country != null) {
					hierarchyName = country.getCname();
				}
			}
			else {
				// The node is an archival institution
				ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().findById(Integer.valueOf(target)); 
				hierarchyName = archivalInstitution.getAiname();
			}
			NodeList sets = verbNode.getChildNodes();
			boolean found = false;
			//search for repeated virtual sets
			for(int j=0;!found && sets.getLength()>j;j++){
				if(sets.item(j).getNodeName().equals("set")){
					for(int k=0;k<sets.item(j).getChildNodes().getLength();k++){
						if(sets.item(j).getChildNodes().item(k).getNodeName().equals("setName") && sets.item(j).getChildNodes().item(k).getTextContent().equals(hierarchyName)){
							//Node targetNode = sets.item(j).getChildNodes().item(k).getNextSibling();
							//if(targetNode.getNodeName().equals("setSpec") && ((targetNode.getTextContent().contains(":") && targetNode.getTextContent().contains(eset.subSequence(0,eset.lastIndexOf(":")))) || !targetNode.getTextContent().contains(":"))){ //setSpec check
								found = true;
							//}
						}
					}
				}
			}
			if(!found){ //build a virtual set
				Node set = doc.createElementNS(OAIResponse.NAMESPACE, "set");
				Node setSpec = doc.createElementNS(OAIResponse.NAMESPACE, "setSpec");
				String targetName = eset.substring(0,eset.indexOf(target));
				if(targetName.length()>1){
					targetName+=target;
				}else{
					targetName = target;
				}
				setSpec.setTextContent(targetName);
				set.appendChild(setSpec);
				Node setName = doc.createElementNS(OAIResponse.NAMESPACE, "setName");
				setName.setTextContent(hierarchyName);
				set.appendChild(setName);
				Node setDescription = doc.createElementNS(OAIResponse.NAMESPACE, "setDescription");
				Element dc = doc.createElementNS(OAIUtils.OAIDC_SCHEMA_LOCATION, "oai_dc:dc");
				dc.setAttribute("xmlns:dc", OAIUtils.DC_METADATA_NAMESPACE);
				dc.setAttribute("xmlns:xsi", OAIUtils.XMLNS_XSI);
				dc.setAttribute("xsi:schemaLocation", OAIUtils.OAIDC_SCHEMA_LOCATION + " " + OAIUtils.OAIDC_SCHEMA_LOCATION_FILE);
				Element dcTitle = doc.createElementNS(OAIUtils.DC_METADATA_NAMESPACE, "dc:title");
				dcTitle.setTextContent("ESE Records for " + hierarchyName);
				dc.appendChild(dcTitle);
				Node dcCreator = doc.createElementNS(OAIUtils.DC_METADATA_NAMESPACE, "dc:creator");
				dcCreator.setTextContent(OAIResponse.CREATOR);
				dc.appendChild(dcCreator);
				Element dcDescription = doc.createElementNS(OAIUtils.DC_METADATA_NAMESPACE, "dc:description");
				dcDescription.setTextContent("This set contains the ESE records created via " + OAIResponse.CREATOR + " by " + hierarchyName + ".");
				dc.appendChild(dcDescription);
				Node dcContributor = doc.createElementNS(OAIUtils.DC_METADATA_NAMESPACE, "dc:contributor");
				dcContributor.setTextContent(hierarchyName);
				dc.appendChild(dcContributor);
				setDescription.appendChild(dc);
				set.appendChild(setDescription);
				verbNode.appendChild(set);
			}
		}
		return doc;
	}

	/**
	 * All the logic related to ListSets verb
	 * 
	 * @param arguments
	 * @param requestURL
	 * @return Document
	 * @throws Exception
	 */
	public static Document getListSetsLogic(Map<String, String> arguments,String requestURL) throws Exception {
		Document doc = OAIResponse.getVerbResponse("ListSets",arguments,requestURL);
		if(doc.getElementsByTagName("error")!=null && doc.getElementsByTagName("error").getLength()>0){
			return doc;
		}else{
			String resumptionToken = arguments.get("resumptionToken");
			if(resumptionToken==null || resumptionToken.isEmpty()){
				List<String> eSets = DAOFactory.instance().getEseDAO().getSetsOfEses(null, null, null, null, null);
				doc = ListSets.buildListSetResponse(eSets,doc,arguments);
			}else{
				if(resumptionToken.contains("/")){
					doc = OAIResponse.resumptionTokenLogic("ListSets",resumptionToken,arguments,doc);
				}
			}
		}
		return doc;
	}
	
}
