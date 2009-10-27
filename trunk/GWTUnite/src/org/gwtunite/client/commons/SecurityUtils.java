package org.gwtunite.client.commons;

import com.google.gwt.user.client.Random;

public class SecurityUtils {
	private static final String passwordCharSet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";
	
	/**
	 * Generates a password of AlphaNumeric characters
	 * 
	 * @param length The length of password required
	 * @return A random password of alphanumeric characters
	 */
	public static String generatePassword(int length) {
		StringBuffer b = new StringBuffer(length);
		for (int i=0;i<length;i++) {
			b.append(passwordCharSet.charAt(Random.nextInt(passwordCharSet.length())));
		}
		
		return b.toString();
	};
}
