package net.gamerspvp.commons.bukkit.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import com.google.common.collect.ImmutableList;

import lombok.Getter;
import lombok.Setter;

public class Hologram {
	
	private boolean spawned;
	@Getter
	@Setter
	private Location location;
	private Map<Integer, HologramLine> lines = new HashMap<>();
	private double linesSpace;
	
	/*
	 * 
	 *  if (clans == null) {
			Hologram clans = new Hologram(location, 0.25,
	  	 "§fO top atualiza em §1§l60 SEGUNDOS§f.",
	  	 "","","", "", "", "", "", "", "", "", "", "",
	  	 "§fcom os clans com mais §9kills§f.",
	  	 "§fO top clans é ordenado de acordo",
	  	 "§1§lTOP 10 CLAN");
			clans.spawn();
	  	 this.clans = clans;
	  	} else {
	  	 clans.despawn();
	  	 clans.setLocation(location);
	  	 clans.spawn();
	 * 
	 * 
	 * 
	 * 
	 * String locString = instance.getConfigurationManager().getConfig().getString("hologram.top."+hologramaName+".loc");
	 Location location = LocationString.stringToLocation(locString);
	 Hologram clans = new Hologram(location);
	 clans.addLineColado("§fO top atualiza em §1§l60 SEGUNDOS§f.");
	 for (int i = 0; i < 12; i++) { clans.addLineColado("");}
	 clans.addLineColado("§fcom os clans com mais §9kills§f.");
	 clans.addLineColado("§fO top clans é ordenado de acordo");
	 clans.addLineColado("§1§lTOP 10 CLAN");

	 clans.spawn();
	 this.clans = clans;
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	
	//0.33 distancia de linha para holograma na cabeça de npc
	public Hologram(Location location, double distancLine, String... lines) {
		this.location = location;
		int index = 0;
		//System.out.println(lines.toString());
		List<String> list = new ArrayList<>();
		for (String line : lines) {
			list.add(line);
		}
		//System.out.println(list.toString());
		Collections.reverse(list);
		for (String line : list) {
			this.lines.put(++index, new HologramLine(this, location.clone().add(0, distancLine * index, 0), line));
		}
	}
	
	public Hologram(Location location) {
		this.location = location;
	}
	
	public void spawn() {
		if (spawned) {
			return;
		}
		spawned = true;
		lines.forEach((index, hologram) -> {
			hologram.setLocation(location.clone().add(0, 0.25 * index, 0));
			hologram.spawn();
		});
	}
	
	public void despawn() {
		if (!spawned) {
			return;
		}
		spawned = false;
		lines.values().forEach(line -> line.despawn());
	}
	
	public void addLineDefalt(String text) {
		int line = 1;
		while (lines.containsKey(line)) {
			line++;
		}
		
		HologramLine hl = new HologramLine(this, location.clone().add(0, 0.33 * line, 0), text);
		lines.put(line, hl);
		if (spawned) {
			hl.spawn();
		}
	}
	
	public void pularLinha() {
		this.linesSpace += 0.25;
	}
	
	public void addLineColado(String text) {
		int line = 1;
		this.linesSpace += 0.25;
		while (lines.containsKey(line)) {
			line++;
		}
		HologramLine hl = new HologramLine(this, this.getLocation().clone().add(0.0, this.linesSpace, 0.0), text);
		lines.put(line, hl);
		if (spawned) {
			hl.spawn();
		}
	}
	
	public void addLineOrdenado(String text) {
		int line = 1; 
		this.linesSpace -= 0.25;
		while (lines.containsKey(line)) {
			line++;
		}

		HologramLine hl = new HologramLine(this, this.getLocation().clone().add(0.0, this.linesSpace, 0.0), text);
		lines.put(line, hl);
		if (spawned) {
			hl.spawn();
		}
	}
	
	public void updateLine(int line, String text) {
		if (lines.containsKey(line)) {
			lines.get(line).setText(text);
		}
	}
	
	public HologramLine getLine(int line) {
		if (lines.containsKey(line)) {
			return lines.get(line);
		}
		return null;
	}
	
	public void editLine(int line, String text, Location location) {
		HologramLine hologramLine = lines.get(line);
		if (hologramLine == null) {
			return;
		}
		hologramLine.setText(text);
		hologramLine.setLocation(location);
		hologramLine.despawn();
		hologramLine.spawn();
		return;
	}
	
	public void editLine(int line, Location location) {
		HologramLine hologramLine = lines.get(line);
		if (hologramLine == null) {
			return;
		}
		hologramLine.setLocation(location);
		hologramLine.despawn();
		hologramLine.spawn();
		return;
	}
	
	public void editLineY(int line, double y) {
		HologramLine hologramLine = lines.get(line);
		if (hologramLine == null) {
			return;
		}
		hologramLine.getLocation().setY(y);
		hologramLine.despawn();
		hologramLine.spawn();
		return;
	}
	
	public void reverseLines() {
		HashMap<HologramLine, Integer> keys = new HashMap<>();
		for (int key : lines.keySet()) {
			HologramLine value = lines.get(key);
			keys.put(value, key);
		}
		List<HologramLine> values = new ArrayList<HologramLine>();
		values.addAll(lines.values());
		Collections.sort(values, new Comparator<HologramLine>() {
		    @Override
		    public int compare(HologramLine pt1, HologramLine pt2) {
		        Integer f1 = keys.get(pt1);
		        Integer f2 = keys.get(pt2);
		        return f2.compareTo(f1);
		    }
		});
		HashMap<Integer, HologramLine> l = new HashMap<>();
		int index = 0;
		for (HologramLine hologramLine : values) {
			l.put(++index, hologramLine);
		}
		this.lines = new HashMap<>(l);
		despawn();
		spawn();
	}
	
	public boolean isSpawned() {
		return spawned;
	}
	
	public Location getInitLocation() {
		return location;
	}
	
	public List<HologramLine> getLines() {
		return ImmutableList.copyOf(lines.values());
	}

	public Map<Integer, HologramLine> getLines2() {
		return lines;
	}
}