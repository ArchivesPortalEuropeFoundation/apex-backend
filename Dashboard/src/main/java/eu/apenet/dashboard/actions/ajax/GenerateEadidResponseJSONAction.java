package eu.apenet.dashboard.actions.ajax;

import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.dao.UpFileDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.FindingAid;



/**
 * Servlet implementation class TopCLevelsServlet
 */
public class GenerateEadidResponseJSONAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	
	private final Logger log = Logger.getLogger(GenerateEadidResponseJSONAction.class);
	
	private static final String UTF8 = "UTF-8";
	private static final String END_ITEM = "}";
	private static final String START_ITEM = "{";
	private static final String COMMA = ",";
	private static final String SEPARATOR = "\"";
	private static String MESSAGE = "\"message\": ";
	/*Ojo hay que comprobar si hay que poner las comillas dobles o bien quitarlas.*/

	private HttpServletResponse response;
	private HttpServletRequest request;
	
	private int ai_id;
    
    private String eadid;
    private String neweadid;
    
    private Integer fileId;
    
    private String responseSaveChanges;
    
    public String getResponseSaveChanges() {
		return responseSaveChanges;
	}

	public void setResponseSaveChanges(String responseSaveChanges) {
		this.responseSaveChanges = responseSaveChanges;
	}

	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public String getNeweadid() {
		return neweadid;
	}

	public void setNeweadid(String neweadid) {
		this.neweadid = neweadid;
	}

	public String getEadid() {
		return eadid;
	}

	public void setEadid(String eadid) {
		this.eadid = eadid;
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
	public String execute() {
		
		//The first directory tree initialization should only display the countries
		//within the Archival Landscape
		try {
			request.setCharacterEncoding(UTF8);
			response.setCharacterEncoding(UTF8);
			response.setContentType("application/json");
			Writer writer = new OutputStreamWriter(response.getOutputStream(), UTF8);
			UpFileDAO upfileDao = DAOFactory.instance().getUpFileDAO();
			this.ai_id = upfileDao.findById(fileId).getAiId();
			
			String m = checkNewEADID().toString();
			writer.write(m);
			
			writer.close();

			writer = null;
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		//return SUCCESS;
		return null;
		
	}
			
	public String executeWithoutFile() {
		System.err.println();
		// Try to check the new EADID when the users edit an EAD file already ingested.
		try {
			request.setCharacterEncoding(UTF8);
			response.setCharacterEncoding(UTF8);
			response.setContentType("application/json");
			Writer writer = new OutputStreamWriter(response.getOutputStream(), UTF8);
			FindingAidDAO findingAidDAO = DAOFactory.instance().getFindingAidDAO();
			this.ai_id = findingAidDAO.findById(this.getFileId()).getAiId();
			
			String m = checkNewEADID().toString();
			writer.write(m);
			
			writer.close();

			writer = null;
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	public StringBuffer checkNewEADID()
	{	
		StringBuffer buffer = new StringBuffer();
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        boolean eadidUsed = eadDAO.isEadidUsed(this.neweadid.trim(), ai_id, FindingAid.class) != null;
       
        if (eadidUsed)
        {
        	buffer.append(START_ITEM);
        	buffer.append(MESSAGE + SEPARATOR + getText("content.message.EadidNotAvailable") + SEPARATOR);
        	buffer.append(COMMA);
        	buffer.append("\"existingChangeEADIDAnswers\": \"KO\"");
        	buffer.append(COMMA);
        	buffer.append("\"eadid\":" + SEPARATOR + this.eadid + SEPARATOR);
        	buffer.append(END_ITEM);
        	LOG.info("There is another file with this new EADID.");
        	return buffer;
        	
        }
        else{
        	if (neweadid.trim().isEmpty())
    		{
    			buffer.append(START_ITEM);
    	    	buffer.append(MESSAGE + SEPARATOR + getText("content.message.EadidEmpty") + SEPARATOR);
    	    	buffer.append(COMMA);
    	    	buffer.append("\"existingChangeEADIDAnswers\": " + SEPARATOR + "KO" + SEPARATOR);
    	    	buffer.append(COMMA);
    	    	buffer.append("\"eadid\":" + SEPARATOR + this.eadid + SEPARATOR);
    	    	buffer.append(END_ITEM);
    	    	return buffer;
    		}
        	else
        	{
		    	buffer.append(START_ITEM);
		    	buffer.append(MESSAGE + SEPARATOR + getText("content.message.EadidAvailable") + SEPARATOR);
		    	buffer.append(COMMA);
		    	buffer.append("\"existingChangeEADIDAnswers\": " + SEPARATOR + "OK" + SEPARATOR);
		    	buffer.append(COMMA);
		    	buffer.append("\"eadid\":" + SEPARATOR + this.eadid + SEPARATOR);
		    	buffer.append(COMMA);
		    	buffer.append("\"komessage\":" + SEPARATOR + getText("content.message.Repeatedfile") + SEPARATOR);
		    	buffer.append(END_ITEM);
		    	return buffer;
        	}
        }
	}

}
