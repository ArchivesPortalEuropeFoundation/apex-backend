package eu.apenet.dashboard.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EadSearchOptions;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;

public class StatisticsAction extends AbstractAction {
	private static final String INSTITUTION_HEADER = "Country;Institution;Identifier of the institution;EAG;Has searchable items;#Published FA;#Published HG;#Published SG;#Published Descriptive units;#Published DAOs;#EDM(providedCHOs);#Delivered to Europeana(providedCHOs)";
	private static final String COUNTRY_HEADER = "Country;#Institutions;#Institutions EAG;#Institutions with searchable items;#Published FA;#Published HG;#Published SG;#Published Descriptive units;#Published DAOs;#EDM(providedCHOs);#Delivered to Europeana(providedCHOs)";
	/**
	 * 
	 */
	private static final long serialVersionUID = -7140890005810933129L;

	public String downloadCountriesStatistics() throws IOException {
		if (this.getSecurityContext().isAdmin()) {
			String name = APEnetUtilities.convertToFilename("countries-statistics.csv");
			CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
			ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
			PrintWriter printWriter = ContentUtils.getWriterToDownload(this.getServletRequest(), getServletResponse(),
					name, "text/csv");
			printWriter.println(COUNTRY_HEADER);
			List<Country> countries = countryDAO.findAll();
			for (Country country : countries) {
				CountryStatistics countryStatistics = new CountryStatistics(country.getCname());
				List<ArchivalInstitution> archivalInstitutions = archivalInstitutionDAO
						.getArchivalInstitutionsByCountryId(country.getId());
				for (ArchivalInstitution archivalInstitution : archivalInstitutions) {
					countryStatistics.addInstitutionStatistics(getInstitutionStatistics(country, archivalInstitution));
				}
				printWriter.println(countryStatistics);
			}

			printWriter.flush();
			printWriter.close();
		}
		return null;
	}

	public String downloadInstitutionsStatistics() throws IOException {
		CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
		List<Country> countries = new ArrayList<Country>();
		if (this.getSecurityContext().isAdmin()) {
			countries = countryDAO.findAll();
		} else if (this.getSecurityContext().isCountryManager()) {
			Integer countryId = this.getSecurityContext().getCountryId();
			countries.add(countryDAO.findById(countryId));
		}
		writeCSV(countries);
		return null;
	}

	private void writeCSV(List<Country> countries) throws IOException {
		ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
		String name = "countries";
		if (countries.size() == 1) {
			name = countries.get(0).getCname();
		}
		name = APEnetUtilities.convertToFilename(name + "-institutions-statistics.csv");
		PrintWriter printWriter = ContentUtils.getWriterToDownload(this.getServletRequest(), getServletResponse(),
				name, "text/csv");
		printWriter.println(INSTITUTION_HEADER);
		for (Country country : countries) {
			List<ArchivalInstitution> archivalInstitutions = archivalInstitutionDAO
					.getArchivalInstitutionsByCountryId(country.getId());
			for (ArchivalInstitution archivalInstitution : archivalInstitutions) {
				InstitutionStatistics institutionStatistics = getInstitutionStatistics(country, archivalInstitution);
				printWriter.println(institutionStatistics);
			}
		}
		printWriter.flush();
		printWriter.close();
	}

	private InstitutionStatistics getInstitutionStatistics(Country country, ArchivalInstitution archivalInstitution) {
		InstitutionStatistics institutionStatistics = new InstitutionStatistics(country.getCname(),
				archivalInstitution.getAiname());
		institutionStatistics.hasEag = StringUtils.isNotBlank(archivalInstitution.getEagPath());
		if (institutionStatistics.hasEag) {
			institutionStatistics.repositoryCode = archivalInstitution.getRepositorycode();
			EadDAO eadDAO = DAOFactory.instance().getEadDAO();
			//
			EadSearchOptions searchOptions = new EadSearchOptions();
			searchOptions.setArchivalInstitionId(archivalInstitution.getAiId());
			searchOptions.setPublished(true);
			// holdings guides
			searchOptions.setEadClazz(HoldingsGuide.class);
			institutionStatistics.holdingsguide = convertLong(eadDAO.countEads(searchOptions));
			institutionStatistics.daos = convertLong(eadDAO.countDaos(searchOptions));
			institutionStatistics.units = convertLong(eadDAO.countUnits(searchOptions));
			// source guides
			searchOptions.setEadClazz(SourceGuide.class);
			institutionStatistics.sourceguide = convertLong(eadDAO.countEads(searchOptions));
			institutionStatistics.daos += convertLong(eadDAO.countDaos(searchOptions));
			institutionStatistics.units += convertLong(eadDAO.countUnits(searchOptions));
			// findingaids
			searchOptions.setEadClazz(FindingAid.class);
			institutionStatistics.findingaids = convertLong(eadDAO.countEads(searchOptions));
			institutionStatistics.daos = convertLong(eadDAO.countDaos(searchOptions));
			institutionStatistics.units += convertLong(eadDAO.countUnits(searchOptions));

			searchOptions.setPublished(null);
			searchOptions.setEuropeana(EuropeanaState.DELIVERED);
			institutionStatistics.totalChosDeliveredToEuropeana = convertLong(eadDAO.countChos(searchOptions));

			searchOptions.getEuropeana().clear();
			searchOptions.getEuropeana().add(EuropeanaState.CONVERTED);
			searchOptions.getEuropeana().add(EuropeanaState.DELIVERED);
			institutionStatistics.totalChos = convertLong(eadDAO.countChos(searchOptions));

		}
		return institutionStatistics;
	}

	private static long convertLong(Long longObject) {
		if (longObject == null) {
			return 0;
		} else {
			return longObject.longValue();
		}
	}

	private static class InstitutionStatistics {
		String country;
		String archivalInstitution;
		boolean hasEag;
		String repositoryCode;
		long findingaids = 0;
		long holdingsguide = 0;
		long sourceguide = 0;
		long daos = 0;
		long units = 0;
		long totalChosDeliveredToEuropeana = 0;
		long totalChos = 0;

		public InstitutionStatistics(String country, String archivalInstitution) {
			this.country = country;
			this.archivalInstitution = archivalInstitution;
		}

		@Override
		public String toString() {
			String result = "\"" + country + "\";\"" + archivalInstitution + "\";";
			if (hasEag) {
				result +=  repositoryCode + ";1;";
			} else {
				result += ";0;";
			}
			if (units > 0){
				result += "1;";
			}else {
				result += "0;";
			}
			result += findingaids + ";" + holdingsguide + ";" + sourceguide + ";" + units + ";" + daos + ";"
					+ totalChos + ";" + totalChosDeliveredToEuropeana;
			return result;
		}

	}

	private static class CountryStatistics {
		String country;
		int numberOfInstitutions = 0;
		int numberOfInstitutionsWithEag = 0;
		int numberOfInstitutionsWithSearchableItems = 0;
		long findingaids = 0;
		long holdingsguide = 0;
		long sourceguide = 0;
		long daos = 0;
		long units = 0;
		long totalChosDeliveredToEuropeana = 0;
		long totalChos = 0;

		public CountryStatistics(String country) {
			this.country = country;
		}

		@Override
		public String toString() {
			String result = "\"" + country + "\";\"" + numberOfInstitutions + "\";" + numberOfInstitutionsWithEag + ";"
					+ numberOfInstitutionsWithSearchableItems + ";" + findingaids + ";" + holdingsguide + ";"
					+ sourceguide + ";" + units + ";" + daos + ";" + totalChos + ";" + totalChosDeliveredToEuropeana;
			return result;
		}

		public void addInstitutionStatistics(InstitutionStatistics institutionStatistics) {
			numberOfInstitutions++;
			if (institutionStatistics.hasEag) {
				numberOfInstitutionsWithEag++;
			}
			if (institutionStatistics.units > 0) {
				numberOfInstitutionsWithSearchableItems++;
			}
			findingaids += institutionStatistics.findingaids;
			holdingsguide += institutionStatistics.holdingsguide;
			sourceguide += institutionStatistics.sourceguide;
			daos += institutionStatistics.daos;
			units += institutionStatistics.units;
			totalChos += totalChos;
			totalChosDeliveredToEuropeana += institutionStatistics.totalChosDeliveredToEuropeana;
		}
	}
}
