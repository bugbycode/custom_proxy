package com.util;

import com.bugbycode.config.AppConfig;

public class TransferUtil {
	
	public static int toHH(byte[] buff) {
		return ((buff[0] << 0x08) & 0xFF00) | (buff[1] & 0xFF);
	}
	
	public static void toHH(int len,byte[] buff) {
		buff[0] = (byte)((len >> 0x08) & 0xFF);
		buff[1] = (byte)(len & 0xFF);
	}
	
	public static boolean isConn(byte[] buff) {
		return toHH(buff) > 0x0A && buff[0x08] == 0x01 && buff[0x09] == 0x3D;
	}
	
	public static boolean isFirstRecv(byte[] buff) {
		return buff[0x00] == 0x00 && buff[0x01] == 0x08 && buff[0x04] == 0x0b;
	}
	
	public static boolean isAuthRecv(byte[] buff) {
		return toHH(buff) > 0xb && buff[0x0A] == 0x03 && buff[0x0B] == 0x76;
	}
	
	public static boolean isLogin(byte[] buff) {
		return toHH(buff) > 0xb && buff[0x0A] == 0x03 && buff[0x0B] == 0x73;
	}
	
	public static boolean contailsEncryptedSK(byte[] buff) {
		return toHH(buff) > 0xb && buff[0x0A] == 0x08 && findKey(AppConfig.AUTH_SESSKEY.getBytes(), buff) != -1;
	}
	
	public static boolean isAuthEncryptedSK(byte[] buff) {
		return toHH(buff) > 0x0A && buff[0x0A] == 0x08 && 
				(buff[0x0B] == 0x02 || buff[0x0B] == 0x03) 
				&& findKey(AppConfig.AUTH_SESSKEY.getBytes(),buff) != -1;
	}
	
	public static boolean isRecvAuthVersion(byte[] buff) {
		return toHH(buff) > 0xb && buff[0x0A] == 0x08 && buff[0x0B] == 0x23;
	}
	
	public static String getUserName(String key,byte[] buff) {
		int user_index = TransferUtil.findKey(key.getBytes(), buff) - 0x06;
		int user_len = -1;
		while(user_index > 0) {
			user_len = buff[user_index] & 0xFF;
			if(user_len < 0x21) {
				break;
			}
			user_index--;
		}
		
		byte[] userByte = new byte[user_len];
		System.arraycopy(buff, user_index + 1, userByte, 0, user_len);
		return new String(userByte);
	}
	
	public static int findKey(byte[] key,byte[] buff) {
		int keLen = key.length;
		int length = toHH(buff);
		for(int index = 0;index < length - keLen;index++) {
			int keyIndex = 0;
			while(keyIndex < keLen) {
				if(key[keyIndex] != buff[index + keyIndex]) {
					break;
				}
				keyIndex++;
			}
			if(keyIndex == keLen) {
				return index;
			}
		}
		return -1;
	}
	
	public static String findKeyText(byte[] key,byte[] buff) {
		int index = findKey(key, buff);
		if(index == -1) {
			return "";
		}
		index += key.length;
		index += 0x04;
		int keyTextLen = buff[index++] & 0xFF;
		byte[] key_text = new byte[keyTextLen];
		System.arraycopy(buff, index, key_text, 0, keyTextLen);
		return new String(key_text);
	}
}
