package eu.apenet.dashboard.infraestructure;

import java.util.Random;

public class PasswordGenerator {

	private static final int DEFAULT_PASSWORD_LENGTH = 15;

	public static final String SPECIAL_CHARSET = "~!@#$%^&*_-+=`|\\(){}[]:;\"\'<>,.?/";
	public static final String LOWERCASE_CHARSET = "abcdefghijklmnopqrstuvwxyz";
	public static final String UPPER_CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String DIGIT_CHARSET = "0123456789";
	public static final String CHARSET = DIGIT_CHARSET + LOWERCASE_CHARSET + UPPER_CHARSET + SPECIAL_CHARSET;
	public static String getRandomString() {
		return getRandomString(DIGIT_CHARSET + LOWERCASE_CHARSET + UPPER_CHARSET, DEFAULT_PASSWORD_LENGTH);
	}
	public static String getRandomString(String charset, int length) {
		Random rand = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int pos = rand.nextInt(charset.length());
			sb.append(charset.charAt(pos));
		}
		try {
			// if you generate more than 1 time, you must
			// put the process to sleep for awhile
			// otherwise it will return the same random string
			Thread.sleep(100);
		} catch (InterruptedException e) {
			
		}		
		return sb.toString();
	}

}
