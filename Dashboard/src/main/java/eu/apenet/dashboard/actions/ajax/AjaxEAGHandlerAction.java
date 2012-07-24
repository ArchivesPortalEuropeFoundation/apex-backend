package eu.apenet.dashboard.actions.ajax;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import eu.apenet.commons.infraestructure.ArchivalInstitutionUnit;
import eu.apenet.commons.infraestructure.HoldingsGuideUnit;
import eu.apenet.dashboard.AbstractInstitutionAction;

public class AjaxEAGHandlerAction extends AbstractInstitutionAction implements ServletRequestAware, ServletResponseAware {
	
	private static final long serialVersionUID = -1232516884380663688L;

	private final Logger log = Logger.getLogger(AjaxEAGHandlerAction.class);
	
	private static final String UTF8 = "UTF-8";
	private static final String END_ARRAY = "]\n";
	private static final String START_ARRAY = "[\n";
	private static final String END_ITEM = "}";
	private static final String START_ITEM = "{";
	private static final String COMMA = ",";
	private static String HGTITLE = "\"hgtitle\": ";
	private static String INFORMATION_MESSAGE = "\"informationMessage\": ";
	

	private HttpServletResponse response;
	private HttpServletRequest request;
	


	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

	
	public String execute() {
		return null;		
	}
	
	//Method to create a JSON response in order to get the HG indexed for this archival institution
	//The HGs will be displayed in the EAG Web Form for repositorguidePossibleHGTitle select if it is required
	public String generaterepositorguidePossibleHGTitlePartJSON() {
		
		try {
			request.setCharacterEncoding(UTF8);
			response.setCharacterEncoding(UTF8);
			response.setContentType("application/json");
			Writer writer = new OutputStreamWriter(response.getOutputStream(), UTF8);
			writer.write(getHoldingsGuideIndexedJSON().toString());
			writer.close();
			
			writer = null;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		return SUCCESS;
				
	}
	
	private StringBuffer getHoldingsGuideIndexedJSON() {

		StringBuffer buffer = new StringBuffer();
		List<HoldingsGuideUnit> holdingsGuideIndexed = ArchivalInstitutionUnit.getHoldingsGuide(getAiId());
		
		buffer.append(START_ARRAY);
		for (int i = 0; i < holdingsGuideIndexed.size(); i ++) {
			//It is necessary to build a JSON response to display all the HG's titles indexed for this archival institution
			buffer.append(START_ITEM);
			addTitle(buffer, holdingsGuideIndexed.get(i).getHgTitle());
			buffer.append(END_ITEM);

			if (i != holdingsGuideIndexed.size() - 1){
				buffer.append(COMMA);
			}
		}
		buffer.append(END_ARRAY);
		
		holdingsGuideIndexed = null;
		
		return buffer;
	}

	
	//Method to create a JSON response in order to get message for Information field within the EAg webform
	public String generateInformationMessageJSON() {
		
		StringBuffer buffer = new StringBuffer();
		
		try {
			request.setCharacterEncoding(UTF8);
			response.setCharacterEncoding(UTF8);
			response.setContentType("application/json");
			Writer writer = new OutputStreamWriter(response.getOutputStream(), UTF8);
			
			buffer.append(START_ARRAY);
			buffer.append(START_ITEM);
			buffer.append(INFORMATION_MESSAGE);
            /////////// LAST MODIFICATIONS REGARDING TICKET #652 -- Begin
            /////////////////////////////////////////////////////////////
			// Remove this line and uncomment the other one if 'Link to holdings guide' default text is wanted again within EAG web form
			buffer.append(" \"" + "" + "\"");
			//buffer.append(" \"" + getText("label.ai.hg.information.content.default") + "\"");
            /////////// LAST MODIFICATIONS REGARDING TICKET #652 -- End
            /////////////////////////////////////////////////////////////
			buffer.append(END_ITEM);
			buffer.append(END_ARRAY);
			
			writer.write(buffer.toString());
			writer.close();
			
			buffer = null;
			writer = null;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		return SUCCESS;
				
	}

	private static void addTitle(StringBuffer buffer, String title) {
		buffer.append(HGTITLE);
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
}
