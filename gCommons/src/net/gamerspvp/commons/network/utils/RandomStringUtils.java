package net.gamerspvp.commons.network.utils;

import java.security.SecureRandom;
import java.util.Random;

public final class RandomStringUtils {
	
	private static final char[] CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	
	private static final Random RANDOM = new SecureRandom();
	
	public static String generate(int length) {
		return generateString(length, 36);
	}
	
	public static String generateHex(int length) {
		return generateString(length, 16);
	}
	
	public static String generateNum(int length) {
		return generateString(length, 10);
	}
	
	public static String generateLowerUpper(int length) {
		return generateString(length, CHARS.length);
	}
	
	private static String generateString(int length, int maxIndex) {
		if (length < 0) {
			throw new IllegalArgumentException("Length must be positive but was " + length);
		}
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(CHARS[RANDOM.nextInt(maxIndex)]);
		}
		return sb.toString();
	}
}