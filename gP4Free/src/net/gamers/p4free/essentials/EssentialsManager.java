package net.gamers.p4free.essentials;

import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import net.gamers.p4free.Main;
import net.gamers.p4free.essentials.listeners.AnvilColors;
import net.gamers.p4free.essentials.listeners.AnvilFall;
import net.gamers.p4free.essentials.listeners.AnvilInfinity;
import net.gamers.p4free.essentials.listeners.BreakPlantationJump;
import net.gamers.p4free.essentials.listeners.CactusDamage;
import net.gamers.p4free.essentials.listeners.DerreterIceAndSnow;
import net.gamers.p4free.essentials.listeners.EssentialsListeners;
import net.gamers.p4free.essentials.listeners.ExplodeItem;
import net.gamers.p4free.essentials.listeners.FlowWaterFire;
import net.gamers.p4free.essentials.listeners.FoodLevelChange;
import net.gamers.p4free.essentials.listeners.IlegalNicknames;
import net.gamers.p4free.essentials.listeners.InventoryClickShift;
import net.gamers.p4free.essentials.listeners.InventoryOpen;
import net.gamers.p4free.essentials.listeners.LeavesDecay;
import net.gamers.p4free.essentials.listeners.MobsOnFireSun;
import net.gamers.p4free.essentials.listeners.PlayerBedEnter;
import net.gamers.p4free.essentials.listeners.PlayerBypassBorda;
import net.gamers.p4free.essentials.listeners.PlayerPortal;
import net.gamers.p4free.essentials.listeners.PlayerUseNameTag;
import net.gamers.p4free.essentials.listeners.PortalCreate;
import net.gamers.p4free.essentials.listeners.PrepareItemCraft;
import net.gamers.p4free.essentials.listeners.PropagacaoFogo;
import net.gamers.p4free.essentials.listeners.SignsColors;
import net.gamers.p4free.essentials.listeners.SpawnNatureMobs;
import net.gamers.p4free.essentials.listeners.VehicleEnter;
import net.gamers.p4free.essentials.listeners.VoidFall;
import net.gamers.p4free.essentials.listeners.WeatherChange;

public class EssentialsManager {
	
	private Main instance;
	
	private String spawnWorld;
	
	private boolean bed, joinVehicles, useNameTag, bypassBorda, explodeItens, derreterGeloNeve, breakPlantJumping,
			cactusDamage, dayCicle, spawnNatureMobs, propagacaoFogo, anvilFall, leavesDecay, flowWaterFire, chuva, anvilInfinity,
			coresAnvil, coresSigns, voidFall, mobsOnFireSun, hunger, createPortals, teleportPortals;
	private List<String> blockedOpenContainers, blockedShiftContainers;
	private HashSet<Integer> blockedCraftIds;
	private HashSet<String> blockedNicks;

	public EssentialsManager(FileConfiguration config, Main instance) throws Exception {
		this.instance = instance;
		this.spawnWorld = config.getString("spawnWorld");
		this.bed = config.getBoolean("Recursos.bloquearCama");
		this.joinVehicles = config.getBoolean("Recursos.bloquearSubirVeiculos");
		this.useNameTag = config.getBoolean("Recursos.bloquearUsarNameTag");
		this.bypassBorda = config.getBoolean("Recursos.bloquearPassarDaBorda");
		this.explodeItens = config.getBoolean("Recursos.bloquearExplodirItens");
		this.derreterGeloNeve = config.getBoolean("Recursos.bloquearDerreterGeloENeve");
		this.breakPlantJumping = config.getBoolean("Recursos.bloquearQuebrarPlantacoesPulando");
		this.cactusDamage = config.getBoolean("Recursos.desativarDanoDeCactus");
		this.dayCicle = config.getBoolean("Recursos.desativarCicloDoDia");
		this.spawnNatureMobs = config.getBoolean("Recursos.desativarMobsNaturais");
		this.propagacaoFogo = config.getBoolean("Recursos.desativarPropagacaoFogo");
		this.leavesDecay = config.getBoolean("Recursos.desativarQuedaDeFolhas");
		this.anvilFall = config.getBoolean("Recursos.desativarQuedaBigorna");
		this.flowWaterFire = config.getBoolean("Recursos.desativarEscorrerAguaLava");
		this.chuva = config.getBoolean("Recursos.desativarChuva");
		this.anvilInfinity = config.getBoolean("Recursos.bigornaInfinita");
		this.coresAnvil = config.getBoolean("Recursos.ativarCorBigorna");
		this.coresSigns = config.getBoolean("Recursos.ativarCorPlacas");
		this.voidFall = config.getBoolean("Recursos.bloquearCairVoid");
		this.mobsOnFireSun = config.getBoolean("Recursos.bloquearMobsPegaremFogoParaSol");
		this.hunger = config.getBoolean("Recursos.desativarFome");
		this.createPortals = config.getBoolean("Recursos.bloquearCriarPortais");
		this.teleportPortals = config.getBoolean("Recursos.bloquearTeleportPorPortais");
		this.blockedOpenContainers = config.getStringList("Recursos.bloquearAbrirContainers");
		this.blockedShiftContainers = config.getStringList("Recursos.bloquearShiftEmContainers");
		this.blockedCraftIds = new HashSet<Integer>(config.getIntegerList("Recursos.bloquearCraft"));
		this.blockedNicks = new HashSet<String>(config.getStringList("Recursos.bloquearNicks"));
		loadListeners();
	}

	private void loadListeners() {
		PluginManager pluginManager = Bukkit.getPluginManager();
		if (bed) {
			pluginManager.registerEvents(new PlayerBedEnter(), instance);
		}
		if (joinVehicles) {
			pluginManager.registerEvents(new VehicleEnter(), instance);
		}
		if (useNameTag) {
			pluginManager.registerEvents(new PlayerUseNameTag(), instance);
		}
		if (bypassBorda) {
			pluginManager.registerEvents(new PlayerBypassBorda(), instance);
		}
		if (explodeItens) {
			pluginManager.registerEvents(new ExplodeItem(), instance);
		}
		if (derreterGeloNeve) {
			pluginManager.registerEvents(new DerreterIceAndSnow(), instance);
		}
		if (breakPlantJumping) {
			pluginManager.registerEvents(new BreakPlantationJump(), instance);
		}
		if (cactusDamage) {
			pluginManager.registerEvents(new CactusDamage(), instance);
		}
		if (dayCicle) {
			new BukkitRunnable() {
				public void run() {
					for (World world : Bukkit.getWorlds()) {
						world.setGameRuleValue("doDaylightCycle", "false");
						world.setTime(6000L);
					}
				}
			}.runTaskLater(Main.getInstance(), 600L);
		}
		if (spawnNatureMobs) {
			pluginManager.registerEvents(new SpawnNatureMobs(), instance);
		}
		if (propagacaoFogo) {
			pluginManager.registerEvents(new PropagacaoFogo(), instance);
		}
		if (leavesDecay) {
			pluginManager.registerEvents(new LeavesDecay(), instance);
		}
		if (anvilFall) {
			pluginManager.registerEvents(new AnvilFall(), instance);
		}
		if (flowWaterFire) {
			pluginManager.registerEvents(new FlowWaterFire(), instance);
		}
		if (chuva) {
			pluginManager.registerEvents(new WeatherChange(), instance);
		}
		if (anvilInfinity) {
			pluginManager.registerEvents(new AnvilInfinity(), instance);
		}
		if (coresAnvil) {
			pluginManager.registerEvents(new AnvilColors(), instance);
		}
		if (coresSigns) {
			pluginManager.registerEvents(new SignsColors(), instance);
		}
		if (voidFall) {
			pluginManager.registerEvents(new VoidFall(spawnWorld), instance);
		}
		if (mobsOnFireSun) {
			pluginManager.registerEvents(new MobsOnFireSun(), instance);
		}
		if (hunger) {
			pluginManager.registerEvents(new FoodLevelChange(), instance);
		}
		if (createPortals) {
			pluginManager.registerEvents(new PortalCreate(), instance);
		}
		if (teleportPortals) {
			pluginManager.registerEvents(new PlayerPortal(), instance);
		}
		if (!blockedOpenContainers.isEmpty()) {
			pluginManager.registerEvents(new InventoryOpen(blockedOpenContainers), instance);
		}
		if (!blockedShiftContainers.isEmpty()) {
			pluginManager.registerEvents(new InventoryClickShift(blockedShiftContainers), instance);
		}
		if (!blockedCraftIds.isEmpty()) {
			pluginManager.registerEvents(new PrepareItemCraft(blockedCraftIds), instance);
		}
		if (!blockedNicks.isEmpty()) {
			pluginManager.registerEvents(new IlegalNicknames(blockedNicks), instance);
		}
		pluginManager.registerEvents(new EssentialsListeners(), instance);
	}
}