package net.gamerspvp.commons.bukkit.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.minecraft.server.v1_8_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;

@SuppressWarnings("deprecation")
public class Item extends ItemStack {

	public Item() {
	}

	public Item(ItemStack itemStack) {
		super(itemStack);
	}

	public Item(Material material) {
		super(material);
	}

	public Item setAmounts(int amount) {
		setAmount(amount);
		return this;
	}

	public Item setDurabilitys(short durability) {
		setDurability(durability);
		return this;
	}

	public Item setDurabilitys(int durability) {
		setDurability((short) durability);
		return this;
	}

	public Item setDisplayName(String name) {
		ItemMeta itemMeta = getItemMeta();
		itemMeta.setDisplayName(name);
		setItemMeta(itemMeta);
		return this;
	}

	public String getDisplayName() {
		return getItemMeta().getDisplayName();
	}

	public Item setLore(String... lore) {
		ItemMeta itemMeta = getItemMeta();
		itemMeta.setLore(Arrays.asList(lore));
		setItemMeta(itemMeta);
		return this;
	}

	public void removeLore() {
		ItemMeta itemMeta = getItemMeta();
		itemMeta.setLore(null);
		setItemMeta(itemMeta);
	}

	public Item setLore(List<String> lore) {
		ItemMeta itemMeta = getItemMeta();
		itemMeta.setLore(lore);
		setItemMeta(itemMeta);
		return this;
	}

	public Item addLineLore(String... line) {
		List<String> lore = new ArrayList<String>();
		if (getItemMeta().hasLore()) {
			lore.addAll(getItemMeta().getLore());
		}
		for (String l : line) {
			lore.add(l);
		}
		ItemMeta itemMeta = getItemMeta();
		itemMeta.setLore(lore);
		setItemMeta(itemMeta);
		return this;
	}

	public Item clearLore() {
		ItemMeta itemMeta = getItemMeta();
		itemMeta.setLore(new ArrayList<String>());
		setItemMeta(itemMeta);
		return this;
	}

	public Item addFlag(ItemFlag flag) {
		ItemMeta itemMeta = getItemMeta();
		itemMeta.addItemFlags(flag);
		setItemMeta(itemMeta);
		return this;
	}

	public Item addFlag(ItemFlag... flag) {
		ItemMeta itemMeta = getItemMeta();
		itemMeta.addItemFlags(flag);
		setItemMeta(itemMeta);
		return this;
	}

	public Item addEnchant(Enchantment enchantment, int level) {
		ItemMeta itemMeta = getItemMeta();
		itemMeta.addEnchant(enchantment, level, true);
		setItemMeta(itemMeta);
		return this;
	}

	public Item addFakeEnchant() {
		net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(this);
		NBTTagCompound tag = null;
		if (!nmsStack.hasTag()) {

			tag = new NBTTagCompound();
			nmsStack.setTag(tag);

		}
		if (tag == null) {
			tag = nmsStack.getTag();
		}
		NBTTagList ench = new NBTTagList();
		tag.set("ench", ench);
		nmsStack.setTag(tag);
		setItemMeta(CraftItemStack.asCraftMirror(nmsStack).getItemMeta());
		return this;
	}

	public Item setUnbreakable(boolean b) {
		ItemMeta itemMeta = getItemMeta();
		itemMeta.spigot().setUnbreakable(b);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		setItemMeta(itemMeta);
		return this;
	}

	public Item setOwner(String owner) {
		SkullMeta skullMeta = (SkullMeta) getItemMeta();
		skullMeta.setOwner(owner);
		setItemMeta(skullMeta);
		return this;
	}


	public Item setColor(Color color) {
		LeatherArmorMeta itemMeta = (LeatherArmorMeta) getItemMeta();
		itemMeta.setColor(color);
		setItemMeta(itemMeta);
		return this;
	}

	public Item setColor(DyeColor color) {
		setDurability(color.getData());
		return this;
	}

	public Item setFireworkColor(Color color) {
		ItemMeta itemMeta = getItemMeta();
		FireworkEffectMeta fireworkMeta = (FireworkEffectMeta) itemMeta;
		FireworkEffect fireworkEffect = FireworkEffect.builder().withColor(color).build();
		fireworkMeta.setEffect(fireworkEffect);
		setItemMeta(fireworkMeta);
		return this;
	}

	public Item setFireworkColor(int blue, int green, int red) {
		ItemMeta itemMeta = getItemMeta();
		FireworkEffectMeta fireworkMeta = (FireworkEffectMeta) itemMeta;
		FireworkEffect fireworkEffect = FireworkEffect.builder().withColor(Color.fromBGR(blue, green, red)).build();
		fireworkMeta.setEffect(fireworkEffect);
		setItemMeta(fireworkMeta);
		return this;
	}

	public String toJson() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		OutputStream dataOutput = new DataOutputStream(outputStream);
		NBTTagCompound outputObject = new NBTTagCompound();
		CraftItemStack craft = CraftItemStack.asCraftCopy(this);
		if (craft != null)
			CraftItemStack.asNMSCopy(craft).save(outputObject);
		try {
			NBTCompressedStreamTools.a(outputObject, dataOutput);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new BigInteger(1, outputStream.toByteArray()).toString(32);
	}

	public ItemStack fromJson(String json) {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(json, 32).toByteArray());
		NBTTagCompound item = null;
		try {
			item = NBTCompressedStreamTools.a(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		net.minecraft.server.v1_8_R3.ItemStack stack = net.minecraft.server.v1_8_R3.ItemStack.createStack(item);
		return CraftItemStack.asCraftMirror(stack);
	}

	public String toJson2() throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		OutputStream dataOutput = new DataOutputStream(outputStream);
		NBTTagCompound outputObject = new NBTTagCompound();
		CraftItemStack craft = CraftItemStack.asCraftCopy(this);
		if (craft != null)
			CraftItemStack.asNMSCopy(craft).save(outputObject);
		NBTCompressedStreamTools.a(outputObject, dataOutput);
		return new BigInteger(1, outputStream.toByteArray()).toString(32);
	}

	public ItemStack fromJson2(String json) throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(json, 32).toByteArray());
		NBTTagCompound item = null;
		item = NBTCompressedStreamTools.a(inputStream);
		net.minecraft.server.v1_8_R3.ItemStack stack = net.minecraft.server.v1_8_R3.ItemStack.createStack(item);
		return CraftItemStack.asCraftMirror(stack);
	}

	public NBTTagCompound getTag() {
		net.minecraft.server.v1_8_R3.ItemStack itemNms = CraftItemStack.asNMSCopy(this);
		NBTTagCompound tag;
		if (itemNms != null && itemNms.hasTag())
			tag = itemNms.getTag();
		else
			tag = new NBTTagCompound();
		return tag;
	}

	public boolean hasTag() {
		return getTag() != null;
	}

	public Item setTag(NBTTagCompound tag) {
		net.minecraft.server.v1_8_R3.ItemStack itemNms = CraftItemStack.asNMSCopy(this);
		CraftItemStack.setItemMeta(itemNms, getItemMeta());
		itemNms.setTag(tag);
		ItemStack asBukkitCopy = CraftItemStack.asBukkitCopy(itemNms);
		setItemMeta(asBukkitCopy.getItemMeta());
		return this;
	}

	public Item addString(String name, String value) {
		NBTTagCompound tag = getTag();
		tag.setString(name, value);
		setTag(tag);
		return this;
	}

	public Item removeString(String name) {
		NBTTagCompound tag = getTag();
		tag.remove(name);
		setTag(tag);
		return this;
	}

	public Item addBoolean(String name, boolean value) {
		NBTTagCompound tag = getTag();
		tag.setBoolean(name, value);
		setTag(tag);
		return this;
	}

	public Item addDouble(String name, double value) {
		NBTTagCompound tag = getTag();
		tag.setDouble(name, value);
		setTag(tag);
		return this;
	}

	public Item addInt(String name, int value) {
		NBTTagCompound tag = getTag();
		tag.setInt(name, value);
		return setTag(tag);
	}

	public boolean hasKey(String name) {
		NBTTagCompound tag = getTag();
		if (tag == null) {
			return false;
		}
		return tag.hasKey(name);
	}

	public String getString(String name) {
		NBTTagCompound tag = getTag();
		return tag.getString(name);
	}

	public int getInt(String name) {
		NBTTagCompound tag = getTag();
		return tag.getInt(name);
	}

	public double getDouble(String name) {
		NBTTagCompound tag = getTag();
		return tag.getDouble(name);
	}

	public boolean getBoolean(String name) {
		NBTTagCompound tag = getTag();
		return tag.getBoolean(name);
	}

	public boolean compareDisplayName(String displayName) {
		if (getItemMeta() != null) {
			if (getItemMeta().hasDisplayName()) {
				return getItemMeta().getDisplayName().equalsIgnoreCase(displayName);
			}
		}
		return false;
	}
}
