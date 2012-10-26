package eu.apenet.dashboard.actions.content;

import eu.apenet.dashboard.AbstractInstitutionAction;

public class ContentManagerAction extends AbstractInstitutionAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4513310293148562803L;
	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb(getText("breadcrumb.section.contentmanager"));
	}
	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	
}
