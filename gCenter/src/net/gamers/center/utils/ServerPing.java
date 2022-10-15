package net.gamers.center.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.ParseException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.Getter;

public class ServerPing {
	
	private InetSocketAddress host;
	public JsonObject json;
	
	private int timeoutInt = 5;
	@Getter
	private String motd = null;
	@Getter
	private int maxPlayers = 0;
	@Getter
	private int onlinePlayers = 0;
	
	public ServerPing(String address, int port) {
		host = new InetSocketAddress(address, port);
		this.timeoutInt = 1000;
		try {
			fetchData();
		} catch (IOException | ParseException e) {
		}
	}
	
	@SuppressWarnings({ "resource", "deprecation" })
	public void fetchData() throws IOException, ParseException {
		Socket socket = new Socket();
		OutputStream outputStream;
		DataOutputStream dataOutputStream;
		InputStream inputStream;
		InputStreamReader inputStreamReader;

		socket.setSoTimeout(timeoutInt);

		socket.connect(host, timeoutInt);

		outputStream = socket.getOutputStream();
		dataOutputStream = new DataOutputStream(outputStream);

		inputStream = socket.getInputStream();
		inputStreamReader = new InputStreamReader(inputStream);

		ByteArrayOutputStream b = new ByteArrayOutputStream();

		DataOutputStream handshake = new DataOutputStream(b);
		handshake.writeByte(0x00);
		writeVarInt(handshake, 47);
		writeVarInt(handshake, host.getHostString().length());

		handshake.writeBytes(host.getHostString());
		handshake.writeShort(host.getPort());
		writeVarInt(handshake, 1);

		writeVarInt(dataOutputStream, b.size());
		dataOutputStream.write(b.toByteArray());

		dataOutputStream.writeByte(0x01);
		dataOutputStream.writeByte(0x00);
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		readVarInt(dataInputStream);

		int id = readVarInt(dataInputStream);

		if (id == -1) {
			throw new IOException("ERRO");
		}

		int length = readVarInt(dataInputStream);

		if (length == -1) {
			throw new IOException("ERRO");
		}

		byte[] in = new byte[length];

		dataInputStream.readFully(in);
		String json = new String(in);

		long now = System.currentTimeMillis();
		dataOutputStream.writeByte(0x09);
		dataOutputStream.writeByte(0x01);
		dataOutputStream.writeLong(now);

		readVarInt(dataInputStream);
		id = readVarInt(dataInputStream);
		if (id == -1) {
			throw new IOException("ERRO");
		}
		
		this.json = (JsonObject) new JsonParser().parse(json);
		dataOutputStream.close();
		outputStream.close();
		inputStreamReader.close();
		inputStream.close();
		socket.close();
		processarTudo();
	}
	
	public void processarTudo() {
		this.motd = String.valueOf(getJson().get("description")).replace("Â", "");
		JsonArray array = new JsonArray();
		JsonObject site = null;
		array.add(getJson().get("players"));
		
		for (int i = 0; i < array.size(); i++) {
			site = (JsonObject) array.get(i);
		}
		maxPlayers = site.get("max").getAsInt();
		onlinePlayers = site.get("online").getAsInt();
	}
	
	private int readVarInt(DataInputStream in) throws IOException {
		int i = 0;
		int j = 0;
		while (true) {
			int k = in.readByte();
			i |= (k & 0x7F) << j++ * 7;
			if (j > 5) {
				throw new RuntimeException("VarInt muito grande");
			}
			if ((k & 0x80) != 128) {
				break;
			}
		}
		return i;
	}
	
	private void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
		while (true) {
			if ((paramInt & 0xFFFFFF80) == 0) {
				out.writeByte(paramInt);
				return;
			}

			out.writeByte(paramInt & 0x7F | 0x80);
			paramInt >>>= 7;
		}
	}
	
	private JsonObject getJson() {
		if (json == null) {
			return null;
		}
		return json;
	}
	
	public boolean isClosedServer() {
		return json == null;
	}
}