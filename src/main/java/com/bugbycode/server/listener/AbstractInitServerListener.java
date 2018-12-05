package com.bugbycode.server.listener;

import com.bugbycode.server.service.ReadTcpRecv;
import com.bugbycode.server.service.WriteTcpRecv;

public abstract class AbstractInitServerListener implements InitServerListener {

	private ReadTcpRecv read;
	
	private WriteTcpRecv write;
	
	@Override
	final public ReadTcpRecv getRead() {
		return this.read;
	}

	@Override
	final public WriteTcpRecv getWrite() {
		return this.write;
	}

	public abstract void init();

	final public void setRead(ReadTcpRecv read) {
		this.read = read;
	}

	final public void setWrite(WriteTcpRecv write) {
		this.write = write;
	}
	
}
