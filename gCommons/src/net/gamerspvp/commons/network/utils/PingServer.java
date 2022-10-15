package net.gamerspvp.commons.network.utils;

import java.io.IOException;
import java.net.Socket;

public class PingServer {
	
	public static boolean hasOnlineServer(String address, int port) {
		try {
			Socket s = new Socket(address, port);
			s.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}