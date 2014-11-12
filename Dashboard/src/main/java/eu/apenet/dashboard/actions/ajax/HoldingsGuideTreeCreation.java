package eu.apenet.dashboard.actions.ajax;

import eu.apenet.commons.types.XmlType;

/**
 * User: Yoann Moranville
 * Date: 06/07/2011
 *
 * @author Yoann Moranville
 */
public class HoldingsGuideTreeCreation extends HgSgTreeCreation {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2571617362432282062L;
	
	
    @Override
	public String execute() {
    	//getServletRequest().setAttribute(EAD_XML_TYPE, XmlType.EAD_HG.getIdentifier());
    	this.eadXmlTypeId = XmlType.EAD_HG.getIdentifier();
		return SUCCESS;
	}

    @Override
    protected void buildBreadcrumbs() {
        super.buildBreadcrumbs();
        addBreadcrumb(getText("dashboard.hgcreation.title"));
    }
}
