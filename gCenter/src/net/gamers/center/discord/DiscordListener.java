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
				channel.sendMessage("Ol� " + message.getAuthor().getAsMention() + ", Preciso que diga o **m�ximo de informa��es do seu problema** e aguarde um membro da equipe respons�vel entrar em contato.").queue();
				jda.getTextChannelById(channel.getId()).getManager().setName(channel.getName().replace("ticket", "t�cnico")).queue();
				return;
			} else if (messageString.contains("2")) {
				channel.sendMessage("Ol� " + message.getAuthor().getAsMention() + ", Informe seu problema e aguarde um membro da equipe entrar em contato.").queue();
				jda.getTextChannelById(channel.getId()).getManager().setName(channel.getName().replace("ticket", "financeiro")).queue();
				return;
			} else if (messageString.contains("3")) {
				EmbedBuilder embedbuilder = getEmbedbuilderDefault("**FILTRO DE SUPORTE**", jda);
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("  Ol� " + message.getAuthor().getName() + ", Voc� deseja verificar seu clan?\n");
				embedbuilder.appendDescription("Se sim preencha o formul�rio abaixo:\n");
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("Seu Nome: \n");
				embedbuilder.appendDescription("Seu Nick: \n");
				embedbuilder.appendDescription("Nome e Tag do seu Cl�: \n");
				embedbuilder.appendDescription("Discord dos L�deres: \n");
				embedbuilder.appendDescription("Quantidade de Membros: \n");
				embedbuilder.appendDescription("Link **PERMANENTE** do Discord do seu Cl�: \n");
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("Caso n�o seja verifica��o de Clans, diga o m�ximo de informa��es possiveis e aguarde a resposta.\n");
				embedbuilder.appendDescription("\n");
				channel.sendMessage(embedbuilder.build()).queue();
				jda.getTextChannelById(channel.getId()).getManager().setName(channel.getName().replace("ticket", "clans")).queue();
				return;
			} else if (messageString.contains("4")) {
				channel.sendMessage("Ol� " + message.getAuthor().getAsMention() + ", Informe o que deseja e aguarde a resposta do **Chefe da Equipe de GC**.").queue();
				jda.getTextChannelById(channel.getId()).getManager().setName(channel.getName().replace("ticket", "gc")).queue();
				return;
			} else if (messageString.contains("5")) {
				EmbedBuilder embedbuilder = getEmbedbuilderDefault("**FILTRO DE SUPORTE**", jda);
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("  Ol� " + message.getAuthor().getName() + ", Leia as informa��es abaixo e veja onde seu problema encaixa-se.\n");
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("**Perguntas frequentes:**\n");
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("� Quais s�o os requisitos da Tag Youtuber?\n");
				embedbuilder.appendDescription("R: N�o temos muitos requisitos, seu canal ser� analisado com base no seu conte�do, qualidade e tempo.\n");
				embedbuilder.appendDescription("� Como funciona a Tag Youtuber?\n");
				embedbuilder.appendDescription("R: A Tag Youtuber � disponibilizada aos criados de conte�do presentes ao servidor como forma de ajudar os mesmos.\n");
				embedbuilder.appendDescription("Vale ressaltar que os **Criados de Conte�do parceiros precisam postar um v�deo por semana no servidor para manter sua TAG**\n");
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("Voc� deseja **receber ou renovar** a **Tag Youtuber**?\n");
				embedbuilder.appendDescription("Se sim preencha o formul�rio abaixo:\n");
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("Seu Nome: \n");
				embedbuilder.appendDescription("Seu Nick: \n");
				embedbuilder.appendDescription("Link do seu canal: \n");
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("� Caso n�o seja nada informado acima, por favor conte seu problema e aguarde um membro da equipe responder.\n");
				embedbuilder.appendDescription("\n");
				channel.sendMessage(embedbuilder.build()).queue();
				jda.getTextChannelById(channel.getId()).getManager().setName(channel.getName().replace("ticket", "youtuber")).queue();
				return;
			} else if (messageString.contains("6")) {
				EmbedBuilder embedbuilder = getEmbedbuilderDefault("**FILTRO DE SUPORTE**", jda);
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("  Ol� " + message.getAuthor().getName() + ", Preencha o formul�rio abaixo, envie aqui e aguarde um membro da equipe responder.\n");
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("Seu Nome: \n");
				embedbuilder.appendDescription("Seu Nick: \n");
				embedbuilder.appendDescription("Tipo de puni��o: \n");
				embedbuilder.appendDescription("Autor: \n");
				embedbuilder.appendDescription("Print da puni��o: \n");
				embedbuilder.appendDescription("Motivo do pedido de revis�o: \n");
				embedbuilder.appendDescription("\n");
				embedbuilder.appendDescription("� **Vale ressaltar que a revis�o de puni��es tem limite de tr�s revis�es negadas**, caso exceda esse limites suas futuras revis�es ser�o ignoradas.\n");
				embedbuilder.appendDescription("\n");
				channel.sendMessage(embedbuilder.build()).queue();
				jda.getTextChannelById(channel.getId()).getManager().setName(channel.getName().replace("ticket", "revis�o")).queue();
				return;
			} else if (messageString.contains("7")) {
				channel.sendMessage("Ol� " + message.getAuthor().getAsMention() + ", Informe o que deseja e aguarde a resposta de um membro da **Equipe de Marketing**.").queue();
				jda.getTextChannelById(channel.getId()).getManager().setName(channel.getName().replace("ticket", "marketing")).queue();
				return;
			} else if (messageString.contains("8")) {
				channel.sendMessage("Ol� " + message.getAuthor().getAsMention() + ", Informe do que se trata o seu problema, diga detalhes do mesmo, e aguarde um membro da equipe entrar em contato.").queue();
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
			embedbuilder.appendDescription("  Ol�, seja bem vindo ao suporte do GamersPvP!\n");
			embedbuilder.appendDescription("Eu sou o rob� respons�vel por filtrar para o departamento respons�vel.\n");
			embedbuilder.appendDescription("Antes de come�armos, **eu preciso que informe o n�mero da categoria est� seu problema:**\n");
			embedbuilder.appendDescription("\n");
			embedbuilder.appendDescription("**1. T�cnico** - **Bugs's** encontrados ou **problemas t�cnicos** com o servidor.\n");
			embedbuilder.appendDescription("**2. Financeiro** - Problemas com **CASH** ou **VIP**.\n");
			embedbuilder.appendDescription("**3. Clans** - **Verifique seu clan** ou **fale com o Chefe do Departamento de Clans**.\n");
			embedbuilder.appendDescription("**4. Equipe GC** - Fale com o Chefe da Equipe GC, caso queira **ingressar** ou **informar problemas** que teve com a equipe GC.\n");
			embedbuilder.appendDescription("**5. Tag Youtuber** - Fale com o Chefe do Departamento respons�vel pelas **Tag's dos Youtubers**.\n");
			embedbuilder.appendDescription("**6. Revis�o de Puni��es** - Pe�a esclarecimentos da sua puni��o caso a considere que ela seja incorreta.\n");
			embedbuilder.appendDescription("**7. Marketing** - Fale com o Chefe da Equipe de Marketing, para **fechar parcerias** ou **ingressar a Equipe de Marketing**.\n");
			embedbuilder.appendDescription("**8. Outro** - Fale sobre outro assunto, o qual n�o, foi informado acima.\n");
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