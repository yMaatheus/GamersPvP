package net.gamerspvp.commons.network.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MoneyFormater {
	
	private static String formatValue(double value) {
		boolean isWholeNumber = value == Math.round(value);
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
		formatSymbols.setDecimalSeparator('.');
		String pattern = isWholeNumber ? "###,###.###" : "###,##0.00";
		DecimalFormat df = new DecimalFormat(pattern, formatSymbols);
		return df.format(value);
	}
	
	public static String format(double amount) {
		amount = getMoneyRounded(amount);
		String suffix = " ";
		if (amount > 0.00 && amount < 1.0) {
			if (amount == 0.01) {
				suffix += "";
			} else if (amount < 1.0) {
				suffix += "";
			}
			amount *= 100;
		} else if (amount == 1.0) {
			suffix += "";
		} else {
			suffix += "";
		}
		if (suffix.equalsIgnoreCase(" ")) {
			suffix = "";
		}
		return formatValue(amount);
	}
	
	public static double getMoneyRounded(double amount) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		String formattedAmount = twoDForm.format(amount);
		formattedAmount = formattedAmount.replace(",", ".");
		return Double.valueOf(formattedAmount);
	}
}