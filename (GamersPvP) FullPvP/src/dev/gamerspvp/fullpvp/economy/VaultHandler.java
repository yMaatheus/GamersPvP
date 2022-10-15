package dev.gamerspvp.fullpvp.economy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import dev.gamerspvp.fullpvp.Main;
import dev.gamerspvp.fullpvp.economy.models.PlayerMoney;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class VaultHandler implements Economy {
	
	@Override
	public EconomyResponse bankBalance(String arg0) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "Este plugin não possui suporte para este tipo de ação.");
	}
	
	@Override
	public EconomyResponse bankDeposit(String arg0, double arg1) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "Este plugin não possui suporte para este tipo de ação.");
	}
	
	@Override
	public EconomyResponse bankHas(String arg0, double arg1) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "Este plugin não possui suporte para este tipo de ação.");
	}
	
	@Override
	public EconomyResponse bankWithdraw(String arg0, double arg1) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "Este plugin não possui suporte para este tipo de ação.");
	}
	
	@Override
	public EconomyResponse createBank(String arg0, String arg1) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "Este plugin não possui suporte para este tipo de ação.");
	}
	
	@Override
	public EconomyResponse createBank(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "Este plugin não possui suporte para este tipo de ação.");
	}
	
	@Override
	public boolean createPlayerAccount(String name) {
		boolean sucess = false;
		EconomyManager economyManager = Main.getInstance().getEconomyManager();
		PlayerMoney playerMoney = new PlayerMoney(name);
		economyManager.createAccount(playerMoney);
		economyManager.putCache(playerMoney);
		Bukkit.getConsoleSender().sendMessage("§e[Economy] " + playerMoney.getPlayerName() + " teve sua conta criada.");
		sucess = true;
		return sucess;
	}
	
	@Override
	public boolean createPlayerAccount(OfflinePlayer arg0) {
		return createPlayerAccount(arg0.getName());
	}
	
	@Override
	public boolean createPlayerAccount(String player, String arg1) {
		return createPlayerAccount(player);
	}
	
	@Override
	public boolean createPlayerAccount(OfflinePlayer arg0, String arg1) {
		return createPlayerAccount(arg0);
	}
	
	@Override
	public String currencyNamePlural() {
		return "Money";
	}
	
	@Override
	public String currencyNameSingular() {
		return "Money";
	}
	
	@Override
	public EconomyResponse deleteBank(String arg0) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "Este plugin não possui suporte para este tipo de ação.");
	}
	
	@Override
	public EconomyResponse depositPlayer(String player, double valor) {
		if (valor > 0) {
			EconomyManager economyManager = Main.getInstance().getEconomyManager();
			economyManager.addMoney(player, valor);
			return new EconomyResponse(0, economyManager.getCache(player.toLowerCase()).getMoney(), ResponseType.SUCCESS, "");
		}
		return new EconomyResponse(0, 0, ResponseType.FAILURE, "Valor negativo");
	}
	
	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player, double valor) {
		return depositPlayer(player.getName(), valor);
	}
	
	@Override
	public EconomyResponse depositPlayer(String arg0, String arg1, double arg2) {
		return depositPlayer(arg0, arg2);
	}
	
	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player, String arg1, double valor) {
		return depositPlayer(player.getName(), valor);
	}
	
	@Override
	public String format(double valor) {
		EconomyManager economyManager = Main.getInstance().getEconomyManager();
		return economyManager.format(valor);
	}
	
	@Override
	public int fractionalDigits() {
		return -1;
	}
	
	@Override
	public double getBalance(String player) {
		EconomyManager economyManager = Main.getInstance().getEconomyManager();
		return economyManager.getCache(player.toLowerCase()).getMoney();
	}
	
	@Override
	public double getBalance(OfflinePlayer player) {
		return getBalance(player.getName());
	}
	
	@Override
	public double getBalance(String player, String arg1) {
		return getBalance(player);
	}
	
	@Override
	public double getBalance(OfflinePlayer player, String arg1) {
		return getBalance(player.getName());
	}
	
	@Override
	public List<String> getBanks() {
		return new ArrayList<String>();
	}
	
	@Override
	public String getName() {
		return "Economy";
	}
	
	@Override
	public boolean has(String player, double valor) {
		EconomyManager economyManager = Main.getInstance().getEconomyManager();
		PlayerMoney cache = economyManager.getCache(player.toLowerCase());
		if (cache == null) {
			return false;
		}
		double saldo = cache.getMoney();
		if (valor >= 0) {
			return saldo >= valor;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean has(OfflinePlayer player, double valor) {
		return has(player.getName(), valor);
	}
	
	@Override
	public boolean has(String player, String arg1, double valor) {
		return has(player, valor);
	}
	
	@Override
	public boolean has(OfflinePlayer player, String arg1, double valor) {
		return has(player.getName(), valor);
	}
	
	@Override
	public boolean hasAccount(String player) {
		EconomyManager economyManager = Main.getInstance().getEconomyManager();
		return economyManager.getCache(player.toLowerCase()) != null;
	}
	
	@Override
	public boolean hasAccount(OfflinePlayer arg0) {
		return hasAccount(arg0.getName());
	}
	
	@Override
	public boolean hasAccount(String arg0, String arg1) {
		return hasAccount(arg0);
	}
	
	@Override
	public boolean hasAccount(OfflinePlayer arg0, String arg1) {
		return hasAccount(arg0.getName());
	}
	
	@Override
	public boolean hasBankSupport() {
		return false;
	}
	
	@Override
	public EconomyResponse isBankMember(String arg0, String arg1) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "Este plugin não possui suporte para este tipo de ação.");
	}
	
	@Override
	public EconomyResponse isBankMember(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "Este plugin não possui suporte para este tipo de ação.");
	}
	
	@Override
	public EconomyResponse isBankOwner(String arg0, String arg1) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "Este plugin não possui suporte para este tipo de ação.");
	}
	
	@Override
	public EconomyResponse isBankOwner(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(0.0, 0.0, ResponseType.NOT_IMPLEMENTED, "Este plugin não possui suporte para este tipo de ação.");
	}
	
	@Override
	public boolean isEnabled() {
		return Main.getInstance().isEnabled();
	}
	
	@Override
	public EconomyResponse withdrawPlayer(String player, double valor) {
		if (valor < 0){
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Nagative value");
		}
		EconomyManager economyManager = Main.getInstance().getEconomyManager();
		economyManager.removeMoney(player, valor);
		return new EconomyResponse(0, economyManager.getCache(player.toLowerCase()).getMoney(), ResponseType.SUCCESS, "");
	}
	
	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, double valor) {
		return withdrawPlayer(player.getName(), valor);
	}
	
	@Override
	public EconomyResponse withdrawPlayer(String player, String arg1, double valor) {
		return withdrawPlayer(player, valor);
	}
	
	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, String arg1, double valor) {
		return withdrawPlayer(player.getName(), valor);
	}
}