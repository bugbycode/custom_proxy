package com.test;

import java.io.IOException;

import org.junit.Test;

import com.bugbycode.server.OracleSocketServer;
import com.bugbycode.server.listener.SimpleInitServerListener;

public class StartupTest {
	
	@Test
	public void testServer() throws IOException {
		OracleSocketServer server = new OracleSocketServer(1521);
		server.addListener(new SimpleInitServerListener());
		server.accept();
	}
}
