package com.bugbycode.server.service.impl;

import java.io.IOException;

import com.bugbycode.config.AppConfig;
import com.bugbycode.server.module.OracleServer;
import com.bugbycode.server.service.OracleHostFormatService;
import com.util.TransferUtil;

public class OracleHostFormatServiceImpl implements OracleHostFormatService {

	@Override
	public OracleServer format(byte[] buff) throws IOException {
		
		String host = "";
		int port = 0;
		String account = "";
		String service = "";
		
		int keyIndex = TransferUtil.findKey(AppConfig.CONNECTION_KEY, buff);
		
		byte[] conn_byte_arr = new byte[buff.length - keyIndex];
		
		System.arraycopy(buff, keyIndex, conn_byte_arr, 0, conn_byte_arr.length);
		
		String body = new String(conn_byte_arr);
		
		int serviceIndex = body.indexOf(AppConfig.SERVICE_NAME_KEY);
		int endIndex = body.indexOf(')', serviceIndex);
		String serviceName = body.substring(serviceIndex + AppConfig.SERVICE_NAME_KEY.length(), endIndex);
		if(serviceName.indexOf('@') == -1) {
			throw new IOException("SERVICE_NAME \"" + serviceName + "\" ERROR.");
		}
		String[] loginArr = serviceName.split("@");
		if(!(loginArr.length == 4 || loginArr.length == 5)) {
			throw new IOException("SERVICE_NAME \"" + serviceName + "\" ERROR.");
		}

		account = loginArr[0];
		host = loginArr[1];
		service = loginArr[3];
		if(loginArr.length == 5) {
			service += " as " + loginArr[4];
		}
		
		try {
			port = Integer.valueOf(loginArr[2]);
		}catch (NumberFormatException e) {
			throw new IOException("SERVICE_NAME \"" + serviceName + "\" ERROR.");
		}
		body = body.substring(0, serviceIndex + AppConfig.SERVICE_NAME_KEY.length()) + loginArr[3] + body.substring(serviceIndex + AppConfig.SERVICE_NAME_KEY.length() + serviceName.length());
		
		int hostLastIndex = body.lastIndexOf(AppConfig.HOST_KEY);
		endIndex = body.indexOf(')', hostLastIndex);
		body = body.substring(0, hostLastIndex + AppConfig.HOST_KEY.length()) + host + body.substring(endIndex);
		
		int portLastIndex = body.lastIndexOf(AppConfig.PORT_KEY);
		endIndex = body.indexOf(')', portLastIndex);
		body = body.substring(0, portLastIndex + AppConfig.PORT_KEY.length()) + port + body.substring(endIndex);
		
		OracleServer server = new OracleServer();
		server.setOracleHost(host);
		server.setOracleAccount(account);
		server.setOraclePort(port);
		server.setServiceName(service);
		server.setBody(body);
		return server;
	}

}
