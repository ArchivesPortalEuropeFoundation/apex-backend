package eu.apenet.dashboard.manual.eag.utils.parseJsonObj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import eu.apenet.dashboard.manual.eag.Eag2012;

/**
 * Class for Fill a {@link Eag2012} eag2012 object got from params, fill it with the information provided into
 * {@link JSONObject} JSONObject (got from params) in part of relation.
 */
public class RelationJsonObjToEag2012 extends AbstractJsonObjtoEag2012 implements JsonObjToEag2012{
	private JSONObject relation;
	private JSONObject resourceRelations;
	private JSONObject resourceRelationTable;
	private JSONObject institutionRelations;
	private JSONObject institutionRelationTable;
	private Map<String, Map<String, List<String>>> mapMapListResourceRelations = null;
	private Eag2012 eag2012;
	private int i;
	private int targetNumber = 1;
	private String target1 = null;
	private final Logger log = Logger.getLogger(getClass());

	/**
	 * Method that checks if it have to create if the object  and fill it with data the {@link Eag2012} Eag2012 object part of 
	 */
	private void resourceRelations()throws JSONException{	
		this.log.debug("Method start: \"resourceRelations\"");
		if(relation.has("resourceRelations")){
			resourceRelations = relation.getJSONObject("resourceRelations");
			i=0;
			while(resourceRelations.has("resourceRelationTable_"+(++i))){
				resourceRelationTable = resourceRelations.getJSONObject("resourceRelationTable_"+i);
				if(resourceRelationTable.has("textWebsiteOfResource")){
					Map<String, List<String>> website = eag2012.getResourceRelationHref();
					if(website==null){
						website = new HashMap<String, List<String>>();
					}
					List<String> listWebsite = null;
					listWebsite = createList(website,listWebsite,Eag2012.TAB_RELATION,0);
					listWebsite.add(replaceIfExistsSpecialReturnString(resourceRelationTable.getString("textWebsiteOfResource")));
					website.put(Eag2012.TAB_RELATION,listWebsite);
					eag2012.setResourceRelationHref(website);
				}
				if(resourceRelationTable.has("selectTypeOfYourRelation")){
					List<String> typeOfYourRelation = eag2012.getResourceRelationResourceRelationType();
					if(typeOfYourRelation==null){
						typeOfYourRelation = new ArrayList<String>();
					}
					typeOfYourRelation.add(replaceIfExistsSpecialReturnString(resourceRelationTable.getString("selectTypeOfYourRelation")));
					eag2012.setResourceRelationResourceRelationType(typeOfYourRelation);
				}
				if(resourceRelationTable.has("textTitleOfRelatedMaterial")){					
					List<Map<String, List<String>>> listRelationEnrtyMap = eag2012.getRelationEntryValue();
					listRelationEnrtyMap=createData(listRelationEnrtyMap,Eag2012.RESOURCE_RELATION, resourceRelationTable.getString("textTitleOfRelatedMaterial"),0,0,0);
					eag2012.setRelationEntryValue(listRelationEnrtyMap);					
				}
				if(resourceRelationTable.has("selectTitleOfRelatedMaterialLang")){
					List<Map<String, List<String>>> listRelationEntryMap = eag2012.getRelationEntryLang();
					listRelationEntryMap=createData(listRelationEntryMap,Eag2012.RESOURCE_RELATION, resourceRelationTable.getString("selectTitleOfRelatedMaterialLang"),0,0,0);
					eag2012.setRelationEntryLang(listRelationEntryMap);
				}				
				if(resourceRelationTable.has("textDescriptionOfRelation")){				
					List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
					descriptiveNotePValue = createListMapList(descriptiveNotePValue,Eag2012.TAB_RELATION,Eag2012.RESOURCE_RELATION, resourceRelationTable.getString("textDescriptionOfRelation"),0,0,0,0);
					eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
				 }
				if(resourceRelationTable.has("selectLanguageDescriptionOfRelation")){
					List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePLang();
					descriptiveNotePValue = createListMapList(descriptiveNotePValue,Eag2012.TAB_RELATION,Eag2012.RESOURCE_RELATION, resourceRelationTable.getString("selectLanguageDescriptionOfRelation"),0,0,0,0);
					eag2012.setDescriptiveNotePLang(descriptiveNotePValue);					
				 }
			}//end while resourceRelations
		}//end if resourceRelation
		this.log.debug("End method: \"resourceRelations\"");
	}

	/**
	 * Method that checks if it have to create if the object  and fill it with data the {@link Eag2012} Eag2012 object part of 
	 */
	private void institutionRepository()throws JSONException{
		this.log.debug("Method start: \"institutionRepository\"");
		if(relation.has("institutionRelations")){
			institutionRelations = relation.getJSONObject("institutionRelations");
			int i=0;
			while(institutionRelations.has("institutionRelationTable_"+(++i))){
				institutionRelationTable = institutionRelations.getJSONObject("institutionRelationTable_"+i);
				if(institutionRelationTable.has("textWebsiteOfDescription")){
					List<String> website = eag2012.getEagRelationHref();
					if(website==null){
						website = new ArrayList<String>();
					}
					website.add(replaceIfExistsSpecialReturnString(institutionRelationTable.getString("textWebsiteOfDescription")));
					eag2012.setEagRelationHref(website);
				}
				if(institutionRelationTable.has("selectTypeOftheRelation")){
					List<String> typeOftheRelation = eag2012.getEagRelationEagRelationType();
					if(typeOftheRelation==null){
						typeOftheRelation = new ArrayList<String>();
					}
					typeOftheRelation.add(replaceIfExistsSpecialReturnString(institutionRelationTable.getString("selectTypeOftheRelation")));
					eag2012.setEagRelationEagRelationType(typeOftheRelation);
				}
				if(institutionRelationTable.has("textTitleOfRelatedInstitution")){
					List<Map<String, List<String>>> listRelationEnrtyMap = eag2012.getRelationEntryValue();
					listRelationEnrtyMap=createData(listRelationEnrtyMap,Eag2012.INSTITUTION_RELATIONS, institutionRelationTable.getString("textTitleOfRelatedInstitution"),0,0,0);
					eag2012.setRelationEntryValue(listRelationEnrtyMap);
				}
				if(institutionRelationTable.has("selectTitleOfRelatedInstitutionLang")){
					List<Map<String, List<String>>> listRelationEnrtyMap = eag2012.getRelationEntryLang();
					listRelationEnrtyMap=createData(listRelationEnrtyMap,Eag2012.INSTITUTION_RELATIONS, institutionRelationTable.getString("selectTitleOfRelatedInstitutionLang"),0,0,0);
					eag2012.setRelationEntryLang(listRelationEnrtyMap);
				}
				if(institutionRelationTable.has("textInstitutionDescriptionOfRelation")){
					List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
					 descriptiveNotePValue = createListMapList(descriptiveNotePValue,Eag2012.TAB_RELATION,Eag2012.INSTITUTION_RELATIONS, institutionRelationTable.getString("textInstitutionDescriptionOfRelation"),0,0,0,0);
					eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
				 }
				if(institutionRelationTable.has("selectLanguageInstitutionDescriptionOfRelation")){
					List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePLang();					
					descriptiveNotePValue = createListMapList(descriptiveNotePValue,Eag2012.TAB_RELATION,Eag2012.INSTITUTION_RELATIONS, institutionRelationTable.getString("selectLanguageInstitutionDescriptionOfRelation"),0,0,0,0);
					eag2012.setDescriptiveNotePLang(descriptiveNotePValue);				 
				 }
			}//end while institution
		}//end if institution
		this.log.debug("End method: \"institutionRepository\"");
	}

	/**
	 * Method that create the {@link JSONObject} JSONObject the {@link Eag2012} Eag2012 for part relation
	 * @param eag2012 {@link Eag2012} Eag2012 
	 * @param jsonObj {@link JSONObject} JSONObject
	 * @return {@link Eag2012} Eag2012 
	 */ 
	@Override
	public Eag2012 JsonObjToEag2012(Eag2012 eag20122, JSONObject jsonObj)throws JSONException {
		eag2012=eag20122;
			relation = jsonObj.getJSONObject("relations");
		if(relation!=null){
			//resourceRelation section
			resourceRelations();			
			//institution Repository section
			institutionRepository();
		}//end if relation
		return eag2012;
	}
}
