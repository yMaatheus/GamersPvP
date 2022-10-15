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
		JDABuilder jdaBuilder = JDABuilder.createDefault("NzY4Mzk1NDczMTM2Mzg2MDQ4.X4_2DA.2kK1yJcXP7iBCBxgOSQvo612hQE");
		jdaBuilder.setActivity(Activity.of(ActivityType.STREAMING, "O yMatheus é um lindo!", "https://loja.gamerspvp.net/"));
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
			textChannel.sendMessage("Relatório de logs expedido pela central para analise da coordenação: ").addFile(new File(new File(".").getCanonicalPath(), fileName)).queue();
		} catch (Exception e) {
			System.out.println("[WARN] Não foi possivel enviar o relatório ao Discord.");
			textChannel.sendMessage("O relatório não teve êxito no envio. O mesmo está salvo no servidor central, entre em contato com o yMatheus_ para obter acesso.").queue();
		}
	}
}