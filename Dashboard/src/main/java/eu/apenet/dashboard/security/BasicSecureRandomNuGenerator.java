package eu.apenet.dashboard.security;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import org.apache.log4j.Logger;


public class BasicSecureRandomNuGenerator {

	private static Logger log = Logger.getLogger(BasicSecureRandomNuGenerator.class);
	private static Integer SEED = 200;
	private static final Random RANDOM = new Random();

	static final String HEXES = "0123456789ABCDEF";
	
	public static void main (String[] args) throws NoSuchAlgorithmException{
		
		BasicSecureRandomNuGenerator bSrNG= new BasicSecureRandomNuGenerator();
		log.info("Test of 20 random numbers");
		for (int i=0; i<20; i++){
			log.info("Random number"+"["+i+"]"+bSrNG.generateRandomString());
			log.info("Random number"+"["+i+"]"+bSrNG.generateToken());
			log.info("Random number"+"["+i+"]"+bSrNG.generateSecureRandomToken());
		}
	}
	
	public  String generateToken(){
		log.info("BasicSecureRandomNuGenerator: generateToken() method called");
		return new BigInteger(165, RANDOM).toString(36).toUpperCase();
	}
	
	
	public  String generateSecureRandomToken() throws NoSuchAlgorithmException{
		log.info("BasicSecureRandomNuGenerator: generateSecureRandomToken() method called");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(200);
		return new BigInteger(165, RANDOM).toString(36).toUpperCase();
	}
		
	public  String generateRandomString() {
		String randomStringNumber = null;
		try{
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(200);
		byte bytes[] = new byte [20];
		random.nextBytes(bytes);
		/*
		for (int i=0; i < bytes.length; i++){
			System.out.println("("+bytes[i]+")");
		}*/
		
		randomStringNumber = getHexString(bytes);
		
		} catch (NoSuchAlgorithmException e){
			log.error(e);
		} catch (Exception e) {
			log.error(e);
		}
		return randomStringNumber;
	}
	
	public String IntToHexString(int var) throws Exception {
		return Integer.toHexString(var);
	}
	

	public String getHexString(byte[] b) throws Exception {
		  String result = "";
		  for (int i=0; i < b.length; i++) {
		    result +=
		          Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		  }
		  return result;
		}

	public String getHex( byte [] raw ) {
	    if ( raw == null ) {
	      return null;
	    }
	    final StringBuilder hex = new StringBuilder( 2 * raw.length );
	    for ( final byte b : raw ) {
	      hex.append(HEXES.charAt((b & 0xF0) >> 4))
	         .append(HEXES.charAt((b & 0x0F)));
	    }
	    return hex.toString();
	  }
	
}
	
	
	
