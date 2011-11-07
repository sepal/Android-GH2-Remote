package com.sepgil.osc;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SLIPUdpOut {
	private static final byte END = (byte) 0300;
	private static final byte ESC = (byte) 0333;
	private static final byte ESC_END = (byte) 0334;
	private static final byte ESC_ESC = (byte) 0335;
	
	private DatagramSocket socket;
	private SocketAddress host;

	public SLIPUdpOut(String host, int port) throws SocketException, UnknownHostException {
		socket = new DatagramSocket();
		this.host = new InetSocketAddress(InetAddress.getByName(host), port);
	}
	
	public void send(Message msg) throws IOException {
		byte[] msgData = msg.getBytes();
		ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
		int i=0;
		dataStream.write(END);
		for (byte b : msgData) {
			switch (msgData[i]) {
			case END:
				dataStream.write(ESC);
				dataStream.write(ESC_END);
				break;
			case ESC:
				dataStream.write(ESC);
				dataStream.write(ESC_ESC);
				break;
			default:
				dataStream.write(b);
			}
		}
		dataStream.write(END);
		
		byte[] data = dataStream.toByteArray();
		DatagramPacket packet = new DatagramPacket(data, data.length, host);
		socket.send(packet);
	}

}
