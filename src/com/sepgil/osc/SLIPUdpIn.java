package com.sepgil.osc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class SLIPUdpIn implements Runnable {
	
	private DatagramSocket socket;
	private boolean listening;
	
	public SLIPUdpIn(int port) throws SocketException {
		socket = new DatagramSocket(port);
	}

	@Override
	public void run() {
		byte[] buffer = new byte[1536];
		DatagramPacket packet = new DatagramPacket(buffer, 1536);
		while (listening) {
			try {
				socket.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void startListening() {
		Thread thread = new Thread(this);
		thread.start();
		listening = true;
	}
	
	public void stopListening() {
		listening = false;
	}
	
	public boolean isListening() {
		return listening;
	}
	
}
