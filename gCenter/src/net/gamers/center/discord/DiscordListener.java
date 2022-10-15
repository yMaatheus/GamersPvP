package net.gamers.center.discord;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordListener extends ListenerAdapter {
	
	public DiscordListener() {
		DiscordManager.getJda().addEventListener(this);
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		MessageChannel channel = event.getChannel();
		Message message = event.getMessage();
		if (message == null || message.getAuthor() == null || message.getAuthor().isBot()) {
			return;
		}
		String messageString = message.getContentDisplay();
		if (messageString.contains("@") || message.getCategory() == null || message.getCategory().getId() == null) {
			return;
		}
		if ((!message.getCategory().getId().equalsIgnoreCase("744061536238174228")) && ((messageString.contains("https://") || messageString.contains("http://")))) {
			channel.deleteMessageById(message.getId()).complete();
			return;
		}
		if (channel.getName().contains("ticket-")) {
			JDA jda = event.getJDA();
			if (messageString.contains("1")) {
				channel.sendMessage("Olá " + message.getAuthor().getAsMention() + ", Preciso que diga o **máximo de informações do seu problema** e aguarde um membro da equipe responsável entrar em contato.").queue();
				jda.getTextChannelById(channel.getId()).getManager().setName(channel.getName().replace("ticket", "técnico")).queue();
				return;
			} else if (messageString.contains("2")) {
				channel.sendMessage("Olá " + message.getAuthor().getAsMention() + ", Informe seu problema e aguarde um membro da equipe entrar em contato.").queue();
				jda.getTextChannelById(channel.getId()).getManager().setName(channel.getName().replace("ticket", "financeiro")).queue();
				return;
			} else if (messageString.contains("3")) {
				EmbedBuilder embedbuilder = getEmbedbuilderDefault("**FILTRO DE SUPORTE**", jda);
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("  Olá " + message.getAuthor().getName() + ", Você deseja verificar seu clan?\n");
				embedbuilder.appendDescription("Se sim preencha o formulário abaixo:\n");
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("Seu Nome: \n");
				embedbuilder.appendDescription("Seu Nick: \n");
				embedbuilder.appendDescription("Nome e Tag do seu Clã: \n");
				embedbuilder.appendDescription("Discord dos Líderes: \n");
				embedbuilder.appendDescription("Quantidade de Membros: \n");
				embedbuilder.appendDescription("Link **PERMANENTE** do Discord do seu Clã: \n");
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("Caso não seja verificação de Clans, diga o máximo de informações possiveis e aguarde a resposta.\n");
				embedbuilder.appendDescription("\n");
				channel.sendMessage(embedbuilder.build()).queue();
				jda.getTextChannelById(channel.getId()).getManager().setName(channel.getName().replace("ticket", "clans")).queue();
				return;
			} else if (messageString.contains("4")) {
				channel.sendMessage("Olá " + message.getAuthor().getAsMention() + ", Informe o que deseja e aguarde a resposta do **Chefe da Equipe de GC**.").queue();
				jda.getTextChannelById(channel.getId()).getManager().setName(channel.getName().replace("ticket", "gc")).queue();
				return;
			} else if (messageString.contains("5")) {
				EmbedBuilder embedbuilder = getEmbedbuilderDefault("**FILTRO DE SUPORTE**", jda);
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("  Olá " + message.getAuthor().getName() + ", Leia as informações abaixo e veja onde seu problema encaixa-se.\n");
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("**Perguntas frequentes:**\n");
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("• Quais são os requisitos da Tag Youtuber?\n");
				embedbuilder.appendDescription("R: Não temos muitos requisitos, seu canal será analisado com base no seu conteúdo, qualidade e tempo.\n");
				embedbuilder.appendDescription("• Como funciona a Tag Youtuber?\n");
				embedbuilder.appendDescription("R: A Tag Youtuber é disponibilizada aos criados de conteúdo presentes ao servidor como forma de ajudar os mesmos.\n");
				embedbuilder.appendDescription("Vale ressaltar que os **Criados de Conteúdo parceiros precisam postar um vídeo por semana no servidor para manter sua TAG**\n");
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("Você deseja **receber ou renovar** a **Tag Youtuber**?\n");
				embedbuilder.appendDescription("Se sim preencha o formulário abaixo:\n");
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("Seu Nome: \n");
				embedbuilder.appendDescription("Seu Nick: \n");
				embedbuilder.appendDescription("Link do seu canal: \n");
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("• Caso não seja nada informado acima, por favor conte seu problema e aguarde um membro da equipe responder.\n");
				embedbuilder.appendDescription("\n");
				channel.sendMessage(embedbuilder.build()).queue();
				jda.getTextChannelById(channel.getId()).getManager().setName(channel.getName().replace("ticket", "youtuber")).queue();
				return;
			} else if (messageString.contains("6")) {
				EmbedBuilder embedbuilder = getEmbedbuilderDefault("**FILTRO DE SUPORTE**", jda);
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("  Olá " + message.getAuthor().getName() + ", Preencha o formulário abaixo, envie aqui e aguarde um membro da equipe responder.\n");
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("Seu Nome: \n");
				embedbuilder.appendDescription("Seu Nick: \n");
				embedbuilder.appendDescription("Tipo de punição: \n");
				embedbuilder.appendDescription("Autor: \n");
				embedbuilder.appendDescription("Print da punição: \n");
				embedbuilder.appendDescription("Motivo do pedido de revisão: \n");
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("• **Vale ressaltar que a revisão de punições tem limite de três revisões negadas**, caso exceda esse limites suas futuras revisões serão ignoradas.\n");
				embedbuilder.appendDescription("\n");
				channel.sendMessage(embedbuilder.build()).queue();
				jda.getTextChannelById(channel.getId()).getManager().setName(channel.getName().replace("ticket", "revisão")).queue();
				return;
			} else if (messageString.contains("7")) {
				channel.sendMessage("Olá " + message.getAuthor().getAsMention() + ", Informe o que deseja e aguarde a resposta de um membro da **Equipe de Marketing**.").queue();
				jda.getTextChannelById(channel.getId()).getManager().setName(channel.getName().replace("ticket", "marketing")).queue();
				return;
			} else if (messageString.contains("8")) {
				channel.sendMessage("Olá " + message.getAuthor().getAsMention() + ", Informe do que se trata o seu problema, diga detalhes do mesmo, e aguarde um membro da equipe entrar em contato.").queue();
				jda.getTextChannelById(channel.getId()).getManager().setName(channel.getName().replace("ticket", "outro")).queue();
				return;
			}
		}
	}
	
	@Override
	public void onTextChannelCreate(TextChannelCreateEvent event) {
		TextChannel channel = event.getChannel();
		JDA jda = event.getJDA();
		if (event.getChannel().getName().contains("ticket-")) {
			EmbedBuilder embedbuilder = getEmbedbuilderDefault("**FILTRO DE SUPORTE**", jda);
			embedbuilder.appendDescription("");
			embedbuilder.appendDescription("  Olá, seja bem vindo ao suporte do GamersPvP!\n");
			embedbuilder.appendDescription("Eu sou o robô responsável por filtrar para o departamento responsável.\n");
			embedbuilder.appendDescription("Antes de começarmos, **eu preciso que informe o número da categoria está seu problema:**\n");
			embedbuilder.appendDescription("\n");
			embedbuilder.appendDescription("**1. Técnico** - **Bugs's** encontrados ou **problemas técnicos** com o servidor.\n");
			embedbuilder.appendDescription("**2. Financeiro** - Problemas com **CASH** ou **VIP**.\n");
			embedbuilder.appendDescription("**3. Clans** - **Verifique seu clan** ou **fale com o Chefe do Departamento de Clans**.\n");
			embedbuilder.appendDescription("**4. Equipe GC** - Fale com o Chefe da Equipe GC, caso queira **ingressar** ou **informar problemas** que teve com a equipe GC.\n");
			embedbuilder.appendDescription("**5. Tag Youtuber** - Fale com o Chefe do Departamento responsável pelas **Tag's dos Youtubers**.\n");
			embedbuilder.appendDescription("**6. Revisão de Punições** - Peça esclarecimentos da sua punição caso a considere que ela seja incorreta.\n");
			embedbuilder.appendDescription("**7. Marketing** - Fale com o Chefe da Equipe de Marketing, para **fechar parcerias** ou **ingressar a Equipe de Marketing**.\n");
			embedbuilder.appendDescription("**8. Outro** - Fale sobre outro assunto, o qual não, foi informado acima.\n");
			embedbuilder.appendDescription("");
			channel.sendMessage(embedbuilder.build()).queueAfter(2, TimeUnit.SECONDS);
		}
	}
	
	public EmbedBuilder getEmbedbuilderDefault(String title, JDA jda) {
		EmbedBuilder embedbuilder = new EmbedBuilder();
		embedbuilder.setTitle(title);
		embedbuilder.setAuthor("GamersPvP", "https://loja.gamerspvp.net/", jda.getSelfUser().getAvatarUrl());
		embedbuilder.setColor(Color.GREEN);
		return embedbuilder;
	}
}