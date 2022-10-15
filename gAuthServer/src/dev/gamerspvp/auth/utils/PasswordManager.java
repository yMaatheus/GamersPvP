package dev.gamerspvp.auth.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dev.gamerspvp.auth.auth.models.AuthPlayer.Password;
import net.gamerspvp.commons.network.utils.RandomStringUtils;

public class PasswordManager {
	
	public String computeHash(String password, String salt) {
		return "$SHA$" + salt + "$" + hash(hash(password) + salt);
	}
	
	public boolean comparePassword(String password, Password hashedPassword) {
		String hash = hashedPassword.getHash();
		String[] line = hash.split("\\$");
		return (line.length == 4 && isEqual(hash, computeHash(password, line[2])));
	}
	
	private boolean isEqual(String string1, String string2) {
		return MessageDigest.isEqual(string1.getBytes(StandardCharsets.UTF_8), string2.getBytes(StandardCharsets.UTF_8));
	}
	
	public String hash(String message) {
		try {
			MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
			algorithm.reset();
		    algorithm.update(message.getBytes());
		    byte[] digest = algorithm.digest();
		    return String.format("%0" + (digest.length << 1) + "x", new Object[] { new BigInteger(1, digest) });
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String generateSalt() {
		return RandomStringUtils.generateHex(getSaltLength());
	}
	
	private int getSaltLength() {
		return 16;
	}
}