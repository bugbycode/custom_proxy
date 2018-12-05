package com.bugbycode.server.service.impl;

import java.io.IOException;

import com.bugbycode.server.service.ReadTcpRecv;
import com.bugbycode.server.service.WriteTcpRecv;

public abstract class AbstractReadTcpRecv implements ReadTcpRecv {

	private WriteTcpRecv write;
	
	@Override
	final public void setWriteTcpRecv(WriteTcpRecv write) {
		this.write = write;
	}

	@Override
	final public void read(byte[] buff) {
		try {
			read(this.write, buff);
		} catch (Exception e) {
			e.printStackTrace();
			write.close();
		}
	}

	public abstract void read(WriteTcpRecv write,byte[] buff) throws Exception;

	public abstract void connection(String host,int port) throws IOException;
	
	public abstract void close();
	
	final public WriteTcpRecv getWrite() {
		return write;
	}
	
}
