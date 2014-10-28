package eu.apenet.dashboard.actions;

import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.User;

public class SetFeedbackEmailaddressAction extends AbstractInstitutionAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2768649306123342939L;
	private String feedbackEmail;
	private String currentFeedbackEmail;


	protected void buildBreadcrumbs() {
        super.buildBreadcrumbs();
        addBreadcrumb(getText("dashboard.menu.ai.setfeedbackemail"));

	}

	public void validate() {
		if (StringUtils.isNotBlank(feedbackEmail)){
			String email = feedbackEmail.trim();
			if (email.length() > 255){
					addFieldError("keywords", getText("errors.toolong", new String[]{"255"}));
			}else {
	            Matcher matcher = APEnetUtilities.EMAIL_PATTERN.matcher(feedbackEmail.trim());
	            if(!matcher.matches())
	                addFieldError("feedbackEmail", getText("email.valid"));
			}
			
		}
	}
	

	@Override
	public void prepare() throws Exception {
		ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getAiId());
		currentFeedbackEmail =  archivalInstitution.getFeedbackEmail();
		if (StringUtils.isBlank(currentFeedbackEmail)){
			if (archivalInstitution.getPartner() != null){
				currentFeedbackEmail = archivalInstitution.getPartner().getEmailAddress();
			}else {
				User countryManager= DAOFactory.instance().getUserDAO().getCountryManagerOfCountry(archivalInstitution.getCountry());
				if (countryManager != null){
					currentFeedbackEmail = countryManager.getEmailAddress() + " " + getText("usermanagement.no.institutionmanager");
				}else {
					currentFeedbackEmail = getText("dashboard.setfeedbackemail.onlyadmin");
				}
			}
		}		
		super.prepare();
	}

	@Override
	public String input() throws Exception {
		ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getAiId());
		feedbackEmail = archivalInstitution.getFeedbackEmail();
		return super.input();
	}

	public String execute() {
		ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getAiId());
		if (StringUtils.isBlank(feedbackEmail)){
			archivalInstitution.setFeedbackEmail(null);
		}else {
			archivalInstitution.setFeedbackEmail(feedbackEmail);
		}
		DAOFactory.instance().getArchivalInstitutionDAO().store(archivalInstitution);
		return SUCCESS;
	}


	public String cancel() {
		return SUCCESS;
	}

	public String getFeedbackEmail() {
		return feedbackEmail;
	}

	public String getCurrentFeedbackEmail() {
		return currentFeedbackEmail;
	}

	public void setFeedbackEmail(String feedbackEmail) {
		this.feedbackEmail = feedbackEmail;
	}

	public void setCurrentFeedbackEmail(String currentFeedbackEmail) {
		this.currentFeedbackEmail = currentFeedbackEmail;
	}

}
