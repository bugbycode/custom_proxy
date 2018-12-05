package com.bugbycode.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.bugbycode.client.listener.OracleRecvListener;
import com.util.TransferUtil;

public class OracleSocketClient {
	
	private Socket socket;
	
	private InputStream in;
	
	private OutputStream out;
	
	private OracleRecvListener recv;
	
	public OracleSocketClient(OracleRecvListener recv) {
		this.socket = new Socket();
		this.recv = recv;
	}
	
	public void connection(String host,int port,int timeout) throws IOException {
		this.socket.connect(new InetSocketAddress(host, port), timeout);
		this.in = this.socket.getInputStream();
		this.out = this.socket.getOutputStream();
		new WorkThread().start();
	}
	
	public void close() throws IOException {
		this.socket.close();
		if(this.out != null) {
			this.out.close();
		}
		if(this.in != null) {
			this.in.close();
		}
		this.recv.close();
	}
	
	public void writeAndFlush(byte[] buff) throws IOException {
		if(this.out != null) {
			this.out.write(buff);
			this.out.flush();
		}
	}
	
	private class WorkThread extends Thread{

		public WorkThread() {
			
		}
		
		@Override
		public void run() {
			byte[] buff = null;
			byte[] tmp;
			int buff_len = -1;
			int offset = 0;
			try {
				while(!socket.isClosed()) {
					if(buff_len == -1) {
						buff = new byte[2];
						offset += in.read(buff,offset,buff.length);
						if(offset < 0) {
							break;
						}
						if(offset == buff.length) {
							buff_len = TransferUtil.toHH(buff);
							tmp = new byte[buff_len];
							System.arraycopy(buff, 0, tmp, 0, offset);
							buff = tmp;
						}
					}else {
						offset += in.read(buff,offset,buff_len - 2);
						if(offset < 0) {
							break;
						}
						if(offset == buff_len) {
							recv.read(buff);;
							offset = 0;
							buff_len = -1;
						}
					}
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
				//e.printStackTrace();
			} finally {
				if(recv != null) {
					recv.close();
				}
				try {
					if(in != null) {
						in.close();
					}
					if(out != null) {
						out.close();
					}
					if(socket != null) {
						socket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
