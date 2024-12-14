package mypackage;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AppSecurity {
	/*protected */ public static String encript(String message) throws NoSuchAlgorithmException{
		String temp = encriptMD5(message);
		String encriptedPassword = encriptSHA256(temp);
		return encriptedPassword;
	}
	protected static String encriptMD5(String password) throws NoSuchAlgorithmException{
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(password.getBytes());
		byte[] bytes = m.digest();
		StringBuilder s = new StringBuilder();
		for(int i = 0; i<bytes.length; i++) {
			s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			//converting to hexadecimal
		}
		return s.toString();
	}
	protected static byte[] getSHA(String input) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		return md.digest(input.getBytes(StandardCharsets.UTF_8));
		
	}
	protected static String toHexString(byte[] hash) {
		BigInteger number = new BigInteger(1,hash);
		StringBuilder hexString = new StringBuilder(number.toString(16));
		while(hexString.length()<0) {
			hexString.insert(0, '0');
			
		}
		return hexString.toString();
		
	}
	protected static String encriptSHA256(String password) throws NoSuchAlgorithmException {
		return toHexString(getSHA(password))+"";
	}

}
//finished