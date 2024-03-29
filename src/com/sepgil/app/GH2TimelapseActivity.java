package com.sepgil.app;


import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.sepgil.osc.Debug;
import com.sepgil.osc.Message;
import com.sepgil.osc.OnPacketArrivedListener;
import com.sepgil.osc.SLIPUdpIn;
import com.sepgil.osc.SLIPUdpOut;

public class GH2TimelapseActivity extends Activity {
	private IntervalField timeInterval;
	private IntervalField timeShutter;
	
	private ToggleButton btnTimer;
	private Button btnShoot;
	private Button btnSaveConfig;
	private Button btnLoadConfig;
	private Button btnGetConfig;
	
	private SLIPUdpOut slipOut;
	private SLIPUdpIn slipIn;

	private Message msgInterval;
	private Message msgShutter;
	
	private Message msgShoot;
	private Message msgStartTimer;
	private Message msgSave;
	private Message msgLoad;
	private Message msgGet;
	
	private Timer pingTimer;
	private Pinger pinger;
	private boolean newConnection;
	private boolean changedTimer;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        timeInterval = new IntervalField((Button)findViewById(R.id.btnIntervalMinuteAdd), (Button)findViewById(R.id.btnIntervalSecondAdd), (Button)findViewById(R.id.btnIntervalMilisAdd), 
        		(Button)findViewById(R.id.btnIntervalMinuteSub), (Button)findViewById(R.id.btnIntervalSecondSub), (Button)findViewById(R.id.btnIntervalMilisSub), 
        		(TextView)findViewById(R.id.txtIntervalMinute), (TextView)findViewById(R.id.txtIntervalSecond), (TextView)findViewById(R.id.txtIntervalMilis));
        timeInterval.setOnChangeListener(onIntervalChange);

        timeShutter = new IntervalField((Button)findViewById(R.id.btnShutterMinuteAdd), (Button)findViewById(R.id.btnShutterSecondAdd), (Button)findViewById(R.id.btnShutterMilisAdd), 
        		(Button)findViewById(R.id.btnShutterMinuteSub), (Button)findViewById(R.id.btnShutterSecondSub), (Button)findViewById(R.id.btnShutterMilisSub), 
        		(TextView)findViewById(R.id.txtShutterMinute), (TextView)findViewById(R.id.txtShutterSecond), (TextView)findViewById(R.id.txtShutterMilis));
        timeShutter.setOnChangeListener(onShutterChange);

        btnTimer = (ToggleButton)findViewById(R.id.btnTimer);
        btnShoot = (Button)findViewById(R.id.btnShutter);
        btnSaveConfig = (Button)findViewById(R.id.btnSaveConfig);
        btnLoadConfig = (Button)findViewById(R.id.btnLoadConfig);
        btnGetConfig = (Button)findViewById(R.id.btnGetConfig);

        btnTimer.setOnCheckedChangeListener(onTimerCheckChange);
        btnShoot.setOnTouchListener(onShootClick);
        btnSaveConfig.setOnClickListener(onSaveConfigClick);
        btnLoadConfig.setOnClickListener(onLoadConfigClick);
        btnGetConfig.setOnClickListener(onGetConfigClick);
        
        msgShoot = new Message("/gh2/shutter");
        msgShoot.add(0);
        
        msgStartTimer = new Message("/gh2/timelapse/start");
        msgStartTimer.add(0);
        
        msgInterval = new Message("/gh2/timelapse/interval");
        msgInterval.add(0.0f);
        
        msgShutter = new Message("/gh2/timelapse/shutter");
        msgShutter.add(0.0f);

        msgSave = new Message("/gh2/config/save");
        msgLoad = new Message("/gh2/config/load");
        msgGet = new Message("/gh2/config/get");
        changedTimer = false;

		try {
			slipIn = new SLIPUdpIn(2000);
		} catch (SocketException e) {
			displayToast("Error while trying to create listening socket.");
		}
		slipIn.setOnPacketArrived(onOscPacket);

		slipIn.startListening();
        pingTimer = new Timer("PingTimer");
        connect("192.168.2.100");
        pingTimer.schedule(pingTimeOut, 2100, 4000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    	slipIn.stopListening();
    	pingTimer.cancel();
    	pingTimer.purge();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.mnuAndroidTether:
            connect("192.168.2.100");
        	break;
        case R.id.mnuHomeApsep:
        	connect("10.0.0.43");
        	break;
        }
        
		return true;
    }
    
    public void connect(String host) {
    	newConnection = true;
        try {
        	if (pinger != null) {
        		pinger.cancel();
        	}
			slipOut = new SLIPUdpOut(host, 2000);
	        pinger = new Pinger(slipOut);
	        pingTimer.schedule(pinger, 1000, 2000);
		} catch (SocketException e) {
			Log.e("gh2_timelapse_socket", e.toString());
			displayToast("Error while trying to create socket.");
		} catch (UnknownHostException e) {
			Log.e("gh2_timelapse_socket", e.toString());
			displayToast("Couln'd find time lapse host.");
		}
    }
    
    public OnIntervalChangeListener onIntervalChange = new OnIntervalChangeListener() {
		
		@Override
		public void onChange() {
			msgInterval.set(timeInterval.getTime(), 0);
			try {
				Debug.printBytes(msgInterval.getBytes());
				slipOut.send(msgInterval);
			} catch (IOException e) {
				Log.e("gh2_timelapse_socket", e.toString());
				displayToast("Error while trying to send timer message.");
			}
		}
	};
    
    public OnIntervalChangeListener onShutterChange = new OnIntervalChangeListener() {
		
		@Override
		public void onChange() {
			msgShutter.set(timeShutter.getTime(), 0);
			try {
				slipOut.send(msgShutter);
			} catch (IOException e) {
				Log.e("gh2_timelapse_socket", e.toString());
				displayToast("Error while trying to send timer message.");
			}
		}
	};
	
	public OnCheckedChangeListener onTimerCheckChange = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked)
				msgStartTimer.set(1, 0);
			else
				msgStartTimer.set(0, 0);
			try {
				slipOut.send(msgStartTimer);
			} catch (IOException e) {
				Log.e("gh2_timelapse_socket", e.toString());
				displayToast("Error while trying to send timer message.");
			}
			changedTimer = true;
			
		}
		
	};
	
	public OnTouchListener onShootClick = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch ( event.getAction() ) {
				case MotionEvent.ACTION_DOWN:
					try {
						msgShoot.set(1, 0);
						slipOut.send(msgShoot);
					} catch (IOException e) {
						Log.e("gh2_timelapse_socket", e.toString());
						displayToast("Error while trying to send shoot message.");
					}
					btnShoot.setBackgroundResource(R.drawable.btn_default_pressed);
					break;
				case MotionEvent.ACTION_UP:
					try {
						msgShoot.set(0, 0);
						slipOut.send(msgShoot);
					} catch (IOException e) {
						Log.e("gh2_timelapse_socket", e.toString());
						displayToast("Error while trying to send shoot message.");
					}
					btnShoot.setBackgroundResource(R.drawable.btn_default_normal);
					break;
			}
			return true;
		}

	};
	
	public OnClickListener onSaveConfigClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				slipOut.send(msgSave);
			} catch (IOException e) {
				Log.e("gh2_timelapse_socket", e.toString());
				displayToast("Error while trying to send save config message.");
			}
		}
	};
	
	public OnClickListener onLoadConfigClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			try {
				slipOut.send(msgLoad);
				slipOut.send(msgGet);
			} catch (IOException e) {
				Log.e("gh2_timelapse_socket", e.toString());
				displayToast("Error while trying to send load & get config message.");
			}
			
		}
	};
	
	public OnClickListener onGetConfigClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			try {
				slipOut.send(msgGet);
			} catch (IOException e) {
				Log.e("gh2_timelapse_socket", e.toString());
				displayToast("Error while trying to send get config message.");
			}
		}
	};
	
	public OnPacketArrivedListener onOscPacket = new OnPacketArrivedListener() {
		
		@Override
		public void onPacketArrived(final Message msg) {
			if (pinger != null) {
				pinger.setPingedBack(true);
				if (newConnection) {
					try {
						slipOut.send(msgGet);
						newConnection = false;
						
						if (msg.getAddress().equals("/gh2/state") && msg.getArgumentTypeString().equals(",i")) {
							if (!changedTimer) {
								runOnUiThread(new Runnable() {
								     public void run() {
								    	 System.out.print("Timerstate: ");
								    	 System.out.println(msg.getArgument(0));
										btnTimer.setChecked(((Integer)msg.getArgument(0) == 1 ? true : false));
								    }
								});
							} else {
								changedTimer = false;
							}
						}
					} catch (IOException e) {
						Log.e("gh2_timelapse_socket", e.toString());
						displayToast("Error while trying to send get config message on new connection.");
					}
				}
			}
			if (msg.getAddress().equals("/gh2/config") && msg.getArgumentTypeString().equals(",ff")) {
				runOnUiThread(new Runnable() {
				     public void run() {
							timeInterval.setTime((Float)msg.getArgument(0));
							timeShutter.setTime((Float)msg.getArgument(1));
				    }
				});
			}
		}
	};
	
	public TimerTask pingTimeOut = new TimerTask() {
		
		@Override
		public void run() {
			boolean state = pinger.hasPingedBack();
			setConnectionTitle(state);
		}
	};
	
	public synchronized void setConnectionTitle(boolean connected) {
		if (connected) {
			runOnUiThread(new Runnable() {
			      public void run() {
						setTitle("GH2 Timelapse - connected");
			      }
		    });

		} else {
			runOnUiThread(new Runnable() {
			      public void run() {
						setTitle("GH2 Timelapse - disconnected");
			      }
		    });
		}
	}
    
    private void displayToast(String msg) {
         Toast.makeText(getBaseContext(), msg, 10).show();   
    }   
}