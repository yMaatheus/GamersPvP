package net.gamerspvp.central.database;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Getter
public class Redis {

	private Gson gson;
	private JedisPool jedisPool;
	private JedisPoolConfig config;
	
	private String host;
	private String password;
	private int port;
	private int select;
	
	public Redis(String host, String password, int port, int select) {
		this.gson = new Gson();
		this.host = host;
		this.password = password;
		this.port = port;
		this.select = select;
		System.out.println("[Redis] Abrindo conexão...");
		config = new JedisPoolConfig();
		config.setMaxTotal(8);
		config.setMaxIdle(8);
		config.setMinIdle(0);
		config.setTestWhileIdle(true);
		config.setMinEvictableIdleTimeMillis(60000);
		config.setTimeBetweenEvictionRunsMillis(30000);
		config.setNumTestsPerEvictionRun(-1);
		this.jedisPool = new JedisPool(config, host, port, 0, password, select);
		loadSetup(config);
		System.out.println("[Redis] A conexão foi aberta com sucesso!");
	}
	
	public void publish(String message) {
		Jedis jedis = getJedis();
		jedis.publish("general", message);
		close(jedis);
	}

	public Jedis getJedis() {
		if ((jedisPool == null) || (jedisPool.isClosed())) {
			jedisPool.destroy();
			this.jedisPool = new JedisPool(config, host, port, 0, password, select);
			loadSetup(config);
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.err.println("[Redis] Houve um problema ao tentar efetuar a conexão.");
		}
		return null;
	}
	
	public void close(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}
	
	private void loadSetup(JedisPoolConfig config) {
		List<Jedis> minIdleJedisList = new ArrayList<Jedis>(config.getMinIdle());
		for (int i = 0; i < config.getMinIdle(); i++) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				minIdleJedisList.add(jedis);
				jedis.ping();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < config.getMinIdle(); i++) {
			Jedis jedis = null;
			try {
				jedis = minIdleJedisList.get(i);
				jedis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}