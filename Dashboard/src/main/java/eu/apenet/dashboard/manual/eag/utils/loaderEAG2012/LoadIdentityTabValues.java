package eu.apenet.dashboard.manual.eag.utils.loaderEAG2012;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dashboard.manual.eag.utils.EAG2012Loader;
import eu.apenet.dpt.utils.eag2012.Date;
import eu.apenet.dpt.utils.eag2012.DateRange;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.Nonpreform;
import eu.apenet.dpt.utils.eag2012.UseDates;

/**
 *Class for load identity tab values from the XML
 */
public class LoadIdentityTabValues implements  LoaderEAG2012{
	private Nonpreform nonpreform;
	private UseDates useDates;
	private final Logger log = Logger.getLogger(getClass());
	
	/**
	 * @param nonpreform {@link String} the nonpreform to add
	 */
	public void addNonpreform(String nonpreform) {
		this.loader.getNonpreform().add(nonpreform);
	}
	/**
	 * @param nonpreformLang {@link String} the nonpreformLang to add
	 */
	public void addNonpreformLang(String nonpreformLang) {
		this.loader.getNonpreformLang().add(nonpreformLang);
	}
	/**
	 * @param nonpreformDate {@link List<String>} the nonpreformDate to add
	 */
	public void addNonpreformDate(List<String> nonpreformDate) {
		this.loader.getNonpreformDate().add(nonpreformDate);
	}
	/**
	 * @param nonpreformDateFrom {@link List<String>} the nonpreformDateFrom to add
	 */
	public void addNonpreformDateFrom(List<String> nonpreformDateFrom) {
		this.loader.getNonpreformDateFrom().add(nonpreformDateFrom);
	}
	/**
	 * @param nonpreformDateTo {@link List<String>} the nonpreformDateTo to add
	 */
	public void addNonpreformDateTo(List<String> nonpreformDateTo) {
		this.loader.getNonpreformDateTo().add(nonpreformDateTo);
	}
	/**
	 * @param repositoryType {@link List<String>} the repositoryType to add
	 */
	public void addRepositoryType(String repositoryType) {
		this.loader.getRepositoryType().add(repositoryType);
	}
	private EAG2012Loader loader;
	/**
	 * EAG {@link Eag} JAXB object.
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
	 * Method to load all values of "Identity" tab of institution
	 */
	private void main(){
		this.log.debug("Method start: \"Main of class LoadIdentityTabValues\"");
		//Formerly used names of the institution.
		if (this.eag.getArchguide() != null && this.eag.getArchguide().getIdentity() != null
				&& !this.eag.getArchguide().getIdentity().getNonpreform().isEmpty()) {
			for (int i = 0; i < this.eag.getArchguide().getIdentity().getNonpreform().size(); i++) {
				 nonpreform = this.eag.getArchguide().getIdentity().getNonpreform().get(i);
				if (nonpreform != null && !nonpreform.getContent().isEmpty()) {
					// Lang.
					if (nonpreform.getLang() != null && !nonpreform.getLang().isEmpty()) {
						this.addNonpreformLang(nonpreform.getLang());
					} else {
						this.addNonpreformLang(Eag2012.OPTION_NONE);
					}
					loadIdentityTabValueAndDates();					
				} 
				// Check if list of "Date" and "DateRange" has the same size.
				if (this.loader.getNonpreformDate().size() > this.loader.getNonpreformDateFrom().size()) {
					this.loader.getNonpreformDateFrom().add(new ArrayList<String>());
					this.loader.getNonpreformDateTo().add(new ArrayList<String>());
				} else if (this.loader.getNonpreformDate().size() < this.loader.getNonpreformDateFrom().size()) {
					this.loader.getNonpreformDate().add(new ArrayList<String>());
				}
			}
		}
		loadIdentityTabTypeOfInstitution();
		this.log.debug("End method: \"Main of class LoadIdentityTabValues\"");
	}

	/**
	 * Method to load all values of "Identity" tab in the part of Value and dates of institution
	 */
	private void loadIdentityTabValueAndDates(){
		this.log.debug("Method start: \"loadIdentityTabValueAndDates\"");
		// Value and dates.
		for (int j = 0; j < nonpreform.getContent().size(); j++) {
			Object object = nonpreform.getContent().get(j);
			// Value.
			if (object != null && object instanceof String) {
				if (!((String) object).startsWith("\n")) {
					this.addNonpreform((String) object);
				}
			} else if (object != null && object instanceof UseDates) {
				// Dates.
				 useDates = (UseDates) object;
				if (useDates != null) {
					if (useDates.getDate() != null && useDates.getDate().getContent() != null
							&& !useDates.getDate().getContent().isEmpty()) {
						List<String> dateList = new ArrayList<String>();
						dateList.add(useDates.getDate().getContent());
						this.addNonpreformDate(dateList);
					}
					loadIdentityTabDates();				
					loadIdentityTabDates2();
				}
			}
		}
		this.log.debug("End method: \"loadIdentityTabValueAndDates\"");
	}

	/**
	 * Method to load all values of "Identity" tab in the part of dates of institution
	 */
	private void loadIdentityTabDates(){
		this.log.debug("Method start: \"loadIdentityTabDates\"");
		if (useDates.getDateRange() != null
		&& ((useDates.getDateRange().getFromDate() != null
			&& useDates.getDateRange().getFromDate().getContent() != null
			&& !useDates.getDateRange().getFromDate().getContent().isEmpty())
		|| (useDates.getDateRange().getToDate() != null
			&& useDates.getDateRange().getToDate().getContent() != null
			&& !useDates.getDateRange().getToDate().getContent().isEmpty()))) {
			List<String> dateFromList = new ArrayList<String>();
			if (useDates.getDateRange().getFromDate() != null
					&& useDates.getDateRange().getFromDate() != null
					&& useDates.getDateRange().getFromDate().getContent() != null
					&& !useDates.getDateRange().getFromDate().getContent().isEmpty()) {
				dateFromList.add(useDates.getDateRange().getFromDate().getContent());
			} else {
				dateFromList.add("");
			}
			List<String> dateToList = new ArrayList<String>();
			if (useDates.getDateRange().getToDate() != null
					&& useDates.getDateRange().getToDate() != null
					&& useDates.getDateRange().getToDate().getContent() != null
					&& !useDates.getDateRange().getToDate().getContent().isEmpty()) {
				dateToList.add(useDates.getDateRange().getToDate().getContent());
			} else {
				dateToList.add("");
			}
			
			this.addNonpreformDateFrom(dateFromList);
			this.addNonpreformDateTo(dateToList);
		}
		this.log.debug("End method: \"loadIdentityTabDates\"");
	}

	/**
	 * Method to load all values of "Identity" tab in the part of dates of institution
	 */
	private void loadIdentityTabDates2(){
		this.log.debug("Method start: \"loadIdentityTabDates2\"");
		if (useDates.getDateSet() != null && !useDates.getDateSet().getDateOrDateRange().isEmpty()) {
			List<String> dateList = new ArrayList<String>();
			List<String> dateFromList = new ArrayList<String>();
			List<String> dateToList = new ArrayList<String>();
			for (int k = 0; k < useDates.getDateSet().getDateOrDateRange().size(); k ++) {
				Object dateObject = useDates.getDateSet().getDateOrDateRange().get(k);
				if (dateObject instanceof Date) {
					Date date = (Date) dateObject;
					if (date != null && date.getContent() != null
						&& !date.getContent().isEmpty()) {
						dateList.add(date.getContent());
					}
				}
				if (dateObject instanceof DateRange) {
					DateRange dateRange = (DateRange) dateObject;
					if (dateRange != null
							&& ((dateRange.getFromDate() != null
								&& dateRange.getFromDate().getContent() != null
								&& !dateRange.getFromDate().getContent().isEmpty())
							|| (dateRange.getToDate() != null
								&& dateRange.getToDate().getContent() != null
								&& !dateRange.getToDate().getContent().isEmpty()))) {
						if (dateRange.getFromDate() != null
								&& dateRange.getFromDate() != null
								&& dateRange.getFromDate().getContent() != null
								&& !dateRange.getFromDate().getContent().isEmpty()) {
							dateFromList.add(dateRange.getFromDate().getContent());
						} else {
							dateFromList.add("");
						}
						if (dateRange.getToDate() != null
								&& dateRange.getToDate() != null
								&& dateRange.getToDate().getContent() != null
								&& !dateRange.getToDate().getContent().isEmpty()) {
							dateToList.add(dateRange.getToDate().getContent());
						} else {
							dateToList.add("");
						}
					}
				}
			}
			this.addNonpreformDate(dateList);
			this.addNonpreformDateFrom(dateFromList);
			this.addNonpreformDateTo(dateToList);
		}
		this.log.debug("End method: \"loadIdentityTabDates2\"");
	}

	/**
	 * Method to load all values of "Identity" tab in the part of type of institution of institution
	 */
	private void loadIdentityTabTypeOfInstitution(){
		this.log.debug("Method start: \"loadIdentityTabTypeOfInstitution\"");
		// Select type of institution.
		if (this.eag.getArchguide() != null && this.eag.getArchguide().getIdentity() != null
				&& !this.eag.getArchguide().getIdentity().getRepositoryType().isEmpty()) {
			for (int i = 0; i < this.eag.getArchguide().getIdentity().getRepositoryType().size(); i++) {
				if (this.eag.getArchguide().getIdentity().getRepositoryType().get(i).getValue() != null
						&& !this.eag.getArchguide().getIdentity().getRepositoryType().get(i).getValue().isEmpty()) {
					String value = this.eag.getArchguide().getIdentity().getRepositoryType().get(i).getValue();
					if (Eag2012.OPTION_NATIONAL_TEXT.equalsIgnoreCase(value)) {
						value = Eag2012.OPTION_NATIONAL;
					} else if (Eag2012.OPTION_REGIONAL_TEXT.equalsIgnoreCase(value)) {
						value = Eag2012.OPTION_REGIONAL;
					} else if (Eag2012.OPTION_COUNTY_TEXT.equalsIgnoreCase(value)) {
						value = Eag2012.OPTION_COUNTY;
					} else if (Eag2012.OPTION_MUNICIPAL_TEXT.equalsIgnoreCase(value)) {
						value = Eag2012.OPTION_MUNICIPAL;
					} else if (Eag2012.OPTION_SPECIALISED_TEXT.equalsIgnoreCase(value)) {
						value = Eag2012.OPTION_SPECIALISED;
					} else if (Eag2012.OPTION_PRIVATE_TEXT.equalsIgnoreCase(value)) {
						value = Eag2012.OPTION_PRIVATE;
					} else if (Eag2012.OPTION_CHURCH_TEXT.equalsIgnoreCase(value)) {
						value = Eag2012.OPTION_CHURCH;
					} else if (Eag2012.OPTION_BUSINESS_TEXT.equalsIgnoreCase(value)) {
						value = Eag2012.OPTION_BUSINESS;
					} else if (Eag2012.OPTION_UNIVERSITY_TEXT.equalsIgnoreCase(value)) {
						value = Eag2012.OPTION_UNIVERSITY;
					} else if (Eag2012.OPTION_MEDIA_TEXT.equalsIgnoreCase(value)) {
						value = Eag2012.OPTION_MEDIA;
					} else if (Eag2012.OPTION_POLITICAL_TEXT.equalsIgnoreCase(value)) {
						value = Eag2012.OPTION_POLITICAL;
					} else if (Eag2012.OPTION_CULTURAL_TEXT.equalsIgnoreCase(value)) {
						value = Eag2012.OPTION_CULTURAL;
					}

					this.addRepositoryType(value);
				}
			}
		}
		this.log.debug("End method: \"loadIdentityTabTypeOfInstitution\"");
	}	
}
