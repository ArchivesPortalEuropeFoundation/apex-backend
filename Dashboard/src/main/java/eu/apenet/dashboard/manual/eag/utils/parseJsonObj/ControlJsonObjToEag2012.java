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
 * {@link JSONObject} JSONObject (got from params) in part of control.
 */
public class ControlJsonObjToEag2012 extends AbstractJsonObjtoEag2012 implements JsonObjToEag2012{
	private JSONObject control;
	private Eag2012 eag2012;
	private final Logger log = Logger.getLogger(getClass());
	private int i;

	/**
	 * Method that checks if it have to create if the object  and fill it with data the {@link Eag2012} Eag2012 object part of selectDescription 
	 */
	private void parseSelectDescription() throws JSONException{
		this.log.debug("Method start: \"parseSelectDescription\"");
		i = 1;
		while(control.has("selectDescriptionLanguage_"+i) || (control.has("selectDescriptionScript_"+i))){
			List<String> listLanguageCode = eag2012.getLanguageLanguageCode();
			if(listLanguageCode==null){
				listLanguageCode = new ArrayList<String>();
			}
			if(control.has("selectDescriptionLanguage_"+i)){
				listLanguageCode.add(replaceIfExistsSpecialReturnString(control.getString("selectDescriptionLanguage_"+i)));
				eag2012.setLanguageLanguageCode(listLanguageCode);
			}
			List<String> listScript = eag2012.getScriptScriptCode();
			if(listScript==null){
				listScript = new ArrayList<String>();
			}
			if(control.has("selectDescriptionScript_"+i)){
				listScript.add(replaceIfExistsSpecialReturnString(control.getString("selectDescriptionScript_"+i)));
				eag2012.setScriptScriptCode(listScript);
			}
			if (listLanguageCode.size() > listScript.size()) {
				listScript.add("");
			} else if (listLanguageCode.size() < listScript.size()) {
				listLanguageCode.add("");
			}
			i++;
		}		
		this.log.debug("End method: \"parseSelectDescription\"");
	}

	/**
	 * Method that checks if it have to create if the object  and fill it with data the {@link Eag2012} Eag2012 object part of conventionDeclaration 
	 */
	private void parseConventionDeclaration() throws JSONException{
		this.log.debug("Method start: \"parseConventionDeclaration\"");
		i=0;
		  while(control.has("textContactAbbreviation_"+(++i)) && (control.has("textContactFullName_"+i))){
			if(control.has("textContactAbbreviation_"+i)){
			  List<String> abbreviation = eag2012.getAbbreviationValue();
			  if(abbreviation == null){
				  abbreviation = new ArrayList<String>();
			  }
			  abbreviation.add(replaceIfExistsSpecialReturnString(control.getString("textContactAbbreviation_"+i)));
            eag2012.setAbbreviationValue(abbreviation);
			}
			if(control.has("textContactFullName_"+i)){
				List<Map<String, List<String>>> listCitationMap = eag2012.getCitationValue();
			    listCitationMap=createData(listCitationMap,Eag2012.TAB_CONTROL, control.getString("textContactFullName_"+i),0,0,0);
				eag2012.setCitationValue(listCitationMap);
			}
		 }//end while		
		  this.log.debug("End method: \"parseConventionDeclaration\"");
	}

	/**
	 * Method that create de jsonobj de {@link Eag2012} Eag2012 for part control
	 * @param eag2012 {@link Eag2012} Eag2012 
	 * @param jsonObj {@link JSONObject} JSONObject
	 * @return {@link Eag2012} Eag2012 
	 */ 
	@Override
	public Eag2012 JsonObjToEag2012(Eag2012 eag20122, JSONObject jsonObj)throws JSONException {
		eag2012=eag20122;
		control = jsonObj.getJSONObject("control");
		if(control!=null){
			if(control.has("textDescriptionIdentifier")){
				String recordId = eag2012.getRecordIdValue();
				if(recordId==null){
					recordId = replaceIfExistsSpecialReturnString(control.getString("textDescriptionIdentifier"));
					eag2012.setRecordIdValue(recordId);
				}
			}
			if(control.has("selectLanguagePesonresponsible")){
				String agentLang = eag2012.getAgentLang();
				if(agentLang==null){
					agentLang = replaceIfExistsSpecialReturnString(control.getString("selectLanguagePesonresponsible"));
					eag2012.setAgentLang(agentLang);
				}
			}
			if(control.has("textPesonResponsible")){
				String agentValue=replaceIfExistsSpecialReturnString(control.getString("textPesonResponsible"));
				eag2012.setAgentValue(agentValue);
			}
			parseSelectDescription();			
		  //convention declaration
			parseConventionDeclaration();		  
		}//end first if
		return eag2012;
	}
}
