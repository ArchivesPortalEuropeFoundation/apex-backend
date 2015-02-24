package eu.apenet.dashboard.manual.eag.utils.createObjectJAXB;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dpt.utils.eag2012.Autform;
import eu.apenet.dpt.utils.eag2012.Date;
import eu.apenet.dpt.utils.eag2012.DateRange;
import eu.apenet.dpt.utils.eag2012.DateSet;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.FromDate;
import eu.apenet.dpt.utils.eag2012.Nonpreform;
import eu.apenet.dpt.utils.eag2012.OtherRepositorId;
import eu.apenet.dpt.utils.eag2012.Parform;
import eu.apenet.dpt.utils.eag2012.Repositorid;
import eu.apenet.dpt.utils.eag2012.RepositoryType;
import eu.apenet.dpt.utils.eag2012.ToDate;
import eu.apenet.dpt.utils.eag2012.UseDates;

/**
 * Class for fill "Identity" element of object JAXB
 */
public class FillIdentityObjectJAXB extends AbstractObjectJAXB implements ObjectJAXB{
	/**
	 * EAG2012 {@link Eag2012} internal object.
	 */
	protected Eag2012 eag2012;	
	/**
	 * Eag {@link Eag} JAXB object.
	 */
	protected Eag eag;
	boolean exists ;
	private SimpleDateFormat df;
	private final Logger log = Logger.getLogger(getClass());

	@Override
	public Eag ObjectJAXB(Eag2012 eag2012, Eag eag) {
		// TODO Auto-generated method stub
		this.eag2012=eag2012;
		this.eag=eag;
		exists=true;
		df = new SimpleDateFormat();
		main();
		return this.eag;
	}

	/**
	 * Method main with method for fill "identity" element of object JAXB
	 */
	private void main(){
		this.log.debug("Method start: \"Main of class FillIdentitycObjectJAXB\"");
		creatCountryCode();
		createAutform();
		createParform();
		createNonpreform();		
		createRepositoryType();		
		this.log.debug("End method: \"createAccessibility\"");
	}

	/**
	 * Method to fill CountryCode element
	 */
	private void creatCountryCode(){
		this.log.debug("Method start: \"createAccessibility\"");
		// eag/archguide/identity/repositorid/countrycode
		if (this.eag.getArchguide().getIdentity().getRepositorid() == null) {
			this.eag.getArchguide().getIdentity().setRepositorid(new Repositorid());
		}
		this.eag.getArchguide().getIdentity().getRepositorid().setCountrycode(this.eag2012.getRepositoridCountrycode());
		// eag/archguide/identity/repositorid/repositorycode
		this.eag.getArchguide().getIdentity().getRepositorid().setRepositorycode(this.eag2012.getRepositoridRepositorycode());
		// eag/archguide/identity/otherRepositorid
		if (this.eag2012.getOtherRepositorId() != null && !this.eag2012.getOtherRepositorId().isEmpty()) {
			if (this.eag.getArchguide().getIdentity().getOtherRepositorId() == null) {
				this.eag.getArchguide().getIdentity().setOtherRepositorId(new OtherRepositorId());
			}
			this.eag.getArchguide().getIdentity().getOtherRepositorId().setContent(this.eag2012.getOtherRepositorId());
		}
		this.log.debug("End method: \"createAccessibilityQuestion\"");
	}

	/**
	 * Method to fill CountryCode element
	 */
	private void createAutform(){
		this.log.debug("Method start: \"createAccessibility\"");
		// eag/archguide/identity/autform
		if (this.eag2012.getAutformValue() != null) {
			for (int i = 0; i < this.eag2012.getAutformValue().size(); i++) {
				Autform autform = new Autform();
				// eag/archguide/identity/autform
				autform.setContent(this.eag2012.getAutformValue().get(i));
				// eag/archguide/identity/autform/lang
				if (!Eag2012.OPTION_NONE.equalsIgnoreCase(this.eag2012.getAutformLang().get(i))) {
					autform.setLang(this.eag2012.getAutformLang().get(i));
				}	
				this.eag.getArchguide().getIdentity().getAutform().add(autform);
			}
		}
		this.log.debug("End method: \"createAccessibilityQuestion\"");
	}

	/**
	 * Method to fill CountryCode element
	 */
	private void createParform(){
		this.log.debug("Method start: \"createAccessibility\"");
		// eag/archguide/identity/parform
		if (this.eag2012.getParformValue() != null
				&& this.eag2012.getParformValue().size() > 0) {
			for (int i = 0; i < this.eag2012.getParformValue().size(); i++) {
				if (this.eag2012.getParformValue().get(i) != null
						&& !this.eag2012.getParformValue().get(i).isEmpty()) {
					Parform parform = new Parform();
					// eag/archguide/identity/parform
					parform.setContent(this.eag2012.getParformValue().get(i));
					// eag/archguide/identity/parform/lang
					if (!Eag2012.OPTION_NONE.equalsIgnoreCase(this.eag2012.getParformLang().get(i))) {
						parform.setLang(this.eag2012.getParformLang().get(i));
					}		
					this.eag.getArchguide().getIdentity().getParform().add(parform);
				}
			}
		}
		this.log.debug("End method: \"createAccessibilityQuestion\"");
	}

	/**
	 * Method to fill Nonpreform element
	 */
	private void createNonpreform(){
		this.log.debug("Method start: \"createAccessibility\"");
		// eag/archguide/identity/nonpreform
		if (this.eag2012.getNonpreformValue() != null){
			for (int i = 0; i < this.eag2012.getNonpreformValue().size(); i++) {
				if (this.eag2012.getNonpreformValue().get(i) != null
						&& !this.eag2012.getNonpreformValue().get(i).isEmpty()) {
					Nonpreform nonpreform = new Nonpreform();
					if (!Eag2012.OPTION_NONE.equalsIgnoreCase(this.eag2012.getNonpreformLang().get(i))) {
						nonpreform.setLang(this.eag2012.getNonpreformLang().get(i));
					}
					nonpreform.getContent().add(this.eag2012.getNonpreformValue().get(i));	
					// eag/archguide/identity/nonpreform/dates
					getNompreformDates(nonpreform, i);	
					this.eag.getArchguide().getIdentity().getNonpreform().add(nonpreform);
				}
			}
		}
		this.log.debug("End method: \"createAccessibilityQuestion\"");
	}

	/**
	 * Method to fill RepositoryType element
	 */
	private void createRepositoryType(){
		this.log.debug("Method start: \"createAccessibility\"");
		// eag/archguide/identity/repositoryType
		if (this.eag2012.getRepositoryTypeValue() != null) {
			for (int i = 0; i < this.eag2012.getRepositoryTypeValue().size(); i++) {
				if (this.eag2012.getRepositoryTypeValue().get(i) != null
						&& !this.eag2012.getRepositoryTypeValue().get(i).isEmpty()) {
					String[] repositoryTypeList = this.eag2012.getRepositoryTypeValue().get(i).split("_");
					for (int j = 0; j < repositoryTypeList.length; j++) {
						String value = repositoryTypeList[j];
						if (Eag2012.OPTION_NATIONAL.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_NATIONAL_TEXT;
						} else if (Eag2012.OPTION_REGIONAL.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_REGIONAL_TEXT;
						} else if (Eag2012.OPTION_COUNTY.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_COUNTY_TEXT;
						} else if (Eag2012.OPTION_MUNICIPAL.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_MUNICIPAL_TEXT;
						} else if (Eag2012.OPTION_SPECIALISED.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_SPECIALISED_TEXT;
						} else if (Eag2012.OPTION_PRIVATE.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_PRIVATE_TEXT;
						} else if (Eag2012.OPTION_CHURCH.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_CHURCH_TEXT;
						} else if (Eag2012.OPTION_BUSINESS.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_BUSINESS_TEXT;
						} else if (Eag2012.OPTION_UNIVERSITY.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_UNIVERSITY_TEXT;
						} else if (Eag2012.OPTION_MEDIA.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_MEDIA_TEXT;
						} else if (Eag2012.OPTION_POLITICAL.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_POLITICAL_TEXT;
						} else if (Eag2012.OPTION_CULTURAL.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_CULTURAL_TEXT;
						}
						RepositoryType repositoryType = new RepositoryType();
						repositoryType.setValue(value);	
						this.eag.getArchguide().getIdentity().getRepositoryType().add(repositoryType);
					}
				}
			}
		}
		this.log.debug("End method: \"createAccessibilityQuestion\"");
	}

	/**
	 * Method to fill all dates for the nonpreform passed.
	 */
	private void getNompreformDates(final Nonpreform nonpreform, final int i) {
		this.log.debug("Method start: \"createAccessibility\"");
		if (this.eag2012.getDateStandardDate() != null) {
			// Main institution date.
			Map<String, Map<String, Map<String, List<List<String>>>>> tabsValueMap = this.eag2012.getDateStandardDate().get(0);
			Iterator<String> tabsValueIt = tabsValueMap.keySet().iterator();
			while (tabsValueIt.hasNext()) {
				String tabValueKey = tabsValueIt.next();
				Map<String, Map<String, List<List<String>>>> sectionsValueMap = tabsValueMap.get(tabValueKey);
				Iterator<String> sectionsValueIt = sectionsValueMap.keySet().iterator();
				if (Eag2012.TAB_IDENTITY.equalsIgnoreCase(tabValueKey)) {
					while (sectionsValueIt.hasNext()) {
						String sectionValueKey = sectionsValueIt.next();
						Map<String, List<List<String>>> subsectionsValueMap = sectionsValueMap.get(sectionValueKey);
						Iterator<String> subsectionsValueIt = subsectionsValueMap.keySet().iterator();
						if (Eag2012.ROOT.equalsIgnoreCase(sectionValueKey)) {
							while (subsectionsValueIt.hasNext()) {
								String subsectionValueKey = subsectionsValueIt.next();
								List<List<String>> valuesList = subsectionsValueMap.get(subsectionValueKey);
								for (int j = 0; j < valuesList.size(); j++) {
									if (j == i) {
										List<String> valueList = valuesList.get(j);
										for (int k = 0; k < valueList.size(); k++) {
											if (valueList.get(k) != null && !valueList.get(k).isEmpty()) {
												Date date = new Date();
												String valueStandardDate = parseDate(valueList.get(k));
												if (valueStandardDate != null && !valueStandardDate.isEmpty()){
													date.setStandardDate(valueStandardDate);
												}
												date.setContent(valueList.get(k));	
												if (Eag2012.ROOT_SUBSECTION.equalsIgnoreCase(subsectionValueKey)) {		
													List<Object> nonpreformObjectList = nonpreform.getContent();
													UseDates useDates = null;
													int index = 0;
													for (int l = 0; l < nonpreformObjectList.size(); l++) {
														if (nonpreformObjectList.get(l) instanceof UseDates) {
															useDates = (UseDates) nonpreformObjectList.get(l);
															index = l;
														}
													}
													boolean emptyUseDates = false;
													if (useDates == null) {
														useDates = new UseDates();
														emptyUseDates = true;
													}	
													if (emptyUseDates) {
														useDates.setDate(date);
													} else {
														DateSet dateSet = null;
														if (useDates.getDateSet() == null) {
															dateSet = new DateSet();
														} else {
															dateSet = useDates.getDateSet();
														}	
														// Recover previous single element.
														if (useDates.getDate() != null) {
															Date previousDate = useDates.getDate();
															useDates.setDate(null);
															dateSet.getDateOrDateRange().add(previousDate);
														}
														if (useDates.getDateRange() != null) {
															DateRange previousDateRange = useDates.getDateRange();
															useDates.setDateRange(null);
															dateSet.getDateOrDateRange().add(previousDateRange);
														}			
														dateSet.getDateOrDateRange().add(date);
														useDates.setDateSet(dateSet);
													}		
													if (index == 0) {
														nonpreform.getContent().add(useDates);
													} else {
														nonpreform.getContent().set(index, useDates);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (this.eag2012.getFromDateStandardDate() != null
				&& this.eag2012.getToDateStandardDate() != null) {
			// Main institution dateRange.
			Map<String, Map<String, Map<String, List<List<String>>>>> tabsValueFromMap = this.eag2012.getFromDateStandardDate().get(0);
			Map<String, Map<String, Map<String, List<List<String>>>>> tabsValueToMap = this.eag2012.getToDateStandardDate().get(0);
			Iterator<String> tabsValueFromIt = tabsValueFromMap.keySet().iterator();
			Iterator<String> tabsValueToIt = tabsValueToMap.keySet().iterator();
			while (tabsValueFromIt.hasNext()) {
				String tabValueFromKey = tabsValueFromIt.next();
				String tabValueToKey = tabsValueToIt.next();
				Map<String, Map<String, List<List<String>>>> sectionsValueFromMap = tabsValueFromMap.get(tabValueFromKey);
				Map<String, Map<String, List<List<String>>>> sectionsValueToMap = tabsValueToMap.get(tabValueToKey);
				Iterator<String> sectionsValueFromIt = sectionsValueFromMap.keySet().iterator();
				Iterator<String> sectionsValueToIt = sectionsValueToMap.keySet().iterator();
				if (Eag2012.TAB_IDENTITY.equalsIgnoreCase(tabValueFromKey)
						&& Eag2012.TAB_IDENTITY.equalsIgnoreCase(tabValueToKey)) {
					while (sectionsValueFromIt.hasNext()) {
						String sectionValueFromKey = sectionsValueFromIt.next();
						String sectionValueToKey = sectionsValueToIt.next();
						Map<String, List<List<String>>> subsectionsValueFromMap = sectionsValueFromMap.get(sectionValueFromKey);
						Map<String, List<List<String>>> subsectionsValueToMap = sectionsValueToMap.get(sectionValueToKey);
						Iterator<String> subsectionsValueFromIt = subsectionsValueFromMap.keySet().iterator();
						Iterator<String> subsectionsValueToIt = subsectionsValueToMap.keySet().iterator();
						while (subsectionsValueFromIt.hasNext()) {
							if (Eag2012.ROOT.equalsIgnoreCase(sectionValueFromKey)
								&& Eag2012.ROOT.equalsIgnoreCase(sectionValueToKey)) {
								String subsectionValueFromKey = subsectionsValueFromIt.next();
								String subsectionValueToKey = subsectionsValueToIt.next();
								List<List<String>> valuesFromList = subsectionsValueFromMap.get(subsectionValueFromKey);
								List<List<String>> valuesToList = subsectionsValueToMap.get(subsectionValueToKey);
								for (int j = 0; j < valuesFromList.size(); j++) {
									if (j == i) {
										List<String> valueFromList = valuesFromList.get(j);
										List<String> valueToList = valuesToList.get(j);
										for (int k = 0; k < valueFromList.size(); k++) {
											if ((valueFromList.get(k) != null && !valueFromList.get(k).isEmpty())
													|| (valueToList.get(k) != null && !valueToList.get(k).isEmpty())){
												FromDate fromDate = new FromDate();
												String valueStandardDate = parseDate(valueFromList.get(k));
												if (valueStandardDate != null && !valueStandardDate.isEmpty()){
													fromDate.setStandardDate(valueStandardDate);
												}
												fromDate.setContent(valueFromList.get(k));		
												ToDate toDate = new ToDate();
												valueStandardDate = parseDate(valueToList.get(k));
												if (valueStandardDate != null && !valueStandardDate.isEmpty()){
													toDate.setStandardDate(valueStandardDate);
												}
												toDate.setContent(valueToList.get(k));		
												DateRange dateRange = new DateRange();
												dateRange.setFromDate(fromDate);
												dateRange.setToDate(toDate);	
												if (Eag2012.ROOT_SUBSECTION.equalsIgnoreCase(subsectionValueFromKey)
														&& Eag2012.ROOT_SUBSECTION.equalsIgnoreCase(subsectionValueToKey)) {
													List<Object> nonpreformObjectList = nonpreform.getContent();
													UseDates useDates = null;
													int index = 0;
													for (int l = 0; l < nonpreformObjectList.size(); l++) {
														if (nonpreformObjectList.get(l) instanceof UseDates) {
															useDates = (UseDates) nonpreformObjectList.get(l);
															index = l;
														}
													}
													boolean emptyUseDates = false;
													if (useDates == null) {
														useDates = new UseDates();
														emptyUseDates = true;
													}	
													if (emptyUseDates) {
														useDates.setDateRange(dateRange);
													} else {
														DateSet dateSet = null;
														if (useDates.getDateSet() == null) {
															dateSet = new DateSet();
														} else {
															dateSet = useDates.getDateSet();
														}	
														// Recover previous single element.
														if (useDates.getDate() != null) {
															Date previousDate = useDates.getDate();
															useDates.setDate(null);
															dateSet.getDateOrDateRange().add(previousDate);
														}
														if (useDates.getDateRange() != null) {
															DateRange previousDateRange = useDates.getDateRange();
															useDates.setDateRange(null);
															dateSet.getDateOrDateRange().add(previousDateRange);
														}	
														dateSet.getDateOrDateRange().add(dateRange);
														useDates.setDateSet(dateSet);
													}	
													if (index == 0) {
														nonpreform.getContent().add(useDates);
													} else {
														nonpreform.getContent().set(index, useDates);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		this.log.debug("End method: \"createAccessibilityQuestion\"");
	}
}
