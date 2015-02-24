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
 * {@link JSONObject} JSONObject (got from params) in part of indentity.
 */
public class IdentityJsonObjToEag2012 extends AbstractJsonObjtoEag2012 implements JsonObjToEag2012{
	private JSONObject identity;
	private JSONObject institutionNames;
	private Eag2012 eag2012;
	private int i;
	private int j;
	private final Logger log = Logger.getLogger(getClass());

	/**
	 * Method that checks if it have to create if the object  and fill it with data the {@link Eag2012} Eag2012 object part of checkListDate
	 */
	private void parseCheckListDate(){
		this.log.debug("Method start: \"parseCheckListDate\"");
		List<List<String>> dateList = null;
		List<List<String>> dateFromList = null;
		List<List<String>> dateToList = null;
		if (eag2012.getDateStandardDate() != null && !eag2012.getDateStandardDate().isEmpty()
				&& eag2012.getDateStandardDate().get(0) != null
				&& eag2012.getDateStandardDate().get(0).get(Eag2012.TAB_IDENTITY) != null
				&& eag2012.getDateStandardDate().get(0).get(Eag2012.TAB_IDENTITY).get(Eag2012.ROOT) != null
				&& eag2012.getDateStandardDate().get(0).get(Eag2012.TAB_IDENTITY).get(Eag2012.ROOT).get(Eag2012.ROOT_SUBSECTION) != null) {
			dateList = eag2012.getDateStandardDate().get(0).get(Eag2012.TAB_IDENTITY).get(Eag2012.ROOT).get(Eag2012.ROOT_SUBSECTION);
		} else {
			Map<String, List<List<String>>> datesMap = new HashMap<String, List<List<String>>>();
			datesMap.put(Eag2012.ROOT_SUBSECTION, new ArrayList<List<String>>());
			Map<String, Map<String, List<List<String>>>> datesMapMap = new HashMap<String, Map<String, List<List<String>>>>();
			datesMapMap.put(Eag2012.ROOT, datesMap);
			HashMap<String, Map<String, Map<String, List<List<String>>>>> datesMapMapMap = new HashMap<String, Map<String, Map<String, List<List<String>>>>>();
			datesMapMapMap.put(Eag2012.TAB_IDENTITY, datesMapMap);
			List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateStandardDate = new ArrayList<Map<String, Map<String, Map<String, List<List<String>>>>>>();
			dateStandardDate.add(datesMapMapMap);
			eag2012.setDateStandardDate(dateStandardDate);
			dateList = eag2012.getDateStandardDate().get(0).get(Eag2012.TAB_IDENTITY).get(Eag2012.ROOT).get(Eag2012.ROOT_SUBSECTION);
		}
		if (eag2012.getFromDateStandardDate() != null && !eag2012.getFromDateStandardDate().isEmpty()
				&& eag2012.getFromDateStandardDate().get(0) != null
				&& eag2012.getFromDateStandardDate().get(0).get(Eag2012.TAB_IDENTITY) != null
				&& eag2012.getFromDateStandardDate().get(0).get(Eag2012.TAB_IDENTITY).get(Eag2012.ROOT) != null
				&& eag2012.getFromDateStandardDate().get(0).get(Eag2012.TAB_IDENTITY).get(Eag2012.ROOT).get(Eag2012.ROOT_SUBSECTION) != null) {
			dateFromList = eag2012.getFromDateStandardDate().get(0).get(Eag2012.TAB_IDENTITY).get(Eag2012.ROOT).get(Eag2012.ROOT_SUBSECTION);
		} else {
			Map<String, List<List<String>>> datesMap = new HashMap<String, List<List<String>>>();
			datesMap.put(Eag2012.ROOT_SUBSECTION, new ArrayList<List<String>>());
			Map<String, Map<String, List<List<String>>>> datesMapMap = new HashMap<String, Map<String, List<List<String>>>>();
			datesMapMap.put(Eag2012.ROOT, datesMap);
			HashMap<String, Map<String, Map<String, List<List<String>>>>> datesMapMapMap = new HashMap<String, Map<String, Map<String, List<List<String>>>>>();
			datesMapMapMap.put(Eag2012.TAB_IDENTITY, datesMapMap);
			List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateStandardDate = new ArrayList<Map<String, Map<String, Map<String, List<List<String>>>>>>();
			dateStandardDate.add(datesMapMapMap);
			eag2012.setFromDateStandardDate(dateStandardDate);
			dateFromList = eag2012.getFromDateStandardDate().get(0).get(Eag2012.TAB_IDENTITY).get(Eag2012.ROOT).get(Eag2012.ROOT_SUBSECTION);
		}
		if (eag2012.getToDateStandardDate() != null && !eag2012.getToDateStandardDate().isEmpty()
				&& eag2012.getToDateStandardDate().get(0) != null
				&& eag2012.getToDateStandardDate().get(0).get(Eag2012.TAB_IDENTITY) != null
				&& eag2012.getToDateStandardDate().get(0).get(Eag2012.TAB_IDENTITY).get(Eag2012.ROOT) != null
				&& eag2012.getToDateStandardDate().get(0).get(Eag2012.TAB_IDENTITY).get(Eag2012.ROOT).get(Eag2012.ROOT_SUBSECTION) != null) {
			dateToList = eag2012.getToDateStandardDate().get(0).get(Eag2012.TAB_IDENTITY).get(Eag2012.ROOT).get(Eag2012.ROOT_SUBSECTION);
		}else {
			Map<String, List<List<String>>> datesMap = new HashMap<String, List<List<String>>>();
			datesMap.put(Eag2012.ROOT_SUBSECTION, new ArrayList<List<String>>());
			Map<String, Map<String, List<List<String>>>> datesMapMap = new HashMap<String, Map<String, List<List<String>>>>();
			datesMapMap.put(Eag2012.ROOT, datesMap);
			HashMap<String, Map<String, Map<String, List<List<String>>>>> datesMapMapMap = new HashMap<String, Map<String, Map<String, List<List<String>>>>>();
			datesMapMapMap.put(Eag2012.TAB_IDENTITY, datesMapMap);
			List<Map<String, Map<String, Map<String, List<List<String>>>>>> dateStandardDate = new ArrayList<Map<String, Map<String, Map<String, List<List<String>>>>>>();
			dateStandardDate.add(datesMapMapMap);

			eag2012.setToDateStandardDate(dateStandardDate);
			dateToList = eag2012.getToDateStandardDate().get(0).get(Eag2012.TAB_IDENTITY).get(Eag2012.ROOT).get(Eag2012.ROOT_SUBSECTION);
		}
		if (dateList.size() > dateFromList.size()) {
			dateFromList.add(new ArrayList<String>());
			dateToList.add(new ArrayList<String>());
		} else if (dateList.size() < dateFromList.size()) {
			dateList.add(new ArrayList<String>());
		}
		this.log.debug("End method: \"parseCheckListDate\"");
	}

	/**
	 * Method that checks if it have to create if the object  and fill it with data the {@link Eag2012} Eag2012 object part of InstitutionNames
	 */
	private void parseInstitutionNames()throws JSONException{
		this.log.debug("Method start: \"parseInstitutionNames\"");
		//JSONObject 
		institutionNames = identity.getJSONObject("institutionNames");
		//Name of the institution
		i=1;
		while(institutionNames.has("identityTableNameOfTheInstitution_"+(++i))){
			JSONObject nameOfTheInstitutionTable = institutionNames.getJSONObject("identityTableNameOfTheInstitution_"+i);
			if(nameOfTheInstitutionTable.has("textNameOfTheInstitution")){
				List<String> listAutforms = eag2012.getAutformValue();
				if(listAutforms==null){
					listAutforms = new ArrayList<String>();
				}
				listAutforms.add(replaceIfExistsSpecialReturnString(nameOfTheInstitutionTable.getString("textNameOfTheInstitution")));
				eag2012.setAutformValue(listAutforms);
			}
			if(nameOfTheInstitutionTable.has("noti_languageList")){
				List<String> listAutformLangs = eag2012.getAutformLang();
				if(listAutformLangs==null){
					listAutformLangs = new ArrayList<String>();
				}
				listAutformLangs.add(replaceIfExistsSpecialReturnString(nameOfTheInstitutionTable.getString("noti_languageList")));
				eag2012.setAutformLang(listAutformLangs);
			}
		}
		this.log.debug("End method: \"parseInstitutionNames\"");
	}

	/**
	 * Method that checks if it have to create if the object  and fill it with data the {@link Eag2012} Eag2012 object part of ParallelName 
	 */
	private void parseParallelName()throws JSONException{
		this.log.debug("Method start: \"parseParallelName\"");
		JSONObject parallelName = identity.getJSONObject("parallelNames");
		int i=1;
		//Parallel name of the institution
		while(parallelName.has("identityTableParallelNameOfTheInstitution_"+(++i))){
			JSONObject parallelNameOfTheInstitution = parallelName.getJSONObject("identityTableParallelNameOfTheInstitution_"+i);
			if(parallelNameOfTheInstitution.has("textParallelNameOfTheInstitution")){
				List<String> listParforms = eag2012.getParformValue();
				if(listParforms==null){
					listParforms = new ArrayList<String>();
				}
				listParforms.add(replaceIfExistsSpecialReturnString(parallelNameOfTheInstitution.getString("textParallelNameOfTheInstitution")));
				eag2012.setParformValue(listParforms);
			}
			if(parallelNameOfTheInstitution.has("pnoti_languageList")){
				List<String> listParformLangs = eag2012.getParformLang();
				if(listParformLangs==null){
					listParformLangs = new ArrayList<String>();
				}
				listParformLangs.add(replaceIfExistsSpecialReturnString(parallelNameOfTheInstitution.getString("pnoti_languageList")));
				eag2012.setParformLang(listParformLangs);
			}
		}	
		this.log.debug("End method: \"parseParallelName\"");
	}

	/**
	 * Method that checks if it have to create if the object  and fill it with data the {@link Eag2012} Eag2012 object part of FormerlyNames
	 */
	private void parseFormerlyNames()throws JSONException{
		this.log.debug("Method start: \"parseFormerlyNames\"");
		JSONObject formerlyName = identity.getJSONObject("formerlyNames");
		int i=0;
		while(formerlyName.has("identityTableFormerlyUsedName_"+(++i))){
			//Formerly used name
			JSONObject previousNameOfTheArchive = formerlyName.getJSONObject("identityTableFormerlyUsedName_"+i);
			if(previousNameOfTheArchive.has("textFormerlyUsedName")){
				List<String> listNonpreform = eag2012.getNonpreformValue();
				if(listNonpreform==null){
					listNonpreform = new ArrayList<String>();
				}
				listNonpreform.add(replaceIfExistsSpecialReturnString(previousNameOfTheArchive.getString("textFormerlyUsedName")));
				eag2012.setNonpreformValue(listNonpreform);
			}
			if(previousNameOfTheArchive.has("tfun_languageList")){
				List<String> listNonpreformLangs = eag2012.getNonpreformLang();
				if(listNonpreformLangs==null){
					listNonpreformLangs = new ArrayList<String>();
				}
				listNonpreformLangs.add(replaceIfExistsSpecialReturnString(previousNameOfTheArchive.getString("tfun_languageList")));
				eag2012.setNonpreformLang(listNonpreformLangs);
			}
			//Identity Single Year
			j=0;
			while(previousNameOfTheArchive.has("textYearWhenThisNameWasUsed_"+(++j))){
				List<Map<String, Map<String, Map<String, List<List<String>>>>>> yearValue = eag2012.getDateStandardDate();
				yearValue = createDataListMapMapList_identity(yearValue,Eag2012.TAB_IDENTITY,Eag2012.ROOT,Eag2012.ROOT_SUBSECTION,previousNameOfTheArchive.getString("textYearWhenThisNameWasUsed_"+j),i);
				eag2012.setDateStandardDate(yearValue);						
			}
			//Identity Range Year From
			j=0;
			while(previousNameOfTheArchive.has("textYearWhenThisNameWasUsedFrom_"+(++j)) && previousNameOfTheArchive.has("textYearWhenThisNameWasUsedTo_"+j)){
			if(previousNameOfTheArchive.has("textYearWhenThisNameWasUsedFrom_"+j)){
				 List<Map<String, Map<String, Map<String, List<List<String>>>>>> yearValue = eag2012.getFromDateStandardDate();
				 yearValue =  createDataListMapMapList_identity(yearValue,Eag2012.TAB_IDENTITY,Eag2012.ROOT,Eag2012.ROOT_SUBSECTION,previousNameOfTheArchive.getString("textYearWhenThisNameWasUsedFrom_"+j),i);
				 eag2012.setFromDateStandardDate(yearValue);	
			}
			if(previousNameOfTheArchive.has("textYearWhenThisNameWasUsedTo_"+j)){
			//Identity Range Year To				
				List<Map<String, Map<String, Map<String, List<List<String>>>>>> yearValue = eag2012.getToDateStandardDate();
				 yearValue =  createDataListMapMapList_identity(yearValue,Eag2012.TAB_IDENTITY,Eag2012.ROOT,Eag2012.ROOT_SUBSECTION,previousNameOfTheArchive.getString("textYearWhenThisNameWasUsedTo_"+j),i);
				 eag2012.setToDateStandardDate(yearValue);
			}	//end if toDate
		}//end While rangeDates
		// Check if list of "Date" and "DateRange" has the same size.
			parseCheckListDate();			
	  }//end while	
		this.log.debug("End method: \"parseFormerlyNames\"");
	}

	/**
	 * Method that create de jsonobj de {@link Eag2012} Eag2012 for part identity
	 * @param eag2012 {@link Eag2012} Eag2012 
	 * @param jsonObj {@link JSONObject} JSONObject
	 * @return {@link Eag2012} Eag2012 
	 */ 
	@Override
	public Eag2012 JsonObjToEag2012(Eag2012 eag20122, JSONObject jsonObj)
			throws JSONException {
		eag2012= eag20122;
		//JSONObject 
		identity = jsonObj.getJSONObject("identity");
		if(identity!=null){
			if(identity.has("institutionNames")){
				parseInstitutionNames();
			}
			if(identity.has("parallelNames")){
				parseParallelName();
			}
			if(identity.has("formerlyNames")){
				parseFormerlyNames();
			}//end if formerly name
			//Identity Type of the Institution
			if (identity.has("selectTypeOfTheInstitution")){
				List<String> listRepositoryType = new ArrayList<String>();
				listRepositoryType.add(replaceIfExistsSpecialReturnString(identity.getString("selectTypeOfTheInstitution")));
				eag2012.setRepositoryTypeValue(listRepositoryType);
			}
		}
		return eag2012;
	}
}
