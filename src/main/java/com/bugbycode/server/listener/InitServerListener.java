package com.bugbycode.server.listener;

import com.bugbycode.server.service.ReadTcpRecv;
import com.bugbycode.server.service.WriteTcpRecv;

public interface InitServerListener {
	
	public ReadTcpRecv getRead();
	
	public WriteTcpRecv getWrite();
	
	public void init();
	
}
