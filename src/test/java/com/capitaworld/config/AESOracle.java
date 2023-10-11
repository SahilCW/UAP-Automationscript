package com.capitaworld.config;



import org.apache.commons.codec.binary.Hex;

import java.nio.charset.*;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;


 

public class AESOracle implements AttributeConverter<String, String> {

	private static final String ALGORITHM = "AES";
	private static final String KEY = "C@p!ta@W0rld#U$d";
	private static final String SECRET = "26f1ac75f77c22ebc66e2359c13ea9955ebd5e2bd7fbe50e5b3ac2977a772302";

    /*public String encrypt(String data) throws Exception {
		byte[] keyBytes = Arrays.copyOf(SECRET.getBytes("ASCII"), 16);

		SecretKey key = new SecretKeySpec(keyBytes, ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);

		byte[] cleartext = data.getBytes("UTF-8");
		byte[] ciphertextBytes = cipher.doFinal(cleartext);

		return new String(Hex.encodeHex(ciphertextBytes));
    }

    public String decrypt(String encryptedData) throws Exception {

		byte[] keyBytes = Arrays.copyOf(SECRET.getBytes("ASCII"), 16);

		SecretKey key = new SecretKeySpec(keyBytes, ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);

		return new String(cipher.doFinal(Hex.decodeHex(encryptedData.toCharArray())));
    }
*/

	public String decriptWithKey(String encryptedText) {
		// do some decryption
		try {
				byte[] keyBytes = Arrays.copyOf(KEY.getBytes("ASCII"), 16);

				SecretKey key = new SecretKeySpec(keyBytes, ALGORITHM);
				Cipher cipher = Cipher.getInstance(ALGORITHM);
				cipher.init(Cipher.DECRYPT_MODE, key);

				// byte[] ciphertextBytes = cipher.doFinal(cleartext);

				return new String(cipher.doFinal(Hex.decodeHex(encryptedText.toCharArray())));
		} catch (Exception e) {
		}
		return null;
	}

	public String encryptionWithKey(String plainText) {
		// do some encryption
		try {
			byte[] keyBytes = Arrays.copyOf(KEY.getBytes("ASCII"), 16);

			SecretKey key = new SecretKeySpec(keyBytes, ALGORITHM);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);

			byte[] cleartext = plainText.getBytes("UTF-8");
			byte[] ciphertextBytes = cipher.doFinal(cleartext);

			return new String(Hex.encodeHex(ciphertextBytes));
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public String convertToDatabaseColumn(String attribute) {

		if (attribute != null) {
			try {
				return encryptionWithKey(attribute.toLowerCase());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		if(dbData!= null) {
			try {
				return decriptWithKey(dbData);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

//	public static void main(String[] args) throws Exception {
//		String s ="RH1WjbYSi27t3EQPMJ/DQLcqBC7/KnboV/h6KkNkzI8=";
//		System.out.println(decrypt(s));
//	}
}