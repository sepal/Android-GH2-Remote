package com.sepgil.osc;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SLIPPort {
	protected static final byte END = (byte) 0300;
	protected static final byte ESC = (byte) 0333;
	protected static final byte ESC_END = (byte) 0334;
	protected static final byte ESC_ESC = (byte) 0335;
	
	protected DatagramSocket socket;
	
	protected void finalize() {
		socket.close();
	}
}
