package com.quickcart.general;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESDecryptor {
	
	   public static String decryptPassword(String Password) throws Exception {
		   
		    String[] parts = Password.split("\\|");

		    String iv = parts[0];
			String encryptedData = parts[1];
			
	        // Decode Base64 encoded IV and encrypted data
	        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
	        byte[] ivBytes = Base64.getDecoder().decode(iv);

	        byte[] secretKey = new byte[]{
	        	    (byte) 42, (byte) 55, (byte) 234, (byte) 121, (byte) 133, (byte) 92, (byte) 13, (byte) 248,
	        	    (byte) 93, (byte) 163, (byte) 175, (byte) 120, (byte) 47, (byte) 188, (byte) 64, (byte) 233,
	        	    (byte) 19, (byte) 62, (byte) 123, (byte) 241, (byte) 95, (byte) 20, (byte) 184, (byte) 221,
	        	    (byte) 5, (byte) 123, (byte) 45, (byte) 67, (byte) 72, (byte) 95, (byte) 145, (byte) 23
	        	};
 // Same key used in FE
	        // Create an IvParameterSpec using the decoded IV
	        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

	        // Create a SecretKeySpec for the secret key
	        SecretKeySpec keySpec = new SecretKeySpec(secretKey, "AES");

	        // Initialize Cipher for AES/CBC/PKCS5Padding decryption
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

	        // Decrypt the encrypted bytes
	        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

	        // Convert decrypted bytes back to a string
	        return new String(decryptedBytes, "UTF-8");
	    }
}
