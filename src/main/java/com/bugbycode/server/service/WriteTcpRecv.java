package com.bugbycode.server.service;

import java.io.IOException;
import java.io.OutputStream;

public interface WriteTcpRecv {
	
	public void close();
	
	public void setOutputStream(OutputStream out);
	
	public void write(byte[] buff) throws IOException;
}
