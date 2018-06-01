package eu.apenet.dashboard.tree;

import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.DisplayUtils;
import eu.apenet.dashboard.services.ead.ChangeDynamicTask;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.EadContentDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;

/**
 * <p>
 * Main class used to build the <b>tree</b> which represent the structure of
 * the <i>apeEAD</i> which will be edited.
 * </p>
 * <p>
 * The methods of this class are called thought different Struts actions but
 * all of them are related to the functionality of <i>edit an apeEAD</i>.
 * </p>
 * <p>
 * This class determines which section of the tree is requested, built it and
 * send it to the client.
 * </p>
 * <p>
 * When a section contains a lot of children (<i>clevels</i>), the result part
 * of the tree is <i>automatically limited to a maximum of <b>20</b></i> nodes.
 * After the maximum level, a last node is added with the key <i>"More after..."</i>
 * in order to indicate the user that exists more elements that the current
 * ones displayed.
 * </p>
 * <p>
 * When a <b>title</b> (<i>{@code <unittitle>}</i>) of a node is too long, more
 * than <b>100</b> characters, is automatically truncated to that maximum
 * length adding to the final of the title three dots (<i>...</i>) which
 * indicates the user that the real title is not fully displayed.
 * </p><br/>
 * <p>
 * Any response is sent to page using JSON.
 * </p>
 */
public class GenerateTreeJSONAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	/**
	 * <p>
	 * Constant for the interface {@link Serializable}.
	 * </p>
	 *
	 * @see Serializable
	 */
	private static final long serialVersionUID = 3891790469632078308L;
	/**
	 * <p>
	 * Constant for the class to use for the log.
	 * </p>
	 */
	private final static Logger LOG = Logger.getLogger(GenerateTreeJSONAction.class);

	/**
	 * <p>
	 * Constant for the maximum amount of levels which should be displayed for
	 * each petition in the tree.
	 * </p>
	 */
	private static final int MAX_NUMBER_OF_CLEVELS = 20;
	/**
	 * <p>
	 * Constant for the maximum amount of characters which should be displayed
	 * for each node in the tree.
	 * </p>
	 */
	private static final int MAX_NUMBER_OF_CHARACTERS = 100;
	/**
	 * <p>
	 * Constant for the <i>folder</i> and <i>lazy</i> elements of the response.
	 * </p>
	 */
	private static final String FOLDER_LAZY = "\"isFolder\": true, \"isLazy\": true";
	/**
	 * <p>
	 * Constant to define the desired encoding for the response.
	 * </p>
	 */
	private static final String UTF8 = "UTF-8";
	/**
	 * <p>
	 * Constant for the closing element of the response. This also contains a
	 * carriage return.
	 * </p>
	 */
	private static final String END_ITEM_WITH_RETURN = "}\n";
	/**
	 * <p>
	 * Constant for the closing element of the response. This also contains a
	 * separator character.
	 * </p>
	 */
	private static final String END_ITEM_WITH_COMMA = "},";
	/**
	 * <p>
	 * Constant for the closing group of values tag for the same element of the
	 * response. This also contains a carriage return.
	 * </p>
	 */
	private static final String END_ARRAY = "]\n";
	/**
	 * <p>
	 * Constant for the star group of values tag for the same element of the
	 * response. This also contains a carriage return.
	 * </p>
	 */
	private static final String START_ARRAY = "[\n";
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
	 * Constant for the <i>folder</i> and <i>children</i> elements of the
	 * response. This also contains a carriage return.
	 * </p>
	 */
	private static final String FOLDER_WITH_CHILDREN = "\"isFolder\": true, \"children\": \n";
	/**
	 * <p>
	 * Constant for the char which will be used to separate different response
	 * elements.
	 * </p>
	 */
	private static final String COMMA = ",";
	/**
	 * <p>
     * Constant for the <i>0</i> value.
	 * </p>
     */
	private static final int ZERO = 0;
	/**
	 * <p>
     * Constant for the name of the tag when more values are before the current
     * node.
	 * </p>
     */
	private static final String MORE_VALUE_BEFORE = "before";
	/**
	 * <p>
     * Constant for the name of the tag when more values are after the current
     * node.
	 * </p>
     */
	private static final String MORE_VALUE_AFTER = "after";
	/**
	 * <p>
	 * Constant for the <i>icon</i> element of the response.
	 * </p>
	 */
    private static final String ICON_MORE = "\"icon\": \"more_folder.gif\"";

	/**
	 * <p>
	 * Variable to store the time consumed in the database interactions.
	 * </p>
	 */
	private long databaseTimeCost =0;
	/**
	 * <p>
	 * Variable to store which nodes should be displayed after a click of the
	 * user.
	 * </p><br/>
	 * <p>
	 * Possible values are:
	 * </p>
	 * <p>
	 *  <ul>
	 *   <li><b>before</b> - Should be loaded the values before the first
	 *   	node.</li>
	 *   <li><b>after</b> - Should be loaded the values after the last node.</li>
	 *  </ul>
	 * </p>
	 */
	private String more;

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

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}


	public String getMore() {
		return more;
	}

	public void setMore(String more) {
		this.more = more;
	}

	/**
	 * <p>
	 * Overrides the method to execute the action.
	 * </p>
	 * <p>
	 * This method is called thought the Struts action <i>"generateTreeJSON"</i>
	 * which is used in the <b>apeEAD edition</b> functionality. In fact, this
	 * method is called when it needed to load some part of the tree after the
	 * initial one.
	 * </p>
	 * <p>
	 * This load could be one of the following cases:
	 * </p>
     * <p>
     *  <ul>
     *   <li>Load <b>"more before"</b> - If the part to be loaded is the
     *   	contents before the first node of the tree for the current level.</li>
     *   <li>Load <b>"more after"</b> - If the part to be loaded is the
     *   	contents after the last node of the tree for the current level.</li>
     *   <li>Load <b>"content"</b> - If the part to be loaded is the contents
     *   	for the current node of the tree.</li>
     *  </ul>
     * </p>
     * <p>
     * To decide which of the previous cases is the selected one, and how the
     * needed data should be loaded, this method recovers all the needed
     * parameters from the <b>request</b> (this is stored in the global
     * variable <i>{@link GenerateTreeJSONAction#request}</i>).
     * </p>
	 * <p>
	 * The parameters recovered are:
	 * </p>
     * <p>
     *  <ul>
     *   <li><b>fileId</b> - Stores the identifier of the file on database
     *   	(automatically sent for all the cases).</li>
     *   <li><b>xmlTypeId</b> - Stores the type of the file (automatically sent
     *   	for all the cases and, by default, the type <i>0 - Finding aid</i>).</li>
     *   <li><b>parentId</b> - Stores the identifier of the parent element in
     *   	database (specifically sent for all the cases).</li>
     *   <li><b>ecId</b> - Stores the identifier of the current <i>ead_content</i>
     *   	(only sent for the cases in which should be loaded <i>"more
     *   	before/after"</i>).</li>
     *   <li><b>cId</b> - Stores the identifier of the current <i>c_level</i>
     *   	(automatically sent for all the cases).</li>
     *   <li><b>orderId</b> - Stores the numeric value which represents the
     *   	position of the current element inside the current level (only
     *   	sent for the cases in which should be loaded <i>"more before/after"</i>).</li>
     *   <li><b>max</b> - Stores the amount of elements before the current one
     *   	for the current level  (only sent for the cases in which should be
     *   	loaded <i>more before</i>).</li>
     *   <li><b>isWithUrl</b> - Stores the value for decide if the nodes of the
     *   	tree should contain URIs or not (specifically sent for all the
     *   	cases).</li>
     *  </ul>
     * </p>
     * <p>
     * <b>NOTE</b>: take in mind that, as specified, not all the parameters are
     * always sent in the request.
     * </p>
	 *
	 * @return null since we use the HttpResponse to write JSON data directly
	 * to the page.
	 *
	 * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 * @see GenerateTreeJSONAction#generateCLevelJSON(List, String, boolean)
	 * @see GenerateTreeJSONAction#generateRootJSON(EadContent, StringBuilder, String, boolean, String)
	 * @see GenerateTreeJSONAction#generateJSONWithSelectedItem(CLevel, String, boolean, String)
	 */
	public String execute() {
		LOG.debug("Entering method \"execute\".");
		try {
			long startTime = System.currentTimeMillis();
			long buildTime = -1;
			String fileIdString = request.getParameter("fileId");
			String xmlTypeIdString = request.getParameter("xmlTypeId");
			String parentIdString = request.getParameter("parentId");
			String ecIdString = request.getParameter("ecId");
			String cIdString = request.getParameter("cId");
			String orderIdString = request.getParameter("orderId");
			String maxString = request.getParameter("max");
            String isWithUrlString = request.getParameter("isWithUrl");
			request.setCharacterEncoding(UTF8);
			response.setCharacterEncoding(UTF8);
			response.setContentType("application/json");
			Writer writer = new OutputStreamWriter(response.getOutputStream(), UTF8);
			String path = request.getContextPath();
			CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
			EadDAO eadDAO = DAOFactory.instance().getEadDAO();
			int orderId = ZERO;
            boolean isWithUrl = true;

            if (StringUtils.isNotBlank(isWithUrlString)) {
                try {
				    isWithUrl = Boolean.parseBoolean(isWithUrlString);
                } catch (Exception e){
                    isWithUrl = true; //default
                }
			}

			if (StringUtils.isNotBlank(orderIdString) && StringUtils.isNumeric(orderIdString)) {
				orderId = new Integer(orderIdString);
			}
			int max = MAX_NUMBER_OF_CLEVELS;
			if (StringUtils.isNotBlank(maxString) && StringUtils.isNumeric(maxString)) {
				max = new Integer(maxString);
				if (max > MAX_NUMBER_OF_CLEVELS){
					max = MAX_NUMBER_OF_CLEVELS;
				}
			}
			String performanceLogParameters = "orderId=" + orderId + ",max=" + max;
			//Long totalNumberOfCLevels = null;
			if (StringUtils.isNotBlank(parentIdString) && StringUtils.isNumeric(parentIdString)) {
				Long parentId = new Long(parentIdString);
				performanceLogParameters += ",parentId=" + parentId;
				long localStartTime = System.currentTimeMillis();
				List<CLevel> clevels = clevelDAO.findChildCLevels(parentId, orderId, max);

//				if (!MORE_VALUE_BEFORE.equalsIgnoreCase(more)){
//					totalNumberOfCLevels = clevelDAO.countChildCLevels(parentId);
//
//				}
				databaseTimeCost += System.currentTimeMillis() - localStartTime;
				StringBuilder result = generateCLevelJSON(clevels, path, isWithUrl);
				buildTime = System.currentTimeMillis() - startTime;
				writer.write(result.toString());
				writer.close();
			} else if (StringUtils.isNotBlank(fileIdString) && StringUtils.isNumeric(fileIdString)) {
				Integer fileId = new Integer(fileIdString);
                XmlType xmlType = XmlType.getType(new Integer(xmlTypeIdString));
				performanceLogParameters += ",fileId=" + fileId;
				long localStartTime = System.currentTimeMillis();
                EadContent eadContent = eadDAO.findById(fileId, xmlType.getClazz()).getEadContent();
				List<CLevel> clevels = clevelDAO.findTopCLevels(eadContent.getEcId(), orderId, max);
//				if (!MORE_VALUE_BEFORE.equalsIgnoreCase(more)){
//					totalNumberOfCLevels = clevelDAO.countTopCLevels(eadContent.getEcId());
//				}
				databaseTimeCost += System.currentTimeMillis() - localStartTime;
				StringBuilder topCLevelsBuffer = generateCLevelJSON(clevels, path, isWithUrl);
				StringBuilder result = generateRootJSON(eadContent, topCLevelsBuffer, path, false, xmlTypeIdString);
				buildTime = System.currentTimeMillis() - startTime;
				writer.write(result.toString());
				writer.close();
			} else if (StringUtils.isNotBlank(ecIdString) && StringUtils.isNumeric(ecIdString)) {
				/*
				 * used for more option
				 */
				Long ecId = new Long(ecIdString);
				performanceLogParameters += ",ecId=" + ecId;
				long localStartTime = System.currentTimeMillis();
				List<CLevel> clevels = clevelDAO.findTopCLevels(ecId, orderId, max);
//				if (!MORE_VALUE_BEFORE.equalsIgnoreCase(more)){
//					totalNumberOfCLevels = clevelDAO.countTopCLevels(ecId);
//				}
				databaseTimeCost += System.currentTimeMillis() - localStartTime;
				StringBuilder result = generateCLevelJSON(clevels, path, isWithUrl);
				buildTime = System.currentTimeMillis() - startTime;
				writer.write(result.toString());
				writer.close();
			}else if (StringUtils.isNotBlank(cIdString)) {
				Long solrId = new Long(cIdString.substring(1));
				performanceLogParameters += ",solrId=" + solrId;
				long localStartTime = System.currentTimeMillis();
				CLevel clevel = clevelDAO.findById(solrId);
//
//				if (clevel.getParentClId() == null){
//					totalNumberOfCLevels = clevelDAO.countTopCLevels(clevel.getEcId());
//				}else {
//					totalNumberOfCLevels = clevelDAO.countChildCLevels(clevel.getParentClId());
//				}
				databaseTimeCost += System.currentTimeMillis() - localStartTime;
				StringBuilder buffer = generateJSONWithSelectedItem(clevel, path, isWithUrl, xmlTypeIdString);
				buildTime = System.currentTimeMillis() - startTime;
				writer.write(buffer.toString());
				writer.close();
			} else{LOG.info("nothing");}
//			if (totalNumberOfCLevels != null){
//				performanceLogParameters+=",totalCLevels="+totalNumberOfCLevels;
//			}
			buildTime -= databaseTimeCost;
			long writeToResponseTime = System.currentTimeMillis() - startTime - buildTime - databaseTimeCost;
			LOG.debug("SD Tree times: " + buildTime + "ms," +  databaseTimeCost + "ms," + writeToResponseTime + "ms (build,database,write-to-response) params: " + performanceLogParameters);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		LOG.debug("Leaving method \"execute\".");

		return null;
	}

	/**
	 * <p>
	 * Overrides the method to execute the action.
	 * </p>
	 * <p>
	 * This method is called thought the Struts action <i>"generateTreeJSONWithoutPreface"</i>
	 * which is used in the <b>apeEAD edition</b> functionality.
	 * </p>
	 * <p>
	 * It is used to load the initial tree, which includes the root level and
	 * all the children of the first level.
	 * </p>
     * <p>
     * To successfully load this information, this method recovers all the
     * needed parameters from the <b>request</b> (this is stored in the global
     * variable <i>{@link GenerateTreeJSONAction#request}</i>).
     * </p>
	 * <p>
	 * The parameters needed are:
	 * </p>
     * <p>
     *  <ul>
     *   <li><b>fileId</b> - Stores the identifier of the file on database.</li>
     *   <li><b>xmlTypeId</b> - Stores the type of the file (by default, the
     *   	type <i>0 - Finding aid</i>).</li>
     *   <li><b>isWithUrl</b> - Stores the value for decide if the nodes of the
     *   	tree should contain URIs or not.</li>
     *  </ul>
     * </p>
     * <p>
	 *
	 * @return null since we use the HttpResponse to write JSON data directly
	 * to the page.
	 *
	 * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 * @see GenerateTreeJSONAction#generateCLevelJSON(List, String, boolean)
	 * @see GenerateTreeJSONAction#generateRootJSON(EadContent, StringBuilder, String, boolean, boolean, String)
	 */
    public String executeNoPreface() {
		LOG.debug("Entering method \"executeNoPreface\".");

        try {
			String fileIdString = request.getParameter("fileId");
			String xmlTypeIdString = request.getParameter("xmlTypeId");
			String parentIdString = request.getParameter("parentId");
			String ecIdString = request.getParameter("ecId");
			String cIdString = request.getParameter("cId");
			String orderIdString = request.getParameter("orderId");
			String maxString = request.getParameter("max");
            String isWithUrlString = request.getParameter("isWithUrl");
            boolean isWithUrl = true;

            if (StringUtils.isNotBlank(isWithUrlString)) {
                try {
				    isWithUrl = Boolean.parseBoolean(isWithUrlString);
                } catch (Exception e){
                    isWithUrl = true; //default
                }
			}

			request.setCharacterEncoding(UTF8);
			response.setCharacterEncoding(UTF8);
			response.setContentType("application/json");
			Writer writer = new OutputStreamWriter(response.getOutputStream(), UTF8);
			String path = request.getContextPath();
			CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
			EadContentDAO eadContentDAO = DAOFactory.instance().getEadContentDAO();
			Integer fileId = new Integer(fileIdString);
            XmlType xmlType = XmlType.getType(new Integer(xmlTypeIdString));
			long localStartTime = System.currentTimeMillis();
			Ead ead = DAOFactory.instance().getEadDAO().findById(fileId, xmlType.getClazz());
            EadContent eadContent = ead.getEadContent();

            if (eadContent == null) {
            	// If is null, probably the EAD never has been published, so,
            	// try to convert the static EAD into a dynamic EAD.
            	ChangeDynamicTask changeDynamicTask = new ChangeDynamicTask();
            	changeDynamicTask.execute(ead, null);

            	// Try to recover the new content of the dynamic EAD.
    			eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByFileId(fileId, xmlType.getEadClazz());

    			if (eadContent == null) {
                	throw new APEnetException(getText("generateTreeJSON.APEnetException.no.correct.FA"));
    			}
            }

            StringBuilder topCLevelsBuffer = generateCLevelJSON(clevelDAO.findTopCLevels(eadContent.getEcId()), path, isWithUrl);
            writer.write(generateRootJSON(eadContent, topCLevelsBuffer, path, false, false, xmlTypeIdString).toString());
            writer.close();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		LOG.debug("Leaving method \"executeNoPreface\".");

		return null;
    }

    /**
	 * <p>
     * This method is called from <i>"{@link GenerateTreeJSONAction#executeNoPreface()}"</i>
     * and from <i>"{@link GenerateTreeJSONAction#generateRootJSON(EadContent, StringBuilder, String, boolean, String)}"</i>.
	 * </p>
	 * <p>
     * It is used to build a {@link StringBuilder} which contains the JSON to
     * be sent as a response to the page. This object has the data to display
     * the tree of the document which should be edited.
	 * </p>
	 * <p>
	 * By itself, this method only create the root part of the tree and include
	 * a title (created using the method {@link GenerateTreeJSONAction#addTitle(StringBuilder, String)})
	 * and the children elements which are passed as a parameter.
	 * </p>
     *
     * @param eadContent {@link EadContent} of the current file to be edited.
     * @param childBuffer {@link StringBuilder} for the children nodes which
     * already has been created.
     * @param path {@link String} specifying the portion of the request URI
     * that indicates the context of the request.
     * @param expand {@code boolean} specifying if the requested root should be
     * expanded or not.
     * @param isWithPreface {@code boolean} specifying if the requested root
     * should display the preface or not.
     * @param xmlTypeIdString {@link String} the type of the XML which should
     * be edited.
     *
     * @return {@link StringBuilder} which contains the builded root of the
     * tree plus the children elements.
     *
     * @see GenerateTreeJSONAction#addTitle(StringBuilder, String)
     * @see GenerateTreeJSONAction#addUrl(StringBuilder, EadContent, String, String)
     */
	private StringBuilder generateRootJSON(EadContent eadContent, StringBuilder childBuffer, String path,
			boolean expand, boolean isWithPreface, String xmlTypeIdString) {
		LOG.debug("Entering method \"generateRootJSON(EadContent, StringBuilder, String, boolean, boolean, String)\".");

		StringBuilder buffer = new StringBuilder();
		buffer.append(START_ARRAY);
		buffer.append(START_ITEM);
		addTitle(buffer, eadContent.getTitleproper());
		buffer.append(COMMA);
        if(isWithPreface){
		    addUrl(buffer, eadContent, path + "/displayEadFrontPage.action", xmlTypeIdString);
		    buffer.append(COMMA);
        }
		buffer.append(FOLDER_WITH_CHILDREN);
		buffer.append(childBuffer);
		buffer.append(END_ITEM_WITH_RETURN);
		buffer.append(END_ARRAY);

		LOG.debug("Leaving method \"generateRootJSON(EadContent, StringBuilder, String, boolean, boolean, String)\".");

		return buffer;
	}

	/**
	 * <p>
     * This method is called from <i>"{@link GenerateTreeJSONAction#generateParentCLevelJSON(CLevel, StringBuilder, String, boolean, String)}"</i>.
	 * </p>
	 * <p>
	 * It is used as an intermediate step to set the value to display the
	 * preface for the requested root generation.
	 * </p>
	 *
     * @param eadContent {@link EadContent} of the current file to be edited.
     * @param childBuffer {@link StringBuilder} for the children nodes which
     * already has been created.
     * @param path {@link String} specifying the portion of the request URI
     * that indicates the context of the request.
     * @param expand {@code boolean} specifying if the requested root should be
     * expanded or not.
     * @param xmlTypeIdString {@link String} the type of the XML which should
     * be edited.
	 *
     * @return {@link StringBuilder} which contains the builded root of the
     * tree plus the children elements.
     *
     * @see GenerateTreeJSONAction#generateRootJSON(EadContent, StringBuilder, String, boolean, boolean, String)
	 */
    private StringBuilder generateRootJSON(EadContent eadContent, StringBuilder childBuffer, String path, boolean expand, String xmlTypeIdString) {
		LOG.debug("Go through for method \"generateRootJSON(EadContent, StringBuilder, String, boolean, String)\".");

		return generateRootJSON(eadContent, childBuffer, path, expand, true, xmlTypeIdString);
	}

    /**
	 * <p>
     * This method is called from <i>"{@link GenerateTreeJSONAction#execute()}"</i>
     * and from <i>"{@link GenerateTreeJSONAction#executeNoPreface()}"</i>.
	 * </p>
	 * <p>
	 * It is used to build a {@link StringBuilder} which contains the children
	 * parts of the tree from the document which should be edited.
	 * </p>
     *
     * @param clevels {@link List}{@code <}{@link CLevel}{@code >} containing
     * all the elements which should be added to the tree.
     * @param path {@link String} specifying the portion of the request URI
     * that indicates the context of the request.
     * @param isWithUrl {@code boolean} specifying if the nodes of the tree
     * should contain URIs or not.
     *
     * @return {@link StringBuilder} which contains the builded structure for
     * the children nodes of the tree.
     *
     * @see GenerateTreeJSONAction#addBefore(StringBuilder, CLevel)
     * @see GenerateTreeJSONAction#addTitle(StringBuilder, CLevel)
     * @see GenerateTreeJSONAction#addCContentUrl(StringBuilder, Long, String)
     * @see GenerateTreeJSONAction#addId(StringBuilder, Long)
     * @see GenerateTreeJSONAction#addChildren(StringBuilder, CLevel, boolean, String, boolean)
     * @see GenerateTreeJSONAction#addAfter(StringBuilder, CLevel)
     */
	private StringBuilder generateCLevelJSON(List<CLevel> clevels, String path, boolean isWithUrl) {
		LOG.debug("Entering method \"generateCLevelJSON\".");

		StringBuilder buffer = new StringBuilder();
		buffer.append(START_ARRAY);
		if (!MORE_VALUE_AFTER.equalsIgnoreCase(more) && clevels.size() > 0){
			CLevel firstCLevel = clevels.get(0);
			addBefore(buffer, firstCLevel);
		}
		for (int i = 0; i < clevels.size(); i++) {
			CLevel clevel = clevels.get(i);
			buffer.append(START_ITEM);
			addTitle(buffer, clevel);
			buffer.append(COMMA);
            if(isWithUrl)
			    addCContentUrl(buffer, clevel.getId(), path);
            else
                addId(buffer, clevel.getId());
			addChildren(buffer, clevel, true, path, isWithUrl);
			if (i < clevels.size() - 1) {
				buffer.append(END_ITEM_WITH_COMMA);
			} else {
				buffer.append(END_ITEM);
			}
		}
		if (!MORE_VALUE_BEFORE.equalsIgnoreCase(more) && clevels.size() > 0 && clevels.size() == MAX_NUMBER_OF_CLEVELS){
			CLevel lastCLevel = clevels.get(clevels.size()-1);
			addAfter(buffer, lastCLevel);
		}
		buffer.append(END_ARRAY);

		LOG.debug("Leaving method \"generateCLevelJSON\".");

		return buffer;
	}

	/**
	 * <p>
     * This method is called from <i>"{@link GenerateTreeJSONAction#generateCLevelJSON(List, String, boolean)}"</i>,
     * from <i>"{@link GenerateTreeJSONAction#generateJSONWithSelectedItem(CLevel, String, boolean, String)}"</i>
     * and from <i>"{@link GenerateTreeJSONAction#generateParentCLevelJSON(CLevel, StringBuilder, String, boolean, String)}"</i>.
	 * </p>
	 * <p>
	 * It is used to build the node of the tree which represents that more
	 * elements (brothers of the displayed ones) there is available previous to
	 * the first node displayed.
	 * </p>
	 * <p>
	 * This node is represented with a folder icon and the English text
	 * <i><b>"More before..."</b></i>.
	 * </p>
	 * <p>
	 * <b>Note</b>: the mentioned English text is able to be
	 * internationalized.
	 * </p>
	 * <p>
	 * In order to determine if the node should be displayed it's checked the
	 * global variable <i>{@link GenerateTreeJSONAction#more}</i>.
	 * </p>
	 *
	 * @param buffer {@link StringBuilder} in which the node of the tree will
	 * be built.
	 * @param clevel {@link CLevel} representing the current first element
	 * which should be displayed.
	 *
     * @see GenerateTreeJSONAction#addMoreTitle(StringBuilder, String)
     * @see GenerateTreeJSONAction#addMore(StringBuilder, String)
	 */
	private void addBefore(StringBuilder buffer, CLevel clevel){
		LOG.debug("Entering method \"addBefore\".");

		if (!MORE_VALUE_AFTER.equalsIgnoreCase(more) && (clevel.getOrderId() > 0)){
			buffer.append(START_ITEM);
			addMoreTitle(buffer, getText("eadcontent.more.before"));
			buffer.append(COMMA);
			addMore(buffer,MORE_VALUE_BEFORE );
			buffer.append(COMMA);
			buffer.append(FOLDER_LAZY);
			buffer.append(COMMA);
			buffer.append("\"parentId\":");
			buffer.append(" \"" + clevel.getParentId() + "\" ");
			buffer.append(COMMA);
			buffer.append("\"ecId\":");
			buffer.append(" \"" + clevel.getEcId() + "\" ");
			buffer.append(COMMA);
			buffer.append("\"orderId\":");
			int orderId = clevel.getOrderId() - MAX_NUMBER_OF_CLEVELS;
			int max = MAX_NUMBER_OF_CLEVELS;
			if (orderId < 0){
				max = clevel.getOrderId();
				orderId = 0;

			}
			buffer.append(" \"" + orderId + "\" ");
			buffer.append(COMMA);
			buffer.append("\"max\":");
			buffer.append(" \"" + max + "\" ");
			buffer.append(END_ITEM);
			buffer.append(COMMA);
		}

		LOG.debug("Leaving method \"addBefore\".");
	}

	/**
	 * <p>
     * This method is called from <i>"{@link GenerateTreeJSONAction#generateCLevelJSON(List, String, boolean)}"</i>,
     * from <i>"{@link GenerateTreeJSONAction#generateJSONWithSelectedItem(CLevel, String, boolean, String)}"</i>
     * and from <i>"{@link GenerateTreeJSONAction#generateParentCLevelJSON(CLevel, StringBuilder, String, boolean, String)}"</i>.
	 * </p>
	 * <p>
	 * It is used to build the node of the tree which represents that more
	 * elements (brothers of the displayed ones) there is available following
	 * the last node displayed.
	 * </p>
	 * <p>
	 * This node is represented with a folder icon and the English text
	 * <i><b>"More after..."</b></i>.
	 * </p>
	 * <p>
	 * <b>Note</b>: the mentioned English text is able to be
	 * internationalized.
	 * </p>
	 * <p>
	 * In order to determine if the node should be displayed it's checked the
	 * global variable <i>{@link GenerateTreeJSONAction#more}</i>.
	 * </p>
	 *
	 * @param buffer {@link StringBuilder} in which the node of the tree will
	 * be built.
	 * @param clevel {@link CLevel} representing the current last element
	 * which should be displayed.
	 *
     * @see GenerateTreeJSONAction#addMoreTitle(StringBuilder, String)
     * @see GenerateTreeJSONAction#addMore(StringBuilder, String)
	 */
	private void addAfter(StringBuilder buffer, CLevel clevel){
		LOG.debug("Entering method \"addAfter\".");

		if (!MORE_VALUE_BEFORE.equalsIgnoreCase(more) ){
			buffer.append(COMMA);
			buffer.append(START_ITEM);
			addMoreTitle(buffer, getText("eadcontent.more.after"));
			buffer.append(COMMA);
			buffer.append("\"parentId\":");
			buffer.append(" \"" + clevel.getParentId() + "\" ");
			buffer.append(COMMA);
			buffer.append("\"ecId\":");
			buffer.append(" \"" + clevel.getEcId() + "\" ");
			buffer.append(COMMA);
			buffer.append("\"orderId\":");
			buffer.append(" \"" + (clevel.getOrderId()+1) + "\" ");
			buffer.append(COMMA);
			addMore(buffer,MORE_VALUE_AFTER );
			buffer.append(COMMA);
			buffer.append(FOLDER_LAZY);
			buffer.append(END_ITEM_WITH_RETURN);
		}

		LOG.debug("Leaving method \"addAfter\".");
	}

	/**
	 * <p>
     * This method is called from <i>"{@link GenerateTreeJSONAction#execute()}"</i>.
	 * </p>
	 * <p>
     * It is used to build a {@link StringBuilder} which contains the JSON to
     * be sent as a response to the page. This object has the data to display
     * the tree which has been recovered from the initial node passed as a
     * parameter.
	 * </p>
	 *
	 * @param clevel {@link CLevel} representing the current element which
	 * JSON representation should be built to be displayed in the tree.
	 * @param path {@link String} specifying the portion of the request URI
     * that indicates the context of the request.
     * @param isWithUrl {@code boolean} specifying if the nodes of the tree
     * should contain URIs or not.
	 * @param xmlTypeIdString {@link String} the type of the XML which should
     * be edited.
	 *
	 * @return {@link StringBuilder} which contains the builded structure for
     * the full tree created from the node passed.
	 *
	 * @see GenerateTreeJSONAction#addBefore(StringBuilder, CLevel)
	 * @see GenerateTreeJSONAction#addTitle(StringBuilder, CLevel)
	 * @see GenerateTreeJSONAction#addCContentUrl(StringBuilder, Long, String)
	 * @see GenerateTreeJSONAction#addId(StringBuilder, Long)
	 * @see GenerateTreeJSONAction#addChildren(StringBuilder, CLevel, boolean, String, boolean)
	 * @see GenerateTreeJSONAction#addAfter(StringBuilder, CLevel)
	 * @see GenerateTreeJSONAction#generateParentCLevelJSON(CLevel, StringBuilder, String, boolean, String)
	 */
	private StringBuilder generateJSONWithSelectedItem(CLevel clevel, String path, boolean isWithUrl, String xmlTypeIdString) {
		LOG.debug("Entering method \"generateJSONWithSelectedItem\".");

		StringBuilder buffer = new StringBuilder();
		buffer.append(START_ARRAY);
		addBefore(buffer, clevel);
		buffer.append(START_ITEM);
		addTitle(buffer, clevel);
		buffer.append(COMMA);
        if(isWithUrl)
		    addCContentUrl(buffer, clevel.getId(), path);
        else
            addId(buffer, clevel.getId());
		buffer.append(COMMA);
		buffer.append("\"selected\":true, \"activate\": true");
//		if (!clevel.isLeaf()) {
//			buffer.append(COMMA);
//			addExpand(buffer);
//		}
		addChildren(buffer, clevel, true, path, isWithUrl);
		buffer.append(END_ITEM);

		addAfter(buffer, clevel);
		buffer.append(END_ARRAY);

		LOG.debug("Leaving method \"generateJSONWithSelectedItem\".");

		return generateParentCLevelJSON(clevel, buffer, path, isWithUrl, xmlTypeIdString);
	}

	/**
	 * <p>
     * This recursive method is called from itself and from the method
     * <i>"{@link GenerateTreeJSONAction#generateJSONWithSelectedItem(CLevel, String, boolean, String)}"</i>.
	 * </p>
	 * <p>
     * It is used to recursively build a {@link StringBuilder} which contains
     * the JSON to be sent as a response to the page. This object is reversely
     * built from the passed node, recovering it's parent, to the root level.
	 * </p>
	 *
	 * @param child {@link CLevel} representing the current element which
	 * JSON representation should be built to be displayed in the tree.
	 * @param childBuffer {@link StringBuilder}  in which the node of the tree
	 * will be built.
	 * @param path {@link String} specifying the portion of the request URI
     * that indicates the context of the request.
     * @param isWithUrl {@code boolean} specifying if the nodes of the tree
     * should contain URIs or not.
	 * @param xmlTypeIdString {@link String} the type of the XML which should
     * be edited.
	 *
	 * @return {@link StringBuilder} which contains the builded structure for
     * the node passed plus the previous structure which is also passed. In the
     * end of the process will be the full tree.
	 *
	 * @see GenerateTreeJSONAction#addBefore(StringBuilder, CLevel)
	 * @see GenerateTreeJSONAction#addTitle(StringBuilder, CLevel)
	 * @see GenerateTreeJSONAction#addCContentUrl(StringBuilder, Long, String)
	 * @see GenerateTreeJSONAction#addId(StringBuilder, Long)
	 * @see GenerateTreeJSONAction#addExpand(StringBuilder)
	 * @see GenerateTreeJSONAction#addAfter(StringBuilder, CLevel)
	 * @see GenerateTreeJSONAction#generateRootJSON(EadContent, StringBuilder, String, boolean, String)
	 */
	private StringBuilder generateParentCLevelJSON(CLevel child, StringBuilder childBuffer, String path, boolean isWithUrl, String xmlTypeIdString) {
		LOG.debug("Entering method \"generateParentCLevelJSON\".");

		long localStartTime = System.currentTimeMillis();
		CLevel parent = child.getParent();
		databaseTimeCost += System.currentTimeMillis() - localStartTime;
		if (parent != null) {
			StringBuilder buffer = new StringBuilder();
			buffer.append(START_ARRAY);
			addBefore(buffer, parent);
			buffer.append(START_ITEM);
			addTitle(buffer, parent);
			buffer.append(COMMA);
            if(isWithUrl)
			    addCContentUrl(buffer, parent.getId(), path);
            else
                addId(buffer, parent.getId());
			buffer.append(COMMA);
			addExpand(buffer);
			buffer.append(COMMA);
			buffer.append(FOLDER_WITH_CHILDREN);
			localStartTime = System.currentTimeMillis();
			//List<CLevel> children = parent.getChildren();
			databaseTimeCost += System.currentTimeMillis() - localStartTime;
			buffer.append(childBuffer);
			buffer.append(END_ITEM);
			addAfter(buffer, parent);
			buffer.append(END_ARRAY);

			LOG.debug("Leaving method \"generateParentCLevelJSON\" when the passed element has parent.");

			return generateParentCLevelJSON(parent, buffer,  path, isWithUrl, xmlTypeIdString);
		} else {
			localStartTime = System.currentTimeMillis();
			//List<CLevel> topCLevels = DAOFactory.instance().getCLevelDAO().findTopCLevels(child.getEadContent().getEcId());
			databaseTimeCost += System.currentTimeMillis() - localStartTime;
			localStartTime = System.currentTimeMillis();
			EadContent eadContent = child.getEadContent();
			databaseTimeCost += System.currentTimeMillis() - localStartTime;

			LOG.debug("Leaving method \"generateParentCLevelJSON\" when the passed element has no parent.");

			return generateRootJSON(eadContent, childBuffer, path, true, xmlTypeIdString);
		}
	}

	/**
	 * <p>
     * This method is called from <i>"{@link GenerateTreeJSONAction#addChildren(StringBuilder, CLevel, boolean, String, boolean)}"</i>.
	 * </p>
	 * <p>
	 * It is used to build a {@link StringBuilder} which contains the JSON from
	 * the list of {@link CLevel} passed.
	 * </p>
	 *
	 * @param clevels {@link List}{@code <}{@link CLevel}{@code >} containing
     * all the elements which should be added to the JSON.
	 * @param path {@link String} specifying the portion of the request URI
     * that indicates the context of the request.
     * @param isWithUrl {@code boolean} specifying if the nodes of the tree
     * should contain URIs or not.
	 *
	 * @return {@link StringBuilder} which contains the builded structure for
     * the list of nodes passed.
	 *
	 * @see GenerateTreeJSONAction#addTitle(StringBuilder, CLevel)
	 * @see GenerateTreeJSONAction#addCContentUrl(StringBuilder, Long, String)
	 * @see GenerateTreeJSONAction#addId(StringBuilder, Long)
	 * @see GenerateTreeJSONAction#addChildren(StringBuilder, CLevel, boolean, String, boolean)
	 */
	private StringBuilder generateChildCLevelJSON(List<CLevel> clevels, String path, boolean isWithUrl) {
		LOG.debug("Entering method \"generateChildCLevelJSON\".");

		StringBuilder buffer = new StringBuilder();
		buffer.append(START_ARRAY);
		for (int i = 0; i < clevels.size(); i++) {
			CLevel clevel = clevels.get(i);
			buffer.append(START_ITEM);
			addTitle(buffer, clevel);
			buffer.append(COMMA);
            if(isWithUrl)
			    addCContentUrl(buffer, clevel.getId(), path);
            else
                addId(buffer, clevel.getId());
			addChildren(buffer, clevel, true, path, isWithUrl);
			buffer.append(END_ITEM);
			if (i < clevels.size() - 1) {
				buffer.append(",");
			} else {
//				buffer.append("");
			}
		}
		buffer.append(END_ARRAY);

		LOG.debug("Leaving method \"generateChildCLevelJSON\".");

		return buffer;
	}

	/**
	 * <p>
     * This method is called from <i>"{@link GenerateTreeJSONAction#generateRootJSON(EadContent, StringBuilder, String, boolean, boolean, String)}"</i>
     * and from <i>"{@link GenerateTreeJSONAction#addTitle(StringBuilder, CLevel)}"</i>.
	 * </p>
	 * <p>
	 * It is used to add, in the {@link StringBuilder} passed, a title to the
	 * JSON object.
	 * </p>
	 * <p>
	 * If the title passed is empty, a default English value for the title is
	 * added with the text <i>"No title specified"</i>.
	 * </p>
	 * <p>
	 * <b>Note</b>: the mentioned English text is able to be
	 * internationalized.
	 * </p>
	 *
	 * @param buffer {@link StringBuilder} in which the title for the node of
	 * the tree will be built.
	 * @param title {@link String} specifying the title of the current node.
	 *
	 * @see DisplayUtils#encodeHtml(String, int)
	 */
	private void addTitle(StringBuilder buffer, String title) {
		LOG.debug("Entering method \"addTitle(StringBuilder, String)\".");

		buffer.append("\"title\":");
		//String fullTitle = DisplayUtils.encodeHtml(title);
		String smallTitle = DisplayUtils.encodeHtml(title, MAX_NUMBER_OF_CHARACTERS);

		if (smallTitle == null || smallTitle.trim().length() == 0) {
			// title = "-- empty --";
			smallTitle = getText("advancedsearch.text.notitle");
			buffer.append(" \"" + smallTitle + "\"");
			buffer.append(COMMA);
			buffer.append("\"addClass\":");
			buffer.append(" \"notitle\"");


		}else {
			smallTitle = smallTitle.replaceAll("[\n\t\r]", "");
			buffer.append(" \"" + smallTitle + "\"");
		}

		LOG.debug("Leaving method \"addTitle(StringBuilder, String)\".");

	}

	/**
	 * <p>
     * This method is called from <i>"{@link GenerateTreeJSONAction#generateChildCLevelJSON(List, String, boolean)}"</i>,
     * from <i>"{@link GenerateTreeJSONAction#generateCLevelJSON(List, String, boolean)}"</i>,
     * from <i>"{@link GenerateTreeJSONAction#generateJSONWithSelectedItem(CLevel, String, boolean, String)}"</i>
     * and from <i>"{@link GenerateTreeJSONAction#generateParentCLevelJSON(CLevel, StringBuilder, String, boolean, String)}"</i>.
	 * </p>
	 * <p>
	 * It is used as an intermediate step to extract the <i>title</i> value
	 * from the {@link CLevel} passed.
	 * </p>
	 *
	 * @param buffer {@link StringBuilder} in which the title for the node of
	 * the tree will be built.
	 * @param clevel {@link CLevel} containing the node from which the title
	 * should be built.
	 *
	 * @see GenerateTreeJSONAction#addTitle(StringBuilder, String)
	 */
	private void addTitle(StringBuilder buffer, CLevel clevel) {
		LOG.debug("Go through method \"addTitle(StringBuilder, CLevel)\".");

		addTitle(buffer,clevel.getUnittitle());
	}

	/**
	 * <p>
	 * This method is use to include the identifier of the <i>finding aid</i>,
	 * which is currently edited, in the JSON.
	 * </p>
	 * <p>
	 * <b>Note: This method is currently unused.</b>
	 * </p>
	 *
	 * @param buffer {@link StringBuilder} representing a JSON in which the
	 * identifier of the <i>finding aid</i> will be added.
	 * @param faId {@link Integer} specifying the identifier of the current
	 * <i>finding aid</i>.
	 */
    private static void addFaId(StringBuilder buffer, Integer faId){
		LOG.debug("Entering method \"addFaId\".");

        buffer.append("\"faId\": \"");
		buffer.append(faId);
		buffer.append("\" ");

		LOG.debug("Leaving method \"addFaId\".");
    }

	/**
	 * <p>
     * This method is called from <i>"{@link GenerateTreeJSONAction#addBefore(StringBuilder, CLevel)}"</i>
     * and from <i>"{@link GenerateTreeJSONAction#addAfter(StringBuilder, CLevel)}"</i>.
	 * </p>
	 * <p>
	 * It is used to add, in the {@link StringBuilder} passed, a title to the
	 * JSON object.
	 * </p>
	 * <p>
	 * This title is the one related to the <i>"More before/after..."</i> parts.
	 * <p>
	 *
	 * @param buffer {@link StringBuilder} in which the title for the node of
	 * the tree will be built.
	 * @param title {@link String} specifying the title of the current node.
	 *
	 * @see DisplayUtils#encodeHtml(String, int)
	 */
	private static void addMoreTitle(StringBuilder buffer, String title) {
		LOG.debug("Entering method \"addMoreTitle\".");

		buffer.append("\"addClass\":");
		buffer.append(" \"more\"");
        buffer.append(COMMA);
        buffer.append(ICON_MORE);
		buffer.append(COMMA);
		buffer.append("\"title\":");
		String fullTitle = DisplayUtils.encodeHtml(title);
		buffer.append("\"" +fullTitle + "\"");

		LOG.debug("Leaving method \"addMoreTitle\".");
	}

	/**
	 * <p>
     * This method is called from <i>"{@link GenerateTreeJSONAction#generateChildCLevelJSON(List, String, boolean)}"</i>,
     * from <i>"{@link GenerateTreeJSONAction#generateCLevelJSON(List, String, boolean)}"</i>,
     * from <i>"{@link GenerateTreeJSONAction#generateJSONWithSelectedItem(CLevel, String, boolean, String)}"</i>
     * and from <i>"{@link GenerateTreeJSONAction#generateParentCLevelJSON(CLevel, StringBuilder, String, boolean, String)}"</i>.
	 * </p>
	 * <p>
	 * It is used to add, in the {@link StringBuilder} passed, a link to the
	 * desired part of the tree.
	 * </p>
	 *
	 * @param buffer {@link StringBuilder} in which the link for the node of
	 * the tree will be built.
	 * @param clId {@link Long} containing the identifier of the current
	 * {@link CLevel} which will be used a link.
	 * @param path {@link String} specifying the portion of the request URI
     * that indicates the context of the request.
	 */
	private static void addCContentUrl(StringBuilder buffer, Long clId, String path) {
		LOG.debug("Entering method \"addCContentUrl\".");

		buffer.append("\"url\":");
		buffer.append(" \"" + path + "/displayCContent.action");
		buffer.append("?id=" + clId);
		buffer.append("\" ");

		LOG.debug("Leaving method \"addCContentUrl\".");
	}

	/**
	 * <p>
     * This method is called from <i>"{@link GenerateTreeJSONAction#generateChildCLevelJSON(List, String, boolean)}"</i>,
     * from <i>"{@link GenerateTreeJSONAction#generateCLevelJSON(List, String, boolean)}"</i>,
     * from <i>"{@link GenerateTreeJSONAction#generateJSONWithSelectedItem(CLevel, String, boolean, String)}"</i>
     * and from <i>"{@link GenerateTreeJSONAction#generateParentCLevelJSON(CLevel, StringBuilder, String, boolean, String)}"</i>.
	 * </p>
	 * <p>
	 * It is used to add, in the {@link StringBuilder} passed, an identifier
	 * and a key for the current node.
	 * </p>
	 *
	 * @param buffer {@link StringBuilder} in which the identifier for the node
	 * of the tree will be built.
	 * @param clId clId {@link Long} containing the identifier of the current
	 * {@link CLevel} which will be used as a key.
	 */
    private static void addId(StringBuilder buffer, Long clId) {
		LOG.debug("Entering method \"addId\".");

		buffer.append("\"id\":");
		buffer.append("\"");
        buffer.append(clId);
		buffer.append("\"");
		buffer.append(COMMA);
		buffer.append("\"key\":" + "\"" + clId + "\"");

		LOG.debug("Leaving method \"addId\".");
	}

    /**
	 * <p>
     * This method is called from <i>"{@link GenerateTreeJSONAction#generateRootJSON(EadContent, StringBuilder, String, boolean, boolean, String)}"</i>.
	 * </p>
     * <p>
     * It is used to add, in the {@link StringBuilder} passed, a link to the
     * main level of the edited file.
     * </p>
     *
     * @param buffer {@link StringBuilder} in which the URL for the node of the
	 * tree will be built.
     * @param eadContent {@link EadContent} of the current file to be edited.
     * @param path {@link String} specifying the portion of the request URI
     * that indicates the context of the request.
     * @param xmlTypeIdString {@link String} the type of the XML which should
     * be edited.
     */
	private static void addUrl(StringBuilder buffer, EadContent eadContent, String path, String xmlTypeIdString) {
		LOG.debug("Entering method \"addUrl\".");

		buffer.append("\"url\":");
		buffer.append(" \"" + path);
        buffer.append("?fileId=").append(eadContent.getEadIdentifier());
		buffer.append("&xmlTypeId=").append(xmlTypeIdString);

		buffer.append("\" ");

		LOG.debug("Leaving method \"addUrl\".");
	}

	/**
	 * <p>
     * This method is called from <i>"{@link GenerateTreeJSONAction#generateParentCLevelJSON(CLevel, StringBuilder, String, boolean, String)}"</i>.
	 * </p>
	 * <p>
	 * It is used to add, in the {@link StringBuilder} passed, the tag
	 * <i>"expand"</i> for the current node of the tree.
	 * </p>
	 *
	 * @param buffer {@link StringBuilder} in which the tag <i>"expand"</i>
	 * will be added for the node of the tree which is currently built.
	 */
	private static void addExpand(StringBuilder buffer) {
		LOG.debug("Entering method \"addExpand\".");

		buffer.append("\"expand\":true");

		LOG.debug("Leaving method \"addExpand\".");
	}

	/**
	 * <p>
     * This method is called from <i>"{@link GenerateTreeJSONAction#generateChildCLevelJSON(List, String, boolean)}"</i>,
     * from <i>"{@link GenerateTreeJSONAction#generateCLevelJSON(List, String, boolean)}"</i>
     * and from <i>"{@link GenerateTreeJSONAction#generateJSONWithSelectedItem(CLevel, String, boolean, String)}"</i>.
	 * </p>
	 * <p>
	 * It is used to build, in the {@link StringBuilder} passed, the JSON
	 * element which corresponds with the current node and its children, if any.
	 * </p>
	 * <p>
	 * The built nodes could be marked as <i>"lazy"</i>, in case not all the
	 * information about the current node should not be loaded.
	 * </p>
	 *
	 * @param buffer {@link StringBuilder} in which the children of the current
	 * {@link CLevel} will be added for the tree.
	 * @param clevel {@link CLevel} containing the node from which the tree of
	 * children should be built.
	 * @param lazy {@code boolean} specifying if the built node of the tree
	 * should be fully loaded or not.
	 * @param path {@link String} specifying the portion of the request URI
     * that indicates the context of the request.
	 * @param isWithUrl {@code boolean} specifying if the nodes of the tree
	 * should contain URIs or not.
     *
     * @see GenerateTreeJSONAction#generateChildCLevelJSON(List, String, boolean)
	 */
	private void addChildren(StringBuilder buffer, CLevel clevel, boolean lazy,String path, boolean isWithUrl) {
		LOG.debug("Entering method \"addChildren\".");

		if (!clevel.isLeaf()) {
			buffer.append(COMMA);
			if (lazy) {
				buffer.append(FOLDER_LAZY);
				buffer.append(COMMA);
				// id
				buffer.append("\"id\":");
				buffer.append(" \"" + clevel.getId() + "\" ");
				buffer.append(COMMA);
				buffer.append("\"key\":" + "\"" + clevel.getId() + "\"");
			} else {
				buffer.append(FOLDER_WITH_CHILDREN);
				long localStartTime = System.currentTimeMillis();
				CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();

				List<CLevel> children  = clevelDAO.findChildCLevels(clevel.getId(), ZERO, MAX_NUMBER_OF_CLEVELS);
				databaseTimeCost += System.currentTimeMillis() - localStartTime;
				buffer.append(generateChildCLevelJSON(children, path, isWithUrl));
			}
		}

		LOG.debug("Leaving method \"addChildren\".");
	}

	/**
	 * <p>
     * This method is called from <i>"{@link GenerateTreeJSONAction#addBefore(StringBuilder, CLevel)}"</i>
     * and from <i>"{@link GenerateTreeJSONAction#addAfter(StringBuilder, CLevel)}"</i>.
	 * </p>
	 * <p>
	 * It is used to build, in the {@link StringBuilder} passed, the JSON
	 * element which corresponds with the <i>"more"</i> action for the current
	 * node.
	 * </p>
	 * 
	 * @param buffer {@link StringBuilder} in which the type of the more action
	 * will be added for the tree.
	 * @param type {@link String} specifying the more action which will be
	 * added to the JSON.
	 */
	private static void addMore(StringBuilder buffer, String type) {
		LOG.debug("Entering method \"addMore\".");

		buffer.append("\"more\":");
		buffer.append(" \"" + type+  "\"");

		LOG.debug("Leaving method \"addMore\".");
	}

}
