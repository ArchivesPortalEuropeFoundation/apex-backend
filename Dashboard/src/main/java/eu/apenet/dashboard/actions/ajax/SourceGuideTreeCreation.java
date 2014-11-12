package eu.apenet.dashboard.actions.ajax;

import eu.apenet.commons.types.XmlType;

/**
 * User: Yoann Moranville Date: 06/07/2011
 * 
 * @author Yoann Moranville
 */
public class SourceGuideTreeCreation extends HgSgTreeCreation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3759323644664648328L;

    @Override
	public String execute() {
    	//getServletRequest().setAttribute(EAD_XML_TYPE, XmlType.EAD_SG.getIdentifier());
    	this.eadXmlTypeId = XmlType.EAD_SG.getIdentifier();
		return SUCCESS;
	}

	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb(getText("dashboard.sgcreation.title"));
	}
}
