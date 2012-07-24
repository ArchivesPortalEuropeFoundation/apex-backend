package eu.apenet.dashboard.help;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import eu.apenet.dashboard.security.cipher.BasicDigestPwd;
import eu.apenet.persistence.dao.UserStateDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateConfigurator;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.User;
import eu.apenet.persistence.vo.UserRole;
import eu.apenet.persistence.vo.UserState;

public class PopulateUsers {
	private static final String URL = "jdbc:postgresql:apenet-november";
	private static final String USERNAME = "apenet_dashboard";
	private static final String PASSWORD = "apenet";

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static void main(String[] args) throws UnsupportedEncodingException,
			NoSuchAlgorithmException {
		System.setProperty("db_url", URL);
		System.setProperty("db_username_dash", USERNAME);
		System.setProperty("db_pwd_dash", PASSWORD);
		HibernateConfigurator.getInstance().init("hibernate.cfg.xml", true);
//		// addUser("wim", "NETHERLANDS", "Nationaal Archief");
//		// addUser("lucile", "FRANCE", "Archives de France");
//		// addUser("kerstin", "GERMANY", "Bundesarchive");
//		// addUser("jara", "SPAIN", "Spain Archives");
//		// addUser("peder", "SWEDEN", "Swedish Archives");
//		addUsers();
		addGenerateSQL();

	}

	private static void addUsers() throws UnsupportedEncodingException,
			NoSuchAlgorithmException {
		List<Country> countries = DAOFactory.instance().getCountryDAO()
				.findAll();
		UserStateDAO userStateDAO = DAOFactory.instance().getUserStateDAO();
		UserState userState = userStateDAO.getUserStateByState("Actived");
		for (Country country : countries) {
			User partner = new User();
			BasicDigestPwd digestMaker = new BasicDigestPwd();
			String countryString = country.getCname().toLowerCase();
			countryString = countryString.substring(0, 1).toUpperCase()
					+ countryString.substring(1);
			partner.setFirstName(countryString);
			partner.setLastName("SUR" + countryString);
			partner.setPassword(digestMaker.generateDigest("test2010"));
			partner.setEmailAddress("bastiaan.verhoef@nationaalarchief.nl");
			// PartnerFacade partnetFacade = new PartnerFacade();

			// Country partnerCountry =
			// partnetFacade.getCountryByName(countryName);
			partner.setCountryId(country.getCouId());
			partner.setUserState(userState);
			partner.setSecretQuestion("Who started to build the Hadrian's Wall in AD 122?");
			partner.setSecretAnswer("Hadrian");
			partner.setUserRole(DAOFactory.instance().getUserRoleDAO()
					.getUserRole("countryManager"));
			User exitsUser = DAOFactory.instance().getUserDAO()
					.exitsEmailUser(partner.getEmailAddress());
			if (exitsUser == null) {
				DAOFactory.instance().getUserDAO().store(partner);
				// ArchivalInstitution institution = new ArchivalInstitution();
				// institution.setAiname(archive);
				// institution.setPartner(userToRegister);
				// //
				// DAOFactory.instance().getArchivalInstitutionDAO().store(institution);
				// String eagPath = APEnetUtilities.FILESEPARATOR +
				// userToRegister.getCountry().getIsoname() +
				// APEnetUtilities.FILESEPARATOR + institution.getAiId() +
				// APEnetUtilities.FILESEPARATOR + "EAG" +
				// APEnetUtilities.FILESEPARATOR + "eag.xml";
				// institution.setEagPath(eagPath);
				// DAOFactory.instance().getArchivalInstitutionDAO().store(institution);
			}
			System.out.println(partner.getEmailAddress());
		}

	}

	private static void addGenerateSQL() throws UnsupportedEncodingException,
			NoSuchAlgorithmException {
		List<Country> countries = DAOFactory.instance().getCountryDAO()
				.findAll();
		UserStateDAO userStateDAO = DAOFactory.instance().getUserStateDAO();
		UserState userState = userStateDAO.getUserStateByState("Actived");
		UserRole roleType = DAOFactory.instance().getUserRoleDAO().getUserRole("countryManager");
		for (Country country : countries) {
			User partner = new User();
			BasicDigestPwd digestMaker = new BasicDigestPwd();
			String countryString = country.getCname().toLowerCase();
			countryString = countryString.substring(0, 1).toUpperCase()
					+ countryString.substring(1);
			partner.setFirstName(countryString);
			partner.setLastName("SUR" + countryString);
			partner.setPassword(digestMaker.generateDigest("test2010"));
			partner.setEmailAddress("bastiaan.verhoef@nationaalarchief.nl");
			// PartnerFacade partnetFacade = new PartnerFacade();

			// Country partnerCountry =
			// partnetFacade.getCountryByName(countryName);
			partner.setCountryId(country.getCouId());
			partner.setUserState(userState);
			partner.setSecretQuestion("Who started to build the Hadrian's Wall in AD 122?");
			partner.setSecretAnswer("Hadrian");
			partner.setUserRole(DAOFactory.instance().getUserRoleDAO()
					.getUserRole("countryManager"));
			// p_id integer NOT NULL,
			// us_id integer NOT NULL,
			// nick character varying(60) NOT NULL,
			// email_address character varying(100) NOT NULL,
			// pwd character varying(60) NOT NULL,
			// cou_id integer NOT NULL,
			// secret_question character varying(200),
			// secret_answer character varying(200),
			// role_id integer NOT NULL,
			// p_name character varying(60) NOT NULL,
			// p_surname character varying(100) NOT NULL
			String output = "INSERT INTO partner (p_id, us_id, nick, email_address, pwd, cou_id, secret_question, secret_answer, role_id, p_name, p_surname) VALUES ("
					+ country.getCouId()
					+ ","
					+ userState.getUsId()
					+ ",'"
					+ country.getCname().toLowerCase()
					+ "',"
					+ "'bastiaan.verhoef@nationaalarchief.nl'" 
					+ ",'"
					+ digestMaker.generateDigest("test2010")
					+ "',"
					+ country.getCouId()
					+ ","					
					+ "'Who started to build the Hadrians Wall in AD 122?'"
					+ ","					
					+ "'Hadrian'"
					+ ","					
					+ roleType.getId()
					+ ",'"					
					+ countryString
					+ "','"					
					+ "SUR" + countryString				
					+ "');";
			// Partner exitsUser =
			// DAOFactory.instance().getPartnerDAO().exitsNickUser(partner.getNick());
			// if (exitsUser == null) {
			// DAOFactory.instance().getPartnerDAO().store(partner);
			// // ArchivalInstitution institution = new ArchivalInstitution();
			// // institution.setAiname(archive);
			// // institution.setPartner(userToRegister);
			// // //
			// //
			// DAOFactory.instance().getArchivalInstitutionDAO().store(institution);
			// // String eagPath = APEnetUtilities.FILESEPARATOR +
			// // userToRegister.getCountry().getIsoname() +
			// // APEnetUtilities.FILESEPARATOR + institution.getAiId() +
			// // APEnetUtilities.FILESEPARATOR + "EAG" +
			// // APEnetUtilities.FILESEPARATOR + "eag.xml";
			// // institution.setEagPath(eagPath);
			// //
			// DAOFactory.instance().getArchivalInstitutionDAO().store(institution);
			// }
			System.out.println(output);
		}

	}
}
