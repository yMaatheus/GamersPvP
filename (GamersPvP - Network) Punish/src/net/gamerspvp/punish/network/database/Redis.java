package net.gamerspvp.punish.network.database;

import redis.clients.jedis.Jedis;

public class Redis {
	
	private Jedis jedis;
	
	private String host;
	private String password;
	private int port;
	
	public Redis(String host, String password, int port) {
		this.host = host;
		this.password = password;
		this.port = port;
		System.out.println("[Redis] Abrindo conexão...");
		try {
			this.jedis = new Jedis(host, port);
			if (password != null) {
				jedis.auth(password);
			}
			System.out.println("[Redis] A conexão foi aberta com sucesso!");
		} catch (Exception e) {
			System.err.println("[Redis] Houve um problema ao tentar efetuar a conexão.");
		}
	}
	
	public Jedis getJedis() {
		if (jedis != null) {
			openConnection();
		}
		return jedis;
	}
	
	public boolean isConnecion() {
		return (jedis == null ? false : true);
	}
	
	public void openConnection() {
		try {
			this.jedis = new Jedis(host, port);
			if (password != null) {
				jedis.auth(password);
			}
		} catch (Exception e) {
			System.err.println("[Redis] Houve um problema ao tentar efetuar a conexão.");
		}
	}
}