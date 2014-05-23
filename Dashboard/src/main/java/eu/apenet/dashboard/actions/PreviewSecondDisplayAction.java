package eu.apenet.dashboard.actions;

import org.apache.commons.lang.StringUtils;

import eu.apenet.commons.seconddisplay.SecondDisplayAction;
import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.services.eaccpf.EacCpfService;
import eu.apenet.dashboard.services.ead.EadService;

public class PreviewSecondDisplayAction extends SecondDisplayAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1953305569537661585L;

	@Override
	public String execute() throws Exception {
		dashboard = true;
		try {
			XmlType xmlType = XmlType.getType(Integer.parseInt(getXmlTypeId()));
			if (StringUtils.isNotBlank(getId()) && StringUtils.isNumeric(getId())) {
				if (xmlType.equals(XmlType.EAC_CPF)){
					EacCpfService.createPreviewHTML(xmlType, Integer.parseInt(getId()));
					return super.displayEacCpf();
				}else{
					EadService.createPreviewHTML(xmlType, Integer.parseInt(getId()));
				}
			}
		}catch (Exception e){
			logger.error(getText("previewseconddisplay.unabletopreview") + " (id,xmlType): (" + getId() + "," + getXmlTypeId() + "): " + e.getMessage() ,e);
			addActionError(getText("error.user.second.display.notindexed"));
			return ERROR;
		}
		return super.execute();
	}

}
