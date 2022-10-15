package net.gamerspvp.commons.bungee.maintenance;

import net.gamerspvp.commons.bungee.ProxiedCommons;
import net.gamerspvp.commons.bungee.maintenance.models.Maintenance;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class MaintenanceListener implements Listener {
	
	private ProxiedCommons instance;
	
	public MaintenanceListener(ProxiedCommons instance) {
		this.instance = instance;
		instance.getProxy().getPluginManager().registerListener(instance, this);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPostLoginEvent(PostLoginEvent event) {
		ProxiedPlayer player = event.getPlayer();
		Maintenance maintenance = instance.getNetworkOptions().getMaintenance();
		if (maintenance == null) {
			return;
		}
		if (player.hasPermission("commons.admin")) {
			return;
		}
		if (player.getName().equalsIgnoreCase("MatheusDeveloper")) {
			return;
		}
		if (player.getName().equalsIgnoreCase("BOT_GamersPvP")) {
			return;
		}
		StringBuilder builder = new StringBuilder();
		builder.append("§c§lGAMERSPVP");
		builder.append("\n\n§cModo de seguran§a ativado, pedimos desculpas pelo transtorno, mas estamos realizando uma manuten§§o. Estaremos de volta em breve!");
		builder.append("\n§cAcompanhe o andamento da manuten§§o atrav§s do canal #avisos do nosso Discord.\n");
		
		player.disconnect(new TextComponent(builder.toString()));
	}
}