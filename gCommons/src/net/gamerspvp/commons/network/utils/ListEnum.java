package net.gamerspvp.commons.network.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListEnum {
	
	public static <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String enumName) {
		try {
			Enum.valueOf(enumClass, enumName);
			return true;
		} catch (Throwable ex) {
			return false;
		}
	}
	
	public static <E extends Enum<E>> List<E> getEnumList(Class<E> enumClass) {
		return new ArrayList<E>(Arrays.asList(enumClass.getEnumConstants()));
	}
	
	public static <E extends Enum<E>> List<E> listToListEnum(Class<E> enumClass, List<String> input) {
		List<E> output = new ArrayList<>();
		for (String element : input) {
			output.add(Enum.valueOf(enumClass, element.toUpperCase()));
		}
		return output;
	}

}