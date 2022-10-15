package net.gamerspvp.commons.bukkit.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class MakeItem {

	Material item = Material.AIR;
	String skullowner = "";
	short durability = 0;
	int quantidade = 1;
	short sh = 0;
	String displayname = "";
	String URLSkull = "";
	List<String> lore = new ArrayList<>();
	HashMap<Enchantment, Integer> enchantLvl = new HashMap<>();
	HashMap<Enchantment, Integer> unsafeenchantLvl = new HashMap<>();
	boolean inq = false;
	List<ItemFlag> flags = new ArrayList<>();

	public MakeItem(Material m) {
		item = m;
	}

	public MakeItem() {
		
	}
	//

	public MakeItem setId(short sh) {
		this.sh = sh;
		return this;
	}

	public MakeItem setName(String name) {
		displayname = name;
		return this;
	}

	public MakeItem setQuantia(int quantia) {
		quantidade = quantia;
		return this;
	}

	public MakeItem setSkullURL(String URL) {
		this.URLSkull = URL;
		return this;
	}

	public MakeItem addLore(String... lore) {
		this.lore.addAll(Arrays.asList(lore));
		return this;
	}

	public MakeItem addLore(List<String> lore) {
		this.lore.addAll(lore);
		return this;
	}

	public MakeItem addEnchant(Enchantment enchant, Integer level) {
		enchantLvl.put(enchant, level);
		return this;
	}

	public MakeItem addUnsafeEnchant(Enchantment enchant, Integer level) {
		unsafeenchantLvl.put(enchant, level);
		return this;
	}

	public MakeItem setInquebravel(boolean inquebravel) {
		inq = inquebravel;
		return this;
	}

	public MakeItem addFlags(ItemFlag flag) {
		flags.add(flag);
		return this;
	}

	public MakeItem setSkullOwner(String owner) {
		skullowner = owner;
		return this;
	}

	public MakeItem setDurability(Short durability) {
		this.durability = durability;
		return this;
	}

	public ItemStack buildhead() {
		ItemStack i = new ItemStack(item, quantidade == 0 ? 1 : quantidade, (short) SkullType.PLAYER.ordinal());
		SkullMeta im = (SkullMeta) i.getItemMeta();
		im.setDisplayName(displayname);
		im.setOwner(skullowner);
		im.setLore(lore);
		i.setItemMeta(im);

		return i;
	}

	public ItemStack buildCustomBook() {
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();

		if (enchantLvl.size() > 0) {
			for (Entry<Enchantment, Integer> enchants : enchantLvl.entrySet()) {
				meta.addStoredEnchant(enchants.getKey(), enchants.getValue(), false);
			}
		}

		if (flags.size() > 0) {
			for (ItemFlag fl : flags) {
				meta.addItemFlags(fl);
			}
		}

		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack build() {
		ItemStack i = new ItemStack(item, quantidade == 0 ? 1 : quantidade, sh);
		ItemMeta im = i.getItemMeta();
		if (durability != 0) {
			i.setDurability(durability);
		}
		im.setDisplayName(displayname);
		im.setLore(lore);
		if (enchantLvl.size() > 0) {
			for (Entry<Enchantment, Integer> enchants : enchantLvl.entrySet()) {
				im.addEnchant(enchants.getKey(), enchants.getValue(), true);
			}
		}

		if (unsafeenchantLvl.size() > 0) {
			for (Entry<Enchantment, Integer> enchants : unsafeenchantLvl.entrySet()) {
				i.addUnsafeEnchantment(enchants.getKey(), enchants.getValue());
			}
		}

		im.spigot().setUnbreakable(inq);
		if (flags.size() > 0) {
			for (ItemFlag fl : flags) {
				im.addItemFlags(fl);
			}
		}
		i.setItemMeta(im);

		return i;
	}
}
