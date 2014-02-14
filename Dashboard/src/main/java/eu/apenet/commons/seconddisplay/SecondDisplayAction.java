package eu.apenet.commons.seconddisplay;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.dao.CouAlternativeNameDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.Ead;

public class SecondDisplayAction extends ActionSupport implements ServletRequestAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9187683291100651008L;
	protected final Logger logger = Logger.getLogger(this.getClass());
	private String term;
	private String solrid;
	private String eadid;
	private String id;
	private String xmlTypeId;
	private String aiId;
	private String title;
	private String countryImageName;
	protected boolean dashboard = false;

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getSolrid() {
		return solrid;
	}

	public void setSolrid(String solrid) {
		this.solrid = solrid;
	}

	public String getEadid() {
		return eadid;
	}

	public void setEadid(String eadid) {
		this.eadid = eadid;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getXmlTypeId() {
		return xmlTypeId;
	}

	public void setXmlTypeId(String xmlTypeId) {
		this.xmlTypeId = xmlTypeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCountryImageName() {
		if (StringUtils.isNotEmpty(countryImageName))
			return "header_" + StringUtils.lowerCase(countryImageName) + ".png";
		return "header_default.png";
	}

	public void setCountryImageName(String countryImageName) {
		this.countryImageName = countryImageName;
	}

	public String getAiId() {
		return aiId;
	}

	public void setAiId(String aiId) {
		this.aiId = aiId;
	}

	public String execute() throws Exception {
		try {
			ArchivalInstitution archivalInstitution = null;
			Ead ead = null;
			if (StringUtils.isNotBlank(solrid)) {
				String subSolrId = solrid.substring(1);
				if (StringUtils.isNotBlank(subSolrId) && StringUtils.isNumeric(subSolrId)) {
					CLevel clevel = DAOFactory.instance().getCLevelDAO().findById(Long.parseLong(subSolrId));
					if (clevel != null) {
						title = clevel.getEadContent().getTitleproper();
						ead = clevel.getEadContent().getEad();
					}
				}
			} else {
				XmlType xmlType = XmlType.getType(Integer.parseInt(xmlTypeId));
				if (StringUtils.isNotBlank(aiId) && StringUtils.isNumeric(aiId)) {
					if (StringUtils.isNotBlank(eadid)) {
						ead = DAOFactory.instance().getEadDAO()
								.getEadByEadid(xmlType.getClazz(), Integer.parseInt(aiId), eadid);
					}

				} else if (StringUtils.isNotBlank(id) && StringUtils.isNumeric(id)) {
					ead = DAOFactory.instance().getEadDAO().findById(Integer.parseInt(id), xmlType.getClazz());
				}

			}
			if (ead == null) {
				logger.error("Could not found EAD in second display for URL params: " + request.getRequestURI() + "?"
						+ request.getQueryString());
				addActionError(getText("error.user.second.display.notexist"));
				return ERROR;
			} else {
				if (dashboard || ead.isPublished()) {
					title = ead.getTitle();
					archivalInstitution = ead.getArchivalInstitution();
				} else {
					logger.error("Found not indexed EAD in second display for URL params: " + request.getRequestURI()
							+ "?" + request.getQueryString());
					addActionError(getText("error.user.second.display.notindexed"));
					return ERROR;
				}
			}
			request.setAttribute("ead", ead);
			request.setAttribute("xmlTypeId", XmlType.getEadType(ead).getIdentifier());
			request.setAttribute("archivalInstitution", archivalInstitution);
			Country country = archivalInstitution.getCountry();
			request.setAttribute("countryImageName", "header_" + StringUtils.lowerCase(country.getIsoname()) + ".png");
			Locale locale = getLocale();
			if (locale == null) {
				request.setAttribute("localizedCountryName", country.getCname());
				request.setAttribute("locale", "en");
			} else {
				String language = getLocale().getLanguage();
				CouAlternativeNameDAO couAlternativeDAO = DAOFactory.instance().getCouAlternativeNameDAO();
				request.setAttribute("localizedCountryName",
						couAlternativeDAO.getLocalizedCountry(country.getIsoname(), language));
				request.setAttribute("locale", locale.getLanguage());
			}
			return SUCCESS;
		} catch (Exception e) {
			logger.error(
					"Error in second display process for URL params: " + request.getRequestURI() + "?"
							+ request.getQueryString(), e);
			addActionError(getText("error.user.second.display.notexist"));
			return ERROR;
		}
	}

	private HttpServletRequest request;

	@Override
	public void setServletRequest(HttpServletRequest httpServletRequest) {
		this.request = httpServletRequest;
	}
}
