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
import eu.apenet.persistence.dao.Ead3DAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.dao.UpFileDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.Ead3;
import eu.apenet.persistence.vo.FindingAid;

/**
 * <p>
 * Main class used to check if the new value of the element
 * <b>{@code <eadid>}</b>, is not already used for another <i>EAD/apeEAD</i> of
 * the same institution.
 * </p>
 * <p>
 * Also is used to check if the new value of the element
 * <b>{@code <recordid>}</b>, is not already used for another
 * <i>EAC-CPF/apeEAC-CPF</i> of the same institution.
 * </p><br/>
 * <p>
 * The methods of this class are called thought different Struts actions but
 * only in the following cases:
 * </p>
 * <p>
 * <ul>
 * <li>The user is <b>editing an apeEAD</b>, of type <i>Finding aid</i>, and has
 * changed the content of the element <i>{@code <eadid>}</i>.</li>
 * <li>The user has <b>uploaded a file</b> (an <i>EAD/apeEAD</i> of any type or
 * an <i>EAC-CPF/apeEAC-CPF</i>) and the field used to <b>identify</b>
 * it (<i>{@code <eadid>}</i> or <i>{@code <recordid>}</i>) <b>is empty</b>
 * and the system has asked the user in order to provide it.</li>
 * <li>The user has <b>uploaded a file</b> (an <i>EAD/apeEAD</i> of any type or
 * an <i>EAC-CPF/apeEAC-CPF</i>) and the field used to <b>identify</b>
 * it (<i>{@code <eadid>}</i> or <i>{@code <recordid>}</i>) <b>is already in
 * use</b> for another file (of the same type) previously uploaded to the
 * institution.</li>
 * </ul>
 * </p><br/>
 * <p>
 * Any response is sent to page using JSON.
 * </p>
 */
public class GenerateEadidResponseJSONAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    /**
     * <p>
     * Constant for the class to use for the log.
     * </p>
     */
    private final Logger log = Logger.getLogger(GenerateEadidResponseJSONAction.class);
    /**
     * <p>
     * Constant to define the desired encoding for the response.
     * </p>
     */
    private static final String UTF8 = "UTF-8";
    /**
     * <p>
     * Constant for the closing element of the response.
     * </p>
     */
    private static final String END_ITEM = "}";
    /**
     * <p>
     * Constant for the start element of the response.
     * </p>
     */
    private static final String START_ITEM = "{";
    /**
     * <p>
     * Constant for the char which will be used to separate different response
     * elements.
     * </p>
     */
    private static final String COMMA = ",";
    /**
     * <p>
     * Constant for the char which will be used to enclose the textual elements
     * in the response.
     * </p>
     */
    private static final String SEPARATOR = "\"";
    /**
     * <p>
     * Constant to define the <i>message</i> part of the response.
     * </p>
     */
    private static String MESSAGE = "\"message\": ";
    /*NOTE: Checks if it's needed put double quotes or remove them*/

    /**
     * <p>
     * Variable to store the response which will be sent to the client.
     * </p>
     */
    private HttpServletResponse response;
    /**
     * <p>
     * Variable to store the request from the client.
     * </p>
     */
    private HttpServletRequest request;
    /**
     * <p>
     * Variable to store the identifier of the current archival institution.
     * </p>
     */
    private int ai_id;
    /**
     * <p>
     * Variable to store the current identifier of the EAD/apeEAD or the
     * EAC-CPF/apeEAC-CPF which is edited, filled or changed.
     * </p>
     */
    private String eadid;
    /**
     * <p>
     * Variable to store the new identifier of the EAD/apeEAD or the
     * EAC-CPF/apeEAC-CPF which is edited, filled or changed.
     * </p>
     */
    private String neweadid;
    /**
     * <p>
     * Variable to store the identifier in database of the current EAD/apeEAD or
     * the EAC-CPF/apeEAC-CPF which is edited, filled or changed.
     * </p>
     */
    private Integer fileId;
    /**
     * <p>
     * Variable to store the result of the save action.
     * </p>
     * <p>
     * <b>Note: This variable seems that currently is unused.</b>
     * </p>
     */
    private String responseSaveChanges;
    /**
     * <p>
     * Variable to store the type of the XML which identifier should be checked.
     * </p><br/>
     * <p>
     * Possible values are:
     * </p>
     * <p>
     * <ul>
     * <li><b>0</b> - Finding Aid.</li>
     * <li><b>1</b> - Holdings Guide.</li>
     * <li><b>2</b> - EAC-CPF.
     * <li><b>3</b> - Source Guide.</li>
     * </ul>
     * </p>
     */
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
     * <p>
     * Overrides the method to execute the action.
     * </p>
     * <p>
     * This method is called thought the Struts action
     * <i>"generateEadidResponseJSON"</i>
     * which is used in the <b>upload process</b> (for EAD/apeEAD and
     * EAC-CPF/apeEAC-CPF).
     * </p>
     * <p>
     * It is used to recover the current institution thought the <b>uploaded
     * file</b>, which is obtained form the file identifier (which is in
     * <i>{@link GenerateEadidResponseJSONAction#fileId}</i>).
     * </p>
     * <p>
     * Once the current institution is obtained, and in combination with the
     * type of the file (which is in
     * <i>{@link GenerateEadidResponseJSONAction#type}</i>), is checked if the
     * new identifier value is available or not, sending an appropriate message
     * to the user.
     * </p>
     *
     * @return null since we use the HttpResponse to write JSON data directly to
     * the page.
     *
     * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     * @see GenerateEadidResponseJSONAction#checkNewID(String)
     */
    public String execute() {
        this.log.debug("Entering method \"execute\".");

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

            this.log.debug("Leaving method \"execute\" when no error occurs.");
        } catch (Exception e) {
            this.log.debug("Leaving method \"execute\" when error occurs.");

            log.error(e.getMessage(), e);
        }

        //return SUCCESS;
        return null;

    }

    /**
     * <p>
     * This method is called thought the Struts action
     * <i>"generateEadidResponseJSONWhenEdit"</i>
     * which is used in the <b>edition of an apeEAD</b>.
     * </p>
     * </p>
     * <p>
     * It is used to recover the current institution thought the <b>edited
     * file</b>. This file always is an apeEAD (of type <i>Finding aid</i>) and
     * its identifier is available in
     * <i>{@link GenerateEadidResponseJSONAction#fileId}</i>).
     * </p>
     * <p>
     * Once the current institution is obtained, and in combination with the
     * type of the file (as mentioned <i>Finding aid</i>), is checked if the new
     * identifier value is available or not, sending an appropriate message to
     * the user.
     * </p>
     *
     * @return null since we use the HttpResponse to write JSON data directly to
     * the page.
     *
     * @see GenerateEadidResponseJSONAction#checkNewID(String)
     */
    public String executeWithoutFile() {
        this.log.debug("Entering method \"executeWithoutFile\".");
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

            this.log.debug("Leaving method \"executeWithoutFile\" when no error occurs.");
        } catch (Exception e) {
            this.log.debug("Leaving method \"executeWithoutFile\" when error occurs.");

            log.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * <p>
     * This method is called from
     * <i>"{@link GenerateEadidResponseJSONAction#execute()}"</i>
     * and from
     * <i>"{@link GenerateEadidResponseJSONAction#executeWithoutFile()}"</i>.
     * </p>
     * <p>
     * It is used to check if the new identifier, which is in
     * <i>{@link GenerateEadidResponseJSONAction#fileId}</i>, it is available to
     * be used, is empty/blank or is already in use.
     * </p>
     * <p>
     * Whatever it is the result of the check, and appropriate internationalized
     * message is sent to the user.
     * </p>
     * <p>
     * <b>NOTE</b>: This method takes into account that some characters, which
     * can be used as part of the identifier, are not possible to be sent
     * between the client and the server if they are not escaped. So previous to
     * make any of the checks the identifiers are unescaped.
     * </p>
     *
     * @param type {@link String} type of the XML which identifier should be
     * checked.
     *
     * @return
     * <p>
     * {@link StringBuffer} which an appropriate internationalized text
     * describing the result of the check.
     * </p>
     * <p>
     * Could be one of the follows (only shown the English text):
     * </p>
     * <p>
     * <ul>
     * <li><b>There is another file with this ID</b> - If already exists another
     * file for that institution, with same type, which have the same value as
     * identifier.</li>
     * <li><b>ID cannot be empty</b> - If the new identifier filled is empty or
     * blank.</li>
     * <li><b>ID is available</b> - If there is no one file, for that
     * institution and with same type, which have the same value as
     * identifier.</li>
     * </ul>
     * </p>
     */
    public StringBuffer checkNewID(String type) {
        this.log.debug("Entering method \"checkNewID\".");

        StringBuffer buffer = new StringBuffer();
        boolean idUsed = false;

        // Check if its needed to recover the list of IDs of the EAC-CPF files
        // or of the FA files.
        if (XmlType.EAC_CPF.getName().equalsIgnoreCase(type)) {
            EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
            idUsed = eacCpfDAO.isEacCpfIdUsed(this.neweadid.trim(), ai_id, EacCpf.class) != null;
        } else if (XmlType.EAD_3.getName().equalsIgnoreCase(type)) {
            Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
            idUsed = ead3DAO.isEad3IdUsed(this.neweadid.trim(), ai_id, Ead3.class) != null;
        } else {
            EadDAO eadDAO = DAOFactory.instance().getEadDAO();
            idUsed = eadDAO.isEadidUsed(this.neweadid.trim(), ai_id, FindingAid.class) != null;
        }

        // Escape char ".
        String escapedEADID = this.getEadid();
        if (escapedEADID.contains("\"")) {
            escapedEADID = escapedEADID.replaceAll("\"", "%22");
        }

        if (idUsed) {
            buffer.append(START_ITEM);
            buffer.append(MESSAGE + SEPARATOR + getText("content.message.EadidNotAvailable") + SEPARATOR);
            buffer.append(COMMA);
            buffer.append("\"existingChangeEADIDAnswers\": \"KO\"");
            buffer.append(COMMA);
            buffer.append("\"eadid\":" + SEPARATOR + escapedEADID + SEPARATOR);
            buffer.append(END_ITEM);
            LOG.info("There is another file with this new EADID.");

            this.log.debug("Leaving method \"checkNewID\" when another file has the new identifier.");

            return buffer;

        } else if (neweadid.trim().isEmpty()) {
            buffer.append(START_ITEM);
            buffer.append(MESSAGE + SEPARATOR + getText("content.message.EadidEmpty") + SEPARATOR);
            buffer.append(COMMA);
            buffer.append("\"existingChangeEADIDAnswers\": " + SEPARATOR + "KO" + SEPARATOR);
            buffer.append(COMMA);
            buffer.append("\"eadid\":" + SEPARATOR + escapedEADID + SEPARATOR);
            buffer.append(END_ITEM);

            this.log.debug("Leaving method \"checkNewID\" when the new identifier is empty or blank.");

            return buffer;
        } else {
            buffer.append(START_ITEM);
            buffer.append(MESSAGE + SEPARATOR + getText("content.message.EadidAvailable") + SEPARATOR);
            buffer.append(COMMA);
            buffer.append("\"existingChangeEADIDAnswers\": " + SEPARATOR + "OK" + SEPARATOR);
            buffer.append(COMMA);
            buffer.append("\"eadid\":" + SEPARATOR + escapedEADID + SEPARATOR);
            buffer.append(COMMA);
            buffer.append("\"komessage\":" + SEPARATOR + getText("content.message.Repeatedfile") + SEPARATOR);
            buffer.append(END_ITEM);

            this.log.debug("Leaving method \"checkNewID\" when no other file has the new identifier.");

            return buffer;
        }
    }

}
