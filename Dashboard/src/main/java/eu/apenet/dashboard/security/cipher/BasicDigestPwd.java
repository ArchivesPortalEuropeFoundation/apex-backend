package eu.apenet.dashboard.security.cipher;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import eu.apenet.commons.exceptions.APEnetRuntimeException;

public class BasicDigestPwd {

	private static Logger log = Logger.getLogger(BasicDigestPwd.class);

	/*
	 * public static void main(String args[]) throws Exception { String eloy
	 * ="Eloy"; String jara ="Jara"; String paul ="Paul";
	 * 
	 * System.out.println("Eloy without replace: "+generateDigest(eloy));
	 * System.out.println("Eloy: "+replacePlus(generateDigest(eloy)));
	 * 
	 * System.out.println("Jara without replace: "+generateDigest(jara));
	 * System.out.println("Jara: "+replacePlus(generateDigest(jara)));
	 * 
	 * System.out.println("Paul without replace: "+generateDigest(paul));
	 * System.out.println("Paul: "+replacePlus(generateDigest(paul))); }
	 */

	public static String generateDigest(String plainText) {
		log.trace("BasicDigestPwd: generateDigest() method called");
		try {
			byte[] pwdBin = plainText.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("SHA1");
			byte[] digest = md.digest(pwdBin);
			String b64Digest = new String(Base64.encodeBase64(digest));
			log.trace("BasicDigestPwd: generateDigest() Digest: " + b64Digest);
			return b64Digest;
		} catch (Exception e) {
			throw new APEnetRuntimeException(e);
		}
	}

	public String generateLinkDigest(String plainText) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		log.trace("BasicDigestPwd: generateLinkDigest() method called");
		byte[] pwdBin = plainText.getBytes("UTF-8");
		MessageDigest md = MessageDigest.getInstance("SHA1");
		byte[] digest = md.digest(pwdBin);
		// String b64Digest = new String(Base64.encodeBase64(digest));
		String b64Digest = new String(Base64.encodeBase64(digest), "UTF-8");
		// int b64DigestToInt = Integer.parseInt(b64Digest);
		// String b64DigestHex = Integer.toHexString(b64DigestToInt);
		log.trace("BasicDigestPwd: generateDigest() Digest: " + replacePlus(b64Digest));
		// return b64Digest;
		return replacePlus(b64Digest);
	}

	public String replacePlus(String text) {
		text = text.replace('+', '=');
		return text;
	}

}
