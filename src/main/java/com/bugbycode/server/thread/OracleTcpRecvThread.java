package com.bugbycode.server.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.bugbycode.server.service.ReadTcpRecv;
import com.bugbycode.server.service.WriteTcpRecv;
import com.util.TransferUtil;

public class OracleTcpRecvThread extends Thread {

	private ReadTcpRecv read;
	
	private WriteTcpRecv write;
	
	private Socket socket;
	
	public OracleTcpRecvThread(Socket socket,ReadTcpRecv read,WriteTcpRecv write) {
		this.socket = socket;
		this.read = read;
		this.write = write;
	}
	
	@Override
	public void run() {
		byte[] buff = null;
		byte[] tmp;
		int buff_len = -1;
		int offset = 0;
		InputStream in = null;
		OutputStream out = null;
		try {
			in = this.socket.getInputStream();
			out = this.socket.getOutputStream();
			this.write.setOutputStream(out);
			this.read.setWriteTcpRecv(this.write);
			//this.read.connection("192.168.1.170", 1521);
			while(!this.socket.isClosed()) {
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
						this.read.read(buff);
						offset = 0;
						buff_len = -1;
					}
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		} finally {
			this.read.close();
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
