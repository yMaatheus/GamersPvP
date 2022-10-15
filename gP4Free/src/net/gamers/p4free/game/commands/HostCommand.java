package net.gamers.p4free.game.commands;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.gamers.p4free.Main;
import net.gamerspvp.commons.bukkit.utils.SystemInfo;
import net.gamerspvp.commons.bukkit.utils.Utils;
import net.gamerspvp.commons.network.utils.MessageUtils;

public class HostCommand extends Command {
	
	public HostCommand(Main instance) {
		super("host");
		Utils.registerCommand(this, instance, "host");
	}

	@Override
	public boolean execute(CommandSender sender, String arg, String[] args) {
		if (!(sender.isOp())) {
			sender.sendMessage(MessageUtils.COMMAND_PERMISSION.getMessage());
			return false;
		}
		// Pegando o runtime onde o programa esta sendo rodado, e pegando o sistema
		// operacional (base)
		Runtime machine = Runtime.getRuntime();
		OperatingSystemMXBean system = ManagementFactory.getOperatingSystemMXBean();
		
		// Pegando os dados do sistema operacional etc..
		String so = system.getName();
		String soVersion = system.getVersion();
		String availableProcessors = String.valueOf(system.getAvailableProcessors());
		String freeRuntimeMemory = Utils.bytesToLegibleValue(machine.freeMemory());
		String totalRuntimeMemory = Utils.bytesToLegibleValue(machine.maxMemory());
		String usedRuntimeMemory = Utils.bytesToLegibleValue(machine.totalMemory() - machine.freeMemory());
		String freeComputerMemory = Utils.bytesToLegibleValue(SystemInfo.getFreeMemoryComputer());
		String totalComputerMemory = Utils.bytesToLegibleValue(SystemInfo.getTotalMemoryComputer());
		String usedComputerMemory = Utils.bytesToLegibleValue(SystemInfo.getTotalMemoryComputer() - SystemInfo.getFreeMemoryComputer());
		String freeComputerSpace = Utils.bytesToLegibleValue(SystemInfo.getFreeSpaceComputer());
		String totalComputerSpace = Utils.bytesToLegibleValue(SystemInfo.getTotalSpaceComputer());
		String usedComputerSpace = Utils.bytesToLegibleValue(SystemInfo.getTotalSpaceComputer() - SystemInfo.getFreeSpaceComputer());
		String processorArch = System.getProperty("os.arch");
		String processor = System.getenv("PROCESSOR_IDENTIFIER");
		
		// Exibindo os dados
		sender.sendMessage("�eSistema Operacional: �6" + so + " �8-�6 " + soVersion);
		sender.sendMessage("�eMem�ria RAM total do servidor: �6" + totalRuntimeMemory);
		sender.sendMessage("�eMem�ria RAM livre do servidor: �6" + freeRuntimeMemory);
		sender.sendMessage("�eMem�ria RAM usada no servidor: �6" + usedRuntimeMemory);
		sender.sendMessage("�eMem�ria RAM total da m�quina: �6" + totalComputerMemory);
		sender.sendMessage("�eMem�ria RAM livre da m�quina: �6" + freeComputerMemory);
		sender.sendMessage("�eMem�ria RAM usada na m�quina: �6" + usedComputerMemory);
		sender.sendMessage("�eArmazenamento total da m�quina: �6" + totalComputerSpace);
		sender.sendMessage("�eArmazenamento livre da m�quina: �6" + freeComputerSpace);
		sender.sendMessage("�eArmazenamento usado na m�quina: �6" + usedComputerSpace);
		sender.sendMessage("�eN�mero de processadores (n�cleos): �6" + availableProcessors);
		sender.sendMessage("�eArquitetura do processador: �6" + processorArch);
		sender.sendMessage("�eModelo do processador: �6" + (processor == null ? "Informa��o indisponivel! Veja o relat�rio avan�ado." : processor));
		return false;
	}
}