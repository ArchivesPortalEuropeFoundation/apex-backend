package eu.apenet.dashboard.contentchecker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import eu.apenet.dashboard.infraestructure.PasswordGenerator;
import eu.apenet.dashboard.security.cipher.BasicDigestPwd;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.dao.UserStateDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.UserRole;
import eu.apenet.persistence.vo.UserState;
import eu.archivesportaleurope.database.mock.DatabaseConfigurator;

public class GenerateUsers {
	private static final String URL = "jdbc:postgresql:apenet-november";
	private static final String USERNAME = "apenet_dashboard";
	private static final String PASSWORD = "apenet";
	private static final String SEPARATOR = ";";

	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, NoSuchAlgorithmException, NamingException {
		if (args == null || args.length != 1) {
			System.err.println("CSV file needed");
			System.exit(1);
		}
		DatabaseConfigurator.getInstance().init();

		UserStateDAO userStateDAO = DAOFactory.instance().getUserStateDAO();
		UserState userState = userStateDAO.getUserStateByState("Actived");
		UserRole roleType = DAOFactory.instance().getUserRoleDAO().getUserRole("countryManager");
		CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
		List<SimplePartner> partners = new ArrayList<SimplePartner>();
		File csvFile = new File(args[0]);
		Reader r = new InputStreamReader(new FileInputStream(csvFile), "UTF-8");
		BufferedReader input = new BufferedReader(r);
		boolean firstLine = true;
		//int idGeneration = 1;
		try {
			String line = null; // not declared within while loop
			while ((line = input.readLine()) != null) {
				line = line.trim();
				if (!firstLine && line.length() >= 3) {
					String[] lineArray = line.split(SEPARATOR);
					String countryName = lineArray[0].trim().toUpperCase();
					String email = lineArray[1].trim();
					String fullName = lineArray[2].trim();
					int firstWhitespace = fullName.indexOf(" ");
					String firstName = fullName.substring(0, firstWhitespace);
					String lastName = fullName.substring(firstWhitespace + 1);
					Country country = countryDAO.getCountryByCname(countryName);
					if (country == null) {
						System.err.println(countryName + " does not exist.");
					} else {
						SimplePartner partner = new SimplePartner();
						partner.id = country.getCouId();
						partner.countryId = country.getCouId();
						partner.userStateId = userState.getUsId();
						partner.username = countryName.toLowerCase();
						partner.emailaddress = email;
						partner.password = PasswordGenerator.getRandomString();
						partner.secretQuestion = "Sorry the answers is also a kind of a password";
						partner.secretAnswer = PasswordGenerator.getRandomString();
						partner.roleId = roleType.getId();
						partner.firstName = firstName;
						partner.lastName = lastName;
						partners.add(partner);
					}
					//idGeneration++;
				} else {
					firstLine = false;
				}

			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		for (SimplePartner partner : partners){
			System.out.println(partner.toSQLString());
		}
		for (SimplePartner partner : partners){
			System.out.println(partner.toCSVSTring());
		}
	}

	static class SimplePartner {
		private int id;
		private int countryId;
		private long userStateId;
		private String username;
		private String emailaddress;
		private String password;
		private String secretQuestion;
		private String secretAnswer;
		private int roleId;
		private String firstName;
		private String lastName;
		private String toSQLString() throws UnsupportedEncodingException, NoSuchAlgorithmException{
			BasicDigestPwd digestMaker = new BasicDigestPwd();
			String output = "INSERT INTO partner (p_id, us_id, nick, email_address, pwd, cou_id, secret_question, secret_answer, role_id, p_name, p_surname) VALUES ("
					+ id
					+ ","
					+ userStateId
					+ ",'"
					+ username
					+ "','"
					+ emailaddress
					+ "','"
					+ digestMaker.generateDigest(password)
					+ "',"
					+ countryId
					+ ",'"					
					+ secretQuestion
					+ "','"					
					+ secretAnswer
					+ "',"					
					+ roleId
					+ ",'"					
					+ firstName
					+ "','"					
					+ lastName				
					+ "');";
			return output;
		}
		private String toCSVSTring(){
			return username + ";" + password + ";" + firstName+ ";" + lastName + ";" + secretQuestion + ";" + secretAnswer;
		}
	}
}
