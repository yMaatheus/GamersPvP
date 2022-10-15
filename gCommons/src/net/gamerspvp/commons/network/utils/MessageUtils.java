package net.gamerspvp.commons.network.utils;

import lombok.Getter;

public enum MessageUtils {
	
	SERVER_PREFIX_TITLE("§2§lGAMERSPVP"), 
	SERVER_SITE("loja.gamerspvp.net"), 
	SERVER_IP("mc.gamerspvp.net"),
	SERVER_TWITTER("@GamersPvPServer"), 
	SERVER_DISCORD("https://discord.gg/GAdvjxv"),
	COMMAND_PERMISSION("§cVocê não possui permissão para executar esse comando."),
	PLAYER_NOT_BANCO("§cJogador não encontrado no banco de dados."),
	PLAYER_NOT_FOUND("§cJogador não encontrado no servidor.");
	
	@Getter
	private String message;

	private MessageUtils(String message) {
		this.message = message;
	}

	public enum StringManager {
		A("A", 0, 'A', 5), a("a", 1, 'a', 5), B("B", 2, 'B', 5), b("b", 3, 'b', 5), C("C", 4, 'C', 5),
		c("c", 5, 'c', 5), D("D", 6, 'D', 5), d("d", 7, 'd', 5), E("E", 8, 'E', 5), e("e", 9, 'e', 5),
		F("F", 10, 'F', 5), f("f", 11, 'f', 4), G("G", 12, 'G', 5), g("g", 13, 'g', 5), H("H", 14, 'H', 5),
		h("h", 15, 'h', 5), I("I", 16, 'I', 3), i("i", 17, 'i', 1), J("J", 18, 'J', 5), j("j", 19, 'j', 5),
		K("K", 20, 'K', 5), k("k", 21, 'k', 4), L("L", 22, 'L', 5), l("l", 23, 'l', 1), M("M", 24, 'M', 5),
		m("m", 25, 'm', 5), N("N", 26, 'N', 5), n("n", 27, 'n', 5), O("O", 28, 'O', 5), o("o", 29, 'o', 5),
		P("P", 30, 'P', 5), p("p", 31, 'p', 5), Q("Q", 32, 'Q', 5), q("q", 33, 'q', 5), R("R", 34, 'R', 5),
		r("r", 35, 'r', 5), S("S", 36, 'S', 5), s("s", 37, 's', 5), T("T", 38, 'T', 5), t("t", 39, 't', 4),
		U("U", 40, 'U', 5), u("u", 41, 'u', 5), V("V", 42, 'V', 5), v("v", 43, 'v', 5), W("W", 44, 'W', 5),
		w("w", 45, 'w', 5), X("X", 46, 'X', 5), x("x", 47, 'x', 5), Y("Y", 48, 'Y', 5), y("y", 49, 'y', 5),
		Z("Z", 50, 'Z', 5), z("z", 51, 'z', 5), NUM_1("NUM_1", 52, '1', 5), NUM_2("NUM_2", 53, '2', 5),
		NUM_3("NUM_3", 54, '3', 5), NUM_4("NUM_4", 55, '4', 5), NUM_5("NUM_5", 56, '5', 5), NUM_6("NUM_6", 57, '6', 5),
		NUM_7("NUM_7", 58, '7', 5), NUM_8("NUM_8", 59, '8', 5), NUM_9("NUM_9", 60, '9', 5), NUM_0("NUM_0", 61, '0', 5),
		EXCLAMATION_POINT("EXCLAMATION_POINT", 62, '!', 1), AT_SYMBOL("AT_SYMBOL", 63, '@', 6),
		NUM_SIGN("NUM_SIGN", 64, '#', 5), DOLLAR_SIGN("DOLLAR_SIGN", 65, '$', 5), PERCENT("PERCENT", 66, '%', 5),
		UP_ARROW("UP_ARROW", 67, '^', 5), AMPERSAND("AMPERSAND", 68, '&', 5), ASTERISK("ASTERISK", 69, '*', 5),
		LEFT_PARENTHESIS("LEFT_PARENTHESIS", 70, '(', 4), RIGHT_PERENTHESIS("RIGHT_PERENTHESIS", 71, ')', 4),
		MINUS("MINUS", 72, '-', 5), UNDERSCORE("UNDERSCORE", 73, '_', 5), PLUS_SIGN("PLUS_SIGN", 74, '+', 5),
		EQUALS_SIGN("EQUALS_SIGN", 75, '=', 5), LEFT_CURL_BRACE("LEFT_CURL_BRACE", 76, '{', 4),
		RIGHT_CURL_BRACE("RIGHT_CURL_BRACE", 77, '}', 4), LEFT_BRACKET("LEFT_BRACKET", 78, '[', 3),
		RIGHT_BRACKET("RIGHT_BRACKET", 79, ']', 3), COLON("COLON", 80, ':', 1), SEMI_COLON("SEMI_COLON", 81, ';', 1),
		DOUBLE_QUOTE("DOUBLE_QUOTE", 82, '\"', 3), SINGLE_QUOTE("SINGLE_QUOTE", 83, '\'', 1),
		LEFT_ARROW("LEFT_ARROW", 84, '<', 4), RIGHT_ARROW("RIGHT_ARROW", 85, '>', 4),
		QUESTION_MARK("QUESTION_MARK", 86, '?', 5), SLASH("SLASH", 87, '/', 5), BACK_SLASH("BACK_SLASH", 88, '\\', 5),
		LINE("LINE", 89, '|', 1), TILDE("TILDE", 90, '~', 5), TICK("TICK", 91, '`', 2), PERIOD("PERIOD", 92, '.', 1),
		COMMA("COMMA", 93, ',', 1), SPACE("SPACE", 94, ' ', 3), DEFAULT("DEFAULT", 95, 'a', 4),
		COLORCODE("COLORCODE", 96, '§', 0);

		private char character;
		private int length;

		private StringManager(String s, int n, char character, int length) {
			this.character = character;
			this.length = length;
		}

		public char getCharacter() {
			return this.character;
		}

		public int getLength() {
			return this.length;
		}

		public int getBoldLength() {
			if (this == SPACE) {
				return this.getLength();
			}
			return this.length + 1;
		}

		public static StringManager getDefaultFontInfo(char c) {
			for (StringManager alues : values()) {
				if (alues.getCharacter() == c) {
					return alues;
				}
			}
			return DEFAULT;
		}

		public static String centeredMotd(String message) {
			int messagePxSize = 0;
			boolean previousCode = false;
			boolean isBold = false;
			for (char arrayOfChar : message.toCharArray()) {
				if (arrayOfChar == '§') {
					previousCode = true;
				} else if (previousCode) {
					previousCode = false;
					isBold = (arrayOfChar == 'l' || arrayOfChar == 'L');
				} else {
					StringManager dFI = getDefaultFontInfo(arrayOfChar);
					messagePxSize += (isBold ? dFI.getBoldLength() : dFI.getLength());
					++messagePxSize;
				}
			}
			int halvedMessageSize = messagePxSize / 2;
			int toCompensate = 132 - halvedMessageSize;
			int spaceLength = SPACE.getLength() + 1;
			int compensated = 0;
			StringBuilder sb = new StringBuilder();
			while (compensated < toCompensate) {
				sb.append(" ");
				compensated += spaceLength;
			}
			return String.valueOf(sb.toString()) + message;
		}

		public static String makeCenteredMessage(String message) {
			int messagePxSize = 0;
			boolean previousCode = false;
			boolean isBold = false;
			for (char c : message.toCharArray()) {
				if (c == '§') {
					previousCode = true;
				} else if (previousCode) {
					previousCode = false;
					isBold = (c == 'l' || c == 'L');
				} else {
					StringManager dFI = getDefaultFontInfo(c);
					messagePxSize += (isBold ? dFI.getBoldLength() : dFI.getLength());
					++messagePxSize;
				}
			}
			int halvedMessageSize = messagePxSize / 2;
			int toCompensate = 154 - halvedMessageSize;
			int spaceLength = SPACE.getLength() + 1;
			int compensated = 0;
			StringBuilder sb = new StringBuilder();
			while (compensated < toCompensate) {
				sb.append(" ");
				compensated += spaceLength;
			}
			return String.valueOf(sb.toString()) + message;
		}

		public static String chatOrganizarTag(String s) {
			if (s == null || s.trim() == "") {
				return null;
			}
			String[] parts = s.split(" ");
			if (parts.length == 2) {
				String nick = parts[0];
				String rank = parts[1];

				return rank.replace("[", "(").replace("]", ")") + " " + nick;
			} else if (parts.length == 3) {
				String grupo = parts[0];
				String nick = parts[1];
				String rank = parts[2];

				return rank.replace("[", "(").replace("]", ")") + " " + grupo + " " + nick;
			} else if (parts.length == 4) {
				String icone = parts[0];
				String grupo = parts[1];
				String nick = parts[2];
				String rank = parts[3];

				return icone + " " + rank.replace("[", "(").replace("]", ")") + " " + grupo + " " + nick;

			}
			return null;
		}

	}

	public static String getStringReplaceLinhas(String linha) {
		String newLinha = linha;
		if (newLinha.contains("&"))
			newLinha = newLinha.replaceAll("&", "§");
		if (newLinha.contains("%aagudo%"))
			newLinha = newLinha.replaceAll("%aagudo%", "á");
		if (newLinha.contains("%agrave%"))
			newLinha = newLinha.replaceAll("%agrave%", "à");
		if (newLinha.contains("%acirc%"))
			newLinha = newLinha.replaceAll("%acirc%", "â");
		if (newLinha.contains("%auml%"))
			newLinha = newLinha.replaceAll("%auml%", "ã");
		if (newLinha.contains("%ccedil%"))
			newLinha = newLinha.replaceAll("%ccedil%", "ç");
		if (newLinha.contains("%egrave%"))
			newLinha = newLinha.replaceAll("%egrave%", "è");
		if (newLinha.contains("%eacute%"))
			newLinha = newLinha.replaceAll("%eacute%", "é");
		if (newLinha.contains("%ecirc%"))
			newLinha = newLinha.replaceAll("%ecirc%", "ê");
		if (newLinha.contains("%euml%"))
			newLinha = newLinha.replaceAll("%euml%", "ë");
		if (newLinha.contains("%icirc%"))
			newLinha = newLinha.replaceAll("%icirc%", "î");
		if (newLinha.contains("%iuml%"))
			newLinha = newLinha.replaceAll("%iuml%", "ï");
		if (newLinha.contains("%ocirc%"))
			newLinha = newLinha.replaceAll("%ocirc%", "ô");
		if (newLinha.contains("%ouml%"))
			newLinha = newLinha.replaceAll("%ouml%", "õ");
		if (newLinha.contains("%ugrave%"))
			newLinha = newLinha.replaceAll("%ugrave%", "ù");
		if (newLinha.contains("%ucirc%"))
			newLinha = newLinha.replaceAll("%ucirc%", "û");
		if (newLinha.contains("%uuml%"))
			newLinha = newLinha.replaceAll("%uuml%", "ü");
		return newLinha;
	}

	public static String setStringReplaceLinhas(String linha) {
		String newLinha = linha;
		if (newLinha.contains("§"))
			newLinha = newLinha.replaceAll("§", "&");
		if (newLinha.contains("à"))
			newLinha = newLinha.replaceAll("à", "%agrave%");
		if (newLinha.contains("â"))
			newLinha = newLinha.replaceAll("â", "%acirc%");
		if (newLinha.contains("ã"))
			newLinha = newLinha.replaceAll("ã", "%auml%");
		if (newLinha.contains("ç"))
			newLinha = newLinha.replaceAll("ç", "%ccedil%");
		if (newLinha.contains("è"))
			newLinha = newLinha.replaceAll("è", "%egrave%");
		if (newLinha.contains("é"))
			newLinha = newLinha.replaceAll("é", "%eacute%");
		if (newLinha.contains("ê"))
			newLinha = newLinha.replaceAll("ê", "%ecirc%");
		if (newLinha.contains("ë"))
			newLinha = newLinha.replaceAll("ë", "%euml%");
		if (newLinha.contains("î"))
			newLinha = newLinha.replaceAll("î", "%icirc%");
		if (newLinha.contains("ï"))
			newLinha = newLinha.replaceAll("ï", "%iuml%");
		if (newLinha.contains("ô"))
			newLinha = newLinha.replaceAll("ô", "%ocirc%");
		if (newLinha.contains("õ"))
			newLinha = newLinha.replaceAll("õ", "%ouml%");
		if (newLinha.contains("ù"))
			newLinha = newLinha.replaceAll("ù", "%ugrave%");
		if (newLinha.contains("û"))
			newLinha = newLinha.replaceAll("û", "%ucirc%");
		if (newLinha.contains("ü"))
			newLinha = newLinha.replaceAll("ü", "%uuml%");
		return newLinha;
	}
}