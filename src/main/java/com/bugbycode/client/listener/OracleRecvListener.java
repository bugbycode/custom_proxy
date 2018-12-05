package com.bugbycode.client.listener;

import java.io.IOException;

public interface OracleRecvListener {
	
	public void read(byte[] buff) throws IOException;
	
	public void close();
}
