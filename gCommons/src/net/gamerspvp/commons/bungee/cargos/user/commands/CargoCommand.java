package net.gamerspvp.commons.bungee.cargos.user.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.cargos.ProxiedCargosManager;
import net.gamerspvp.commons.bungee.cargos.group.ProxiedGroupManager;
import net.gamerspvp.commons.bungee.cargos.user.ProxiedUserManager;
import net.gamerspvp.commons.network.CargosController;
import net.gamerspvp.commons.network.log.LogManager;
import net.gamerspvp.commons.network.models.Group;
import net.gamerspvp.commons.network.models.User;
import net.gamerspvp.commons.network.utils.DateUtils;
import net.gamerspvp.commons.network.utils.MessageUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CargoCommand extends Command {
	
	public CargoCommand(ProxiedCommons commons) {
		super("cargo", "gamers.coordenador", "cargos", "group");
		commons.getProxy().getPluginManager().registerCommand(commons, this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender.hasPermission("gamers.admin"))) {
			sender.sendMessage(new TextComponent(MessageUtils.COMMAND_PERMISSION.getMessage()));
			return;
		}
		boolean diretor = sender.hasPermission("gamers.diretor");
		if (args.length > 0) {
			try {
				CargosController databaseController = ProxiedCargosManager.getDatabaseController();
				ProxiedUserManager proxiedUserManager = ProxiedCargosManager.getUserManager();
				ProxiedGroupManager proxiedGroupManager = ProxiedCargosManager.getGroupManager();
				if (args[0].equalsIgnoreCase("listar") || args[0].equalsIgnoreCase("list")) {
					List<Group> values = new ArrayList<Group>();
					values.addAll(proxiedGroupManager.getGroups().values());
					Collections.sort(values, new Comparator<Group>() {
					    @Override
					    public int compare(Group pt1, Group pt2) {
					        Integer f1 = pt1.getRank();
					        Integer f2 = pt2.getRank();
					        return f1.compareTo(f2);
					    }
					});
					sender.sendMessage(new TextComponent(""));
					sender.sendMessage(new TextComponent("§a[Cargos] Lista de grupos: "));
					for (int a = 0; a < values.size(); a++) {
						Group group = values.get(a);
						sender.sendMessage(new TextComponent("§7" + (a + 1) + "§8.§r " + group.getPreffix() + "§f(" + group.getName() + ")"));
					}
					return;
				} else if (args.length > 1 && (args[0].equalsIgnoreCase("purge") || args[0].equalsIgnoreCase("deletar"))) {
					if (!(diretor)) {
						sender.sendMessage(new TextComponent(MessageUtils.COMMAND_PERMISSION.getMessage()));
						return;
					}
					String userName = args[1];
					proxiedUserManager.purgeUser(userName);
					sender.sendMessage(new TextComponent("§aO usu§rio §f" + userName + " §afoi totalmente deletado do banco de dados de cargos."));
					return;
				} else if (args.length > 1 && (args[0].equalsIgnoreCase("profile") || args[0].equalsIgnoreCase("perfil"))) {
					String userName = args[1];
					User user = databaseController.getUser(userName);
					if (user == null) {
						sender.sendMessage(new TextComponent(MessageUtils.PLAYER_NOT_BANCO.getMessage()));
						return;
					}
					sender.sendMessage(new TextComponent(""));
					sender.sendMessage(new TextComponent("§a[Cargos] Informa§§es do jogador:"));
					sender.sendMessage(new TextComponent(""));
					sender.sendMessage(new TextComponent("§7* §aNome§8:§f " + user.getPlayerName()));
					sender.sendMessage(new TextComponent("§7* §aGrupo§8:§f " + user.getGroup()));
					sender.sendMessage(new TextComponent("§7* §aPrimeiro Login§8:§f " + DateUtils.longToDate(user.getFirstLogin())));
					sender.sendMessage(new TextComponent("§7* §a§ltimo Login§8:§f " + DateUtils.longToDate(user.getLastLogin())));
					return;
				} else if (args.length > 1 && args[0].equalsIgnoreCase("info")) {
					String groupName = args[1].substring(0,1).toUpperCase().concat(args[1].toLowerCase().substring(1));
					Group group = proxiedGroupManager.getGroup(groupName);
					if (group == null) {
						sender.sendMessage(new TextComponent("§cO grupo informado n§o foi encontrado."));
						return;
					}
					sender.sendMessage(new TextComponent(""));
					sender.sendMessage(new TextComponent("§a[Cargos] Informa§§es do Grupo:"));
					sender.sendMessage(new TextComponent(""));
					sender.sendMessage(new TextComponent("§7* §aNome§8:§f " + group.getName()));
					sender.sendMessage(new TextComponent("§7* §aPrefixo§8:§f " + group.getPreffix()));
					sender.sendMessage(new TextComponent("§7* §aRank§8:§f " + group.getRank()));
					sender.sendMessage(new TextComponent("§7* §aPermissions§8: "));
					for (String permission : group.getPermissions()) {
						sender.sendMessage(new TextComponent("  §7-§f " + permission));
					}
					return;
				} else if (args.length > 1 && args[0].equalsIgnoreCase("users")) {
					String groupName = args[1].substring(0,1).toUpperCase().concat(args[1].toLowerCase().substring(1));
					HashSet<User> users = databaseController.searchGroupUsers(groupName);
					if (users.isEmpty()) {
						sender.sendMessage(new TextComponent("§cN§o foi encontrado nenhuma informa§§o no banco de dados."));
						return;
					}
					if (users.size() > 150) {
						sender.sendMessage(new TextComponent("§aA lista de usu§rios § muito extensa, devido a isso ser§ informado apenas a quantidade total a qual §: §f" + users.size()));
						return;
					}
					sender.sendMessage(new TextComponent(""));
					sender.sendMessage(new TextComponent("§a[Cargos] Lista de usu§rios do grupo " + groupName + ":"));
					int i = 0;
					for (User user : users) {
						i++;
						sender.sendMessage(new TextComponent("§a" + i + "§8. §f" + user.getPlayerName() + " §8- §7§ltimo Login§8: §a" + DateUtils.longToDate(user.getLastLogin())));
					}
					return;
				} else if (args.length > 2 && (args[0].equalsIgnoreCase("definir") || args[0].equalsIgnoreCase("set"))) {
					String userName = args[1];
					String groupName = args[2].substring(0,1).toUpperCase().concat(args[2].toLowerCase().substring(1));
					Group group = proxiedGroupManager.getGroup(groupName);
					if (group == null) {
						sender.sendMessage(new TextComponent("§cO grupo informado n§o foi encontrado."));
						return;
					}
					if (sender instanceof ProxiedPlayer) {
						User senderUser = databaseController.getUser(sender.getName());
						Group senderGroup = proxiedGroupManager.getGroup(senderUser.getGroup());
						if (senderGroup != null && senderGroup.getRank() > group.getRank()) {
							sender.sendMessage(new TextComponent("§cVoc§ n§o tem autoriza§§o para definir um jogador ao grupo."));
							return;
						}
						User user = databaseController.getUser(userName);
						if (user != null) {
							Group userGroup = proxiedGroupManager.getGroup(user.getGroup());
							if (userGroup != null && senderGroup.getRank() >= userGroup.getRank()) {
								sender.sendMessage(new TextComponent("§cVoc§ n§o tem autoriza§§o para definir um grupo ao jogador."));
								return;
							}
						}
					}
					proxiedUserManager.defineUserGroup(userName, groupName);
					LogManager.log("Cargos", sender.getName(), userName, sender.getName() + " definiu o grupo de " + userName + " para " + groupName, "");
					
					sender.sendMessage(new TextComponent("§aO usu§rio §f" + userName + "§a teve o grupo definido para §f" + groupName));
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sender.sendMessage(new TextComponent(""));
		sender.sendMessage(new TextComponent("§a[Cargos] Comandos dispon§veis:"));
		sender.sendMessage(new TextComponent("§7/Cargo§a listar"));
		sender.sendMessage(new TextComponent("§7/Cargo§a definir (nick) (grupo)"));
		if (diretor) {
			sender.sendMessage(new TextComponent("§7/Cargo§a deletar (nick)"));
		}
		sender.sendMessage(new TextComponent("§7/Cargo§a perfil (nick)"));
		sender.sendMessage(new TextComponent("§7/Cargo§a info (grupo)"));
		sender.sendMessage(new TextComponent("§7/Cargo§a users (grupo)"));
	}
}