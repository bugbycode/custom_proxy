package com.bugbycode.server.service.impl;

import java.io.IOException;
import java.io.OutputStream;

import com.bugbycode.server.service.WriteTcpRecv;

public class SimpleWriteTcpRecv implements WriteTcpRecv {

	private OutputStream out;
	
	@Override
	final public void close() {
		try {
			if(this.out != null) {
				this.out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	final public void setOutputStream(OutputStream out) {
		if(this.out == null) {
			this.out = out;
		}
	}

	@Override
	final public void write(byte[] buff) throws IOException {
		out.write(buff);
		out.flush();
	}

}
