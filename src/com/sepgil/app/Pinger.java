package com.sepgil.app;

import java.io.IOException;
import java.util.TimerTask;

import android.util.Log;

import com.sepgil.osc.Message;
import com.sepgil.osc.SLIPUdpOut;

public class Pinger extends TimerTask{
	private SLIPUdpOut slipOut;
	private Message msgPing;
	private boolean pingedBack;
	
	public Pinger(SLIPUdpOut slipOut) {
		this.slipOut = slipOut;
		this.msgPing = new Message("/ping");
		setPingedBack(false);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			setPingedBack(false);
			slipOut.send(msgPing);
		} catch (IOException e) {
			Log.e("ping_error", e.toString());
		}
	}

	public boolean hasPingedBack() {
		return pingedBack;
	}

	public synchronized void setPingedBack(boolean pingedBack) {
		this.pingedBack = pingedBack;
	}

}
