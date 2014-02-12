package eu.apenet.commons.seconddisplay;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import eu.apenet.persistence.dao.CouAlternativeNameDAO;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.solr.SolrValues;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;


/**
 * 
 * This is details page for the c level.
 * @author bverhoef
 *
 */
public class DisplayCContentAction extends ActionSupport implements ServletRequestAware {



	private static final int PAGE_SIZE = 10;

	/**
	 * 
	 */
	private static final long serialVersionUID = 5126707833256227612L;
	private final static Logger LOG = Logger.getLogger(DisplayCContentAction.class);
	private String id;
	private String pageNumber;

    private HttpServletRequest request;



    public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}



	public String getPageNumber() {
		return pageNumber;
	}


	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}


	public DisplayCContentAction(){

    }


    public String execute(){
		if (StringUtils.isNotBlank(id)){
			if (id.startsWith(SolrValues.C_LEVEL_PREFIX)){
				id = id.substring(1);
			}
			Long idLong = new Long(id);
			CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
			long startTime = System.currentTimeMillis();
			CLevel currentCLevel = clevelDAO.findById(idLong);
			Integer pageNumberInt = 1;
			if (StringUtils.isNotBlank(pageNumber) && StringUtils.isNumeric(pageNumber)){
				pageNumberInt = new Integer(pageNumber);
			}
			int orderId = (pageNumberInt -1) *PAGE_SIZE;
			List<CLevel> children = clevelDAO.findChildCLevels(currentCLevel.getClId(), orderId, PAGE_SIZE);
			Long totalNumberOfChildren = clevelDAO.countChildCLevels(idLong);
			LOG.debug("Database time: " + (System.currentTimeMillis() - startTime) + "ms");
			StringBuilder builder = new StringBuilder();
			builder.append("<c xmlns=\"urn:isbn:1-931666-22-9\">");
			for (CLevel child: children){
				builder.append(child.getXml());
			}
			builder.append("</c>");
			request.setAttribute("c", currentCLevel);
			request.setAttribute("totalNumberOfChildren", totalNumberOfChildren);
			request.setAttribute("pageNumber", pageNumberInt);
			request.setAttribute("pageSize", PAGE_SIZE);
			request.setAttribute("childXml", builder.toString());
            if(request.getParameter("print") != null) {
                Country country = currentCLevel.getEadContent().getEad().getArchivalInstitution().getCountry();
                request.setAttribute("countryImageName", "header_" + StringUtils.lowerCase(country.getIsoname()) + ".png");

                Locale locale = getLocale();
                if (locale == null) {
                    request.setAttribute("localizedCountryName", country.getCname());
                } else {
                    String language = getLocale().getLanguage();
                    request.setAttribute("localizedCountryName", DAOFactory.instance().getCouAlternativeNameDAO().getLocalizedCountry(country.getIsoname(), language));
                }
            }
		}

        return SUCCESS;
    }

    @Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

 

 
}
