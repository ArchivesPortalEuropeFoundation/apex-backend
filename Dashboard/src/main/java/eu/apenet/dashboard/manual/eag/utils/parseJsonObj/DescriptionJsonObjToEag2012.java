package eu.apenet.dashboard.manual.eag.utils.parseJsonObj;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import eu.apenet.dashboard.manual.eag.Eag2012;

/**
 * Class for Fill a {@link Eag2012} eag2012 object got from params, fill it with the information provided into
 * {@link JSONObject} JSONObject (got from params) in part of description.
 */
public class DescriptionJsonObjToEag2012 extends AbstractJsonObjtoEag2012 implements JsonObjToEag2012{
	private JSONObject description;
	private JSONObject descriptionTable;
	private Eag2012 eag2012;
	private String target1;
	private String target2;
	private int targetNumber;
	private int i;
	private int j;
	private final Logger log = Logger.getLogger(getClass());

	/**
	 * Method that create de jsonobj de {@link Eag2012} Eag2012 for part description
	 * @param eag2012 {@link Eag2012} Eag2012 
	 * @param jsonObj {@link JSONObject}
	 * @return {@link Eag2012} Eag2012 
	 */ 
	@Override
	public Eag2012 JsonObjToEag2012(Eag2012 eag20122, JSONObject jsonObj)throws JSONException {
		eag2012=eag20122;
		//JSONObject 
		description = jsonObj.getJSONObject("description");
		if(description!=null){
			i=0;
			while(description.has("descriptionTable_"+(i+1))){
				//JSONObject 
				descriptionTable = description.getJSONObject("descriptionTable_"+(i+1));
				//repository description section
				parseRepositoryDescription();				
				//date y rule of repositorfound
				if(descriptionTable.has("textDateOfRepositoryFoundation")){  //date
					List<Map<String,Map<String,Map<String,List<List<String>>>>>> dateValue = eag2012.getDateStandardDate();
					dateValue = createDataListMapMapList(dateValue,Eag2012.TAB_DESCRIPTION,Eag2012.REPOSITORHIST,Eag2012.REPOSITOR_FOUND,descriptionTable.getString("textDateOfRepositoryFoundation"),i,0);
					eag2012.setDateStandardDate(dateValue);					
				 }
				//rule of repositorfound
				 parseRepositorFound();
				//date of repositorsup
				if(descriptionTable.has("textDateOfRepositorySuppression")){
					List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateValue = eag2012.getDateStandardDate();
					dateValue = createDataListMapMapList(dateValue,Eag2012.TAB_DESCRIPTION,Eag2012.REPOSITORHIST,Eag2012.REPOSITOR_SUP,descriptionTable.getString("textDateOfRepositorySuppression"),i,0);
					eag2012.setDateStandardDate(dateValue);					 
				 }
				//rule repositorsup
				parseRuleOfRepositorySuppression();
				 //Administrative structure
				parseAdministrativeStructure();
				 //Building description
				parseBuildingDescription();
				 //holding description
				parseHoldingDescription();				  
			 	 //date of holdings
				parseDateOfHoldings();				
			  	 //date range of holdings
				parseDateRangeOfHoldings();				
				//year
				 parseTextYear();				  
				  //extent
				parseTextExtent();
			 i++;
			}//end while description
		}
		return eag2012;
	}

	/**
	 * Method that checks if it have to create if the object<br>  
	 * and fill it with data the {@link Eag2012} Eag2012 object part of textExtent 
	 */
	private void parseTextExtent() throws JSONException{
		this.log.debug("Method start: \"parseTextExtent\"");
		if(descriptionTable.has("textExtent")){			 
			 List<Map<String,Map<String,Map<String,List<String>>>>> numValue = eag2012.getNumValue();
			 numValue = createDataListMapMapListS(numValue,Eag2012.TAB_DESCRIPTION,Eag2012.HOLDINGS,Eag2012.HOLDING_EXTENT,descriptionTable.getString("textExtent"),i,i,0,0,0);
			 eag2012.setNumValue(numValue);
		}
		this.log.debug("End method: \"parseTextExtent\"");
	}

	/**
	 * Method that checks if it have to create if the object<br>  
	 * and fill it with data the {@link Eag2012} Eag2012 object part of TextYear
	 */
	private void parseTextYear(){
		this.log.debug("Method start: \"parseTextYear\"");
		if (!descriptionTable.has("textYearWhenThisNameWasUsedFrom_"+j) || !descriptionTable.has("textYearWhenThisNameWasUsedTo_"+j)) {
			  // From date.
			 List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateValue = eag2012.getFromDateStandardDate();
			 if(dateValue==null){
				 dateValue = new ArrayList<Map<String,Map<String,Map<String,List<List<String>>>>>>();
			 }
			   Map<String, Map<String, Map<String, List<List<String>>>>> dateMapMapMap = null;
			   dateMapMapMap = createMapMapMapList(dateMapMapMap,dateValue,i);			   
			   Map<String, Map<String, List<List<String>>>> dateMapMap = null;
			   dateMapMap = createMapMapList_list(dateMapMapMap,dateMapMap,Eag2012.TAB_DESCRIPTION);			   
			   Map<String, List<List<String>>> datesMap = null;
			   datesMap = createMapList_list(dateMapMap,datesMap,Eag2012.HOLDINGS);			   
			   List<List<String>> listDates = null;
			   listDates=createList_list(datesMap,listDates,Eag2012.HOLDING_SUBSECTION);			   
			   List<String> dates = null;
			   dates = createList2(listDates,dates,0);
			   dates.add("");
			   dates = createList2(listDates,dates,0);
			   datesMap.put(Eag2012.HOLDING_SUBSECTION, listDates);
			   dateMapMap.put(Eag2012.HOLDINGS,datesMap);
			   dateMapMapMap.put(Eag2012.TAB_DESCRIPTION,dateMapMap);
			   if( dateValue.size()>i){
				 dateValue.set(i,dateMapMapMap);
			   }else{
				 dateValue.add(dateMapMapMap);
			   }
			   eag2012.setFromDateStandardDate(dateValue);
		   	   // To date.
			   dateValue = eag2012.getToDateStandardDate();
			   if(dateValue==null){
				   dateValue = new ArrayList<Map<String, Map<String, Map<String, List<List<String>>>>>>();
			   }
			   dateMapMapMap = null;
			   dateMapMapMap =  createMapMapMapList(dateMapMapMap,dateValue,i);
			   dateMapMap = null;
			   dateMapMap = createMapMapList_list(dateMapMapMap,dateMapMap,Eag2012.TAB_DESCRIPTION);
			   datesMap = null;
			   datesMap = createMapList_list(dateMapMap,datesMap,Eag2012.HOLDINGS);
			   listDates = null;
			   listDates=createList_list(datesMap,listDates,Eag2012.HOLDING_SUBSECTION);
			   dates = null;
			   dates = createList2(listDates,dates,0);
			   dates.add("");
			   if(listDates.size()>0){
				   listDates.set(0, dates);
			   }else{
				   listDates.add(dates);
			   }
			   datesMap.put(Eag2012.HOLDING_SUBSECTION, listDates);
			   dateMapMap.put(Eag2012.HOLDINGS,datesMap);
			   dateMapMapMap.put(Eag2012.TAB_DESCRIPTION,dateMapMap);
			   if( dateValue.size()>i){
				 dateValue.set(i,dateMapMapMap);
			   }else{
				 dateValue.add(dateMapMapMap);
			   }
			   eag2012.setToDateStandardDate(dateValue);			   
		  }
		this.log.debug("End method: \"parseTextYear\"");
	}

	/**
	 * Method that checks if it have to create if the object<br>  
	 * and fill it with data the {@link Eag2012} Eag2012 object part of DateRangeOfHoldings
	 */
	private void parseDateRangeOfHoldings()throws JSONException{
		this.log.debug("Method start: \"parseDateRangeOfHoldings\"");
		j=0;
		  while(descriptionTable.has("textYearWhenThisNameWasUsedFrom_"+(++j)) && descriptionTable.has("textYearWhenThisNameWasUsedTo_"+j)){
			  if(descriptionTable.has("textYearWhenThisNameWasUsedFrom_"+j)){				 
				   List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateValue = eag2012.getFromDateStandardDate();
				   dateValue = createDataListMapMapList(dateValue,Eag2012.TAB_DESCRIPTION,Eag2012.HOLDINGS,Eag2012.HOLDING_SUBSECTION,descriptionTable.getString("textYearWhenThisNameWasUsedFrom_"+j),i,0);
				   eag2012.setFromDateStandardDate(dateValue);
			  }
			  if(descriptionTable.has("textYearWhenThisNameWasUsedTo_"+j)){				 
				  List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateValue = eag2012.getToDateStandardDate();
				  dateValue = createDataListMapMapList(dateValue,Eag2012.TAB_DESCRIPTION,Eag2012.HOLDINGS,Eag2012.HOLDING_SUBSECTION,descriptionTable.getString("textYearWhenThisNameWasUsedTo_"+j),i,0);
				  eag2012.setToDateStandardDate(dateValue);
				}
		  }
		  this.log.debug("End method: \"parseDateRangeOfHoldings\"");	
	}

	/**
	 * Method that checks if it have to create if the object<br>  
	 * and fill it with data the {@link Eag2012} Eag2012 object part of DateRangeOfHoldings
	 */
	private void parseDateOfHoldings() throws JSONException{
		this.log.debug("Method start: \"parseDateOfHoldings\"");
		j=0;
		  while(descriptionTable.has("textYearWhenThisNameWasUsed_"+(++j))){
			if(descriptionTable.has("textYearWhenThisNameWasUsed_"+j)){			
				 List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateValue = eag2012.getDateStandardDate();
				 dateValue = createDataListMapMapList(dateValue,Eag2012.TAB_DESCRIPTION,Eag2012.HOLDINGS,Eag2012.HOLDING_SUBSECTION,descriptionTable.getString("textYearWhenThisNameWasUsed_"+j),i,0);
				 eag2012.setDateStandardDate(dateValue);
			}
		  }
		  this.log.debug("End method: \"parseDateOfHoldings\"");
	}

	/**
	 * Method that checks if it have to create if the object <br>
	 * and fill it with data the {@link Eag2012} Eag2012 object part of HoldingDescription
	 */
	private void parseHoldingDescription() throws JSONException{
		this.log.debug("Method start: \"parseHoldingDescription\"");
		j=0;
		while (descriptionTable.has("textArchivalAndOtherHoldings_"+(++j)) && (descriptionTable.has("selectLanguageArchivalAndOtherHoldings_"+(j)))){
			if(descriptionTable.has("textArchivalAndOtherHoldings_"+j)){				
				List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();				
				descriptiveNotePValue= createListMapList(descriptiveNotePValue,Eag2012.TAB_DESCRIPTION,Eag2012.HOLDINGS,descriptionTable.getString("textArchivalAndOtherHoldings_"+j),i,i,0,0);
				eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
			}
			if(descriptionTable.has("selectLanguageArchivalAndOtherHoldings_"+j)){				
				List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePLang();				 
				descriptiveNotePValue = createListMapList(descriptiveNotePValue,Eag2012.TAB_DESCRIPTION,Eag2012.HOLDINGS,descriptionTable.getString("selectLanguageArchivalAndOtherHoldings_"+j),i,i,0,0);
				 eag2012.setDescriptiveNotePLang(descriptiveNotePValue);
			}
		}
		this.log.debug("End method: \"parseHoldingDescription\"");
	}

	/**
	 * Method that checks if it have to create if the object<br>  
	 * and fill it with data the {@link ag2012} Eag2012 object part of BuildingDescription 
	 */
	private void parseBuildingDescription() throws JSONException{
		this.log.debug("Method start: \"parseBuildingDescription\"");
		target1 = "textBuilding";
		 target2 = "selectLanguageBuilding";
		 targetNumber = 1;
		 do{
			 target1 = ((target1.indexOf("_")!=-1)?target1.substring(0,target1.indexOf("_")):target1)+"_"+targetNumber;
			 target2 = ((target2.indexOf("_")!=-1)?target2.substring(0,target2.indexOf("_")):target2)+"_"+targetNumber;
			 targetNumber++;
			 if(descriptionTable.has(target1)){				
				 List<Map<String, Map<String, List<String>>>> descriptiveNotePs = eag2012.getDescriptiveNotePValue();				
				 descriptiveNotePs = createListMapList(descriptiveNotePs,Eag2012.TAB_DESCRIPTION,Eag2012.BUILDING,descriptionTable.getString(target1),i,i,0,0);
				 eag2012.setDescriptiveNotePValue(descriptiveNotePs);
			 }
			 if(descriptionTable.has(target2)){				 
				 List<Map<String, Map<String, List<String>>>> descriptiveNotePs = eag2012.getDescriptiveNotePLang();				 
				 descriptiveNotePs = createListMapList(descriptiveNotePs,Eag2012.TAB_DESCRIPTION,Eag2012.BUILDING,descriptionTable.getString(target2),i,i,0,0);
				 eag2012.setDescriptiveNotePLang(descriptiveNotePs);
			 }
		 }while(descriptionTable.has(target1) && descriptionTable.has(target2));
		 if(descriptionTable.get("textRepositoryArea")!=null){			
			 List<Map<String,Map<String,Map<String,List<String>>>>> numValue = eag2012.getNumValue();			 
			 numValue = createDataListMapMapListS(numValue,Eag2012.TAB_DESCRIPTION,Eag2012.BUILDING,Eag2012.BUILDING_AREA,descriptionTable.getString("textRepositoryArea"),i,i,0,0,0);
			 eag2012.setNumValue(numValue);
		 }
		 if(descriptionTable.get("textLengthOfShelf")!=null){			
			 List<Map<String,Map<String,Map<String,List<String>>>>> numValue = eag2012.getNumValue();
			 numValue = createDataListMapMapListS(numValue,Eag2012.TAB_DESCRIPTION,Eag2012.BUILDING,Eag2012.BUILDING_LENGTH,descriptionTable.getString("textLengthOfShelf"),i,i,0,0,0);
			 eag2012.setNumValue(numValue);
		 }
		 this.log.debug("End method: \"parseBuildingDescription\"");
	}

	/**
	 * Method that checks if it have to create if the object<br>  
	 * and fill it with data the {@link Eag2012{@link  Eag2012 object part of AdministrativeStructure
	 */
	private void parseAdministrativeStructure() throws JSONException{
		this.log.debug("Method start: \"parseAdministrativeStructure\"");
		target1 = "textUnitOfAdministrativeStructure";
		 target2 = "selectLanguageUnitOfAdministrativeStructure";
		 targetNumber = 1;
		 do{
			 target1 = ((target1.indexOf("_")!=-1)?target1.substring(0,target1.indexOf("_")):target1)+"_"+targetNumber;
			 target2 = ((target2.indexOf("_")!=-1)?target2.substring(0,target2.indexOf("_")):target2)+"_"+targetNumber;
			 targetNumber++;
			
			 if(descriptionTable.has(target1)){
				 List<List<String>> adminUnit = eag2012.getAdminunitValue();
				 adminUnit = createDataList(adminUnit,descriptionTable.getString(target1),i);
				 eag2012.setAdminunitValue(adminUnit);
			 }
			 if(descriptionTable.has(target2)){
				  List<List<String>> adminUnitLang = eag2012.getAdminunitLang();
				 adminUnitLang = createDataList(adminUnitLang,descriptionTable.getString(target2),i);
				 eag2012.setAdminunitLang(adminUnitLang);
			 }
		 }while(descriptionTable.has(target1) && descriptionTable.has(target2));
		 this.log.debug("End method: \"parseAdministrativeStructure\"");
	}

	/**
	 * Method that checks if it have to create if the object<br>  
	 * and fill it with data the {@link Eag2012} Eag2012 object part of RuleOfRepositorySuppression
	 */
	private void parseRuleOfRepositorySuppression() throws JSONException{
		this.log.debug("Method start: \"parseRuleOfRepositorySuppression\"");
		j=0;
		while(descriptionTable.has("textRuleOfRepositorySuppression_"+(++j)) && descriptionTable.has("selectLanguageRuleOfRepositorySuppression_"+(j))) {
		 if(descriptionTable.has("textRuleOfRepositorySuppression_"+j)){
			 List<Map<String, List<String>>> listRulesMap = eag2012.getRuleValue();
			 listRulesMap = createData(listRulesMap,Eag2012.REPOSITOR_SUP,descriptionTable.getString("textRuleOfRepositorySuppression_"+j),i,i,i);
			 eag2012.setRuleValue(listRulesMap);			
		 }
		 if(descriptionTable.has("selectLanguageRuleOfRepositorySuppression_"+j)){
			 List<Map<String,List<String>>> listRulesLang =eag2012.getRuleLang();
			 listRulesLang = createData(listRulesLang,Eag2012.REPOSITOR_SUP,descriptionTable.getString("selectLanguageRuleOfRepositorySuppression_"+j),i,i,i);
			 eag2012.setRuleLang(listRulesLang);			 
		 }
		}
		this.log.debug("End method: \"parseRuleOfRepositorySuppression\"");
	}

	/**
	 * Method that checks if it have to create if the object<br>  
	 * and fill it with data the {@link Eag2012} Eag2012 object part of RepositorFound
	 */
	private void parseRepositorFound() throws JSONException{
		this.log.debug("Method start: \"parseRepositorFound\"");
		j=0;
		while(descriptionTable.has("textRuleOfRepositoryFoundation_"+(++j)) && descriptionTable.has("selectLanguageRuleOfRepositoryFoundation_"+(j))) {
		 if(descriptionTable.has("textRuleOfRepositoryFoundation_"+j)){
			 List<Map<String, List<String>>> listRulesMap = eag2012.getRuleValue();
			 listRulesMap = createData(listRulesMap,Eag2012.REPOSITOR_FOUND,descriptionTable.getString("textRuleOfRepositoryFoundation_"+j),i,i,i);
			 eag2012.setRuleValue(listRulesMap);			
		 }
		 if(descriptionTable.has("selectLanguageRuleOfRepositoryFoundation_"+j)){
			 List<Map<String,List<String>>> listRulesLang =eag2012.getRuleLang();
			 listRulesLang = createData(listRulesLang,Eag2012.REPOSITOR_FOUND,descriptionTable.getString("selectLanguageRuleOfRepositoryFoundation_"+j),i,i,i);
			 eag2012.setRuleLang(listRulesLang);		 }
		}
		this.log.debug("End method: \"parseRepositorFound\"");
	}

	/**
	 * Method that checks if it have to create if the object<br>  
	 * and fill it with data the {@link Eag2012} Eag2012 object part of RepositoryDescription
	 */
	private void parseRepositoryDescription() throws JSONException{
		this.log.debug("Method start: \"parseRepositoryDescription\"");
		j=0;
		while (descriptionTable.has("textRepositoryHistory_"+(++j)) && (descriptionTable.has("selectLanguageRepositoryHistory_"+(j)))){
			if(descriptionTable.has("textRepositoryHistory_"+j)){
				List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();				
				descriptiveNotePValue = createListMapList(descriptiveNotePValue,Eag2012.TAB_DESCRIPTION,Eag2012.REPOSITORHIST,descriptionTable.getString("textRepositoryHistory_"+j),i,i,0,0);
				 eag2012.setDescriptiveNotePValue(descriptiveNotePValue);				
			 }
			if(descriptionTable.has("selectLanguageRepositoryHistory_"+j)){
				List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePLang();				
				descriptiveNotePValue = createListMapList(descriptiveNotePValue,Eag2012.TAB_DESCRIPTION,Eag2012.REPOSITORHIST,descriptionTable.getString("selectLanguageRepositoryHistory_"+j),i,i,0,0);
				eag2012.setDescriptiveNotePLang(descriptiveNotePValue);				
				}
		}
		this.log.debug("End method: \"parseRepositoryDescription\"");
	}
}
