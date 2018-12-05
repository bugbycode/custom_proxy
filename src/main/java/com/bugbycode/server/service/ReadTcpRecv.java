package com.bugbycode.server.service;

import java.io.IOException;

public interface ReadTcpRecv {
	
	public void setWriteTcpRecv(WriteTcpRecv write);
	
	public void read(byte[] buff);
	
	public void connection(String host,int port) throws IOException;
	
	public void close();
}
