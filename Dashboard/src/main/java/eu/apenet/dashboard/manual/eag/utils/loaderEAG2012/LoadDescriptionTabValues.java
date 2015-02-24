package eu.apenet.dashboard.manual.eag.utils.loaderEAG2012;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dashboard.manual.eag.utils.EAG2012Loader;
import eu.apenet.dpt.utils.eag2012.Date;
import eu.apenet.dpt.utils.eag2012.DateRange;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.Repository;

/**
 * Class for load description tab values from the XML
 */
public class LoadDescriptionTabValues implements  LoaderEAG2012{		
	// Holdings.
	private List<String> descHoldingsDescription;
	private List<String> descHoldingsDescriptionLang;
	private List<String> descDateHoldings;
	private List<String> descNumberOfDateRange;
	private List<String> descHoldingsDateFrom;
	private List<String> descHoldingsDateTo;
	private List<String> descHoldingsExtent;
	private List<String> descHoldingsExtentUnit;
	private final Logger log = Logger.getLogger(getClass());

	/**
	 * @param descRepositorhist {@link List<String>} the descRepositorhist to add
	 */
	public void addDescRepositorhist(List<String> descRepositorhist) {
		this.loader.getDescRepositorhist().add(descRepositorhist);
	}
	/**
	 * @param descRepositorhistLang {@link List<String>} the descRepositorhistLang to add
	 */
	public void addDescRepositorhistLang(List<String> descRepositorhistLang) {
		this.loader.getDescRepositorhistLang().add(descRepositorhistLang);
	}
	/**
	 * @param descRepositorFoundDate {@link List<String>} the descRepositorFoundDate to add
	 */
	public void addDescRepositorFoundDate(List<String> descRepositorFoundDate) {
		this.loader.getDescRepositorFoundDate().add(descRepositorFoundDate);
	}
	/**
	 * @param descRepositorFoundRule {@link List<String>} the descRepositorFoundRule to add
	 */
	public void addDescRepositorFoundRule(List<String> descRepositorFoundRule) {
		this.loader.getDescRepositorFoundRule().add(descRepositorFoundRule);
	}
	/**
	 * @param descRepositorFoundRuleLang {@link List<String>} the descRepositorFoundRuleLang to add
	 */
	public void addDescRepositorFoundRuleLang(List<String> descRepositorFoundRuleLang) {
		this.loader.getDescRepositorFoundRuleLang().add(descRepositorFoundRuleLang);
	}
	/**
	 * @param descRepositorSupDate {@link List<String>} the descRepositorSupDate to add
	 */
	public void addDescRepositorSupDate(List<String> descRepositorSupDate) {
		this.loader.getDescRepositorSupDate().add(descRepositorSupDate);
	}
	/**
	 * @param descRepositorSupRule {@link List<String>} the descRepositorSupRule to add
	 */
	public void addDescRepositorSupRule(List<String> descRepositorSupRule) {
		this.loader.getDescRepositorSupRule().add(descRepositorSupRule);
	}
	/**
	 * @param descRepositorSupRuleLang {@link List<String>} the descRepositorSupRuleLang to add
	 */
	public void addDescRepositorSupRuleLang(List<String> descRepositorSupRuleLang) {
		this.loader.getDescRepositorSupRuleLang().add(descRepositorSupRuleLang);
	}
	/**
	 * @param descAdminunit {@link List<String>} the descAdminunit to add
	 */
	public void addDescAdminunit(List<String> descAdminunit) {
		this.loader.getDescAdminunit().add(descAdminunit);
	}
	/**
	 * @param descAdminunitLang {@link List<String>} the descAdminunitLang to add
	 */
	public void addDescAdminunitLang(List<String> descAdminunitLang) {
		this.loader.getDescAdminunitLang().add(descAdminunitLang);
	}
	/**
	 * @param descBuilding {@link List<String>} the descBuilding to add
	 */
	public void addDescBuilding(List<String> descBuilding) {
		this.loader.getDescBuilding().add(descBuilding);
	}		
	/**
	 * @param descBuildingLang {@link List<String>} the descBuildingLang to add
	 */
	public void addDescBuildingLang(List<String> descBuildingLang) {
		this.loader.getDescBuildingLang().add(descBuildingLang);
	}		
	/**
	 * @param descRepositorarea {@link List<String>} the descRepositorarea to add
	 */
	public void addDescRepositorarea(List<String> descRepositorarea) {
		this.loader.getDescRepositorarea().add(descRepositorarea);
	}		
	/**
	 * @param descRepositorareaUnit {@link List<String>} the descRepositorareaUnit to add
	 */
	public void addDescRepositorareaUnit(List<String> descRepositorareaUnit) {
		this.loader.getDescRepositorareaUnit().add(descRepositorareaUnit);
	}		
	/**
	 * @param descLengthshelf {@link List<String>} the descLengthshelf to add
	 */
	public void addDescLengthshelf(List<String> descLengthshelf) {
		this.loader.getDescLengthshelf().add(descLengthshelf);
	}
	/**
	 * @param descLengthshelfUnit {@link List<String>} the descLengthshelfUnit to add
	 */
	public void addDescLengthshelfUnit(List<String> descLengthshelfUnit) {
		this.loader.getDescLengthshelfUnit().add(descLengthshelfUnit);
	}
	/**
	 * @param descHoldings {@link List<String>} the descHoldings to add
	 */
	public void addDescHoldings(List<String> descHoldings) {
		this.loader.getDescHoldings().add(descHoldings);
	}
	/**
	 * @param descHoldingsLang {@link List<String>} the descHoldingsLang to add
	 */
	public void addDescHoldingsLang(List<String> descHoldingsLang) {
		this.loader.getDescHoldingsLang().add(descHoldingsLang);
	}
	/**
	 * @param descHoldingsDate {@link List<String>} the descHoldingsDate to add
	 */
	public void addDescHoldingsDate(List<String> descHoldingsDate) {
		this.loader.getDescHoldingsDate().add(descHoldingsDate);
	}
	/**
	 * @param descNumberOfHoldingsDateRange {@link List<String>} the descNumberOfHoldingsDateRange to add
	 */
	public void addDescNumberOfHoldingsDateRange(List<String> descNumberOfHoldingsDateRange) {
		this.loader.getDescNumberOfHoldingsDateRange().add(descNumberOfHoldingsDateRange);
	}
	/**
	 * @param descHoldingsDateRangeFromDate {@link List<String>} the descHoldingsDateRangeFromDate to add
	 */
	public void addDescHoldingsDateRangeFromDate(List<String> descHoldingsDateRangeFromDate) {
		this.loader.getDescHoldingsDateRangeFromDate().add(descHoldingsDateRangeFromDate);
	}
	/**
	 * @param descHoldingsDateRangeToDate {@link List<String>} the descHoldingsDateRangeToDate to add
	 */
	public void addDescHoldingsDateRangeToDate(List<String> descHoldingsDateRangeToDate) {
		this.loader.getDescHoldingsDateRangeToDate().add(descHoldingsDateRangeToDate);
	}
	/**
	 * @param descExtent {@link List<String>} the descExtent to add
	 */
	public void addDescExtent(List<String> descExtent) {
		this.loader.getDescExtent().add(descExtent);
	}
	/**
	 * @param descExtentUnit {@link List<String>} the descExtentUnit to add
	 */
	public void addDescExtentUnit(List<String> descExtentUnit) {
		this.loader.getDescExtentUnit().add(descExtentUnit);
	}
	private EAG2012Loader loader;

	/**
	 * Eag {@link Eag} JAXB object.
	 */
	protected Eag eag;
	Repository repository;

	@Override
	public Eag LoaderEAG2012(Eag eag, EAG2012Loader eag2012Loader) {		
		this.eag=eag;
		this.loader = eag2012Loader;
		main();
		return this.eag;
	}

	/**
	 * Method to load all values of "description" tab of institution
	 */
	private void main(){
		this.log.debug("Method start: \"Main of class LoadDescriptionTabValues\"");
		// Repositories info.
		if (this.eag.getArchguide() != null && this.eag.getArchguide().getDesc() != null
				&& this.eag.getArchguide().getDesc().getRepositories() != null
				&& !this.eag.getArchguide().getDesc().getRepositories().getRepository().isEmpty()) {
			// For each repository
			for (int i = 0; i < this.eag.getArchguide().getDesc().getRepositories().getRepository().size(); i++) {
				 repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				// Repository history.
				loadDescriptionHistory();
				// Date of repository foundation.
				loadDescriptionFoundation();
				// Date of repository suppression.
				loadDescriptionAuppression();
				// Unit of administrative structure.
				loadDescriptionAdministrative();
				// Building.
				loadDescriptionBuilding();
				// Holdings.
				loadDescriptionHoldings();
			}
		}
		this.log.debug("End method: \"Main of class LoadDescriptionTabValues\"");
	}

	/**
	 * Method to load all values of "Description" tab in the part of Repository history of institution
	 */
	private void loadDescriptionHistory(){
		this.log.debug("Method start: \"loadDescriptionHistory\"");
		// Repository history.
		List<String> descRHDescription = new ArrayList<String>();
		List<String> descRHDescriptionLang = new ArrayList<String>();
		if (repository.getRepositorhist() != null
				&& repository.getRepositorhist().getDescriptiveNote() != null
				&& !repository.getRepositorhist().getDescriptiveNote().getP().isEmpty()) {
			for (int j = 0; j < repository.getRepositorhist().getDescriptiveNote().getP().size(); j++) {
				if (repository.getRepositorhist().getDescriptiveNote().getP().get(j) != null
						&& repository.getRepositorhist().getDescriptiveNote().getP().get(j).getContent() != null
						&& !repository.getRepositorhist().getDescriptiveNote().getP().get(j).getContent().isEmpty()) {
					descRHDescription.add(repository.getRepositorhist().getDescriptiveNote().getP().get(j).getContent());
					if (repository.getRepositorhist().getDescriptiveNote().getP().get(j).getLang() != null
							&& !repository.getRepositorhist().getDescriptiveNote().getP().get(j).getLang().isEmpty()) {
						descRHDescriptionLang.add(repository.getRepositorhist().getDescriptiveNote().getP().get(j).getLang());
					} else {
						descRHDescriptionLang.add(Eag2012.OPTION_NONE);
					}
				} else {
					descRHDescription.add("");
					descRHDescriptionLang.add(Eag2012.OPTION_NONE);
				}
			}
		}
		this.addDescRepositorhist(descRHDescription);
		this.addDescRepositorhistLang(descRHDescriptionLang);		
		this.log.debug("End method: \"loadDescriptionHistory\"");
	}

	/**
	 * Method to load all values of "Description" tab in the part of Date of repository foundation of institution
	 */
	private void loadDescriptionFoundation(){
		this.log.debug("Method start: \"loadDescriptionFoundation\"");
		// Date of repository foundation.
		List<String> descRHFoundationDate = new ArrayList<String>();
		List<String> descRHFoundationRule = new ArrayList<String>();
		List<String> descRHFoundationLang = new ArrayList<String>();
		if (repository.getRepositorfound() != null) {
			if (repository.getRepositorfound().getDate() != null
					&& repository.getRepositorfound().getDate().getContent() != null
					&& !repository.getRepositorfound().getDate().getContent().isEmpty()) {
				descRHFoundationDate.add(repository.getRepositorfound().getDate().getContent());
			} else {
				descRHFoundationDate.add("");
			}
			// Rule of repository foundation.
			if (repository.getRepositorfound().getRule() != null
					&& !repository.getRepositorfound().getRule().isEmpty()) {
				for (int j = 0; j < repository.getRepositorfound().getRule().size(); j++) {
					if (repository.getRepositorfound().getRule().get(j) != null
							&& repository.getRepositorfound().getRule().get(j).getContent() != null
							&& !repository.getRepositorfound().getRule().get(j).getContent().isEmpty()) {
						descRHFoundationRule.add(repository.getRepositorfound().getRule().get(j).getContent());
						if (repository.getRepositorfound().getRule().get(j).getLang() != null
								&& !repository.getRepositorfound().getRule().get(j).getLang().isEmpty()) {
							descRHFoundationLang.add(repository.getRepositorfound().getRule().get(j).getLang());
						} else {
							descRHFoundationLang.add(Eag2012.OPTION_NONE);
						}
					} else {
						descRHFoundationRule.add("");
						descRHFoundationLang.add(Eag2012.OPTION_NONE);
					}
				}
			} else {
				descRHFoundationRule.add("");
				descRHFoundationLang.add(Eag2012.OPTION_NONE);
			}
		}
		this.addDescRepositorFoundDate(descRHFoundationDate);
		this.addDescRepositorFoundRule(descRHFoundationRule);
		this.addDescRepositorFoundRuleLang(descRHFoundationLang);
		this.log.debug("End method: \"loadDescriptionFoundation\"");
	}

	/**
	 * Method to load all values of "Description" tab in the part of Date of repository suppression of institution
	 */
	private void loadDescriptionAuppression(){
		this.log.debug("Method start: \"loadDescriptionAuppression\"");
		// Date of repository suppression.
		List<String> descRHSuppressionDate = new ArrayList<String>();
		List<String> descRHSuppressionRule = new ArrayList<String>();
		List<String> descRHSuppressionLang = new ArrayList<String>();
		if (repository.getRepositorsup() != null) {
			if (repository.getRepositorsup().getDate() != null
					&& repository.getRepositorsup().getDate().getContent() != null
					&& !repository.getRepositorsup().getDate().getContent().isEmpty()) {
				descRHSuppressionDate.add(repository.getRepositorsup().getDate().getContent());
			} else {
				descRHSuppressionDate.add("");
			}
			// Rule of repository suppression.
			if (repository.getRepositorsup().getRule() != null
					&& !repository.getRepositorsup().getRule().isEmpty()) {
				for (int j = 0; j < repository.getRepositorsup().getRule().size(); j++) {
					if (repository.getRepositorsup().getRule().get(j) != null
							&& repository.getRepositorsup().getRule().get(j).getContent() != null
							&& !repository.getRepositorsup().getRule().get(j).getContent().isEmpty()) {
						descRHSuppressionRule.add(repository.getRepositorsup().getRule().get(j).getContent());
						if (repository.getRepositorsup().getRule().get(j).getLang() != null
								&& !repository.getRepositorsup().getRule().get(j).getLang().isEmpty()) {
							descRHSuppressionLang.add(repository.getRepositorsup().getRule().get(j).getLang());
						} else {
							descRHSuppressionLang.add(Eag2012.OPTION_NONE);
						}
					} else {
						descRHSuppressionRule.add("");
						descRHSuppressionLang.add(Eag2012.OPTION_NONE);
					}
				}
			} else {
				descRHSuppressionRule.add("");
				descRHSuppressionLang.add(Eag2012.OPTION_NONE);
			}
		}
		this.addDescRepositorSupDate(descRHSuppressionDate);
		this.addDescRepositorSupRule(descRHSuppressionRule);
		this.addDescRepositorSupRuleLang(descRHSuppressionLang);	
		this.log.debug("End method: \"loadDescriptionAuppression\"");
	}

	/**
	 * Method to load all values of "Description" tab in the part of Unit of administrative structure of institution
	 */
	private void loadDescriptionAdministrative(){
		this.log.debug("Method start: \"loadDescriptionAdministrative\"");
		// Unit of administrative structure.
		List<String> descAdminStructure = new ArrayList<String>();
		List<String> descAdminStructureLang = new ArrayList<String>();
		if (repository.getAdminhierarchy() != null
				&& !repository.getAdminhierarchy().getAdminunit().isEmpty()) {
			for (int j = 0; j < repository.getAdminhierarchy().getAdminunit().size(); j++) {
				if (repository.getAdminhierarchy().getAdminunit().get(j) != null
						&& repository.getAdminhierarchy().getAdminunit().get(j).getContent() != null
						&& !repository.getAdminhierarchy().getAdminunit().get(j).getContent().isEmpty()) {
					descAdminStructure.add(repository.getAdminhierarchy().getAdminunit().get(j).getContent());
					if (repository.getAdminhierarchy().getAdminunit().get(j).getLang() != null
							&& !repository.getAdminhierarchy().getAdminunit().get(j).getLang().isEmpty()) {
						descAdminStructureLang.add(repository.getAdminhierarchy().getAdminunit().get(j).getLang());
					} else {
						descAdminStructureLang.add(Eag2012.OPTION_NONE);
					}
				} else {
					descAdminStructure.add("");
					descAdminStructureLang.add(Eag2012.OPTION_NONE);
				}
			}
		}
		this.addDescAdminunit(descAdminStructure);
		this.addDescAdminunitLang(descAdminStructureLang);	
		this.log.debug("End method: \"loadDescriptionAdministrative\"");
	}

	/**
	 * Method to load all values of "Description" tab in the part of Building of institution
	 */
	private void loadDescriptionBuilding(){
		this.log.debug("Method start: \"loadDescriptionBuilding\"");
		List<String> descBuildingDescription = new ArrayList<String>();
		List<String> descBuildingDescriptionLang = new ArrayList<String>();
		List<String> descBuildingArea = new ArrayList<String>();
		List<String> descBuildingAreaUnit = new ArrayList<String>();
		List<String> descBuildingShelf = new ArrayList<String>();
		List<String> descBuildingShelfUnit = new ArrayList<String>();
		if (repository.getBuildinginfo() != null) {
			if (repository.getBuildinginfo().getBuilding() != null
					&& repository.getBuildinginfo().getBuilding().getDescriptiveNote() != null
					&& !repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().isEmpty()) {
				for (int j = 0; j < repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().size(); j++) {
					if (repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().get(j) != null
							&& repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().get(j).getContent() != null
							&& !repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().get(j).getContent().isEmpty()) {
						descBuildingDescription.add(repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().get(j).getContent());
						if (repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().get(j).getLang() != null
								&& !repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().get(j).getLang().isEmpty()) {
							descBuildingDescriptionLang.add(repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().get(j).getLang());
						} else {
							descBuildingDescriptionLang.add(Eag2012.OPTION_NONE);
						}
					} else {
						descBuildingDescription.add("");
						descBuildingDescriptionLang.add(Eag2012.OPTION_NONE);
					}
				}
			} else {
				descBuildingDescription.add("");
				descBuildingDescriptionLang.add(Eag2012.OPTION_NONE);
			}
			// Repository Area.
			if (repository.getBuildinginfo().getRepositorarea() != null
					&& repository.getBuildinginfo().getRepositorarea().getNum() != null
					&& repository.getBuildinginfo().getRepositorarea().getNum().getContent() != null
					&& !repository.getBuildinginfo().getRepositorarea().getNum().getContent().isEmpty()) {
				descBuildingArea.add(repository.getBuildinginfo().getRepositorarea().getNum().getContent());

				if (repository.getBuildinginfo().getRepositorarea().getNum().getUnit() != null
						&& !repository.getBuildinginfo().getRepositorarea().getNum().getUnit().isEmpty()) {
					descBuildingAreaUnit.add(repository.getBuildinginfo().getRepositorarea().getNum().getUnit());
				} else {
					descBuildingAreaUnit.add("");
				}
			} else {
				descBuildingArea.add("");
				descBuildingAreaUnit.add("");
			}
			// Length of shelf.
			if (repository.getBuildinginfo().getLengthshelf() != null
					&& repository.getBuildinginfo().getLengthshelf().getNum() != null
					&& repository.getBuildinginfo().getLengthshelf().getNum().getContent() != null
					&& !repository.getBuildinginfo().getLengthshelf().getNum().getContent().isEmpty()) {
				descBuildingShelf.add(repository.getBuildinginfo().getLengthshelf().getNum().getContent());

				if (repository.getBuildinginfo().getLengthshelf().getNum().getUnit() != null
						&& !repository.getBuildinginfo().getLengthshelf().getNum().getUnit().isEmpty()) {
					descBuildingShelfUnit.add(repository.getBuildinginfo().getLengthshelf().getNum().getUnit());
				} else {
					descBuildingShelfUnit.add("");
				}
			} else {
				descBuildingShelf.add("");
				descBuildingShelfUnit.add("");
			}
		}
		this.addDescBuilding(descBuildingDescription);
		this.addDescBuildingLang(descBuildingDescriptionLang);
		this.addDescRepositorarea(descBuildingArea);
		this.addDescRepositorareaUnit(descBuildingAreaUnit);
		this.addDescLengthshelf(descBuildingShelf);
		this.addDescLengthshelfUnit(descBuildingShelfUnit);	
		this.log.debug("End method: \"loadDescriptionBuilding\"");
	}

	/**
	 * Method to load all values of "Description" tab in the part of Holdings of institution
	 */
	private void loadDescriptionHoldings(){
		this.log.debug("Method start: \"loadDescriptionHoldings\"");
		// Holdings.
		descHoldingsDescription = new ArrayList<String>();
		descHoldingsDescriptionLang = new ArrayList<String>();
		descDateHoldings = new ArrayList<String>();
		descNumberOfDateRange = new ArrayList<String>();
		descHoldingsDateFrom= new ArrayList<String>();
		descHoldingsDateTo = new ArrayList<String>();
		descHoldingsExtent = new ArrayList<String>();
		descHoldingsExtentUnit = new ArrayList<String>();
		if (repository.getHoldings() != null) {
			loadDescriptionHoldingsLang();
			// Date of holdings.
			// Date range of holdings
			loadDescriptionHoldingsDateRange();
			// DateSet of holdings.
			loadDescriptionHoldingsDateSet();
			// Extent.
			loadDescriptionHoldingsExtent();
		}
		this.addDescHoldings(descHoldingsDescription);
		this.addDescHoldingsLang(descHoldingsDescriptionLang);
		this.addDescHoldingsDate(descDateHoldings);
		this.addDescNumberOfHoldingsDateRange(descNumberOfDateRange);
		this.addDescHoldingsDateRangeFromDate(descHoldingsDateFrom);
		this.addDescHoldingsDateRangeToDate(descHoldingsDateTo);
		this.addDescExtent(descHoldingsExtent);
		this.addDescExtentUnit(descHoldingsExtentUnit);		
		this.log.debug("End method: \"loadDescriptionHoldings\"");
	}

	/**
	 * Method to load all values of "Description" tab in the part of Holdings in lang of institution
	 */
	private void loadDescriptionHoldingsLang(){
		this.log.debug("Method start: \"loadDescriptionHoldingsLang\"");
		if (repository.getHoldings().getDescriptiveNote() != null
				&& !repository.getHoldings().getDescriptiveNote().getP().isEmpty()) {
			for (int j = 0; j < repository.getHoldings().getDescriptiveNote().getP().size(); j++) {
				if (repository.getHoldings().getDescriptiveNote().getP().get(j) != null
						&& repository.getHoldings().getDescriptiveNote().getP().get(j).getContent() != null
						&& !repository.getHoldings().getDescriptiveNote().getP().get(j).getContent().isEmpty()) {
					descHoldingsDescription.add(repository.getHoldings().getDescriptiveNote().getP().get(j).getContent());
					if (repository.getHoldings().getDescriptiveNote().getP().get(j).getLang() != null
							&& !repository.getHoldings().getDescriptiveNote().getP().get(j).getLang().isEmpty()) {
						descHoldingsDescriptionLang.add(repository.getHoldings().getDescriptiveNote().getP().get(j).getLang());
					} else {
						descHoldingsDescriptionLang.add(Eag2012.OPTION_NONE);
					}
				} else {
					descHoldingsDescription.add("");
					descHoldingsDescriptionLang.add(Eag2012.OPTION_NONE);
				}
			}
		} else {
			descHoldingsDescription.add("");
			descHoldingsDescriptionLang.add(Eag2012.OPTION_NONE);
		}		 
		this.log.debug("End method: \"loadDescriptionHoldingsLang\"");
	}

	/**
	 * Method to load all values of "Description" tab in the part of Holdings in date and daterange of institution
	 */
	private void loadDescriptionHoldingsDateRange(){
		this.log.debug("Method start: \"loadDescriptionHoldingsDateRange\"");
		// Date of holdings.
		if (repository.getHoldings().getDate() != null) {
			if (repository.getHoldings().getDate().getContent() != null
				&& !repository.getHoldings().getDate().getContent().isEmpty()) {
			descDateHoldings.add(repository.getHoldings().getDate().getContent());
			} else {
				descDateHoldings.add("");
			}
		}
		// Date range of holdings.
		if (repository.getHoldings().getDateRange() != null) {
			descNumberOfDateRange.add("");
			// From date.
			if (repository.getHoldings().getDateRange().getFromDate() != null
					&& repository.getHoldings().getDateRange().getFromDate().getContent() != null
					&& !repository.getHoldings().getDateRange().getFromDate().getContent().isEmpty()) {
				descHoldingsDateFrom.add(repository.getHoldings().getDateRange().getFromDate().getContent());
			} else {
				descHoldingsDateFrom.add("");
			}
			// To date.
			if (repository.getHoldings().getDateRange().getToDate() != null
					&& repository.getHoldings().getDateRange().getToDate().getContent() != null
					&& !repository.getHoldings().getDateRange().getToDate().getContent().isEmpty()) {
				descHoldingsDateTo.add(repository.getHoldings().getDateRange().getToDate().getContent());
			} else {
				descHoldingsDateTo.add("");
			}
		}
		this.log.debug("End method: \"loadDescriptionHoldingsDateRange\"");
	}

	/**
	 * Method to load all values of "Description" tab in the part of Holdings in dateSet of institution
	 */
	private void loadDescriptionHoldingsDateSet(){
		this.log.debug("Method start: \"loadDescriptionHoldingsDateSet\"");
		// DateSet of holdings.
		if (repository.getHoldings().getDateSet() != null) {
			if (!repository.getHoldings().getDateSet().getDateOrDateRange().isEmpty()) {
				for (int j = 0; j < repository.getHoldings().getDateSet().getDateOrDateRange().size(); j++) {
					Object dateObject = repository.getHoldings().getDateSet().getDateOrDateRange().get(j);
					// Date inside DateSet.
					if (dateObject != null
							&& dateObject instanceof Date) {
						Date date = (Date) dateObject;
						if (date != null
								&& date.getContent() != null
								&& !date.getContent().isEmpty()) {
							descDateHoldings.add(date.getContent());
						} else {
							descDateHoldings.add("");
						}
					} else if (dateObject != null
							&& dateObject instanceof DateRange) {
						// DateRange inside DateSet.
						DateRange dateRange = (DateRange) dateObject;

						descNumberOfDateRange.add("");
						// From date.
						if (dateRange.getFromDate() != null
								&& dateRange.getFromDate().getContent() != null
								&& !dateRange.getFromDate().getContent().isEmpty()) {
							descHoldingsDateFrom.add(dateRange.getFromDate().getContent());
						} else {
							descHoldingsDateFrom.add("");
						}
						// To date.
						if (dateRange.getToDate() != null
								&& dateRange.getToDate().getContent() != null
								&& !dateRange.getToDate().getContent().isEmpty()) {
							descHoldingsDateTo.add(dateRange.getToDate().getContent());
						} else {
							descHoldingsDateTo.add("");
						}
					}
				}
			} else {
				descDateHoldings.add("");
				descNumberOfDateRange.add("");
				descHoldingsDateFrom.add("");
				descHoldingsDateTo.add("");
			}
		}		 
		this.log.debug("End method: \"loadDescriptionHoldingsDateSet\"");	
	}

	/**
	 * Method to load all values of "Description" tab in the part of Holdings in Extent of institution
	 */
	private void loadDescriptionHoldingsExtent(){
		this.log.debug("Method start: \"loadDescriptionHoldingsExtent\"");
		// Extent.
		if (repository.getHoldings().getExtent() != null
				&& repository.getHoldings().getExtent().getNum() != null
				&& repository.getHoldings().getExtent().getNum().getContent() != null
				&& !repository.getHoldings().getExtent().getNum().getContent().isEmpty()) {
			descHoldingsExtent.add(repository.getHoldings().getExtent().getNum().getContent());

			if (repository.getHoldings().getExtent().getNum().getUnit() != null
					&& !repository.getHoldings().getExtent().getNum().getUnit().isEmpty()) {
				descHoldingsExtentUnit.add(repository.getHoldings().getExtent().getNum().getUnit());
			} else {
				descHoldingsExtentUnit.add("");
			}
		} else {
			descHoldingsExtent.add("");
			descHoldingsExtentUnit.add("");
		}	
		this.log.debug("End method: \"loadDescriptionHoldingsExtent\"");
	}
}
