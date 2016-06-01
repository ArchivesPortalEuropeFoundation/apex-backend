package eu.apenet.dashboard.actions;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.StyleTypeDefinitions.FontStyle;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Table;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;
import eu.apenet.persistence.vo.EacCpf;

public class StatisticsAction extends AbstractAction {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    /**
     *
     */
    private static final long serialVersionUID = -7140890005810933129L;

    public String downloadCountriesStatistics() throws Exception {
        if (this.getSecurityContext().isAdminOrCoordinator()) {
            String name = APEnetUtilities.convertToFilename("countries-statistics-"
                    + SIMPLE_DATE_FORMAT.format(new Date()) + ".ods");
            CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
            ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
            OutputStream outputStream = ContentUtils.getOutputStreamToDownload(this.getServletRequest(),
                    getServletResponse(), name, "application/vnd.oasis.opendocument.spreadsheet");
            CountriesStatisticsDocumentWriter documentWriter = new CountriesStatisticsDocumentWriter();
            List<Country> countries = countryDAO.findAll();
            for (Country country : countries) {
                CountryStatistics countryStatistics = new CountryStatistics(country.getCname());
                List<ArchivalInstitution> archivalInstitutions = archivalInstitutionDAO
                        .getArchivalInstitutionsByCountryId(country.getId());
                for (ArchivalInstitution archivalInstitution : archivalInstitutions) {
                    countryStatistics.addInstitutionStatistics(getInstitutionStatistics(country, archivalInstitution));
                }
                documentWriter.addStatistics(countryStatistics);
            }
            documentWriter.save(outputStream);
        }
        return null;
    }

    public String downloadInstitutionsStatistics() throws Exception {
        CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
        List<Country> countries = new ArrayList<Country>();
        if (this.getSecurityContext().isAdminOrCoordinator()) {
            countries = countryDAO.findAll();
        } else if (this.getSecurityContext().isCountryManager()) {
            Integer countryId = this.getSecurityContext().getCountryId();
            countries.add(countryDAO.findById(countryId));
        }
        ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
        String name = "countries";
        if (countries.size() == 1) {
            name = countries.get(0).getCname();
        }
        name = APEnetUtilities.convertToFilename(name + "-institutions-statistics-"
                + SIMPLE_DATE_FORMAT.format(new Date()) + ".ods");
        OutputStream outputStream = ContentUtils.getOutputStreamToDownload(this.getServletRequest(),
                getServletResponse(), name, "application/vnd.oasis.opendocument.spreadsheet");
        InstitutionsStatisticsDocumentWriter documentWriter = new InstitutionsStatisticsDocumentWriter();
        for (Country country : countries) {
            List<ArchivalInstitution> archivalInstitutions = archivalInstitutionDAO
                    .getArchivalInstitutionsByCountryId(country.getId());
            for (ArchivalInstitution archivalInstitution : archivalInstitutions) {
                InstitutionStatistics institutionStatistics = getInstitutionStatistics(country, archivalInstitution);
                documentWriter.addStatistics(institutionStatistics);
            }
        }
        documentWriter.save(outputStream);
        return null;
    }

    private InstitutionStatistics getInstitutionStatistics(Country country, ArchivalInstitution archivalInstitution) {
        InstitutionStatistics institutionStatistics = new InstitutionStatistics(country.getCname(),
                archivalInstitution.getAiname());
        institutionStatistics.openDataEnabled = archivalInstitution.isOpenDataEnabled();
        institutionStatistics.hasEag = StringUtils.isNotBlank(archivalInstitution.getEagPath());
        if (institutionStatistics.hasEag) {
            institutionStatistics.repositoryCode = archivalInstitution.getRepositorycode();
            EadDAO eadDAO = DAOFactory.instance().getEadDAO();
            //
            ContentSearchOptions searchOptions = new ContentSearchOptions();
            searchOptions.setArchivalInstitionId(archivalInstitution.getAiId());
            searchOptions.setPublished(true);
            // holdings guides
            searchOptions.setContentClass(HoldingsGuide.class);
            institutionStatistics.holdingsguide = convertLong(eadDAO.countEads(searchOptions));
            if (institutionStatistics.holdingsguide > 0) {
                institutionStatistics.daos = convertLong(eadDAO.countDaos(searchOptions));
                institutionStatistics.units = convertLong(eadDAO.countUnits(searchOptions));
            }
            // source guides
            searchOptions.setContentClass(SourceGuide.class);
            institutionStatistics.sourceguide = convertLong(eadDAO.countEads(searchOptions));
            if (institutionStatistics.sourceguide > 0) {
                institutionStatistics.daos += convertLong(eadDAO.countDaos(searchOptions));
                institutionStatistics.units += convertLong(eadDAO.countUnits(searchOptions));
            }
            // eac-cpf
            searchOptions.setContentClass(EacCpf.class);
            institutionStatistics.eaccpf = convertLong(eadDAO.countEads(searchOptions));
            // findingaids
            searchOptions.setContentClass(FindingAid.class);
            institutionStatistics.findingaids = convertLong(eadDAO.countEads(searchOptions));
            if (institutionStatistics.findingaids > 0) {
                institutionStatistics.daos = convertLong(eadDAO.countDaos(searchOptions));
                institutionStatistics.units += convertLong(eadDAO.countUnits(searchOptions));
                institutionStatistics.webResources += convertLong(eadDAO.countWebResources(searchOptions));
            }

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
        long eaccpf = 0;
        long daos = 0;
        long units = 0;
        long totalChosDeliveredToEuropeana = 0;
        long totalChos = 0;
        long webResources = 0;
        boolean openDataEnabled = false;

        public InstitutionStatistics(String country, String archivalInstitution) {
            this.country = country;
            this.archivalInstitution = archivalInstitution;
        }

        public boolean containsSearchableItems() {
            return (findingaids + holdingsguide + sourceguide + eaccpf) > 0;
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
        long eaccpf = 0;
        long daos = 0;
        long units = 0;
        long totalChosDeliveredToEuropeana = 0;
        long totalChos = 0;
        long webResources = 0;

        public CountryStatistics(String country) {
            this.country = country;
        }

        public void addInstitutionStatistics(InstitutionStatistics institutionStatistics) {
            numberOfInstitutions++;
            if (institutionStatistics.hasEag) {
                numberOfInstitutionsWithEag++;
            }
            if (institutionStatistics.containsSearchableItems()) {
                numberOfInstitutionsWithSearchableItems++;
            }
            findingaids += institutionStatistics.findingaids;
            holdingsguide += institutionStatistics.holdingsguide;
            sourceguide += institutionStatistics.sourceguide;
            eaccpf += institutionStatistics.eaccpf;
            daos += institutionStatistics.daos;
            units += institutionStatistics.units;
            totalChos += totalChos;
            totalChosDeliveredToEuropeana += institutionStatistics.totalChosDeliveredToEuropeana;
            webResources += institutionStatistics.webResources;
        }
    }

    private class InstitutionsStatisticsDocumentWriter extends DocumentWriter {

        int rowNumber = 0;

        public InstitutionsStatisticsDocumentWriter() throws Exception {
            int colNumber = 0;
            fillHeaderCell(colNumber, rowNumber, "Date of export: " + SIMPLE_DATE_FORMAT.format(new Date()));
            rowNumber++;
            fillHeaderCell(colNumber, rowNumber, "Country");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "Institution");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "Identifier of the institution");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "EAG");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "Has searchable items");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "OpenData Enabled");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#Published FA");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#Published HG");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#Published SG");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#Published EAC-CPF");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#Published Descriptive units");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#Published DAOs");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#EDM(providedCHOs)");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#Delivered to Europeana(providedCHOs)");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#EDM(webResources)");
            rowNumber++;
        }

        public void addStatistics(InstitutionStatistics institutionStatistics) {
            int colNumber = 0;
            fillCell(colNumber, rowNumber, institutionStatistics.country);
            colNumber++;
            fillCell(colNumber, rowNumber, institutionStatistics.archivalInstitution);
            colNumber++;
            if (institutionStatistics.hasEag) {
                fillCell(colNumber, rowNumber, institutionStatistics.repositoryCode);
                colNumber++;
                fillCell(colNumber, rowNumber, 1);
                colNumber++;
            } else {
                colNumber++;
                fillCell(colNumber, rowNumber, 0);
                colNumber++;
            }
            if (institutionStatistics.units > 0) {
                fillCell(colNumber, rowNumber, 1);
                colNumber++;
            } else {
                fillCell(colNumber, rowNumber, 0);
                colNumber++;
            }

            if (institutionStatistics.openDataEnabled) {
                fillCell(colNumber, rowNumber, 1);
            } else {
                fillCell(colNumber, rowNumber, 0);
            }
            colNumber++;

            fillCell(colNumber, rowNumber, institutionStatistics.findingaids);
            colNumber++;
            fillCell(colNumber, rowNumber, institutionStatistics.holdingsguide);
            colNumber++;
            fillCell(colNumber, rowNumber, institutionStatistics.sourceguide);
            colNumber++;
            fillCell(colNumber, rowNumber, institutionStatistics.eaccpf);
            colNumber++;
            fillCell(colNumber, rowNumber, institutionStatistics.units);
            colNumber++;
            fillCell(colNumber, rowNumber, institutionStatistics.daos);
            colNumber++;
            fillCell(colNumber, rowNumber, institutionStatistics.totalChos);
            colNumber++;
            fillCell(colNumber, rowNumber, institutionStatistics.totalChosDeliveredToEuropeana);
            colNumber++;
            fillCell(colNumber, rowNumber, institutionStatistics.webResources);
            rowNumber++;
        }

    }

    private class DocumentWriter {

        SpreadsheetDocument document;
        Table table;

        public DocumentWriter() throws Exception {
            document = SpreadsheetDocument.newSpreadsheetDocument();
            table = document.getSheetByIndex(0);
        }

        public void save(OutputStream outputStream) throws Exception {
            document.save(outputStream);
        }

        protected void fillHeaderCell(int col, int row, String stringValue) {
            table.getColumnByIndex(col).setUseOptimalWidth(true);
            Cell cell = table.getCellByPosition(col, row);
            Font font = cell.getFont();
            font.setFontStyle(FontStyle.BOLD);
            font.setSize(10);
            cell.setFont(font);
            cell.setDisplayText(stringValue);
        }

        protected void fillCell(int col, int row, String stringValue) {
            Cell cell = table.getCellByPosition(col, row);
            cell.setDisplayText(stringValue);
        }

        protected void fillCell(int col, int row, int stringValue) {
            Cell cell = table.getCellByPosition(col, row);
            cell.setDisplayText(stringValue + "");
        }

        protected void fillCell(int col, int row, long stringValue) {
            Cell cell = table.getCellByPosition(col, row);
            cell.setDisplayText(stringValue + "");
        }
    }

    private class CountriesStatisticsDocumentWriter extends DocumentWriter {

        int rowNumber = 0;

        public CountriesStatisticsDocumentWriter() throws Exception {
            int colNumber = 0;
            fillHeaderCell(colNumber, rowNumber, "Date of export: " + SIMPLE_DATE_FORMAT.format(new Date()));
            rowNumber++;
            fillHeaderCell(colNumber, rowNumber, "Country");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#Institutions");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#Institutions EAG");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#Institutions with searchable items");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#Published FA");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#Published HG");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#Published SG");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#Published EAC-CPF");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#Published Descriptive units");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#Published DAOs");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#EDM(providedCHOs)");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#Delivered to Europeana(providedCHOs)");
            colNumber++;
            fillHeaderCell(colNumber, rowNumber, "#EDM(webResources)");
            rowNumber++;
        }

        public void addStatistics(CountryStatistics countryStatistics) {
            int colNumber = 0;
            fillCell(colNumber, rowNumber, countryStatistics.country);
            colNumber++;
            fillCell(colNumber, rowNumber, countryStatistics.numberOfInstitutions);
            colNumber++;
            fillCell(colNumber, rowNumber, countryStatistics.numberOfInstitutionsWithEag);
            colNumber++;
            fillCell(colNumber, rowNumber, countryStatistics.numberOfInstitutionsWithSearchableItems);
            colNumber++;
            fillCell(colNumber, rowNumber, countryStatistics.findingaids);
            colNumber++;
            fillCell(colNumber, rowNumber, countryStatistics.holdingsguide);
            colNumber++;
            fillCell(colNumber, rowNumber, countryStatistics.sourceguide);
            colNumber++;
            fillCell(colNumber, rowNumber, countryStatistics.eaccpf);
            colNumber++;
            fillCell(colNumber, rowNumber, countryStatistics.units);
            colNumber++;
            fillCell(colNumber, rowNumber, countryStatistics.daos);
            colNumber++;
            fillCell(colNumber, rowNumber, countryStatistics.totalChos);
            colNumber++;
            fillCell(colNumber, rowNumber, countryStatistics.totalChosDeliveredToEuropeana);
            colNumber++;
            fillCell(colNumber, rowNumber, countryStatistics.webResources);
            rowNumber++;
        }

    }
}
