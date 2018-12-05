package com.bugbycode.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.bugbycode.server.listener.InitServerListener;
import com.bugbycode.server.thread.OracleTcpRecvThread;

public class OracleSocketServer {
	
	private int port;
	
	private ServerSocket server;
	
	private InitServerListener listener;
	
	public OracleSocketServer(int port) throws IOException {
		this.port = port;
		this.server = new ServerSocket(this.port);
	}
	
	public void addListener(InitServerListener listener) {
		this.listener = listener;
	}
	
	public void accept() {
		if(this.listener == null) {
			throw new RuntimeException("InitServerListener is null.");
		}
		while(!this.server.isClosed()) {
			try {
				Socket socket = this.server.accept();
				listener.init();
				new OracleTcpRecvThread(socket,listener.getRead(),
						listener.getWrite()).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void close() throws IOException {
		if(this.server != null) {
			this.server.close();
		}
	}
}
