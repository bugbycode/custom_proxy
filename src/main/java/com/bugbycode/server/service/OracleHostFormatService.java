package com.bugbycode.server.service;

import java.io.IOException;

import com.bugbycode.server.module.OracleServer;

public interface OracleHostFormatService {
	
	public OracleServer format(byte[] buff) throws IOException;
}
