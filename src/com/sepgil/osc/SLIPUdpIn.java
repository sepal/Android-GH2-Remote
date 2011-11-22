package com.sepgil.osc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class SLIPUdpIn extends SLIPPort implements Runnable {
	
	private DatagramSocket socket;
	private boolean listening;
	private OnPacketArrivedListener packetArrivedListener;
	
	public SLIPUdpIn(int port) throws SocketException {
		socket = new DatagramSocket(port);
	}

	@Override
	public void run() {
		while (listening) {
			byte[] buffer = new byte[1536];
			DatagramPacket packet = new DatagramPacket(buffer, 1536);
			try {
				socket.receive(packet);
				Message msg = generateMessage(packet);
				if (packetArrivedListener != null && msg != null)
					packetArrivedListener.onPacketArrived(msg);
				else if (msg == null) {
					System.out.println("Message null");
				}
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
	
	public void setOnPacketArrived(OnPacketArrivedListener listener) {
		this.packetArrivedListener = listener;
	}
	
	private Message generateMessage(DatagramPacket packet) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byte[] data = packet.getData();
		for (int i=0; i<data.length; i++) {
			byte b = data[i];
			switch (b) {
			case END:
				if (byteStream.size() > 0) {
					return new Message(byteStream.toByteArray());
				}
				break;
			case ESC:
				i++;
				b = data[i];
				switch (b) {
				case ESC_END:
					b = END;
					break;
				case ESC_ESC:
					b = ESC;
					break;
				}
			default:
				byteStream.write(b);
			}
		}
		return null;
	}
}
