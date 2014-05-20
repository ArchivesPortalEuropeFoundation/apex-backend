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

import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.dao.UpFileDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;
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
	/*NOTE: Checks if it's needed put double quotes or remove them*/

	private HttpServletResponse response;
	private HttpServletRequest request;
	
	private int ai_id;
    
    private String eadid;
    private String neweadid;
    
    private Integer fileId;
    
    private String responseSaveChanges;

    private String type;
    
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
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
			
			String m = checkNewID(this.getType()).toString();
			writer.write(m);
			
			writer.close();

			writer = null;
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		//return SUCCESS;
		return null;
		
	}

	/**
	 * Method to check if the EADID set when a FA is edited is already in use.
	 */
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
			
			String m = checkNewID(XmlType.EAD_FA.getName()).toString();
			writer.write(m);
			
			writer.close();

			writer = null;
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * Method to write the response of the checks if the ID is already in use.
	 */
	public StringBuffer checkNewID(String type)
	{	
		StringBuffer buffer = new StringBuffer();
        boolean idUsed = false;

        // Check if its needed to recover the list of IDs of the EAC-CPF files
        // or of the FA files.
        if (XmlType.EAC_CPF.getName().equalsIgnoreCase(type)) {
        	EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        	idUsed = eacCpfDAO.isEacCpfIdUsed(this.neweadid.trim(), this.ai_id, EacCpf.class) != null;
        } else {
    		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        	idUsed = eadDAO.isEadidUsed(this.neweadid.trim(), ai_id, FindingAid.class) != null;
        }
       
        if (idUsed)
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
