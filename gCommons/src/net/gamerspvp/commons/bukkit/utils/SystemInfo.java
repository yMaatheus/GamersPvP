package net.gamerspvp.commons.bukkit.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class SystemInfo {

	public static void createFullLog(Plugin instance) {
		try {
			File file = new File(instance.getDataFolder() + File.separator + "systeminfo.txt");
			if (file.exists())
				file.delete();
			Runtime runtime = Runtime.getRuntime();
			FileWriter arquivo = new FileWriter(file);
			PrintWriter gravador = new PrintWriter(new FileWriter(file));

			gravador.println("+----------------------------------+");
			gravador.println("| Variaveis de Ambiente do Sistema |");
			gravador.println("+----------------------------------+\n\n");
			Map<String, String> envs = System.getenv();
			for (Entry<String, String> env : envs.entrySet()) {
				gravador.println(env.getKey() + " : " + env.getValue());
			}

			gravador.println("\n\n+----------------------------------+");
			gravador.println("|  Propriedades Gerais do Sistema  |");
			gravador.println("+----------------------------------+\n\n");
			Set<Entry<Object, Object>> properties = System.getProperties().entrySet();
			for (Entry<Object, Object> property : properties) {
				gravador.println(property.getKey() + " : " + property.getValue());
			}

			gravador.println("\n\n+----------------------------------+");
			gravador.println("|   Informa��es Gerais da Maquina  |");
			gravador.println("+----------------------------------+");
			String[] infoCommands = { "systeminfo", "cmdinfo", "srvinfo", "lshw", "lscpu", "hwinfo", "lspci", "free -m",
					"cat /proc/meminfo", "cat /proc/cpuinfo", "df -h" };
			boolean success = false;
			for (String command : infoCommands) {
				try {
					String line;
					Process process = runtime.exec(command);
					BufferedReader retorno = new BufferedReader(new InputStreamReader(process.getInputStream()));
					gravador.println("\n**********************************");
					gravador.println("COMMAND: " + command);
					while ((line = retorno.readLine()) != null) {
						gravador.println(line);
					}
					gravador.println("**********************************");
					success = true;
				} catch (Throwable e) {
				}
			}
			if (!success)
				gravador.println("Informa��es n�o dispon�veis!");

			success = false;
			gravador.println("\n\n+----------------------------------+");
			gravador.println("|  Informa��es de Rede da Maquina  |");
			gravador.println("+----------------------------------+");
			String[] netCommands = { "winipcfg", "ipconfig /all", "ifconfig", "ip a" };
			for (String command : netCommands) {
				try {
					String line;
					Process process = runtime.exec(command);
					BufferedReader retorno = new BufferedReader(new InputStreamReader(process.getInputStream()));
					gravador.println("\n**********************************");
					gravador.println("COMMAND: " + command);
					while ((line = retorno.readLine()) != null) {
						gravador.println(line);
					}
					gravador.println("**********************************");
					success = true;
				} catch (Throwable e) {
				}
			}
			if (!success)
				gravador.println("Informa��es n�o dispon�veis!");

			gravador.close();
			arquivo.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public static String getMinecraftVersion() {
		try {
			String info = Bukkit.getVersion();
			return info.split("MC: ")[1].split("\\)")[0];
		} catch (Throwable e) {
			return "Desconhecida";
		}
	}
	
	public static String getJarType() {
		try {
			String info = Bukkit.getVersion();
			return info.split("git-")[1].split("-")[0];
		} catch (Throwable e) {
			return "Desconhecida";
		}
	}
	
	public static String getApiVersion() {
		try {
			return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		} catch (Throwable e) {
			return "Desconhecida";
		}
	}
	
	public static long getFreeMemoryComputer() {
		try {
			OperatingSystemMXBean system = ManagementFactory.getOperatingSystemMXBean();
			Method getFreeMemory = system.getClass().getMethod("getFreePhysicalMemorySize");
			getFreeMemory.setAccessible(true);
			return (long) getFreeMemory.invoke(system);
		} catch (Throwable e) {
			return -1;
		}
	}
	
	public static long getTotalMemoryComputer() {
		try {
			OperatingSystemMXBean system = ManagementFactory.getOperatingSystemMXBean();
			Method getTotalMemory = system.getClass().getMethod("getTotalPhysicalMemorySize");
			getTotalMemory.setAccessible(true);
			return (long) getTotalMemory.invoke(system);
		} catch (Throwable e) {
			return -1;
		}
	}
	
	public static long getFreeSpaceComputer() {
		long space = 0;
		for (File f : File.listRoots()) {
			space += f.getFreeSpace();
		}
		return space;
	}
	
	public static long getTotalSpaceComputer() {
		long space = 0;
		for (File f : File.listRoots()) {
			space += f.getTotalSpace();
		}
		return space;
	}
}
