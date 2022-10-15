package net.gamerspvp.commons.bungee.vips.commands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.cargos.ProxiedCargosManager;
import net.gamerspvp.commons.bungee.cargos.group.ProxiedGroupManager;
import net.gamerspvp.commons.bungee.cargos.user.ProxiedUserManager;
import net.gamerspvp.commons.bungee.vips.ProxiedVipsManager;
import net.gamerspvp.commons.network.VipController;
import net.gamerspvp.commons.network.VipController.VipKey;
import net.gamerspvp.commons.network.log.LogManager;
import net.gamerspvp.commons.network.models.Group;
import net.gamerspvp.commons.network.models.PlayerVip;
import net.gamerspvp.commons.network.utils.DateUtils;
import net.gamerspvp.commons.network.utils.MessageUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class VipCommand extends Command {
	
	private ProxiedCommons commons;
	
	public VipCommand(ProxiedCommons commons) {
		super("vip", "gamers.admin");
		commons.getProxy().getPluginManager().registerCommand(commons, this);
		this.commons = commons;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender.hasPermission("gamers.admin"))) {
			sender.sendMessage(new TextComponent(MessageUtils.COMMAND_PERMISSION.getMessage()));
			return;
		}
		boolean coordenador = sender.hasPermission("gamers.coordenador");
		boolean diretor = sender.hasPermission("gamers.diretor");
		if (args.length > 0) {
			try {
				VipController vipController = ProxiedVipsManager.getController();
				ProxiedGroupManager proxiedGroupManager = ProxiedCargosManager.getGroupManager();
				ProxiedUserManager proxiedUserManager = ProxiedCargosManager.getUserManager();
				if (args[0].equalsIgnoreCase("keys")) {
					if (!(coordenador)) {
						sender.sendMessage(new TextComponent(MessageUtils.COMMAND_PERMISSION.getMessage()));
						return;
					}
					HashSet<VipKey> keys = vipController.getAllKeys();
					if (keys.isEmpty()) {
						sender.sendMessage(new TextComponent("§cN§o foi encontrado nenhum resultado no banco de dados."));
						return;
					}
					sender.sendMessage(new TextComponent(""));
					sender.sendMessage(new TextComponent("§a[Vips] Lista de chaves:"));
					int i = 0;
					for (VipKey key : keys) {
						i++;
						long time = key.getTime();
						if (System.currentTimeMillis() > time) {
							continue;
						}
						String timeText = DateUtils.formatDifference(time);
						String keyName = key.getKey();
						String author = key.getAuthor();
						String group = key.getGroup();
						TextComponent textComponent = new TextComponent("§f" + i + "§8. §a" + keyName + " §7(" + group  + ") §7- §aAutor:§f " + author + " §aAcaba em:§f " + timeText);
						ComponentBuilder builder = new ComponentBuilder("§eClique aqui para digitar a chave!");
						HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, builder.create());
						textComponent.setHoverEvent(hover);
						ClickEvent click = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, keyName);
						textComponent.setClickEvent(click);
						
						sender.sendMessage(textComponent);
					}
					return;
				} else if (args.length > 2 && (args[0].equalsIgnoreCase("addtempo") || args[0].equalsIgnoreCase("addtime"))) {
					if (!(diretor)) {
						sender.sendMessage(new TextComponent(MessageUtils.COMMAND_PERMISSION.getMessage()));
						return;
					}
					String groupName = args[1].substring(0,1).toUpperCase().concat(args[1].toLowerCase().substring(1));
					int days = Integer.parseInt(args[2]);
					Group group = proxiedGroupManager.getGroup(groupName);
					if (group == null || !(group.isVip())) {
						sender.sendMessage(new TextComponent("§cO grupo informado n§o encontrado ou n§o § §lVIP§c."));
						return;
					}
					commons.runAsync(new Runnable() {
						
						@Override
						public void run() {
							try {
								HashSet<PlayerVip> playersVips = vipController.getAllVips();
								int totalUsers = 0;
								int affectedUsers = 0;
								if (playersVips.isEmpty()) {
									sender.sendMessage(new TextComponent("§cN§o foi encontrado nenhuma informa§§o no banco de dados."));
									return;
								}
								for (PlayerVip playerVip : playersVips) {
									totalUsers++;
									if (!playerVip.getVips().containsKey(groupName)) {
										continue;
									}
									for (String vipName : playerVip.getVips().keySet()) {
										long value = playerVip.getVips().get(vipName);
										if (System.currentTimeMillis() > value) {
											continue;
										}
										playerVip.getVips().put(vipName, value + TimeUnit.DAYS.toMillis(days));
										vipController.updatePlayerVip(playerVip);
										affectedUsers++;
									}
								}
								sender.sendMessage(new TextComponent(""));
								sender.sendMessage(new TextComponent("§a[Vips] Informa§§es:"));
								sender.sendMessage(new TextComponent(""));
								sender.sendMessage(new TextComponent("§aForam adicionados §f" + days + " §adias aos usu§rios afetados do §lVIP §f" + groupName + "§a."));
								sender.sendMessage(new TextComponent(""));
								sender.sendMessage(new TextComponent("§e* §aTotal de usu§rios carregados§8: §f" + totalUsers));
								sender.sendMessage(new TextComponent("§e* §aTotal de usu§rios afetados§8: §f" + affectedUsers));
							} catch (Exception e) {
								sender.sendMessage(new TextComponent("§cN§o foi possivel completar essa opera§§o."));
							}
						}
					});
					return;
				} else if (args[0].equalsIgnoreCase("purge")) {
					if (!(diretor)) {
						sender.sendMessage(new TextComponent(MessageUtils.COMMAND_PERMISSION.getMessage()));
						return;
					}
					commons.runAsync(new Runnable() {
						
						@Override
						public void run() {
							try {
								HashSet<PlayerVip> playersVips = vipController.getAllVips();
								int totalUsers = 0;
								int purgedUsers = 0;
								if (playersVips.isEmpty()) {
									sender.sendMessage(new TextComponent("§cN§o foi encontrado nenhuma informa§§o no banco de dados."));
									return;
								}
								for (PlayerVip playerVip : playersVips) {
									totalUsers++;
									for (String vipName : playerVip.getVips().keySet()) {
										long value = playerVip.getVips().get(vipName);
										if (value > System.currentTimeMillis()) { //Tempo do vip n§o expirou ainda.
											continue;
										}
										vipController.purgePlayerVip(playerVip.getName());
										purgedUsers++;
									}
								}
								sender.sendMessage(new TextComponent(""));
								sender.sendMessage(new TextComponent("§a[Vips] Informa§§es:"));
								sender.sendMessage(new TextComponent("§e* §aTotal de usu§rios carregados§8: §f" + totalUsers));
								sender.sendMessage(new TextComponent("§e* §aTotal de usu§rios deletados§8: §f" + purgedUsers));
							} catch (Exception e) {
								sender.sendMessage(new TextComponent("§cN§o foi possivel completar essa opera§§o."));
							}
						}
					});
					return;
				} else if (args[0].equalsIgnoreCase("users")) {
					if (!(diretor)) {
						sender.sendMessage(new TextComponent(MessageUtils.COMMAND_PERMISSION.getMessage()));
						return;
					}
					commons.runAsync(new Runnable() {
						
						@Override
						public void run() {
							try {
								HashSet<PlayerVip> playersVips = vipController.getAllVips();
								if (playersVips.isEmpty()) {
									sender.sendMessage(new TextComponent("§cN§o foi encontrado nenhuma informa§§o no banco de dados."));
									return;
								}
								StringBuilder builder = new StringBuilder();
								HashMap<String, Integer> vipUsersCount = new HashMap<String, Integer>();
								int totalUsers = 0;
								int activeUsers = 0;
								for (PlayerVip playerVip : playersVips) {
									totalUsers++;
									
									HashMap<String, Long> vipsNames = new HashMap<String, Long>();
									for (String vipName : playerVip.getVips().keySet()) {
										long value = playerVip.getVips().get(vipName);
										if (System.currentTimeMillis() > value) {
											continue;
										}
										vipsNames.put(vipName, value);
										if (vipUsersCount.containsKey(vipName)) {
											vipUsersCount.put(vipName, vipUsersCount.get(vipName) + 1);
										} else {
											vipUsersCount.put(vipName, 1);
										}
									}
									
									if (vipsNames.isEmpty()) {
										continue;
									}
									activeUsers++;
									if (activeUsers > 150) {
										break;
									}
									String playerName = playerVip.getPlayerName();
									String activeVips = vipsNames.keySet().toString().replace("[", "").replace("]", "");
									builder.append("§f" + activeUsers + "§8. §a" + playerName + " §7- §7(§f" + activeVips + "§7)\n");
								}
								sender.sendMessage(new TextComponent(""));
								sender.sendMessage(new TextComponent("§a[Vips] Lista de §lVIP§a(s):"));
								sender.sendMessage(new TextComponent(builder.toString()));
								sender.sendMessage(new TextComponent("§e* §aTotal de usu§rios carregados§8: §f" + totalUsers));
								sender.sendMessage(new TextComponent("§e* §aTotal de usu§rios ativos§8: §f" + activeUsers));
								for (String vipName : vipUsersCount.keySet()) {
									sender.sendMessage(new TextComponent("§e* §aUsu§rios ativos usando " + vipName + "§8: §f" + vipUsersCount.get(vipName)));
								}
							} catch (Exception e) {
								sender.sendMessage(new TextComponent("§cN§o foi possivel completar essa opera§§o."));
							}
						}
					});
					return;
				} else if (args.length > 1 && args[0].equalsIgnoreCase("perfil")) {
					String playerName = args[1];
					PlayerVip playerVip = ProxiedVipsManager.getPlayerVip(playerName, false);
					if (playerVip == null) {
						sender.sendMessage(new TextComponent(MessageUtils.PLAYER_NOT_BANCO.getMessage()));
						return;
					}
					if (playerVip.getVips().isEmpty()) {
						sender.sendMessage(new TextComponent("§cN§o foi encontrado nenhum §lVIP §cnessa conta."));
						return;
					}
					StringBuilder builder = new StringBuilder();
					int i = 0;
					for (String key : playerVip.getVips().keySet()) {
						long value = playerVip.getVips().get(key);
						if (System.currentTimeMillis() > value) {
							continue;
						}
						i++;
						builder.append("§7* §a" + key + " §7- §fTermina em: §a" + DateUtils.formatDifference(value) + " §8(§f"+ DateUtils.longToDate(value) + "§8)\n");
					}
					if (i == 0) {
						sender.sendMessage(new TextComponent("§cN§o foi encontrado nenhum §lVIP §cativo nessa conta."));
						return;
					}
					sender.sendMessage(new TextComponent(""));
					sender.sendMessage(new TextComponent("§a[Vips] Informa§§es do jogador:"));
					sender.sendMessage(new TextComponent(""));
					sender.sendMessage(new TextComponent("§7* §aNome§8:§f " + playerVip.getPlayerName()));
					sender.sendMessage(new TextComponent(builder.toString()));
					return;
				} else if (args.length > 3 && args[0].equalsIgnoreCase("gerarkey")) {
					String groupName = args[1].substring(0,1).toUpperCase().concat(args[1].toLowerCase().substring(1));
					int days = Integer.parseInt(args[2]);
					String reason = "";
					for (int i = 3; i < args.length; i++) {
						reason = reason + args[i] + " ";
					}
					Group group = proxiedGroupManager.getGroup(groupName);
					if (group == null) {
						sender.sendMessage(new TextComponent("§cO grupo informado n§o foi encontrado."));
						return;
					}
					if (!(group.isVip())) {
						sender.sendMessage(new TextComponent("§cVoc§ informou um grupo cujo n§o § considerado vip."));
						return;
					}
					
					String key = vipController.generateKey();
					vipController.computeKey(key, sender.getName(), groupName, days);
					LogManager.log("Keys", sender.getName(), sender.getName() + " criou uma key " + key + " do vip " + groupName + " com dura§§o de " + days + " dias", reason);
					
					TextComponent textComponent = new TextComponent("§a[Vips] Chave do grupo §f" + groupName + " §afoi gerada com sucesso.");
					ComponentBuilder builder = new ComponentBuilder("§eClique aqui para vizualizar a chave!");
					HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, builder.create());
					textComponent.setHoverEvent(hover);
					ClickEvent click = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, key);
					textComponent.setClickEvent(click);
					
					sender.sendMessage(textComponent);
					sender.sendMessage(new TextComponent("§a[Vips] Sua justificativa dessa a§§o: §f" + reason));
					return;
				} else if (args.length > 2 && (args[0].equalsIgnoreCase("apagarkey"))) {
					String key = args[1];
					String reason = "";
					for (int i = 2; i < args.length; i++) {
						reason = reason + args[i] + " ";
					}
					if (vipController.searchKey(key) == null) {
						sender.sendMessage(new TextComponent("§cA chave informada n§o encontrada."));
						return;
					}
					
					vipController.purgeKey(key);
					
					LogManager.log("Keys", sender.getName(), sender.getName() + " apagou a key " + key, reason);
					
					sender.sendMessage(new TextComponent("§a[Vips] Chave §f" + key + " §afoi apagada com sucesso."));
					sender.sendMessage(new TextComponent("§a[Vips] Sua justificativa dessa a§§o: §f" + reason));
					return;
				} else if (args.length > 4 && (args[0].equalsIgnoreCase("dar"))) {
					String playerName = args[1];
					String groupName = args[2].substring(0,1).toUpperCase().concat(args[2].toLowerCase().substring(1));
					int days = Integer.parseInt(args[3]);
					long time = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(days);
					String reason = "";
					for (int i = 4; i < args.length; i++) {
						reason = reason + args[i] + " ";
					}
					Group group = proxiedGroupManager.getGroup(groupName);
					if (group == null || !(group.isVip())) {
						sender.sendMessage(new TextComponent("§cO grupo informado n§o encontrado ou n§o § §lVIP§c."));
						return;
					}
					PlayerVip playerVip = ProxiedVipsManager.getPlayerVip(playerName, true);
					if (playerVip.getVips().get(groupName) != null && playerVip.getVips().get(groupName) > System.currentTimeMillis()) {
						sender.sendMessage(new TextComponent("§cVoc§ n§o pode usar essa chave pois j§ possui esse §lVIP §cativo."));
						return;
					}
					
					playerVip.getVips().put(groupName, time);
					
					vipController.updatePlayerVip(playerVip);
					proxiedUserManager.defineUserGroup(playerName, groupName);
					
					LogManager.log("Vips", sender.getName(), playerName, sender.getName() + " deu VIP " + groupName + " para " + playerName, reason);
					
					sender.sendMessage(new TextComponent("§a[Vips] Voc§ deu um §lVIP§f " + groupName + " §ade§f " + DateUtils.formatDifference(time) + " §apara§f " + playerName));
					sender.sendMessage(new TextComponent("§a[Vips] Sua justificativa dessa a§§o: §f" + reason));
					return;
				} else if (args.length > 2 && (args[0].equalsIgnoreCase("remover") || args[0].equalsIgnoreCase("remove"))) {
					String playerName = args[1];
					String reason = "";
					for (int i = 2; i < args.length; i++) {
						reason = reason + args[i] + " ";
					}
					PlayerVip playerVip = ProxiedVipsManager.getPlayerVip(playerName, true);
					playerVip.setVips(new HashMap<String, Long>());
					
					vipController.updatePlayerVip(playerVip);
					proxiedUserManager.defineUserGroup(playerName, proxiedGroupManager.getDefaultGroup().getName());
					
					LogManager.log("Vips", sender.getName(), playerName, sender.getName() + " removeu o VIP de " + playerName, reason);
					
					sender.sendMessage(new TextComponent("§a[Vips] Voc§ removeu o(s) §lVIP(s) §ade§f " + playerName));
					sender.sendMessage(new TextComponent("§a[Vips] Sua justificativa dessa a§§o: §f" + reason));
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sender.sendMessage(new TextComponent(""));
		sender.sendMessage(new TextComponent("§a[Vips] Comandos dispon§veis:"));
		if (coordenador) {
			sender.sendMessage(new TextComponent("§7/Vip§a keys"));
			sender.sendMessage(new TextComponent("§7/Vip§a addtempo (grupo) (dias)"));
			sender.sendMessage(new TextComponent("§7/Vip§a purge"));
			sender.sendMessage(new TextComponent("§7/Vip§a users"));
		}
		sender.sendMessage(new TextComponent("§7/Vip§a perfil (nick)"));
		sender.sendMessage(new TextComponent("§7/Vip§a gerarkey (grupo) (dias) (justificativa)"));
		sender.sendMessage(new TextComponent("§7/Vip§a apagarkey (chave) (justificativa)"));
		sender.sendMessage(new TextComponent("§7/Vip§a dar (nick) (grupo) (dias) (justificativa)"));
		sender.sendMessage(new TextComponent("§7/Vip§a remover (nick) (justificativa)"));
		return;
	}
}