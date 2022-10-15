package net.gamers.center.utils;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class FtpClient {
	
	private final String server;
	private final int port;
	private final String user;
	private final String password;
	private FTPClient ftp;
	
	public FtpClient(String server, int port, String user, String password) {
		this.server = server;
		this.port = port;
		this.user = user;
		this.password = password;
	}
	
	public void open() throws IOException {
		ftp = new FTPClient();
		
		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		
		ftp.connect(server, port);
		int reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			throw new IOException("Exception in connecting to FTP Server");
		}
		
		ftp.login(user, password);
	}
	
	public void close() throws IOException {
		ftp.disconnect();
	}
	
	public Collection<String> listFiles(String path) throws IOException {
		FTPFile[] files = ftp.listFiles(path);
		
		return Arrays.stream(files).map(FTPFile::getName).collect(Collectors.toList());
	}
	
	public void putFileToPath(File file, String path) throws IOException {
		ftp.storeFile(path, new FileInputStream(file));
	}
	
    public void downloadFile(String source, String destination) throws IOException {
		FileOutputStream out = new FileOutputStream(destination);
		ftp.retrieveFile(source, out);
		out.close();
	}
}