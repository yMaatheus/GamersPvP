package dev.gamerspvp.fullpvp.economy.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gamerspvp.fullpvp.Main;
import dev.gamerspvp.fullpvp.economy.EconomyManager;
import dev.gamerspvp.fullpvp.economy.models.PlayerMoney;

public class MoneyCommand extends Command {
	
	private Main instance;
	
	public MoneyCommand(Main instance) {
		super("money");
		this.instance = instance;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		Player player = null;
		EconomyManager economyManager = instance.getEconomyManager();
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		String senderName = sender.getName();
		if (args.length > 0) {
			if (args.length >= 2) {
				if ((args[0].equalsIgnoreCase("receive")) || (args[0].equalsIgnoreCase("receber"))) {
					if (player == null) {
						return false;
					}
					PlayerMoney playerMoney = economyManager.getCache(senderName.toLowerCase());
					if (playerMoney == null) {
						return false;
					}
					if (args[1].equalsIgnoreCase("on")) {
						if (playerMoney.isReciveMoney()) {
							sender.sendMessage("§cO recebimento de dinheiro já está ativo.");
							return true;
						}
						playerMoney.setReciveMoney(true);
						sender.sendMessage("§a[Economia] O recebimento de dinheiro foi ativado com sucesso.");
					} else if (args[1].equalsIgnoreCase("off")) {
						if (!(playerMoney.isReciveMoney())) {
							sender.sendMessage("§cO recebimento de dinheiro já está desativado.");
							return true;
						}
						playerMoney.setReciveMoney(false);
						sender.sendMessage("§a[Economia] O recebimento de dinheiro foi desativado com sucesso.");
					}
					playerMoney.update(instance);
					economyManager.putCache(playerMoney);
					return true;
				}
				if (args.length >= 3) {
					String target = args[1];
					String valor = args[2];
					PlayerMoney targetMoney = economyManager.getCache(target.toLowerCase());
					if (targetMoney == null) {
						sender.sendMessage("§cJogador inválido.");
						return false;
					}
					if (!(economyManager.isDouble(valor))) {
						sender.sendMessage("§cValor inválido.");
						return false;
					}
					double value = Double.valueOf(valor);
					if (args[0].equalsIgnoreCase("give")) {
						if (!(sender.hasPermission("economy.admin"))) {
							sender.sendMessage("§cSem permissão.");
							return false;
						}
						targetMoney.setMoney(targetMoney.getMoney() + value);
						targetMoney.update(instance);
						economyManager.putCache(targetMoney);
						String targetName = targetMoney.getPlayerName();
						String balanceAdicioned = economyManager.getBalance(value);
						sender.sendMessage("§a[Economia] Você adicionou §f" + balanceAdicioned + " §ade dinheiro para o jogador §f" + targetName);
					} else if (args[0].equalsIgnoreCase("remove")) {
						if (!(sender.hasPermission("economy.admin"))) {
							sender.sendMessage("§cSem permissão.");
							return false;
						}
						targetMoney.setMoney(targetMoney.getMoney() - value);
						targetMoney.update(instance);
						economyManager.putCache(targetMoney);
						String targetName = targetMoney.getPlayerName();
						String balanceRetired = economyManager.getBalance(value);
						sender.sendMessage("§a[Economia] Você removeu §f" + balanceRetired + " §ade dinheiro do jogador §f" + targetName);
					} else if (args[0].equalsIgnoreCase("set")) {
						if (!(sender.hasPermission("economy.admin"))) {
							sender.sendMessage("§cSem permissão.");
							return false;
						}
						targetMoney.setMoney(value);
						targetMoney.update(instance);
						economyManager.putCache(targetMoney);
						String targetName = targetMoney.getPlayerName();
						String balance = economyManager.getBalance(targetMoney);
						sender.sendMessage("§a[Economia] Você definiu o dinheiro de §f" + targetName + " §apara " + balance);
					} else if (args[0].equalsIgnoreCase("enviar") || args[0].equalsIgnoreCase("pay")) {
						if (player == null) {
							return false;
						}
						PlayerMoney playerMoney = economyManager.getCache(senderName.toLowerCase());
						if (playerMoney == null) {
							return false;
						}
						if (target.equalsIgnoreCase(player.getName())) {
							player.sendMessage("§cVocê não pode enviar dinheiro para você mesmo.");
							return false;
						}
						if (!(targetMoney.isReciveMoney())) {
							sender.sendMessage("§cO recebimento de dinheiro do jogador está desativado.");
							return false;
						}
						Player targetPlayer = Bukkit.getPlayer(target);
						if (targetPlayer == null) {
							player.sendMessage("§cJogador inválido.");
							return false;
						}
						if (playerMoney.getMoney() < value) {
							player.sendMessage("§cVocê não possui dinheiro suficiente.");
							return false;
						}
						playerMoney.setMoney(playerMoney.getMoney() - value);
						targetMoney.setMoney(targetMoney.getMoney() + value);
						playerMoney.update(instance);
						targetMoney.update(instance);
						economyManager.putCache(playerMoney);
						economyManager.putCache(targetMoney);
						String targetName = targetPlayer.getName();
						String balance = economyManager.getBalance(value);
						player.sendMessage("§a[Economia] Você enviou §f" + balance + " §ade dinheiro para §f" + targetName);
						targetPlayer.sendMessage("§a[Economia] Você recebeu §f" + balance + " §ade dinheiro do jogador §f" + senderName);
					}
					return true;
				}
				helpCommands(arg, sender);
				return true;
			}
			if (args[0].equalsIgnoreCase("top")) {
				sender.sendMessage("");
				sender.sendMessage("         §2§lTOP  RICOS         ");
				sender.sendMessage(" §7(Atualizado a cada 5 minutos) ");
				sender.sendMessage("");
				List<PlayerMoney> pm = economyManager.getTopList();
				for (int a = 0; a < pm.size(); a++) {
					PlayerMoney playerMoney = pm.get(a);
					int position = a + 1;
					String playerName = playerMoney.getPlayerName();
					String balance = economyManager.getBalance(playerMoney);
					sender.sendMessage(" §f" + position + "§7. §a" + playerName + " §7- " + balance);
				}
				sender.sendMessage("");
				return true;
			} else if ((args[0].equalsIgnoreCase("help")) || (args[0].equalsIgnoreCase("ajuda"))) {
				helpCommands(arg, sender);
				return true;
			}
			String target = args[0];
			PlayerMoney targetMoney = economyManager.getCache(target.toLowerCase());
			if (targetMoney == null) {
				return false;
			}
			String targetName = targetMoney.getPlayerName();
			String balance = economyManager.getBalance(targetMoney);
			sender.sendMessage("§a[Economia] §7O jogador §f" + targetName + " §7possui: " + balance);
			return true;
		}
		if (player == null) {
			helpCommands(arg, sender);
			return false;
		}
		PlayerMoney playerMoney = economyManager.getCache(senderName.toLowerCase());
		if (playerMoney == null) {
			return false;
		}
		sender.sendMessage("§a[Economia] §7Você possui: " + economyManager.getBalance(playerMoney));
		return true;
	}
	
	public void helpCommands(String arg, CommandSender sender) {
		sender.sendMessage("");
		sender.sendMessage("§a[Economia] Comandos disponíveis:");
		sender.sendMessage("§7/" + arg + "§a ajuda");
		sender.sendMessage("§7/" + arg + "§a (jogador)");
		sender.sendMessage("§7/" + arg + "§a enviar (jogador) (valor)");
		sender.sendMessage("§7/" + arg + "§a receber (on | off)");
		sender.sendMessage("§7/" + arg + "§a top");
		if (sender.hasPermission("economy.admin")) {
			sender.sendMessage("");
			sender.sendMessage("§7/" + arg + "§a give (jogador) (valor)");
			sender.sendMessage("§7/" + arg + "§a remove (jogador) (valor)");
			sender.sendMessage("§7/" + arg + "§a set (jogador) (valor)");
		}
		sender.sendMessage("");
	}
}