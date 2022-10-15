package net.gamers.center;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import org.simpleyaml.configuration.file.FileConfiguration;
import org.simpleyaml.configuration.file.YamlConfiguration;

import lombok.Getter;
import net.gamers.center.address.AddressManager;
import net.gamers.center.backup.BackupManager;
import net.gamers.center.cargos.CargosManager;
import net.gamers.center.database.DataCenterManager;
import net.gamers.center.database.Redis;
import net.gamers.center.discord.DiscordManager;
import net.gamers.center.games.status.GamesStatusManager;
import net.gamers.center.log.LogManager;
import redis.clients.jedis.JedisPubSub;

@Getter
public class Main {
	
	@Getter
	private static Main instance;
	private String dataFolder;
	private FileConfiguration config;
	private DataCenterManager dataCenter;
	private TaskUpdater taskUpdater;
	
	private AddressManager addressManager;
	private BackupManager backupManager;
	private CargosManager cargosManager;
	private DiscordManager discordManager;
	private GamesStatusManager gamesStatusManager;
	
	public void init() throws Exception {
		instance = this;
		this.dataFolder = new File(".").getCanonicalPath();
		this.config = loadConfig("settings.yml");
		this.dataCenter = new DataCenterManager(loadConfig("database.yml"));
		this.taskUpdater = new TaskUpdater();
		
		subscribe(dataCenter.getRedis()).start();
		
		this.addressManager = new AddressManager();
		this.backupManager = new BackupManager();
		this.cargosManager = new CargosManager(loadConfig("cargos.yml"));
		this.discordManager = new DiscordManager();
		this.gamesStatusManager = new GamesStatusManager();
		
		
		System.out.println("[INFO] gCenter started!");
		taskUpdater.init();
	}
	
	public Thread subscribe(Redis redis) {
		return new Thread("Redis Subscriber") {
			@Override
			public void run() {
				redis.getJedis().subscribe(new JedisPubSub() {
					@Override
					public void onMessage(String c, String m) {
						String channel = m.split(";")[0];
						String message = m.split(";")[1];
						MessageEvent event = new MessageEvent(channel, message);
						if (message.length() < 1000) {
							System.out.println("[Redis] Message recivied " + m);
						} else {
							System.out.println("[Redis] Message recivied " + channel + " ...");
						}
						
						LogManager.onMessageEvent(event);
					}
				}, "general");
			}
		};
	}
	
	public FileConfiguration loadConfig(String fileName) throws URISyntaxException, IOException {
		String destinoFile = dataFolder + File.separator + fileName;
		if (!(new File(destinoFile).exists())) {
			InputStream intput = getFileInJar(fileName);
			OutputStream output = new FileOutputStream(destinoFile);
			byte[] buffer = new byte[intput.available()];
			intput.read(buffer);
			output.write(buffer);
			if (intput != null)
				intput.close();
			if (output != null)
				output.close();
			System.out.println("[INFO] File created: " + destinoFile);
		}
		File file = new File(fileName);
		return YamlConfiguration.loadConfiguration(file);
	}
	
	public InputStream getFileInJar(String fileName) throws URISyntaxException {
		String arquivo = fileName;
		return getClass().getClassLoader().getResourceAsStream(arquivo);
	}
	
	public static void main(String[] args) {
		try {
			new Main().init();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[INFO] Não foi posível iniciar o Center!");
			System.exit(0);
		}
	}
}