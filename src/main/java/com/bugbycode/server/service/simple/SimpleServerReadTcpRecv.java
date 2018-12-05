package com.bugbycode.server.service.simple;

import java.io.IOException;

import com.bugbycode.client.OracleSocketClient;
import com.bugbycode.client.listener.OracleRecvListener;
import com.bugbycode.config.AppConfig;
import com.bugbycode.server.module.OracleServer;
import com.bugbycode.server.service.OracleHostFormatService;
import com.bugbycode.server.service.WriteTcpRecv;
import com.bugbycode.server.service.impl.AbstractReadTcpRecv;
import com.bugbycode.server.service.impl.OracleHostFormatServiceImpl;
import com.util.StringUtil;
import com.util.TransferUtil;

public class SimpleServerReadTcpRecv extends AbstractReadTcpRecv {

	private OracleSocketClient client;
	
	private OracleServer server;
	
	private OracleHostFormatService oracleHostFormatService;
	
	public SimpleServerReadTcpRecv() {
		this.oracleHostFormatService = new OracleHostFormatServiceImpl();
	}
	
	@Override
	public void read(WriteTcpRecv write, byte[] buff) throws Exception{
		System.out.println("0	" + StringUtil.byteToHexString(buff));
		System.out.println("0	" + new String(buff));
		if(TransferUtil.isConn(buff)) {
			OracleServer server = oracleHostFormatService.format(buff);
			byte[] body_buff = server.getBody().getBytes();
			int index = TransferUtil.findKey(AppConfig.CONNECTION_KEY, buff);
			int conn_buff_len = index + body_buff.length;
			byte[] conn_buff = new byte[conn_buff_len];
			System.arraycopy(buff, 0, conn_buff, 0, index);
			System.arraycopy(body_buff, 0, conn_buff, index, body_buff.length);
			
			conn_buff[0x19] = (byte)((conn_buff[0x19] & 0xFF) + (conn_buff_len - buff.length));
			TransferUtil.toHH(conn_buff_len, conn_buff);
			
			if(this.server == null) {
				this.server = server;
				client = new OracleSocketClient(new MyOracleRecvListener());
				client.connection(server.getOracleHost(), server.getOraclePort(), 5000);
				this.client.writeAndFlush(conn_buff);
			}else if(this.server.toString().equals(server.toString())){
				this.client.writeAndFlush(conn_buff);
			}else {
				write.close();
				close();
			}
		}else {
			client.writeAndFlush(buff);
		}
	}

	@Override
	public void connection(String host, int port) throws IOException {
		
		client.connection(host, port, 5000);
	}
	
	@Override
	public void close() {
		System.out.println("SQL*Plus tcp close.");
		try {
			if(client != null) {
				client.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class MyOracleRecvListener implements OracleRecvListener{

		@Override
		public void read(byte[] buff) throws IOException {
			System.out.println("1	" + StringUtil.byteToHexString(buff));
			System.out.println("1	" + new String(buff));
			getWrite().write(buff);
		}

		@Override
		public void close() {
			getWrite().close();
			System.out.println("Oracle tcp close.");
		}
	}
}
