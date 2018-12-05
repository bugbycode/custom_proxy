package com.bugbycode.server.listener;

import com.bugbycode.server.service.impl.SimpleWriteTcpRecv;
import com.bugbycode.server.service.simple.SimpleServerReadTcpRecv;

public class SimpleInitServerListener extends AbstractInitServerListener {

	@Override
	public void init() {
		this.setRead(new SimpleServerReadTcpRecv());
		this.setWrite(new SimpleWriteTcpRecv());
	}

}
