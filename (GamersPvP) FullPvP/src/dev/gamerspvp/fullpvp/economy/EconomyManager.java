package dev.gamerspvp.fullpvp.economy;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import dev.gamerspvp.fullpvp.Main;
import dev.gamerspvp.fullpvp.database.SQLite;
import dev.gamerspvp.fullpvp.economy.commands.MoneyCommand;
import dev.gamerspvp.fullpvp.economy.models.PlayerMoney;
import dev.gamerspvp.fullpvp.utils.Titles;

public class EconomyManager {
	
	private Main instance;
	
	private FileConfiguration config;
	
	private HashMap<String, PlayerMoney> cache;
	private String magnata;
	private List<PlayerMoney> top;
	private List<PlayerMoney> topList;
	
	public EconomyManager(Main instance) {
		this.instance = instance;
		this.config = instance.loadConfig("economy.yml");
		this.cache = new HashMap<String, PlayerMoney>();
		this.magnata = config.getString("magnata");
		this.top = new ArrayList<PlayerMoney>();
		this.topList = new ArrayList<PlayerMoney>();
		topMoneyTask();
		loadData();
		instance.registerCommand(new MoneyCommand(instance), "money");
		new EconomyListener(instance);
	}
	
	public void loadData() {
		SQLite sqlite = instance.getSQLite();
		ConsoleCommandSender console = instance.getServer().getConsoleSender();
		console.sendMessage("§a[Economy] Carregando contas...");
		try {
			sqlite.execute("CREATE TABLE IF NOT EXISTS money (name TEXT, playerName TEXT, money DOUBLE PRECISION, reciveMoney BOOL)", false);
			Statement stmt = sqlite.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM money;");
			int i = 0;
			while (rs.next()) {
				String playerName = rs.getString("playerName");
				double money = rs.getDouble("money");
				boolean reciveMoney = rs.getBoolean("reciveMoney");
				PlayerMoney playerMoney = new PlayerMoney(playerName);
				playerMoney.setMoney(money);
				playerMoney.setReciveMoney(reciveMoney);
				cache.put(playerMoney.getName(), playerMoney);
				i++;
			}
			console.sendMessage("§a[Economy] Foram carregadas §f" + i + " §acontas.");
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			console.sendMessage("§c[Economy] N§O FOI POSS§VEL CARREGAR AS CONTAS, DESABILITANDO...");
			instance.getServer().getPluginManager().disablePlugin(instance);
		}
		executeUpdateMoneyTop();
	}
	
	private void topMoneyTask() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if (!(Bukkit.getOnlinePlayers().isEmpty())) {
					executeUpdateMoneyTop();
				}
			}
		}.runTaskTimerAsynchronously(instance, 20L, 5 * 60 * 20L);
	}
	
	public void executeUpdateMoneyTop() {
		try {
			SQLite sqlite = instance.getSQLite();
			top.clear();
			Statement stmt = sqlite.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `money`;");
			while (rs.next()) {
				String playerName = rs.getString("playerName");
				double money = rs.getDouble("money");
				boolean reciveMoney = rs.getBoolean("reciveMoney");
				PlayerMoney playerMoney = new PlayerMoney(playerName);
				playerMoney.setMoney(money);
				playerMoney.setReciveMoney(reciveMoney);
				top.add(playerMoney);
			}
			if (top.isEmpty()) {
				return;
			}
			topList.clear();
			List<PlayerMoney> getTop = getTop(10);
			topList.addAll(getTop);
			String magnataAtual = getMagnata();
			String newMagnata = getTop.get(0).getPlayerName();
			if (!(magnataAtual.equalsIgnoreCase(newMagnata))) {
				magnata = newMagnata;
				config.set("magnata", newMagnata);
				config.save(new File(instance.getDataFolder(), "economy.yml"));
				Titles title = new Titles();
				title.setTitle("§2" + newMagnata);
				title.setSubtitle("§a§ o novo Magnata!");
				title.broadcast();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<PlayerMoney> getTop(int size) {
		List<PlayerMoney> convert = new ArrayList<>();
		convert.addAll(top);
		if (convert.isEmpty()) {
			return convert;
		}
		Collections.sort(convert, new Comparator<PlayerMoney>() {

			@Override
			public int compare(PlayerMoney pt1, PlayerMoney pt2) {
				Float f1 = (float) pt1.getMoney();
				Float f2 = (float) pt2.getMoney();
				return f2.compareTo(f1);
			}
		});
		if (convert.size() > size) {
			convert = convert.subList(0, size);
		}
		return convert;
	}
	
	public void createAccount(PlayerMoney playerMoney) {
		try {
			String columns = "(name, playerName, money, reciveMoney)";
			String insert = "('" + playerMoney.getName() + "','" + playerMoney.getPlayerName() + "','" + playerMoney.getMoney() + "','" + playerMoney.isReciveMoney() + "')";
			SQLite sqlite = instance.getSQLite();
			sqlite.execute("INSERT INTO money " + columns + " " + " VALUES " + insert + ";", true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addMoney(String name, double valor) {
		PlayerMoney playerMoney = cache.get(name.toLowerCase());
		if (playerMoney == null) {
			return;
		}
		playerMoney.setMoney(playerMoney.getMoney() + valor);
		playerMoney.update(instance);
		cache.put(playerMoney.getName(), playerMoney);
	}
	
	public void removeMoney(String name, double valor) {
		PlayerMoney playerMoney = cache.get(name.toLowerCase());
		if (playerMoney == null) {
			return;
		}
		playerMoney.setMoney(playerMoney.getMoney() - valor);
		playerMoney.update(instance);
		cache.put(playerMoney.getName(), playerMoney);
	}
	
	public String getBalance(PlayerMoney playerMoney) {
		return "§2$§f" + format(playerMoney.getMoney());
	}
	
	public String getBalance(double value) {
		return "§2$§f" + format(value);
	}
	
	private String formatValue(double value) {
		boolean isWholeNumber = value == Math.round(value);
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
		formatSymbols.setDecimalSeparator('.');
		String pattern = isWholeNumber ? "###,###.###" : "###,##0.00";
		DecimalFormat df = new DecimalFormat(pattern, formatSymbols);
		return df.format(value);
	}
	
	public String format(double amount) {
		amount = getMoneyRounded(amount);
		String suffix = " ";
		if (amount > 0.00 && amount < 1.0) {
			if (amount == 0.01) {
				suffix += "";
			} else if (amount < 1.0) {
				suffix += "";
			}
			amount *= 100;
		} else if (amount == 1.0) {
			suffix += "";
		} else {
			suffix += "";
		}
		if (suffix.equalsIgnoreCase(" ")) {
			suffix = "";
		}
		return formatValue(amount);
	}
	
	public double getMoneyRounded(double amount) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		String formattedAmount = twoDForm.format(amount);
		formattedAmount = formattedAmount.replace(",", ".");
		return Double.valueOf(formattedAmount);
	}
	
	public boolean isDouble(String valor) {
		if (valor.contains("NaN")) {
			return false;
		}
		try {
			double d = Double.valueOf(valor);
			if (d <= 0) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public PlayerMoney getCache(String name) {
		return cache.get(name.toLowerCase());
	}
	
	public void putCache(PlayerMoney playerMoney) {
		cache.put(playerMoney.getName(), playerMoney);
	}
	
	public HashMap<String, PlayerMoney> getCache() {
		return cache;
	}
	
	public String getMagnata() {
		return magnata;
	}
	
	public List<PlayerMoney> getTopList() {
		return topList;
	}
}