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
 * {@link JSONObject} JSONObject (got from params) in part of contact.
 */
public class ContactJsonObjToEag2012 extends AbstractJsonObjtoEag2012 implements JsonObjToEag2012{
	private Eag2012 eag2012;
	private JSONObject contact;
	private final Logger log = Logger.getLogger(getClass());
	private int x;

	/**
	 * Method that checks if it have to create if the object  and fill it with data the {@link Eag2012} Eag2012 object part of visitorAddress 
	 * @param postalAddress {@link JSONObject} JSONObject
	 */
	private void parseVisitorAddress(JSONObject visitorAddress) throws JSONException{
		this.log.debug("Method start: \"parseVisitorAddress\"");
		if(visitorAddress!=null){			
			List<String> listStreet = new ArrayList<String>();
			List<String> listCities = new ArrayList<String>();
			List<String> listDistrict = new ArrayList<String>();
			List<String> listLocalAuthority = new ArrayList<String>();
			List<String> listAutonomus = new ArrayList<String>();
			List<String> listCountries = new ArrayList<String>();
			List<String> listLatitudes = new ArrayList<String>();
			List<String> listLongitudes = new ArrayList<String>();
			List<String> listStreetLanguage = new ArrayList<String>();
			int i=1;
			if(visitorAddress.has("contactTableVisitorsAddress_"+i)){
		      do{
		    	    JSONObject visitorAddressTable = visitorAddress.getJSONObject("contactTableVisitorsAddress_"+i);
					listStreet.add(replaceIfExistsSpecialReturnString(visitorAddressTable.getString("textContactStreetOfTheInstitution")));
					listStreetLanguage.add(replaceIfExistsSpecialReturnString(visitorAddressTable.getString("selectLanguageVisitorAddress")));
		            listCities.add(replaceIfExistsSpecialReturnString(visitorAddressTable.getString("textContactCityOfTheInstitution")));
		            listDistrict.add(replaceIfExistsSpecialReturnString(visitorAddressTable.getString("textContactDistrictOfTheInstitution")));
		            listLocalAuthority.add(replaceIfExistsSpecialReturnString(visitorAddressTable.getString("textContactCountyOfTheInstitution")));
		            listAutonomus.add(replaceIfExistsSpecialReturnString(visitorAddressTable.getString("textContactRegionOfTheInstitution")));
		            listCountries.add(replaceIfExistsSpecialReturnString(visitorAddressTable.getString("textContactCountryOfTheInstitution")));
		            listLatitudes.add(replaceIfExistsSpecialReturnString(visitorAddressTable.getString("textContactLatitudeOfTheInstitution")));
		            listLongitudes.add(replaceIfExistsSpecialReturnString(visitorAddressTable.getString("textContactLongitudeOfTheInstitution")));
		      }while(visitorAddress.has("contactTableVisitorsAddress_"+(++i)));
		      List<Map<String, List<String>>> tempListList = eag2012.getStreetValue();
		      tempListList = createDataListList2(tempListList,Eag2012.TAB_CONTACT,listStreet,x,x);
		      eag2012.setStreetValue(tempListList);
		      tempListList = eag2012.getStreetLang();
		      tempListList = createDataListList2(tempListList,Eag2012.TAB_CONTACT,listStreetLanguage,x,x);
		      eag2012.setStreetLang(tempListList);					
		      tempListList = eag2012.getCitiesValue();
			  tempListList = createDataListList2(tempListList,Eag2012.TAB_CONTACT,listCities,x,x);
			  eag2012.setCitiesValue(tempListList);					
			  //begin listDistrict
				List<List<String>> localEntityValue = null;
				if(eag2012.getLocalentityValue()==null){
					localEntityValue = new ArrayList<List<String>>();
				}else{					localEntityValue = eag2012.getLocalentityValue();
				}
				if(localEntityValue.size()>x){
					localEntityValue.set(x,listDistrict);
				}else{
					localEntityValue.add(listDistrict);
				}
				eag2012.setLocalentityValue(localEntityValue);				
				//end listDistrict
				List<List<String>> tempListList2 = null;
				if(eag2012.getSecondemValue()==null){
					tempListList2 = new ArrayList<List<String>>();
				}else{
					tempListList2 = eag2012.getSecondemValue();
				}
				tempListList2.add(listLocalAuthority);
				eag2012.setSecondemValue(tempListList2);				
				tempListList2 = null;
				if(eag2012.getFirstdemValue()==null){
					tempListList2 = new ArrayList<List<String>>();
				}else{
					tempListList2 = eag2012.getFirstdemValue();
				}
				tempListList2.add(listAutonomus);
				eag2012.setFirstdemValue(tempListList2);				
				tempListList = eag2012.getCountryValue();
			     tempListList = createDataListList2(tempListList,Eag2012.TAB_CONTACT,listCountries,x,x);
				eag2012.setCountryValue(tempListList);				
				tempListList = eag2012.getLocationLatitude();
			    tempListList = createDataListList2(tempListList,Eag2012.TAB_CONTACT,listLatitudes,x,x);
				eag2012.setLocationLatitude(tempListList);					
				tempListList = eag2012.getLocationLongitude();
			    tempListList = createDataListList2(tempListList,Eag2012.TAB_CONTACT,listLongitudes,x,x);
				eag2012.setLocationLongitude(tempListList);
		      }
		}//end visitorAddress
		this.log.debug("End method: \"parseVisitorAddress\"");
	}

	/**
	 * Method that checks if it have to create if the object  and fill it with data the {@link ag2012} Eag2012 object part of postalAddress 
	 * @param postalAddress {@link JSONObject} JSONObject	
	 */
	private void parsePostalAdress(JSONObject postalAddress) throws JSONException{
		this.log.debug("Method start: \"parsePostalAdress\"");
		if(postalAddress!=null){
			List<String> listStreet = new ArrayList<String>();
			List<String> listStreetLanguage = new ArrayList<String>();
			List<String> listCities = new ArrayList<String>();
			int i=1;
			if(postalAddress.has("contactTablePostalAddress_"+i)){
				do{
					JSONObject postalAddressTable = postalAddress.getJSONObject("contactTablePostalAddress_"+i);
					listStreet.add(replaceIfExistsSpecialReturnString(postalAddressTable.getString("textContactPAStreet")));
					listStreetLanguage.add(replaceIfExistsSpecialReturnString(postalAddressTable.getString("selectContactLanguagePostalAddress")));
					listCities.add(replaceIfExistsSpecialReturnString(postalAddressTable.getString("textContactPACity")));

				}while(postalAddress.has("contactTablePostalAddress_"+(++i)));
				//begin listStreet
				List<Map<String, List<String>>> tempListList2 = eag2012.getPostalStreetValue();
				tempListList2 = createDataListList2(tempListList2,Eag2012.TAB_CONTACT,listStreet,x,x);
				eag2012.setPostalStreetValue(tempListList2);
				//begin listStreetLanguage					
				tempListList2 = eag2012.getPostalStreetLang();
				tempListList2 = createDataListList2(tempListList2,Eag2012.TAB_CONTACT,listStreetLanguage,x,x);
				eag2012.setPostalStreetLang(tempListList2);	
				//begin listCities
				tempListList2 = eag2012.getMunicipalityPostalcodeValue();
				tempListList2 = createDataListList2(tempListList2,Eag2012.TAB_CONTACT,listCities,x,x);
				eag2012.setMunicipalityPostalcodeValue(tempListList2);	
			} else {
				// Add values for empty "Postal Address" in the repository.
				listStreet.add("");
				listStreetLanguage.add("");
				listCities.add("");
				//begin listStreet
				List<Map<String, List<String>>>  tempListList2 = eag2012.getPostalStreetValue();
				tempListList2 = createDataListList2(tempListList2,Eag2012.TAB_CONTACT,listStreet,x,x);
				eag2012.setPostalStreetValue(tempListList2);
				//begin listStreetLanguage
				tempListList2 = eag2012.getPostalStreetLang();
				tempListList2 = createDataListList2(tempListList2,Eag2012.TAB_CONTACT,listStreetLanguage,x,x);
				eag2012.setPostalStreetLang(tempListList2);
				//begin listCities
				tempListList2 = eag2012.getMunicipalityPostalcodeValue();
				tempListList2 = createDataListList2(tempListList2,Eag2012.TAB_CONTACT,listCities,x,x);
				eag2012.setMunicipalityPostalcodeValue(tempListList2);
			}
		}
		this.log.debug("End method: \"parsePostalAdress\"");
	}

	/**
	 * Method that create de jsonobj de {@link Eag2012} Eag2012 for part contact
	 * @param eag2012 {@link Eag2012} Eag2012 
	 * @param jsonObj {@link JSONObject} JSONObject
	 * @return {@link Eag2012} Eag2012 
	 */ 
	@Override
	public Eag2012 JsonObjToEag2012(Eag2012 eag20122, JSONObject jsonObj)throws JSONException {
		eag2012=eag20122;
		contact = jsonObj.getJSONObject("contact");
		if(contact!=null){
			x = 0;
			while(contact.has("contactTable_"+(x+1))){ //each child of contact is the container of visitor address and all attributes
				JSONObject contactTable = contact.getJSONObject("contactTable_"+(x+1));
				//Contact visitorsAddress

			if(contactTable.has("textNameOfRepository")){
				String nameOfRepository = replaceIfExistsSpecialReturnString(contactTable.getString("textNameOfRepository"));
				List<String> repositorNames = eag2012.getRepositoryNameValue();
				if(repositorNames==null){
					repositorNames = new ArrayList<String>();
				}
				repositorNames.add(nameOfRepository);
				eag2012.setRepositoryNameValue(repositorNames);
			}
			if(contactTable.has("selectRoleOfRepository")){
				String roleOfRepository = replaceIfExistsSpecialReturnString(contactTable.getString("selectRoleOfRepository"));
				List<String> rolesOfRepository = eag2012.getRepositoryRoleValue();
				if(rolesOfRepository==null){
					rolesOfRepository = new ArrayList<String>();
				}
				rolesOfRepository.add(roleOfRepository);
				eag2012.setRepositoryRoleValue(rolesOfRepository);
			}
			JSONObject visitorAddress = contactTable.getJSONObject("visitorsAddress");
			parseVisitorAddress(visitorAddress);
			JSONObject postalAddress = contactTable.getJSONObject("postalAddress");	
			parsePostalAdress(postalAddress);
			int i=1;
			if (x > 0) {
				i = 0;
			}			
			while(contactTable.has("textContactTelephoneOfTheInstitution_"+(++i))){
				List<Map<String, Map<String, List<String>>>> telephones = eag2012.getTelephoneValue();
				telephones =createListMapList(telephones,Eag2012.TAB_CONTACT,Eag2012.ROOT,contactTable.getString("textContactTelephoneOfTheInstitution_"+i),x,x,0,0);
				eag2012.setTelephoneValue(telephones);				
			}
		    i=0;
			while(contactTable.has("textContactFaxOfTheInstitution_"+(++i))){				
				List<Map<String, List<String>>> listMapFaxList = eag2012.getFaxValue();
				
				listMapFaxList = createDataListList(listMapFaxList,Eag2012.TAB_CONTACT,contactTable.getString("textContactFaxOfTheInstitution_"+i),x,x,0);
				eag2012.setFaxValue(listMapFaxList);
			}
			 i=1;
			 if (x > 0) {
				 i = 0;
			 }
			 while(contactTable.has("textContactEmailOfTheInstitution_"+(++i))){
				 List<Map<String, Map<String, List<String>>>> listMapEmailList = eag2012.getEmailHref();
				 listMapEmailList =createListMapList(listMapEmailList,Eag2012.TAB_CONTACT,Eag2012.ROOT,contactTable.getString("textContactEmailOfTheInstitution_"+i),x,x,0,0);
				 eag2012.setEmailHref(listMapEmailList);
			 }
			 i=1;
			 if (x > 0) {
				 i = 0;
			 }
			 while(contactTable.has("textContactLinkTitleForEmailOfTheInstitution_"+(++i))){
				 List<Map<String, Map<String, List<String>>>> listMapEmailValueList = eag2012.getEmailValue();
				 listMapEmailValueList = createListMapList(listMapEmailValueList,Eag2012.TAB_CONTACT,Eag2012.ROOT,contactTable.getString("textContactLinkTitleForEmailOfTheInstitution_"+i),x,x,0,0);
				 eag2012.setEmailValue(listMapEmailValueList);				
			 }
			 i=1;
			 if (x > 0) {
				 i = 0;
			 }
			 while(contactTable.has("selectEmailLanguageOfTheInstitution_"+(++i))){
				 List<Map<String, Map<String, List<String>>>> listMapEmailValueList = eag2012.getEmailLang();
				 listMapEmailValueList = createListMapList(listMapEmailValueList,Eag2012.TAB_CONTACT,Eag2012.ROOT,contactTable.getString("selectEmailLanguageOfTheInstitution_"+i),x,x,0,0);
				 eag2012.setEmailLang(listMapEmailValueList);
			 }
			 i=1;
			 if (x > 0) {
				 i = 0;
			 }
			 while(contactTable.has("textContactWebOfTheInstitution_"+(++i))){
				 List<Map<String, Map<String, List<String>>>> listMapWebpageHrefList = eag2012.getWebpageHref();
				 listMapWebpageHrefList = createListMapList(listMapWebpageHrefList,Eag2012.TAB_CONTACT,Eag2012.ROOT,contactTable.getString("textContactWebOfTheInstitution_"+i),x,x,0,0);
				 eag2012.setWebpageHref(listMapWebpageHrefList);
			 }
			 i=1;
			 if (x > 0) {
				 i = 0;
			 }
			 while(contactTable.has("selectWebpageLanguageOfTheInstitution_"+(++i))){
				 List<Map<String, Map<String, List<String>>>> listMapWebpageHrefList = eag2012.getWebpageLang();
				 listMapWebpageHrefList = createListMapList(listMapWebpageHrefList,Eag2012.TAB_CONTACT,Eag2012.ROOT,contactTable.getString("selectWebpageLanguageOfTheInstitution_"+i),x,x,0,0);
				 eag2012.setWebpageLang(listMapWebpageHrefList);
			 }
			 i=1;
			 if (x > 0) {
				 i = 0;
			 }
			 while(contactTable.has("textContactLinkTitleForWebOfTheInstitution_"+(++i))){
				 List<Map<String, Map<String, List<String>>>> listMapWebList = eag2012.getWebpageValue();
				 listMapWebList = createListMapList(listMapWebList,Eag2012.TAB_CONTACT,Eag2012.ROOT,contactTable.getString("textContactLinkTitleForWebOfTheInstitution_"+i),x,x,0,0);
				 eag2012.setWebpageValue(listMapWebList);
			}
			x++;
		}
	}	
		return eag2012;
	}
}
