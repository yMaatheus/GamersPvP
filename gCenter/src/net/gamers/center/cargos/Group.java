package net.gamers.center.cargos;

import java.util.HashSet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Group {
	
	private String name;
	private String preffix;
	private int rank;
	private boolean vip;
	private HashSet<String> permissions;
	
	public Group(String name, String preffix, boolean vip, int rank) {
		this.name = name;
		this.preffix = preffix;
		this.rank = rank;
		this.vip = vip;
		this.permissions = new HashSet<String>();
	}
	
	public Group(String name, String preffix, int rank, boolean vip, HashSet<String> permissions) {
		this.name = name;
		this.preffix = preffix;
		this.rank = rank;
		this.vip = vip;
		this.permissions = permissions;
	}
}