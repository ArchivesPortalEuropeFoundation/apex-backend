package eu.apenet.dashboard.tree;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.infraestructure.ArchivalInstitutionUnit;
import eu.apenet.commons.infraestructure.CountryUnit;
import eu.apenet.commons.infraestructure.NavigationTree;
import eu.apenet.commons.infraestructure.StrutsNavigationTree;

/**
 * Servlet implementation class TopCLevelsServlet
 */
public class GeneratePreviewALTreeJSONAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	
	private final Logger log = Logger.getLogger(GeneratePreviewALTreeJSONAction.class);
	
	private static final String FOLDER_LAZY = "\"isFolder\": true, \"isLazy\": true";
	private static final String FOLDER_NOT_LAZY = "\"isFolder\": true";
	private static final String NOT_SELECTABLE = "\"unselectable\": true";
	private static final String NOT_CHECKBOX = "\"hideCheckbox\": true";
	private static final String NO_LINK = "\"noLink\": true";
	private static final long serialVersionUID = 1L;
	private static final String UTF8 = "UTF-8";
	private static final String END_ARRAY = "]\n";
	private static final String START_ARRAY = "[\n";
	private static final String END_ITEM = "}";
	private static final String START_ITEM = "{";
	private static final String COMMA = ",";
	private static final String SHOW_EAG_ACTION = "/showEAGPreviewAL.action";

	private HttpServletResponse response;
	private HttpServletRequest request;
	
	private String navTreeLang;	//This attribute is used for selecting the Directory Tree Language
	private String couId; 		//This attribute is used for selecting the Country



	//Getters and Setters
	public void setCouId(String couId) {
		this.couId = couId;
	}

	public String getCouId() {
		return couId;
	}
	
	public void setNavTreeLang(String navTreeLang) {
		this.navTreeLang = navTreeLang;
	}

	public String getNavTreeLang() {
		return navTreeLang;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unchecked")
	public String execute() {
		
		//The first directory tree initialization should only display the country
		//within the Archival Landscape
		try {
			request.setCharacterEncoding(UTF8);
			response.setCharacterEncoding(UTF8);
			response.setContentType("application/json");
			Writer writer = new OutputStreamWriter(response.getOutputStream(), UTF8);			
			NavigationTree navigationTree = new StrutsNavigationTree();
			
			//It is necessary to obtain all the countries within the Archival Landscape
			List<CountryUnit> countryList = new ArrayList<CountryUnit>(); 
			countryList.add(navigationTree.getCountry(Integer.parseInt(this.getCouId())));
			writer.write(generateCountriesTreeJSON(countryList).toString());
			writer.close();

			navigationTree = null;
			writer = null;
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return null;
		
	}

	//Method to create a JSON response in order to expand an archival institution on demand within the Directory Tree
	@SuppressWarnings("unchecked")
	public String generateArchivalInstitutionPartJSON() {
		
		try {
			request.setCharacterEncoding(UTF8);
			response.setCharacterEncoding(UTF8);
			response.setContentType("application/json");
			String nodeId = request.getParameter("nodeId");
			Writer writer = new OutputStreamWriter(response.getOutputStream(), UTF8);
			NavigationTree navigationTree = new StrutsNavigationTree();
			List<ArchivalInstitutionUnit> archivalInstitutionList = navigationTree.getArchivalInstitutionsByParentAiId(nodeId);
			Collections.sort(archivalInstitutionList);
			writer.write(generateArchivalInstitutionsTreeJSON(archivalInstitutionList).toString());
			writer.close();

			navigationTree = null;
			writer = null;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		
		return null;
				
	}
	
	private StringBuffer generateCountriesTreeJSON(List<CountryUnit> countryList) {
		
		StringBuffer buffer = new StringBuffer();
		CountryUnit countryUnit = null;
		
		buffer.append(START_ARRAY);
		for (int i=0; i < countryList.size(); i++) {
			//It is necessary to build a JSON response to display all the countries in Directory Tree
			countryUnit = countryList.get(i);
			if (countryUnit.isHasArchivalInstitutions()){
				//The Country has one or several archival institutions
				buffer.append(START_ITEM);
				addTitle(buffer, countryUnit.getLocalizedName());
				buffer.append(COMMA);
				buffer.append(NOT_CHECKBOX);
				buffer.append(COMMA);
				buffer.append(FOLDER_LAZY);
				buffer.append(COMMA);
				buffer.append(NO_LINK);
				buffer.append(COMMA);
				addKey(buffer, countryUnit.getCountry().getCouId(), "country");
				buffer.append(END_ITEM);
			}
			else {
				//The Country doesn't have any archival institutions
				buffer.append(START_ITEM);
				addTitle(buffer, countryUnit.getLocalizedName());
				buffer.append(COMMA);
				buffer.append(NOT_CHECKBOX);
				buffer.append(COMMA);
				buffer.append(FOLDER_NOT_LAZY);
				buffer.append(COMMA);
				buffer.append(NO_LINK);
				buffer.append(COMMA);
				buffer.append(NOT_SELECTABLE);
				buffer.append(COMMA);
				addKey(buffer, countryUnit.getCountry().getCouId(), "country");
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

	private StringBuffer generateArchivalInstitutionsTreeJSON(List<ArchivalInstitutionUnit> archivalInstitutionList) {
		
		StringBuffer buffer = new StringBuffer();
		ArchivalInstitutionUnit archivalInstitutionUnit = null;
		
		buffer.append(START_ARRAY);
		for (int i=0; i < archivalInstitutionList.size(); i++) {
			//It is necessary to build a JSON response to display all the archival institutions in Directory Tree
			archivalInstitutionUnit = archivalInstitutionList.get(i);
			if (archivalInstitutionUnit.getIsgroup() && archivalInstitutionUnit.getNumberOfArchivalInstitutions() > 0){
				//The Archival Institution is a group and it has archival institutions within it
				buffer.append(START_ITEM);
				addTitle(buffer, archivalInstitutionUnit.getAiScndname());
				buffer.append(COMMA);
				buffer.append(NOT_CHECKBOX);
				buffer.append(COMMA);
				buffer.append(FOLDER_LAZY);
				buffer.append(COMMA);
				buffer.append(NO_LINK);
				buffer.append(COMMA);
				addKey(buffer, archivalInstitutionUnit.getAiId(), "archival_institution_group");
				buffer.append(END_ITEM);
			}
			else if (archivalInstitutionUnit.getIsgroup() && archivalInstitutionUnit.getNumberOfArchivalInstitutions() == 0) {
				//The Archival Institution is a group but it doesn't have any archival institutions within it
				buffer.append(START_ITEM);
				addTitle(buffer, archivalInstitutionUnit.getAiScndname());
				buffer.append(COMMA);
				buffer.append(NOT_CHECKBOX);
				buffer.append(COMMA);
				buffer.append(FOLDER_NOT_LAZY);
				buffer.append(COMMA);
				buffer.append(NO_LINK);
				buffer.append(COMMA);
				buffer.append(NOT_SELECTABLE);
				buffer.append(COMMA);
				addKey(buffer, archivalInstitutionUnit.getAiId(), "archival_institution_group");
				buffer.append(END_ITEM);
			}
			else if (!archivalInstitutionUnit.getIsgroup()){
				//The Archival Institution is a leaf
				buffer.append(START_ITEM);
				addTitle(buffer, archivalInstitutionUnit.getAiScndname());
				buffer.append(COMMA);
				if (archivalInstitutionUnit.getPathEAG() != null && !archivalInstitutionUnit.getPathEAG().equals("")) {
					//The archival institution has EAG
					addKey(buffer, archivalInstitutionUnit.getAiId(), "archival_institution_eag");
					buffer.append(COMMA);
					addUrl(buffer, archivalInstitutionUnit.getAiId(), this.getNavTreeLang());
				}
				else {
					addKey(buffer, archivalInstitutionUnit.getAiId(), "archival_institution_no_eag");
					buffer.append(COMMA);
					buffer.append(NO_LINK);					
				}
				buffer.append(COMMA);
				buffer.append(NOT_CHECKBOX);					
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
	
	private void addUrl(StringBuffer buffer, Integer identifier, String language) {
		buffer.append("\"url\":");
		buffer.append(" \"" + this.request.getContextPath() +  SHOW_EAG_ACTION);
		buffer.append("?ai_id=" + identifier.toString());
		buffer.append("&navTreeLang=" + language);
		buffer.append("\" ");
	}

}
