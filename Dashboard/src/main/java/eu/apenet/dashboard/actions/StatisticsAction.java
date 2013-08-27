package eu.apenet.dashboard.actions;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.StyleTypeDefinitions.CellBordersType;
import org.odftoolkit.simple.style.StyleTypeDefinitions.FontStyle;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Table;

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

	public String downloadInstitutionsStatistics() throws Exception {
		CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
		List<Country> countries = new ArrayList<Country>();
		if (this.getSecurityContext().isAdmin()) {
			countries = countryDAO.findAll();
		} else if (this.getSecurityContext().isCountryManager()) {
			Integer countryId = this.getSecurityContext().getCountryId();
			countries.add(countryDAO.findById(countryId));
		}
		writeODS(countries);
		return null;
	}
	private void writeODS(List<Country> countries) throws Exception {
		ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
		String name = "countries";
		if (countries.size() == 1) {
			name = countries.get(0).getCname();
		}
		name = APEnetUtilities.convertToFilename(name + "-institutions-statistics.ods");
		OutputStream outputStream = ContentUtils.getOutputStreamToDownload(this.getServletRequest(), getServletResponse(),
				name, "application/vnd.oasis.opendocument.spreadsheet");
		//printWriter.println(INSTITUTION_HEADER);
		DocumentWriter documentWriter = new DocumentWriter();
		for (Country country : countries) {
			List<ArchivalInstitution> archivalInstitutions = archivalInstitutionDAO
					.getArchivalInstitutionsByCountryId(country.getId());
			for (ArchivalInstitution archivalInstitution : archivalInstitutions) {
				InstitutionStatistics institutionStatistics = getInstitutionStatistics(country, archivalInstitution);
				documentWriter.addInstitutionStatistics(institutionStatistics);
			}
		}
		documentWriter.save(outputStream);
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
	private class DocumentWriter {
		SpreadsheetDocument document;
		Table table;
		int rowNumber = 0;
		public DocumentWriter() throws Exception{
			document = SpreadsheetDocument.newSpreadsheetDocument();
	        table = document.getSheetByIndex(0);
	       
			int colNumber = 0;
			fillHeaderCell(colNumber, rowNumber, "Country");
			colNumber++;
			fillHeaderCell(colNumber, rowNumber, "Identifier of the institution");
			colNumber++;
			fillHeaderCell(colNumber, rowNumber, "EAG");
			colNumber++;	
			fillHeaderCell(colNumber, rowNumber, "Has searchable items");
			colNumber++;					
			fillHeaderCell(colNumber, rowNumber, "#Published FA");
			colNumber++;					
			fillHeaderCell(colNumber, rowNumber, "#Published HG");
			colNumber++;
			fillHeaderCell(colNumber, rowNumber, "#Published SG");
			colNumber++;
			fillHeaderCell(colNumber, rowNumber, "#Published Descriptive units");
			colNumber++;
			fillHeaderCell(colNumber, rowNumber, "#Published DAOs");
			colNumber++;
			fillHeaderCell(colNumber, rowNumber,"#EDM(providedCHOs)");
			colNumber++;	
			fillHeaderCell(colNumber, rowNumber, "#Delivered to Europeana(providedCHOs)");
			colNumber++;
			rowNumber++;
		}
		public void save(OutputStream outputStream) throws Exception{
			document.save(outputStream);
		}
		
		
		public void addInstitutionStatistics(InstitutionStatistics institutionStatistics){
			int colNumber = 0;
			fillCell(colNumber, rowNumber, institutionStatistics.country);
			colNumber++;
			fillCell(colNumber, rowNumber, institutionStatistics.archivalInstitution);
			colNumber++;
			if (institutionStatistics.hasEag){
				fillCell(colNumber, rowNumber, institutionStatistics.repositoryCode);
				colNumber++;	
				fillCell(colNumber, rowNumber, 1);
				colNumber++;					
			}else {
				colNumber++;	
				fillCell(colNumber, rowNumber, 0);
				colNumber++;					
			}
			if (institutionStatistics.units > 0){
				fillCell(colNumber, rowNumber, 1);
				colNumber++;					
			}else {
				fillCell(colNumber, rowNumber, 0);
				colNumber++;					
			}
			fillCell(colNumber, rowNumber,institutionStatistics.findingaids);
			colNumber++;
			fillCell(colNumber, rowNumber,institutionStatistics.holdingsguide);
			colNumber++;
			fillCell(colNumber, rowNumber,institutionStatistics.sourceguide);
			colNumber++;	
			fillCell(colNumber, rowNumber,institutionStatistics.units);
			colNumber++;
			fillCell(colNumber, rowNumber,institutionStatistics.daos);
			colNumber++;
			fillCell(colNumber, rowNumber,institutionStatistics.totalChos);
			colNumber++;
			fillCell(colNumber, rowNumber,institutionStatistics.totalChosDeliveredToEuropeana);
			colNumber++;	
			rowNumber++;
		}
		private void fillHeaderCell(int col, int row, String stringValue){
			table.getColumnByIndex(col).setUseOptimalWidth(true);
			Cell cell = table.getCellByPosition(col, row);
			Font font = cell.getFont();
			font.setFontStyle(FontStyle.BOLD);
			font.setSize(10);
			cell.setFont(font);
	        cell.setDisplayText(stringValue);
		}
		private void fillCell(int col, int row, String stringValue){
			Cell cell = table.getCellByPosition(col, row);
	        cell.setDisplayText(stringValue);
		}
		private void fillCell(int col, int row, int stringValue){
			Cell cell = table.getCellByPosition(col, row);
	        cell.setDisplayText(stringValue  + "");
		}
		private void fillCell(int col, int row, long stringValue){
			Cell cell = table.getCellByPosition(col, row);
	        cell.setDisplayText(stringValue  + "");
		}
	}
}
