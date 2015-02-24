package eu.apenet.dashboard.manual.eag.utils.createObjectJAXB;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dpt.utils.eag2012.Date;
import eu.apenet.dpt.utils.eag2012.DateRange;
import eu.apenet.dpt.utils.eag2012.DateSet;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.FromDate;
import eu.apenet.dpt.utils.eag2012.Holdings;
import eu.apenet.dpt.utils.eag2012.Repositorfound;
import eu.apenet.dpt.utils.eag2012.Repositorsup;
import eu.apenet.dpt.utils.eag2012.Repository;
import eu.apenet.dpt.utils.eag2012.ToDate;

/**
 * Class to get all dates object JAXB
 */
public class GetAllDatesObjectJAXB extends AbstractObjectJAXB {
	/**
	 * EAG2012 internal object.
	 */
	protected Eag2012 eag2012;	
	/**
	 * EAG2012 JAXB object.
	 */
	protected Eag eag;
	/**
	 * repository {@link Repository} object.
	 */
	private Repository repository;	
	private final Logger log = Logger.getLogger(getClass());

	/**
	 * Method to receives object Eag2012 and Eag
	 * @param eag2012 {@link Eag2012} the Eag2012
	 * @param eag {@link Eag} the Eag
	 * @return {@link Eag}  the Eag
	 */
	public Eag GetAllDatesJAXB(Eag2012 eag2012, Eag eag) {
		// TODO Auto-generated method stub
		this.eag2012=eag2012;
		this.eag=eag;		
		main();
		return this.eag;
	}

	/**
	 * Method main
	 */
	private void main(){
		this.log.debug("Method start: \"Main of class GetAllDatesObjectJAXB\"");
		if (this.eag2012.getDateStandardDate() != null) {
			createDate();
		}

		if (this.eag2012.getFromDateStandardDate() != null
				&& this.eag2012.getToDateStandardDate() != null) {
			createDateRange();
			
		}
		this.log.debug("End method: \"Main of class GetAllDatesObjectJAXB\"");
	}

	/**
	 * Method to fill date element
	 */
	private void createDate(){
		
		this.log.debug("Method start: \"createResourceRelation\"");
		for (int i = 0; i < this.eag2012.getDateStandardDate().size(); i++) {
			// Repository.
			 repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
			Map<String, Map<String, Map<String, List<List<String>>>>> tabsValueMap = this.eag2012.getDateStandardDate().get(i);
			Iterator<String> tabsValueIt = tabsValueMap.keySet().iterator();
			while (tabsValueIt.hasNext()) {
				String tabValueKey = tabsValueIt.next();
				Map<String, Map<String, List<List<String>>>> sectionsValueMap = tabsValueMap.get(tabValueKey);
				Iterator<String> sectionsValueIt = sectionsValueMap.keySet().iterator();
				if (Eag2012.TAB_DESCRIPTION.equalsIgnoreCase(tabValueKey)) {
					while (sectionsValueIt.hasNext()) {
						String sectionValueKey = sectionsValueIt.next();
						Map<String, List<List<String>>> subsectionsValueMap = sectionsValueMap.get(sectionValueKey);
						Iterator<String> subsectionsValueIt = subsectionsValueMap.keySet().iterator();
						if (Eag2012.REPOSITORHIST.equalsIgnoreCase(sectionValueKey)) {
						createRepositorhistAndHoldings(true,subsectionsValueIt, subsectionsValueMap);							
						} else if (Eag2012.HOLDINGS.equalsIgnoreCase(sectionValueKey)) {
							while (subsectionsValueIt.hasNext()) {
								createRepositorhistAndHoldings(false,subsectionsValueIt, subsectionsValueMap);								
							}
						}
					}
				}
			}
		}
		this.log.debug("End method: \"createResourceRelation\"");
	}

	/**
	 * Method to fill dateRange element
	 */
	private void createDateRange(){
		this.log.debug("Method start: \"createResourceRelation\"");
		for (int i = 0; i < this.eag2012.getFromDateStandardDate().size(); i++) {
			// Repository.
			repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
			Map<String, Map<String, Map<String, List<List<String>>>>> tabsValueFromMap = this.eag2012.getFromDateStandardDate().get(i);
			Map<String, Map<String, Map<String, List<List<String>>>>> tabsValueToMap = this.eag2012.getToDateStandardDate().get(i);
			Iterator<String> tabsValueFromIt = tabsValueFromMap.keySet().iterator();
			Iterator<String> tabsValueToIt = tabsValueToMap.keySet().iterator();
			while (tabsValueFromIt.hasNext()) {
				String tabValueFromKey = tabsValueFromIt.next();
				String tabValueToKey = tabsValueToIt.next();
				Map<String, Map<String, List<List<String>>>> sectionsValueFromMap = tabsValueFromMap.get(tabValueFromKey);
				Map<String, Map<String, List<List<String>>>> sectionsValueToMap = tabsValueToMap.get(tabValueToKey);
				Iterator<String> sectionsValueFromIt = sectionsValueFromMap.keySet().iterator();
				Iterator<String> sectionsValueToIt = sectionsValueToMap.keySet().iterator();
				if (Eag2012.TAB_DESCRIPTION.equalsIgnoreCase(tabValueFromKey)
						&& Eag2012.TAB_DESCRIPTION.equalsIgnoreCase(tabValueToKey)) {
					while (sectionsValueFromIt.hasNext()) {
						String sectionValueFromKey = sectionsValueFromIt.next();
						String sectionValueToKey = sectionsValueToIt.next();
						Map<String, List<List<String>>> subsectionsValueFromMap = sectionsValueFromMap.get(sectionValueFromKey);
						Map<String, List<List<String>>> subsectionsValueToMap = sectionsValueToMap.get(sectionValueToKey);
						Iterator<String> subsectionsValueFromIt = subsectionsValueFromMap.keySet().iterator();
						Iterator<String> subsectionsValueToIt = subsectionsValueToMap.keySet().iterator();
						while (subsectionsValueFromIt.hasNext()) {
							String subsectionValueFromKey = subsectionsValueFromIt.next();
							String subsectionValueToKey = subsectionsValueToIt.next();
							List<List<String>> valuesFromList = subsectionsValueFromMap.get(subsectionValueFromKey);
							List<List<String>> valuesToList = subsectionsValueToMap.get(subsectionValueToKey);
								if (Eag2012.HOLDINGS.equalsIgnoreCase(sectionValueFromKey)
										&& Eag2012.HOLDINGS.equalsIgnoreCase(sectionValueToKey)) {
								for (int j = 0; j < valuesFromList.size(); j++) {
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
											if (Eag2012.HOLDING_SUBSECTION.equalsIgnoreCase(subsectionValueFromKey)
													&& Eag2012.HOLDING_SUBSECTION.equalsIgnoreCase(subsectionValueToKey)) {
												createRepositoryAllDates(dateRange, null,false);
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
		this.log.debug("End method: \"createResourceRelation\"");	
	}

	/**
	 * Method to fill repositorfound/date element and repositorsup/date element
	 * @param isRepositorhist {@link boolean} if is repositorhist
	 * @param subsectionsValueIt {@link Iterator<String>} the subsectionsValueIt
	 * @param subsectionsValueMap {@link Map<String, List<List<String>>>} the subsectionsValueMap
	 */
	private  void createRepositorhistAndHoldings(boolean isRepositorhist,Iterator<String> subsectionsValueIt,Map<String, List<List<String>>> subsectionsValueMap){
		// eag/archguide/desc/repositories/repository/repositorfuond/date and  // eag/archguide/desc/repositories/repository/repositorsup/date
		this.log.debug("Method start: \"createRepositorhistAndHoldings\"");
		while (subsectionsValueIt.hasNext()) {//
			String subsectionValueKey = subsectionsValueIt.next();//
			List<List<String>> valuesList = subsectionsValueMap.get(subsectionValueKey);//
			for (int j = 0; j < valuesList.size(); j++) {
				List<String> valueList = valuesList.get(j);
				for (int k = 0; k < valueList.size(); k++) {
					if (valueList.get(k) != null && !valueList.get(k).isEmpty()) {
						Date date = new Date();
						String valueStandardDate = parseDate(valueList.get(k));
						if (valueStandardDate != null && !valueStandardDate.isEmpty()){
							date.setStandardDate(valueStandardDate);
						}
						date.setContent(valueList.get(k));
						if(!isRepositorhist){
							if (Eag2012.HOLDING_SUBSECTION.equalsIgnoreCase(subsectionValueKey)) {
								createRepositoryAllDates(null,date,true);
							}
						}else if(isRepositorhist){
							if (Eag2012.REPOSITOR_FOUND.equalsIgnoreCase(subsectionValueKey)) {
								// eag/archguide/desc/repositories/repository/repositorfuond/date
									if (repository.getRepositorfound() == null) {
										repository.setRepositorfound(new Repositorfound());
									}
									repository.getRepositorfound().setDate(date);
								} else if (Eag2012.REPOSITOR_SUP.equalsIgnoreCase(subsectionValueKey)) {
									// eag/archguide/desc/repositories/repository/repositorsup/date
									if (repository.getRepositorsup() == null) {
										repository.setRepositorsup(new Repositorsup());
									}
									repository.getRepositorsup().setDate(date);
								}
						}						
					}
				}
			}
		}		
		this.log.debug("End method: \"createRepositorhistAndHoldings\"");
	}

	/**
	 * Method to fill "Desc" element date and date range
	 * @param dateRange {@link DateRange} the dateRange
	 * @param date {@link Date} the date
	 * @param isData {@link boolean} if is data
	 */
	private void createRepositoryAllDates(DateRange dateRange,Date date,boolean isData){
		this.log.debug("Method start: \"createRepositoryAllDates\"");
			if (repository.getHoldings() == null) {
				repository.setHoldings(new Holdings());
			}	
			if (repository.getHoldings().getDate() == null
					&& repository.getHoldings().getDateRange() == null
					&& repository.getHoldings().getDateSet() == null) {
				if(isData){
					repository.getHoldings().setDate(date);
				}else{
					repository.getHoldings().setDateRange(dateRange);
				}				
			} else {
				DateSet dateSet = null;
				if (repository.getHoldings().getDateSet() == null) {
					dateSet = new DateSet();
				} else {
					dateSet = repository.getHoldings().getDateSet();
				}
				// Recover previous single element.
				if (repository.getHoldings().getDate() != null) {
					Date previousDate = repository.getHoldings().getDate();
					repository.getHoldings().setDate(null);
					dateSet.getDateOrDateRange().add(previousDate);
				}
				if (repository.getHoldings().getDateRange() != null) {
					DateRange previousDateRange = repository.getHoldings().getDateRange();
					repository.getHoldings().setDateRange(null);
					dateSet.getDateOrDateRange().add(previousDateRange);
				}
				if(isData){
					dateSet.getDateOrDateRange().add(date);
				}else{
					dateSet.getDateOrDateRange().add(dateRange);
				}
				repository.getHoldings().setDateSet(dateSet);
			}
			this.log.debug("End method: \"createRepositoryAllDates\"");
	}

}
