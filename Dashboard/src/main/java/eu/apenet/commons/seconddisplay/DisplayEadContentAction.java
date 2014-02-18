package eu.apenet.commons.seconddisplay;

import javax.servlet.http.HttpServletRequest;

import eu.apenet.persistence.vo.Country;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;

import java.util.Locale;


/**
 *
 * This is a index page
 * @author bverhoef
 *
 */
public class DisplayEadContentAction extends ActionSupport implements ServletRequestAware {

    private static final Logger LOG = Logger.getLogger(DisplayEadContentAction.class);

	/**
	 *
	 */
	private static final long serialVersionUID = 5126707833256227612L;

	private String fileId;
	private String xmlTypeId;

	private String xml;
	private String contextPath;

    private HttpServletRequest request;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getXmlTypeId() {
        return xmlTypeId;
    }

    public void setXmlTypeId(String xmlTypeId) {
        this.xmlTypeId = xmlTypeId;
    }

    public String execute(){
        Integer fileIdInteger = new Integer(fileId);
        XmlType xmlType = XmlType.getType(new Integer(xmlTypeId));
        EadContent eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByFileId(fileIdInteger, (Class<? extends Ead>) xmlType.getClazz());
        request.setAttribute("eadContent", eadContent);

        if(request.getParameter("print") != null) {
            Country country = eadContent.getEad().getArchivalInstitution().getCountry();
            request.setAttribute("countryImageName", "header_" + StringUtils.lowerCase(country.getIsoname()) + ".png");

            Locale locale = getLocale();
            if (locale == null) {
                request.setAttribute("localizedCountryName", country.getCname());
            } else {
                String language = getLocale().getLanguage();
                request.setAttribute("localizedCountryName", DAOFactory.instance().getCouAlternativeNameDAO().getLocalizedCountry(country.getIsoname(), language));
            }
        }

		return SUCCESS;
    }

    public String getXml() {
		return xml;
	}


	public void setXml(String xml) {
		this.xml = xml;
	}


	public String getContextPath() {
		return contextPath;
	}


	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}


	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}




}
