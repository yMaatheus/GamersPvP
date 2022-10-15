package net.gamerspvp.commons.network.log;

import lombok.Getter;
import net.gamerspvp.commons.network.database.DataCenterManager;

public class LogManager {
	
	@Getter
	private static LogController controller;
	
	public LogManager(DataCenterManager dataCenter) throws Exception {
		controller = new LogController(dataCenter);
	}
	
	/*
	 * Key: Identificador do tipo de log
	 * Author/PlayerName: Jogador que efetuou a ação
	 * Player2: Jogador que sofreu a ação (Opcional)
	 * Text: Mensagem que ficará na log
	 * Reason: Justificativa do author (Opcional)
	 * 
	 */
	
	public static boolean log(String key, String playerName, String text, String reason) throws Exception {
		if (playerName.equalsIgnoreCase("CONSOLE")) {
			return false;
		}
		return controller.executeLog(key, playerName, text, reason);
	}
	
	public static boolean log(String key, String author, String player2, String text, String reason) throws Exception {
		if (author.equalsIgnoreCase("CONSOLE") || player2.equalsIgnoreCase("CONSOLE")) {
			return false;
		}
		return controller.executeLog(key, author, text, reason) && controller.executeLog(key, player2, text, reason);
	}
	
	public static boolean log(String key, String playerName, String text) throws Exception {
		return log(key, playerName, text, "");
	}
	
	public static boolean log(Log log) throws Exception {
		if (log.getPlayerName().equalsIgnoreCase("CONSOLE")) {
			return false;
		}
		return controller.executeLog(log.getKey(), log.getPlayerName(), log.getText(), log.getReason());
	}
}