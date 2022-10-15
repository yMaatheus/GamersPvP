package net.gamerspvp.commons.network.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ListString {
	
	public static String listToString(List<String> list) {
		return list.toString().replace("[", "").replace("]", "");
	}
	
	public static String hashSetToString(HashSet<String> list) {
		return list.toString().replace("[", "").replace("]", "");
	}
	
	public static List<String> stringToList(String string) {
		if (string == null || string.equalsIgnoreCase("")) {
			return new ArrayList<>();
		}
		List<String> list = java.util.Arrays.asList(string.split(","));
		return list;
	}
	
	public static HashSet<String> stringToHashSet(String string) {
		List<String> list = Arrays.asList(string.split(","));
		return new HashSet<String>(list);
	}
}