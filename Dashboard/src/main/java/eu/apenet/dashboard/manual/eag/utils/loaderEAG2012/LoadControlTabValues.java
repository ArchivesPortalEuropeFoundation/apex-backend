package eu.apenet.dashboard.manual.eag.utils.loaderEAG2012;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dashboard.manual.eag.utils.EAG2012Loader;
import eu.apenet.dpt.utils.eag2012.Eag;

/**
 * Class for load control tab values from the XML
 */
public  class LoadControlTabValues implements  LoaderEAG2012{
	private final Logger log = Logger.getLogger(getClass());

	/**
	 * @param controlNumberOfLanguages {@link String} the controlNumberOfLanguages to add
	 */
	public void addControlNumberOfLanguages(String controlNumberOfLanguages) {
		this.loader.getControlNumberOfLanguages().add(controlNumberOfLanguages);
	}
	/**
	 * @param controlLanguageDeclaration {@link String} the controlLanguageDeclaration to add
	 */
	public void addControlLanguageDeclaration(String controlLanguageDeclaration) {
		this.loader.getControlLanguageDeclaration().add(controlLanguageDeclaration);
	}
	/**
	 * @param controlScript  {@link String} the controlScript to add
	 */
	public void addControlScript(String controlScript) {
		this.loader.getControlScript().add(controlScript);
	}
	/**
	 * @param controlNumberOfRules {@link String} the controlNumberOfRules to add
	 */
	public void addControlNumberOfRules(String controlNumberOfRules) {
		this.loader.getControlNumberOfRules().add(controlNumberOfRules);
	}
	/**
	 * @param controlAbbreviation {@link String} the controlAbbreviation to add
	 */
	public void addControlAbbreviation(String controlAbbreviation) {
		this.loader.getControlAbbreviation().add(controlAbbreviation);
	}
	/**
	 * @param controlCitation {@link String} the controlCitation to add
	 */
	public void addControlCitation(String controlCitation) {
		this.loader.getControlCitation().add(controlCitation);
	}

	private EAG2012Loader loader;
	/**
	 * EAG2012b {@link Eag2012} JAXB object.
	 */
	protected Eag eag;

	@Override
	public Eag LoaderEAG2012(Eag eag, EAG2012Loader eag2012Loader) {		
		this.eag=eag;
		this.loader = eag2012Loader;
		main();
		return this.eag;
	}

	/**
	 * Method to load all values of "control" tab of institution
	 */
	private void main(){
		this.log.debug("Method start: \"Main of class LoadControlTabValues\"");
		// Lang of person/institution responsible for the description.
		if (this.eag.getControl() != null && this.eag.getControl().getMaintenanceHistory() != null
				&& !this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().isEmpty()) {
			if (this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().get(this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().size()-1).getAgent() != null) {
				this.loader.setControlAgentLang(this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().get(this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().size()-1).getAgent().getLang());
			}
		}
		// Identifier of responsible institution.
		if (this.eag.getControl() != null && this.eag.getControl().getMaintenanceAgency() != null) {
			if (this.eag.getControl().getMaintenanceAgency().getAgencyCode() != null
					&& this.eag.getControl().getMaintenanceAgency().getAgencyCode().getContent() != null
					&& !this.eag.getControl().getMaintenanceAgency().getAgencyCode().getContent().isEmpty()) {
				this.loader.setControlAgencyCode(this.eag.getControl().getMaintenanceAgency().getAgencyCode().getContent());
			}
		}
		// Used languages and scripts for the description.
		loadControlUsedLanguages();
		//Used rules / conventions / standards.
		loadControlUsedRules();	
		this.log.debug("End method: \"Main of class LoadControlTabValues\"");
	}

	/**
	 * Method to load all values of "Control" tab in the part of  Used languages and scripts for the description of institution
	 */
	private void loadControlUsedLanguages(){
		this.log.debug("Method start: \"loadControlUsedLanguages\"");
		// Used languages and scripts for the description.
		if (this.eag.getControl() != null && this.eag.getControl().getLanguageDeclarations() != null) {
			if (!this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().isEmpty()) {
				for (int i = 0; i < this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().size(); i++) {
					this.addControlNumberOfLanguages("");
					if (this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i) != null
							&& this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getLanguage() != null
							&& this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getLanguage().getLanguageCode() != null
							&& !this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getLanguage().getLanguageCode().isEmpty()) {
						this.addControlLanguageDeclaration(this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getLanguage().getLanguageCode());
					} else {
						this.addControlLanguageDeclaration("");
					}
					if (this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i) != null
							&& this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getScript() != null
							&& this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getScript().getScriptCode() != null
							&& !this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getScript().getScriptCode().isEmpty()) {
						this.addControlScript(this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().get(i).getScript().getScriptCode());
					} else {
						this.addControlScript("");
					}
				}
			}
		}
		this.log.debug("End method: \"loadControlUsedLanguages\"");
	}

	/**
	 * Method to load all values of "Control" tab in the part of  Used rules / conventions / standards of institution
	 */
	private void loadControlUsedRules(){
		this.log.debug("Method start: \"loadControlUsedRules\"");
		//Used rules / conventions / standards.
		if (this.eag.getControl() != null && this.eag.getControl().getConventionDeclaration() != null) {
			if (!this.eag.getControl().getConventionDeclaration().isEmpty()) {
				for (int i = 0; i < this.eag.getControl().getConventionDeclaration().size(); i++) {
					this.addControlNumberOfRules("");
					if (this.eag.getControl().getConventionDeclaration().get(i) != null
							&& this.eag.getControl().getConventionDeclaration().get(i).getAbbreviation() != null
							&& this.eag.getControl().getConventionDeclaration().get(i).getAbbreviation().getContent() != null
							&& !this.eag.getControl().getConventionDeclaration().get(i).getAbbreviation().getContent().isEmpty()) {
						this.addControlAbbreviation(this.eag.getControl().getConventionDeclaration().get(i).getAbbreviation().getContent());
					} else {
						this.addControlAbbreviation("");
					}
					if (this.eag.getControl().getConventionDeclaration().get(i) != null
							&& !this.eag.getControl().getConventionDeclaration().get(i).getCitation().isEmpty()) {
						for (int j = 0; j < this.eag.getControl().getConventionDeclaration().get(i).getCitation().size(); j++) {
							if (this.eag.getControl().getConventionDeclaration().get(i).getCitation().get(j) != null
									&& this.eag.getControl().getConventionDeclaration().get(i).getCitation().get(j).getContent() != null
									&& !this.eag.getControl().getConventionDeclaration().get(i).getCitation().get(j).getContent().isEmpty()) {
								this.addControlCitation(this.eag.getControl().getConventionDeclaration().get(i).getCitation().get(j).getContent());
							} else {
								this.addControlCitation("");
							}
						}
					} else {
						this.addControlCitation("");
					}
				}
			}
		}
		this.log.debug("End method: \"loadControlUsedRules\"");
	}	
}
