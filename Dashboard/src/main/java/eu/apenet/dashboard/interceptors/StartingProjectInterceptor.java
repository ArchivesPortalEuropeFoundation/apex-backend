package eu.apenet.dashboard.interceptors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.apenet.persistence.vo.*;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.exceptions.APEnetRuntimeException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.persistence.dao.CouAlternativeNameDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.dao.FileStateDAO;
import eu.apenet.persistence.dao.FileTypeDAO;
import eu.apenet.persistence.dao.LangDAO;
import eu.apenet.persistence.dao.UserRoleDAO;
import eu.apenet.persistence.dao.UpFileStateDAO;
import eu.apenet.persistence.dao.UploadMethodDAO;
import eu.apenet.persistence.dao.UserStateDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.CouAlternativeNameHibernateDAO;
import eu.apenet.persistence.hibernate.CountryHibernateDAO;
import eu.apenet.persistence.hibernate.FileStateHibernateDAO;
import eu.apenet.persistence.hibernate.FileTypeHibernateDAO;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.hibernate.LangHibernateDAO;
import eu.apenet.persistence.hibernate.UpFileStateHibernateDAO;
import eu.apenet.persistence.hibernate.UploadMethodHibernateDAO;
import eu.apenet.persistence.hibernate.UserStateHibernateDAO;

/**
 * User: Yoann Moranville
 * Date: Jan 25, 2011
 *
 * @author Yoann Moranville
 */
public class StartingProjectInterceptor extends AbstractInterceptor {
    private static final Logger LOG = Logger.getLogger(StartingProjectInterceptor.class);
    private static final long serialVersionUID = -7360950497086673753L;
    private static final String STARTPAGEACTION = "startpage";
    private boolean isReady = true;
    private static boolean isDisabled = true;

    public static boolean isDisabled() {
        return isDisabled;
    }

    @Override
    public void init() {
        isDisabled = false;
        startingProject();
    }

    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        String actionName = actionInvocation.getProxy().getActionName();
        if (!isReady && !actionName.equals(STARTPAGEACTION)) {
            if (DAOFactory.instance().getUserDAO().doesAdminExist()) {
                isReady = true;
                return actionInvocation.invoke();
            }
            LOG.info("Project ready, but admin account does not exist");
            return "start_page";
        }
        return actionInvocation.invoke();
    }

    public void startingProject() {
        LOG.info("Checking state of project...");
        try {
            if (!DAOFactory.instance().getUserDAO().doesAdminExist())
                isReady = false;
            else
                LOG.info("Project is not new, nothing is done");
        } catch (Exception e) {
            LOG.info("Starting creation of project");
            isReady = false;
            try {
                createTables();
                createNeededDatabaseInput();
                createFiles();
                populateCountryAlternativeName();
            } catch (Exception ex) {
                throw new APEnetRuntimeException("Error when creating the project", ex);
            }
            LOG.info("Finished creation of project");
        }
    }

    private void createFiles() throws IOException {
        File destFile = new File(APEnetUtilities.getDashboardConfig().getArchivalLandscapeDirPath() + APEnetUtilities.FILESEPARATOR + "AL.xml");
        OutputStream outputStream = new FileOutputStream(destFile);
        InputStream inputStream = getClass().getResourceAsStream("/StartingProject/AL.xml");
        IOUtils.copy(inputStream, outputStream);
        outputStream.close();
        inputStream.close();

        File mailDir = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + "mail");
        if (!mailDir.exists())
            mailDir.mkdir();

        destFile = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + "mail" + APEnetUtilities.FILESEPARATOR + "Archives Portal Europe - Content Provider Agreement.txt");
        outputStream = new FileOutputStream(destFile);
        inputStream = getClass().getResourceAsStream("/StartingProject/APEnetPrivacyPolicy.txt");
        IOUtils.copy(inputStream, outputStream);
        outputStream.close();
        inputStream.close();

        destFile = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + "mail" + APEnetUtilities.FILESEPARATOR + "archivesportallogo.jpg");
        outputStream = new FileOutputStream(destFile);
        inputStream = getClass().getResourceAsStream("/StartingProject/Logo.jpg");
        IOUtils.copy(inputStream, outputStream);
        outputStream.close();
        inputStream.close();
    }

    public void createTables() throws IOException, APEnetException {
        InputStream inputStream = User.class.getResourceAsStream("/dbScripts/db_script.sql");
        String queryString = IOUtils.toString(inputStream);
        String[] queryStrings = queryString.split(";");
        HibernateUtil.beginDatabaseTransaction();
        for (String oneQuery : queryStrings) {
            String queryCheck = "";
            try {
                SQLQuery query = HibernateUtil.getDatabaseSession().createSQLQuery(oneQuery);
                queryCheck = query.getQueryString();
                query.executeUpdate();
            } catch (Exception e) {
                LOG.error("Error when launching query: " + queryCheck, e);
                HibernateUtil.rollbackDatabaseTransaction();
                HibernateUtil.closeDatabaseSession();
                throw new APEnetException("Error when launching query: ", e);
            }
        }
        HibernateUtil.commitDatabaseTransaction();
        HibernateUtil.closeDatabaseSession();
    }

    private void createNeededDatabaseInput() {
        LOG.info("Create needed database input");

        createRoleTypeInformation();
        createCountryInformation();
        createUserStateInformation();
        createLangInformation();
        createFileStateInformation();
        createFileTypeInformation();
        createUploadMethodInformation();
        createUpFileStateInformation();

    }

    private void createCountryInformation() {
        LOG.info("Insert Country information");

        CountryDAO countryDAO = new CountryHibernateDAO();
        Country country;

        HibernateUtil.beginDatabaseTransaction();
        try {
            country = new Country("SPAIN", "ES", 4);
            countryDAO.insertSimple(country);
            country = new Country("FRANCE", "FR", 5);
            countryDAO.insertSimple(country);
            country = new Country("GERMANY", "DE", 2);
            countryDAO.insertSimple(country);
            country = new Country("GREECE", "GR", 6);
            countryDAO.insertSimple(country);
            country = new Country("POLAND", "PL", 10);
            countryDAO.insertSimple(country);
            country = new Country("SWEDEN", "SE", 14);
            countryDAO.insertSimple(country);
            country = new Country("NETHERLANDS", "NL", 9);
            countryDAO.insertSimple(country);
            country = new Country("BELGIUM", "BE", 1);
            countryDAO.insertSimple(country);
            country = new Country("SLOVENIA", "SI", 12);
            countryDAO.insertSimple(country);
            country = new Country("IRELAND", "IE", 3);
            countryDAO.insertSimple(country);
            country = new Country("LATVIA", "LV", 7);
            countryDAO.insertSimple(country);
            country = new Country("MALTA", "MT", 8);
            countryDAO.insertSimple(country);
            country = new Country("PORTUGAL", "PT", 11);
            countryDAO.insertSimple(country);
            country = new Country("FINLAND", "FI", 13);
            countryDAO.insertSimple(country);
            country = new Country("ESTONIA", "ET", 15);
            countryDAO.insertSimple(country);

            HibernateUtil.commitDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();
        } catch (Exception e) {
            LOG.error("Could not create country information");
            HibernateUtil.rollbackDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();
        }
    }

    private void createUserStateInformation() {
        LOG.info("Insert UserState information...");

        UserStateDAO userStateDAO = new UserStateHibernateDAO();
        UserState userState;

        HibernateUtil.beginDatabaseTransaction();
        try {
            userState = new UserState();
            userState.setState("Blocked");
            userStateDAO.insertSimple(userState);
            userState = new UserState();
            userState.setState("Actived");
            userStateDAO.insertSimple(userState);

            HibernateUtil.commitDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();
        } catch (Exception e) {
            LOG.error("Could not create user state information");
            HibernateUtil.rollbackDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();
        }
    }

    private void createRoleTypeInformation() {
        LOG.info("Insert RoleType information...");

        UserRoleDAO roleTypeDAO = DAOFactory.instance().getUserRoleDAO();
        UserRole roleType;

        HibernateUtil.beginDatabaseTransaction();
        try {
            roleType = new UserRole();
            roleType.setRole("countryManager");
            roleTypeDAO.insertSimple(roleType);
            roleType = new UserRole();
            roleType.setRole("institutionManager");
            roleTypeDAO.insertSimple(roleType);
            roleType = new UserRole();
            roleType.setRole("admin");
            roleTypeDAO.insertSimple(roleType);

            HibernateUtil.commitDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();
        } catch (Exception e) {
            LOG.error("Could not create role type information");
            HibernateUtil.rollbackDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();
        }
    }

    private void createLangInformation() {
        LOG.info("Insert Language information...");
        LangDAO langDAO = new LangHibernateDAO();
        Lang lang;

        HibernateUtil.beginDatabaseTransaction();
        try {
            lang = new Lang("SPANISH", "SPA", "ES", "Español");
            langDAO.insertSimple(lang);
            lang = new Lang("FRENCH", "FRE", "FR", "Français");
            langDAO.insertSimple(lang);
            lang = new Lang("GERMANY", "GER", "DE", "Deutch");
            langDAO.insertSimple(lang);
            lang = new Lang("GREEK", "GRE", "EL", "Ελληνικά");
            langDAO.insertSimple(lang);
            lang = new Lang("POLISH", "POL", "PL", "Polsky");
            langDAO.insertSimple(lang);
            lang = new Lang("SWEDISH", "SWE", "SE", "Svenska");
            langDAO.insertSimple(lang);
            lang = new Lang("DUTCH", "DUT", "NL", "Nederlands");
            langDAO.insertSimple(lang);
            lang = new Lang("ENGLISH", "ENG", "EN", "English");
            langDAO.insertSimple(lang);
            lang = new Lang("IRISH", "GLE", "GA", "Gaeilge");
            langDAO.insertSimple(lang);
            lang = new Lang("LATVIAN", "LAV", "LV", "Latviešu");
            langDAO.insertSimple(lang);
            lang = new Lang("MALTESE", "MLT", "MT", "Malti");
            langDAO.insertSimple(lang);
            lang = new Lang("PORTUGUESE", "POR", "PT", "Português");
            langDAO.insertSimple(lang);
            lang = new Lang("SLOVENIAN", "SLV", "SL", "Slovenščina");
            langDAO.insertSimple(lang);
            lang = new Lang("FINNISH", "FIN", "FI", "Suomi");
            langDAO.insertSimple(lang);
            lang = new Lang("ESTONIAN", "EST", "ET", "Eesti");
            langDAO.insertSimple(lang);

            HibernateUtil.commitDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();
        } catch (Exception e) {
            LOG.error("Could not create Lang information");
            HibernateUtil.rollbackDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();
        }
    }

    private void createFileStateInformation() {
        LOG.info("Insert File State information...");
        FileStateDAO fileStateDAO = new FileStateHibernateDAO();
        FileState fileState;

        HibernateUtil.beginDatabaseTransaction();
        try {
            fileState = new FileState();
            fileState.setState("New");
            fileStateDAO.insertSimple(fileState);
            fileState = new FileState();
            fileState.setState("Not_Validated_Not_Converted");
            fileStateDAO.insertSimple(fileState);
            fileState = new FileState();
            fileState.setState("Not_Validated_Converted");
            fileStateDAO.insertSimple(fileState);
            fileState = new FileState();
            fileState.setState("Validated_Converted");
            fileStateDAO.insertSimple(fileState);
            fileState = new FileState();
            fileState.setState("Validated_Not_Converted");
            fileStateDAO.insertSimple(fileState);
            fileState = new FileState();
            fileState.setState("Validated_Final_Error");
            fileStateDAO.insertSimple(fileState);
            fileState = new FileState();
            fileState.setState("Indexing");
            fileStateDAO.insertSimple(fileState);
            fileState = new FileState();
            fileState.setState("Indexed_Not converted to ESE/EDM");
            fileStateDAO.insertSimple(fileState);
            fileState = new FileState();
            fileState.setState("Indexed_Converted to ESE/EDM");
            fileStateDAO.insertSimple(fileState);
            fileState = new FileState();
            fileState.setState("Indexed_Delivered to Europeana");
            fileStateDAO.insertSimple(fileState);
            fileState = new FileState();
            fileState.setState("Indexed_Harvested to Europeana");
            fileStateDAO.insertSimple(fileState);
            fileState = new FileState();
            fileState.setState("Indexed_No html");
            fileStateDAO.insertSimple(fileState);
            fileState = new FileState();
            fileState.setState("Indexed_Not Linked");
            fileStateDAO.insertSimple(fileState);
            fileState = new FileState();
            fileState.setState("Indexed_Linked");
            fileStateDAO.insertSimple(fileState);
            fileState = new FileState();
            fileState.setState("Ready_to_index");
            fileStateDAO.insertSimple(fileState);

            HibernateUtil.commitDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();
        } catch (Exception e) {
            LOG.error("Could not create File State information");
            HibernateUtil.rollbackDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();
        }
    }

    private void createUploadMethodInformation() {
        LOG.info("Insert Upload Method information...");
        UploadMethodDAO uploadMethodDAO = new UploadMethodHibernateDAO();
        UploadMethod uploadMethod;

        HibernateUtil.beginDatabaseTransaction();
        try {
            uploadMethod = new UploadMethod();
            uploadMethod.setMethod("OAI-PMH");
            uploadMethodDAO.insertSimple(uploadMethod);
            uploadMethod = new UploadMethod();
            uploadMethod.setMethod("FTP");
            uploadMethodDAO.insertSimple(uploadMethod);
            uploadMethod = new UploadMethod();
            uploadMethod.setMethod("HTTP");
            uploadMethodDAO.insertSimple(uploadMethod);

            HibernateUtil.commitDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();
        } catch (Exception e) {
            LOG.error("Could not create Upload Method information");
            HibernateUtil.rollbackDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();
        }
    }

    private void createFileTypeInformation() {
        LOG.info("Insert File Type information...");
        FileTypeDAO fileTypeDAO = new FileTypeHibernateDAO();
        FileType fileType;

        HibernateUtil.beginDatabaseTransaction();
        try {
            fileType = new FileType();
            fileType.setFtype("zip");
            fileTypeDAO.insertSimple(fileType);
            fileType = new FileType();
            fileType.setFtype("xml");
            fileTypeDAO.insertSimple(fileType);
            fileType = new FileType();
            fileType.setFtype("xsl");
            fileTypeDAO.insertSimple(fileType);

            HibernateUtil.commitDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();
        } catch (Exception e) {
            LOG.error("Could not create File Type information");
            HibernateUtil.rollbackDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();
        }
    }

    private void createUpFileStateInformation() {
        LOG.info("Insert Up File State information...");
        UpFileStateDAO upFileStateDAO = new UpFileStateHibernateDAO();
        UpFileState upFileState;

        HibernateUtil.beginDatabaseTransaction();
        try {
            upFileState = new UpFileState();
            upFileState.setState("New");
            upFileStateDAO.insertSimple(upFileState);

            HibernateUtil.commitDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();
        } catch (Exception e) {
            LOG.error("Could not create Up File State information");
            HibernateUtil.rollbackDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();
        }
    }



    public void populateCountryAlternativeName() throws APEnetException {
        Map<String, String> countryMap = new HashMap<String, String>();
        countryMap.put("belgium", "Belgien");
        countryMap.put("finland", "Finnland");
        countryMap.put("france", "Frankreich");
        countryMap.put("germany", "Deutschland");
        countryMap.put("ireland", "Irland");
        countryMap.put("latvia", "Lettland");
        countryMap.put("greece", "Griechenland");
        countryMap.put("malta", "Malta");
        countryMap.put("netherlands", "Niederlande");
        countryMap.put("poland", "Polen");
        countryMap.put("portugal", "Portugal");
        countryMap.put("slovenia", "Slowenien");
        countryMap.put("spain", "Spanien");
        countryMap.put("sweden", "Schweden");

        String isoname = "DE";
        setCountryLanguage(countryMap, isoname);

        countryMap = new HashMap<String, String>();
        countryMap.put("belgium", "Βέλγιο");
        countryMap.put("finland", "Φινλανδία");
        countryMap.put("france", "Γαλλία");
        countryMap.put("germany", "Γερμανία");
        countryMap.put("greece", "Ελλάς");
        countryMap.put("ireland", "Ιρλανδίας");
        countryMap.put("latvia", "Λεττονία");
        countryMap.put("malta", "Μάλτα");
        countryMap.put("netherlands", "Ολλανδία");
        countryMap.put("poland", "Πολωνία");
        countryMap.put("portugal", "Πορτογαλία");
        countryMap.put("slovenia", "Σλοβενία");
        countryMap.put("spain", "Ισπανία");
        countryMap.put("sweden", "Σουηδία");

        isoname = "EL";
        setCountryLanguage(countryMap, isoname);

        countryMap = new HashMap<String, String>();
        countryMap.put("belgium", "Belgium");
        countryMap.put("finland", "Finland");
        countryMap.put("france", "France");
        countryMap.put("germany", "Germany");
        countryMap.put("greece", "Greece");
        countryMap.put("ireland", "Ireland");
        countryMap.put("latvia", "Latvia");
        countryMap.put("malta", "Malta");
        countryMap.put("netherlands", "Netherlands");
        countryMap.put("poland", "Poland");
        countryMap.put("portugal", "Portugal");
        countryMap.put("slovenia", "Slovenia");
        countryMap.put("spain", "Spain");
        countryMap.put("sweden", "Sweden");

        isoname = "EN";
        setCountryLanguage(countryMap, isoname);

        countryMap = new HashMap<String, String>();
        countryMap.put("belgium", "Bélgica");
        countryMap.put("finland", "Finlandia");
        countryMap.put("france", "Francia");
        countryMap.put("germany", "Alemania");
        countryMap.put("greece", "Grecia");
        countryMap.put("ireland", "Irlanda");
        countryMap.put("latvia", "Letonia");
        countryMap.put("malta", "Malta");
        countryMap.put("netherlands", "Países Bajos");
        countryMap.put("poland", "Polonia");
        countryMap.put("portugal", "Portugal");
        countryMap.put("slovenia", "Eslovenia");
        countryMap.put("spain", "España");
        countryMap.put("sweden", "Suecia");

        isoname = "ES";
        setCountryLanguage(countryMap, isoname);

        countryMap = new HashMap<String, String>();
        countryMap.put("belgium", "Belgia");
        countryMap.put("finland", "Suomi");
        countryMap.put("france", "Ranska");
        countryMap.put("germany", "Saksa");
        countryMap.put("greece", "Kreikka");
        countryMap.put("ireland", "Irlanti");
        countryMap.put("latvia", "Latvia");
        countryMap.put("malta", "Malta");
        countryMap.put("netherlands", "Alankomaat");
        countryMap.put("poland", "Puola");
        countryMap.put("portugal", "Portugali");
        countryMap.put("slovenia", "Slovenia");
        countryMap.put("spain", "Espanja");
        countryMap.put("sweden", "Ruotsi");

        isoname = "FI";
        setCountryLanguage(countryMap, isoname);

        countryMap = new HashMap<String, String>();
        countryMap.put("belgium", "Belgique");
        countryMap.put("finland", "Finlande");
        countryMap.put("france", "France");
        countryMap.put("germany", "Allemagne");
        countryMap.put("greece", "Grèce");
        countryMap.put("ireland", "Irlande");
        countryMap.put("latvia", "Lettonie");
        countryMap.put("malta", "Malta");
        countryMap.put("netherlands", "Pays-Bas");
        countryMap.put("poland", "Pologne");
        countryMap.put("portugal", "Portugal");
        countryMap.put("slovenia", "Slovénie");
        countryMap.put("spain", "Espagne");
        countryMap.put("sweden", "Suède");

        isoname = "FR";
        setCountryLanguage(countryMap, isoname);

        countryMap = new HashMap<String, String>();
        countryMap.put("belgium", "An Bheilg");
        countryMap.put("finland", "An Fhionlainn");
        countryMap.put("france", "An Fhrainc");
        countryMap.put("germany", "An Ghearmáin");
        countryMap.put("greece", "An Ghréig");
        countryMap.put("ireland", "Éire");
        countryMap.put("latvia", "An Laitvia");
        countryMap.put("malta", "Málta");
        countryMap.put("netherlands", "An Ísiltír");
        countryMap.put("poland", "An Pholainn");
        countryMap.put("portugal", "An Phortaingéil");
        countryMap.put("slovenia", "An tSlóivéin");
        countryMap.put("spain", "An Spáinn");
        countryMap.put("sweden", "An tSualainn");

        isoname = "GA";
        setCountryLanguage(countryMap, isoname);

        countryMap = new HashMap<String, String>();
        countryMap.put("belgium", "Beļģija");
        countryMap.put("finland", "Somija");
        countryMap.put("france", "Francija");
        countryMap.put("germany", "Vācija");
        countryMap.put("greece", "Grieķija");
        countryMap.put("ireland", "Īrija");
        countryMap.put("latvia", "Latvija");
        countryMap.put("malta", "Malta");
        countryMap.put("netherlands", "Nīderlande");
        countryMap.put("poland", "Polija");
        countryMap.put("portugal", "Portugāle");
        countryMap.put("slovenia", "Slovēnija");
        countryMap.put("spain", "Spānija");
        countryMap.put("sweden", "Zviedrija");

        isoname = "LV";
        setCountryLanguage(countryMap, isoname);

        countryMap = new HashMap<String, String>();
        countryMap.put("belgium", "Belġju");
        countryMap.put("finland", "Finlandja");
        countryMap.put("france", "Franza");
        countryMap.put("germany", "Ġermanja");
        countryMap.put("greece", "Greċja");
        countryMap.put("ireland", "Irlanda");
        countryMap.put("latvia", "Latvja");
        countryMap.put("malta", "Malta");
        countryMap.put("netherlands", "Olanda");
        countryMap.put("poland", "Polonja");
        countryMap.put("portugal", "Portugall");
        countryMap.put("slovenia", "Slovenja");
        countryMap.put("spain", "Spanja");
        countryMap.put("sweden", "Żvezja");

        isoname = "MT";
        setCountryLanguage(countryMap, isoname);

        countryMap = new HashMap<String, String>();
        countryMap.put("belgium", "België");
        countryMap.put("finland", "Finland");
        countryMap.put("france", "Frankrijk");
        countryMap.put("germany", "Duitsland");
        countryMap.put("greece", "Griekenland");
        countryMap.put("ireland", "Ierland");
        countryMap.put("latvia", "Letland");
        countryMap.put("malta", "Malta");
        countryMap.put("netherlands", "Nederland");
        countryMap.put("poland", "Polen");
        countryMap.put("portugal", "Portugal");
        countryMap.put("slovenia", "Slovenië");
        countryMap.put("spain", "Spanje");
        countryMap.put("sweden", "Zweden");

        isoname = "NL";
        setCountryLanguage(countryMap, isoname);

        countryMap = new HashMap<String, String>();
        countryMap.put("belgium", "Belgia");
        countryMap.put("finland", "Finlandia");
        countryMap.put("france", "Francja");
        countryMap.put("germany", "Niemcy");
        countryMap.put("greece", "Grecja");
        countryMap.put("ireland", "Irlandia");
        countryMap.put("latvia", "Łotwa");
        countryMap.put("malta", "Malta");
        countryMap.put("netherlands", "Holandia");
        countryMap.put("poland", "Polska");
        countryMap.put("portugal", "Portugalia");
        countryMap.put("slovenia", "Słowenia");
        countryMap.put("spain", "Hiszpania");
        countryMap.put("sweden", "Szwecja");

        isoname = "PL";
        setCountryLanguage(countryMap, isoname);

        countryMap = new HashMap<String, String>();
        countryMap.put("belgium", "Bélgica");
        countryMap.put("finland", "Finlândia");
        countryMap.put("france", "França");
        countryMap.put("germany", "Alemanha");
        countryMap.put("greece", "Grécia");
        countryMap.put("ireland", "Irlanda");
        countryMap.put("latvia", "Letónia");
        countryMap.put("malta", "Malta");
        countryMap.put("netherlands", "Países Baixos");
        countryMap.put("poland", "Polónia");
        countryMap.put("portugal", "Portugal");
        countryMap.put("slovenia", "Eslovénia");
        countryMap.put("spain", "Espanha");
        countryMap.put("sweden", "Suécia");

        isoname = "PT";
        setCountryLanguage(countryMap, isoname);

        countryMap = new HashMap<String, String>();
        countryMap.put("belgium", "Belgija");
        countryMap.put("finland", "Finska");
        countryMap.put("france", "Francija");
        countryMap.put("germany", "Nemčija");
        countryMap.put("greece", "Grčija");
        countryMap.put("ireland", "Irska");
        countryMap.put("latvia", "Latvija");
        countryMap.put("malta", "Malta");
        countryMap.put("netherlands", "Nizozemska");
        countryMap.put("poland", "Poljska");
        countryMap.put("portugal", "Portugalska");
        countryMap.put("slovenia", "Slovenija");
        countryMap.put("spain", "Španija");
        countryMap.put("sweden", "Švedska");

        isoname = "SL";
        setCountryLanguage(countryMap, isoname);

        countryMap = new HashMap<String, String>();
        countryMap.put("belgium", "Belgien");
        countryMap.put("finland", "Finland");
        countryMap.put("france", "Frankrike");
        countryMap.put("germany", "Tyskland");
        countryMap.put("greece", "Grekland");
        countryMap.put("ireland", "Irland");
        countryMap.put("latvia", "Lettland");
        countryMap.put("malta", "Malta");
        countryMap.put("netherlands", "Nederländerna");
        countryMap.put("poland", "Polen");
        countryMap.put("portugal", "Portugal");
        countryMap.put("slovenia", "Slovenien");
        countryMap.put("spain", "Spanien");
        countryMap.put("sweden", "Sverige");

        isoname = "SE";
        setCountryLanguage(countryMap, isoname);
    }

    private void setCountryLanguage(Map<String, String> anCountries, String isoname) throws APEnetException {
        try {
            LangDAO langDAO = new LangHibernateDAO();
            Lang lang = langDAO.getLangByIso2Name(isoname);
            List<Country> countries = new CountryHibernateDAO().findAll();
            CouAlternativeNameDAO couAlternativeNameDAO = new CouAlternativeNameHibernateDAO();
            HibernateUtil.beginDatabaseTransaction();
            for (Country country : countries) {
                String cname = country.getCname().toLowerCase();
                CouAlternativeName couAlternativeName = new CouAlternativeName();
                couAlternativeName.setCouAnName(anCountries.get(cname));
                couAlternativeName.setLang(lang);
                couAlternativeName.setCountry(country);
                couAlternativeNameDAO.insertSimple(couAlternativeName);
            }
            HibernateUtil.commitDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();
        } catch (Exception e){
            HibernateUtil.rollbackDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();
            throw new APEnetException(e);
        }
    }
}
