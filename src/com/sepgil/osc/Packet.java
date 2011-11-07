package com.sepgil.osc;

import java.io.IOException;
import java.nio.ByteBuffer;

abstract class Packet {
	protected boolean generated;
	protected byte[] data;
	
	protected abstract void generateBytes() throws IOException;
	
	protected int getStringByteSize(String str) {
		return str.length() + 4 - str.length() % 4;
	}
	
	protected byte[] generateBytes(float f) {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putFloat(f);
		return buffer.array();
	}
	
	protected byte[] generateBytes(int i) {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(i);
		return buffer.array();
	}
	
	protected byte[] generateBytes(String str) {
		byte[] buffer = new byte[getStringByteSize(str)];
		byte[] strBuffer = str.getBytes();
		for (int i=0; i < getStringByteSize(str); i++) {
			if (i < str.length()) {
				buffer[i] = strBuffer[i];
			} else {
				buffer[i] = 0;
			}
		}
		return buffer;
		
	}
	
	public byte[] getBytes() throws IOException {
		if (!generated) {
			generateBytes();
		}
		return data;
	}
	
}
