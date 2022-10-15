package net.gamers.center.discord;

import java.io.File;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class DiscordManager {
	
	@Getter
	private static JDA jda;
	
	public DiscordManager() throws Exception {
		JDABuilder jdaBuilder = JDABuilder.createDefault("");
		jdaBuilder.setActivity(Activity.of(ActivityType.STREAMING, "O yMatheus � um lindo!", "https://loja.gamerspvp.net/"));
		jda = jdaBuilder.build();
		new DiscordListener();
	}
	
	public void sendLogReportFile(String fileName) {
		Guild guild = jda.getGuildById("368594399862587392");
		if (guild == null) {
			return;
		}
		TextChannel textChannel = guild.getTextChannelById("813103787191304252");
		if (textChannel == null) {
			return;
		}
		try {
			textChannel.sendMessage("Relat�rio de logs expedido pela central para analise da coordena��o: ").addFile(new File(new File(".").getCanonicalPath(), fileName)).queue();
		} catch (Exception e) {
			System.out.println("[WARN] N�o foi possivel enviar o relat�rio ao Discord.");
			textChannel.sendMessage("O relat�rio n�o teve �xito no envio. O mesmo est� salvo no servidor central, entre em contato com o yMatheus_ para obter acesso.").queue();
		}
	}
}