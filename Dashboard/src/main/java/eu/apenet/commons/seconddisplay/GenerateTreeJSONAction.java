package eu.apenet.commons.seconddisplay;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.dao.EadDAO;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.utils.DisplayUtils;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.EadContentDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.EadContent;

/**
 * Servlet implementation class TopCLevelsServlet
 */
public class GenerateTreeJSONAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	
	private static final String END_ITEM_WITH_COMMA = "},";

	/**
	 * 
	 */
	private static final long serialVersionUID = 3891790469632078308L;

	private final static Logger LOG = Logger.getLogger(GenerateTreeJSONAction.class);
	private static final int MAX_NUMBER_OF_CLEVELS = 20;

	private static final int MAX_NUMBER_OF_CHARACTERS = 100;
	private static final String FOLDER_LAZY = "\"isFolder\": true, \"isLazy\": true";
	private static final String UTF8 = "UTF-8";
	private static final String END_ITEM_WITH_RETURN = "}\n";
	private static final String END_ARRAY = "]\n";
	private static final String START_ARRAY = "[\n";
	private static final String END_ITEM = "}";
	private static final String START_ITEM = "{";
	private static final String FOLDER_WITH_CHILDREN = "\"isFolder\": true, \"children\": \n";
	private static final String COMMA = ",";
	private static final int ZERO = 0;
	private static final String MORE_VALUE_BEFORE = "before";
	private static final String MORE_VALUE_AFTER = "after";
    private static final String ICON_MORE = "\"icon\": \"more_folder.gif\"";
	private long databaseTimeCost =0;
	private String more;


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


	public String getMore() {
		return more;
	}

	public void setMore(String more) {
		this.more = more;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public String execute() {
		try {
			long startTime = System.currentTimeMillis();
			long buildTime = -1;
			String fileIdString = request.getParameter("fileId");
			String xmlTypeIdString = request.getParameter("xmlTypeId");
			String parentIdString = request.getParameter("parentId");
			String ecIdString = request.getParameter("ecId");
			String solrIdString = request.getParameter("solrId");
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
			}  else if (StringUtils.isNotBlank(ecIdString) && StringUtils.isNumeric(ecIdString)) {
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
			}else if (StringUtils.isNotBlank(solrIdString)) {
				Long solrId = new Long(solrIdString.substring(1));
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
			}else if (StringUtils.isNotBlank(fileIdString) && StringUtils.isNumeric(fileIdString)) {
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
		return null;
	}

    public String executeNoPreface() {
        try {
			String findingAidIdString = request.getParameter("findingAidId");
			String holdingsGuideIdString = request.getParameter("holdingsGuideId");
			String isWithUrlString = request.getParameter("isWithUrl");
			String xmlTypeIdString = request.getParameter("xmlTypeId");
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

            EadContent eadContent = null;
            if (findingAidIdString != null && findingAidIdString.trim().length() > 0) {
				Integer findingAidId = new Integer(findingAidIdString);
				eadContent = eadContentDAO.getEadContentByFindingAidId(findingAidId);
            } else if (holdingsGuideIdString != null && holdingsGuideIdString.trim().length() > 0){
                Integer holdingsGuideId = new Integer(holdingsGuideIdString);
				eadContent = eadContentDAO.getEadContentByHoldingsGuideId(holdingsGuideId);
            }
            if(eadContent == null)
                throw new APEnetException(getText("generateTreeJSON.APEnetException.no.correct.FA"));

            StringBuilder topCLevelsBuffer = generateCLevelJSON(clevelDAO.findTopCLevels(eadContent.getEcId()), path, isWithUrl);
            writer.write(generateRootJSON(eadContent, topCLevelsBuffer, path, false, false, xmlTypeIdString).toString());
            writer.close();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
    }

	private StringBuilder generateRootJSON(EadContent eadContent, StringBuilder childBuffer, String path, 
			boolean expand, boolean isWithPreface, String xmlTypeIdString) {
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
		buffer.append(generateChildrenOfRootJSON(eadContent, childBuffer, path, expand, isWithPreface, xmlTypeIdString));
		buffer.append(END_ITEM_WITH_RETURN);
		buffer.append(END_ARRAY);
		return buffer;
	}

    private StringBuilder generateRootJSON(EadContent eadContent, StringBuilder childBuffer, String path, boolean expand, String xmlTypeIdString) {
		return generateRootJSON(eadContent, childBuffer, path, expand, true, xmlTypeIdString);
	}

	private StringBuilder generateChildrenOfRootJSON(EadContent eadContent, StringBuilder childBuffer, String path, boolean expand, boolean isWithPreface, String xmlTypeIdString) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(START_ARRAY);
        buffer.append(START_ITEM);
        if(isWithPreface){
            addTitle(buffer, getText("eadcontent.introduction"));
            buffer.append(COMMA);
            addUrl(buffer, eadContent, path + "/displayEadIntroduction.action", xmlTypeIdString);
            buffer.append(END_ITEM_WITH_COMMA);
            buffer.append(START_ITEM);
            addTitle(buffer, eadContent.getUnittitle());
            buffer.append(COMMA);
            addUrl(buffer, eadContent, path + "/displayEadDidContent.action", xmlTypeIdString);
        } else {
            addTitle(buffer, eadContent.getUnittitle());
            buffer.append(COMMA);
            addFaId(buffer, eadContent.getFaId()) ;
        }
        if (expand) {
            buffer.append(COMMA);
            addExpand(buffer);
        }
		buffer.append(COMMA);

		buffer.append(FOLDER_WITH_CHILDREN);
		buffer.append(childBuffer);
		buffer.append(END_ITEM_WITH_RETURN);
		buffer.append(END_ARRAY);
		return buffer;

	}

	private StringBuilder generateCLevelJSON(List<CLevel> clevels, String path, boolean isWithUrl) {
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
			    addCContentUrl(buffer, clevel.getClId(), path);
            else
                addId(buffer, clevel.getClId());
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
		return buffer;
	}

	private void addBefore(StringBuilder buffer, CLevel clevel){
		if (!MORE_VALUE_AFTER.equalsIgnoreCase(more) && (clevel.getOrderId() > 0)){
			buffer.append(START_ITEM);
			addMoreTitle(buffer, getText("eadcontent.more.before"));
			buffer.append(COMMA);
			addMore(buffer,MORE_VALUE_BEFORE );
			buffer.append(COMMA);
			buffer.append(FOLDER_LAZY);
			buffer.append(COMMA);
			buffer.append("\"parentId\":");
			buffer.append(" \"" + clevel.getParentClId() + "\" ");
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
	}
	private void addAfter(StringBuilder buffer, CLevel clevel){
		if (!MORE_VALUE_BEFORE.equalsIgnoreCase(more) ){
			buffer.append(COMMA);
			buffer.append(START_ITEM);
			addMoreTitle(buffer, getText("eadcontent.more.after"));
			buffer.append(COMMA);
			buffer.append("\"parentId\":");
			buffer.append(" \"" + clevel.getParentClId() + "\" ");
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
	
	}
	private StringBuilder generateJSONWithSelectedItem(CLevel clevel, String path, boolean isWithUrl, String xmlTypeIdString) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(START_ARRAY);
		addBefore(buffer, clevel);
		buffer.append(START_ITEM);
		addTitle(buffer, clevel);
		buffer.append(COMMA);
        if(isWithUrl)
		    addCContentUrl(buffer, clevel.getClId(), path);
        else
            addId(buffer, clevel.getClId());
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
		return generateParentCLevelJSON(clevel, buffer, path, isWithUrl, xmlTypeIdString);
	}

	private StringBuilder generateParentCLevelJSON(CLevel child, StringBuilder childBuffer, String path, boolean isWithUrl, String xmlTypeIdString) {
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
			    addCContentUrl(buffer, parent.getClId(), path);
            else
                addId(buffer, parent.getClId());
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
			return generateParentCLevelJSON(parent, buffer,  path, isWithUrl, xmlTypeIdString);
		} else {
			localStartTime = System.currentTimeMillis();
			//List<CLevel> topCLevels = DAOFactory.instance().getCLevelDAO().findTopCLevels(child.getEadContent().getEcId());
			databaseTimeCost += System.currentTimeMillis() - localStartTime;
			localStartTime = System.currentTimeMillis();
			EadContent eadContent = child.getEadContent();
			databaseTimeCost += System.currentTimeMillis() - localStartTime;
			return generateRootJSON(eadContent, childBuffer, path, true, xmlTypeIdString);
		}
	}



	private StringBuilder generateChildCLevelJSON(List<CLevel> clevels, String path, boolean isWithUrl) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(START_ARRAY);
		for (int i = 0; i < clevels.size(); i++) {
			CLevel clevel = clevels.get(i);
			buffer.append(START_ITEM);
			addTitle(buffer, clevel);
			buffer.append(COMMA);
            if(isWithUrl)
			    addCContentUrl(buffer, clevel.getClId(), path);
            else
                addId(buffer, clevel.getClId());
			addChildren(buffer, clevel, true, path, isWithUrl);
			buffer.append(END_ITEM);
			if (i < clevels.size() - 1) {
				buffer.append(",");
			} else {
//				buffer.append("");
			}
		}
		buffer.append(END_ARRAY);
		return buffer;
	}
	private void addTitle(StringBuilder buffer, String title) {
		addNoIcon(buffer);
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
		

	}
	private void addTitle(StringBuilder buffer, CLevel clevel) {
		addTitle(buffer,clevel.getUnittitle());
	}

    private static void addFaId(StringBuilder buffer, Integer faId){
        buffer.append("\"faId\": \"");
		buffer.append(faId);
		buffer.append("\" ");
    }
	
	private static void addMoreTitle(StringBuilder buffer, String title) {
		buffer.append("\"addClass\":");
		buffer.append(" \"more\"");
        buffer.append(COMMA);
        addNoIcon(buffer);
		buffer.append("\"title\":");
		String fullTitle = DisplayUtils.encodeHtml(title);
		buffer.append("\"" +fullTitle + "\"");

	}
	


	private static void addCContentUrl(StringBuilder buffer, Long clId, String path) {
		buffer.append("\"url\":");
		buffer.append(" \"" + path + "/displayCContent.action");
		buffer.append("?id=" + clId);
		buffer.append("\" ");
	}

    private static void addId(StringBuilder buffer, Long clId) {
		buffer.append("\"id\":");
		buffer.append("\"");
        buffer.append(clId);
		buffer.append("\"");
	}

	private static void addUrl(StringBuilder buffer, EadContent eadContent, String path, String xmlTypeIdString) {
		buffer.append("\"url\":");
		buffer.append(" \"" + path);
        buffer.append("?fileId=").append(eadContent.getEadIdentifier());
		buffer.append("&xmlTypeId=").append(xmlTypeIdString);

		buffer.append("\" ");
	}

	private static void addExpand(StringBuilder buffer) {
		buffer.append("\"expand\":true");
	}

	private void addChildren(StringBuilder buffer, CLevel clevel, boolean lazy,String path, boolean isWithUrl) {
		if (!clevel.isLeaf()) {
			buffer.append(COMMA);
			if (lazy) {
				buffer.append(FOLDER_LAZY);
				buffer.append(COMMA);
				// id
				buffer.append("\"id\":");
				buffer.append(" \"" + clevel.getClId() + "\" ");
			} else {
				buffer.append(FOLDER_WITH_CHILDREN);
				long localStartTime = System.currentTimeMillis();
				CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
				
				List<CLevel> children  = clevelDAO.findChildCLevels(clevel.getClId(), ZERO, MAX_NUMBER_OF_CLEVELS);
				databaseTimeCost += System.currentTimeMillis() - localStartTime;
				buffer.append(generateChildCLevelJSON(children, path, isWithUrl));
			}
		}
	}
	
	private static void addMore(StringBuilder buffer, String type) {
		buffer.append("\"more\":");
		buffer.append(" \"" + type+  "\"");
	
	}
	protected static void addNoIcon(StringBuilder buffer) {
		buffer.append("\"icon\":");
		buffer.append(" false");
		buffer.append(COMMA);
	}
}
