package net.gamerspvp.commons.bungee.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.network.models.GameStatus;
import net.gamerspvp.commons.network.models.GameStatus.Server;
import net.gamerspvp.commons.network.models.GameStatus.gameStatus;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LobbyCommand extends Command {
	
	private ProxiedCommons instance;
	
	public LobbyCommand(ProxiedCommons instance) {
		super("lobby", null, "hub", "saguao");
		this.instance = instance;
	}
	
	@Override
	public void execute(CommandSender sender, String[] arg) {
		if (!(sender instanceof ProxiedPlayer)) {
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) sender;
		GameStatus game = instance.getGameStatus("lobby");
		if (game.getServers().containsKey(player.getServer().getInfo().getName())) {
			sender.sendMessage(new TextComponent("§cVocê já está conectado a um Saguão."));
			return;
		}
		if (game.getStatus() != gameStatus.ONLINE) {
			player.sendMessage(new TextComponent("§cNo momento não é possivel enviar ninguém aos Saguões."));
			return;
		}
		List<Server> values = new ArrayList<Server>();
		values.addAll(game.getServers().values());
		Collections.sort(values, new Comparator<Server>() {
		    @Override
		    public int compare(Server pt1, Server pt2) {
		        Integer f1 = pt1.getOnline();
		        Integer f2 = pt2.getOnline();
		        return f1.compareTo(f2);
		    }
		});
		if (!sender.hasPermission("gamers.vip")) {
			boolean find = false;
			for (int a = 0; a < values.size(); a++) {
				Server server = values.get(a);
				if (server.getOnline() >= server.getMaxPlayers()) {
					continue;
				}
				find = true;
				player.connect(BungeeCord.getInstance().getServerInfo(server.getServerName()));
				break;
			}
			if (!find) {
				sender.sendMessage(new TextComponent("§cNão foi possivel efetuar o envio para nenhum de nossos Saguões pois estão lotados."));
			}
			return;
		}
		player.connect(BungeeCord.getInstance().getServerInfo(values.get(0).getServerName()));
		return;
	}
}