package eu.apenet.dashboard.actions;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.services.eaccpf.EacCpfService;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.dashboard.utils.PropertiesKeys;
import eu.apenet.dashboard.utils.PropertiesUtil;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;

public class PreviewSecondDisplayAction extends AbstractInstitutionAction {
	private static final Logger LOGGER = Logger.getLogger(PreviewSecondDisplayAction.class);
	private String id;
	private String xmlTypeId;
	
	public String getId() {
		return id;
	}

	public String getXmlTypeId() {
		return xmlTypeId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setXmlTypeId(String xmlTypeId) {
		this.xmlTypeId = xmlTypeId;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1953305569537661585L;

	public String execute() throws Exception {
		try {
			XmlType xmlType = XmlType.getType(Integer.parseInt(getXmlTypeId()));
			if (StringUtils.isNotBlank(getId()) && StringUtils.isNumeric(getId())) {
				if (xmlType.equals(XmlType.EAC_CPF)){
					EacCpfService.createPreviewHTML(xmlType, Integer.parseInt(getId()));
					//return super.displayEacCpf();
				}else{
					EadService.createPreviewHTML(xmlType, Integer.parseInt(getId()));
					Ead ead = DAOFactory.instance().getEadDAO().findById(Integer.parseInt(id), xmlType.getClazz());
					if (ead != null && getAiId() != null && getAiId().equals(ead.getAiId())){
						getServletRequest().setAttribute("xmlTypeName", xmlType.getResourceName());
						getServletRequest().setAttribute("identifier", ead.getEncodedIdentifier());
						getServletRequest().setAttribute("repoCode", ead.getArchivalInstitution().getEncodedRepositorycode());
						String url = "http://" + PropertiesUtil.get(PropertiesKeys.APE_PORTAL_DOMAIN) + PropertiesUtil.get(PropertiesKeys.APE_PORTAL_EAD_DISPLAY);
						getServletRequest().setAttribute("url", url);
					}
				}
			}
		}catch (Exception e){
			LOGGER.error(getText("previewseconddisplay.unabletopreview") + " (id,xmlType): (" + getId() + "," + getXmlTypeId() + "): " + e.getMessage() ,e);
			addActionError(getText("error.user.second.display.notindexed"));
			return ERROR;
		}
		return SUCCESS;
	}

}
