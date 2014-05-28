package eu.apenet.dashboard.archivallandscape;

import java.util.List;

import eu.apenet.commons.infraestructure.ArchivalInstitutionUnit;
import eu.apenet.commons.infraestructure.CountryUnit;
import eu.apenet.dashboard.tree.DynatreeAction;

public class ArchivalLandscapeDynatreeAction extends DynatreeAction {
	
	protected StringBuffer generateCountriesTreeJSON(List<CountryUnit> countryList) {
		
		StringBuffer buffer = new StringBuffer();
		CountryUnit countryUnit = null;
		
		buffer.append(START_ARRAY);
		for (int i=0; i < countryList.size(); i++) {
			//It is necessary to build a JSON response to display all the countries in Directory Tree
			countryUnit = countryList.get(i);
			if(countryUnit.isHasArchivalInstitutions()){
				//The Country has one or several archival institutions
				buffer.append(START_ITEM);
				addTitle(buffer, countryUnit.getLocalizedName());
				buffer.append(COMMA);
				buffer.append(NOT_CHECKBOX);
				buffer.append(COMMA);
				buffer.append(FOLDER_LAZY);
				buffer.append(COMMA);
//				buffer.append(NO_LINK);
//				buffer.append(COMMA);
				addKey(buffer, countryUnit.getCountry().getId(), "country");
				buffer.append(END_ITEM);
			}else{
				//The Country doesn't have any archival institutions
				buffer.append(START_ITEM);
				addTitle(buffer, countryUnit.getLocalizedName());
				buffer.append(COMMA);
				buffer.append(NOT_CHECKBOX);
				buffer.append(COMMA);
				buffer.append(FOLDER_NOT_LAZY);
				buffer.append(COMMA);
//				buffer.append(NO_LINK);
//				buffer.append(COMMA);
				buffer.append(NOT_SELECTABLE);
				buffer.append(COMMA);
				addKey(buffer, countryUnit.getCountry().getId(), "country");
				buffer.append(END_ITEM);
			}
			if (i!=countryList.size()-1){
				buffer.append(COMMA);
			}
		}
		
		buffer.append(END_ARRAY);
		countryUnit = null;
		return buffer;

	}
	
	public StringBuffer generateArchivalInstitutionsTreeJSON(List<ArchivalInstitutionUnit> archivalInstitutionList,String targetAction,String language,boolean selectable) {
		
		StringBuffer buffer = new StringBuffer();
		ArchivalInstitutionUnit archivalInstitutionUnit = null;
		
		buffer.append(START_ARRAY);
		for (int i=0; i < archivalInstitutionList.size(); i++) {
			//It is necessary to build a JSON response to display all the archival institutions in Directory Tree
			archivalInstitutionUnit = archivalInstitutionList.get(i);
			if (archivalInstitutionUnit.getIsgroup() && archivalInstitutionUnit.getNumberOfArchivalInstitutions() > 0){
				//The Archival Institution is a group and it has archival institutions within it
				buffer.append(START_ITEM);
				addTitle(buffer, archivalInstitutionUnit.getAiname());
				buffer.append(COMMA);
				buffer.append(NOT_CHECKBOX);
				buffer.append(COMMA);
				buffer.append(FOLDER_LAZY);
				buffer.append(COMMA);
				buffer.append(LINK);
				buffer.append(COMMA);
				if(selectable){
					buffer.append(SELECTABLE);
				}else{
					buffer.append(NOT_SELECTABLE);
				}
				buffer.append(COMMA);
				addKey(buffer, archivalInstitutionUnit.getAiId(), "archival_institution_group");
				buffer.append(END_ITEM);
			}
			else if (archivalInstitutionUnit.getIsgroup() && archivalInstitutionUnit.getNumberOfArchivalInstitutions() == 0) {
				//The Archival Institution is a group but it doesn't have any archival institutions within it
				buffer.append(START_ITEM);
				addTitle(buffer, archivalInstitutionUnit.getAiname());
				buffer.append(COMMA);
				buffer.append(NOT_CHECKBOX);
				buffer.append(COMMA);
				buffer.append(FOLDER_NOT_LAZY);
				buffer.append(COMMA);
				buffer.append(LINK);
				buffer.append(COMMA);
				if(selectable){
					buffer.append(SELECTABLE);
				}else{
					buffer.append(NOT_SELECTABLE);
				}
				buffer.append(COMMA);
				addKey(buffer, archivalInstitutionUnit.getAiId(), "archival_institution_group");
				buffer.append(END_ITEM);
			}
			else if (!archivalInstitutionUnit.getIsgroup()){
				//The Archival Institution is a leaf
				buffer.append(START_ITEM);
				addTitle(buffer, archivalInstitutionUnit.getAiname());
				buffer.append(COMMA);
				addUrl(buffer, archivalInstitutionUnit.getAiId(), targetAction, language);
				buffer.append(COMMA);
				buffer.append(NOT_CHECKBOX);
				buffer.append(COMMA);
				addKey(buffer, archivalInstitutionUnit.getAiId(), "archival_institution_eag");
				buffer.append(END_ITEM);
			}
			if (i!=archivalInstitutionList.size()-1){
				buffer.append(COMMA);
			}
		}
		
		buffer.append(END_ARRAY);
		archivalInstitutionUnit = null;
		return buffer;
	}
	
	private static void addTitle(StringBuffer buffer, String title) {
		buffer.append("\"title\":");
		if (title == null) {
			title = " ";
		} else {
			title = title.replace('"', '\'');
			title = title.replaceAll("[\n\t\r]", "");
			if (title.trim().length() == 0) {
				title = " ";
			}
			title = title.replaceAll("<", "&lt;");
		}
		buffer.append(" \"" + title + "\"");
	}
	
	private static void addKey(StringBuffer buffer, Number key, String nodeType) {
		if (nodeType.equals("country")) {
			buffer.append("\"key\":" + "\"country_" + key.toString() + "\"");
		}
		else if (nodeType.equals("archival_institution_group")) {
			buffer.append("\"key\":" + "\"aigroup_" + key.toString() + "\"");			
		}
		else if (nodeType.equals("archival_institution_eag")) {
			buffer.append("\"key\":" + "\"aieag_" + key.toString() + "\"");			
		}
		else if (nodeType.equals("archival_institution_no_eag")) {
			buffer.append("\"key\":" + "\"ainoeag_" + key.toString() + "\"");			
		}
	}
	
	private void addUrl(StringBuffer buffer, Integer identifier, String targetAction ,String language) {
		buffer.append("\"url\":");
		buffer.append(" \"" + getServletRequest().getContextPath()+targetAction);
		buffer.append("?ai_id=" + identifier.toString());
		buffer.append("&navTreeLang=" + language);
		buffer.append("\" ");
	}
}
