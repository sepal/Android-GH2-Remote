package com.sepgil.app;


import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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

import com.sepgil.osc.Message;
import com.sepgil.osc.SLIPUdpOut;

public class GH2TimelapseActivity extends Activity {
	private IntervalField timeInterval;
	private IntervalField timeShutter;
	
	private ToggleButton btnTimer;
	private Button btnShoot;
	private Button btnSaveConfig;
	private Button btnLoadConfig;
	
	private SLIPUdpOut slipOut;
	private boolean connected = false;
	
	private Message msgShoot;
	private Message msgTimer;
	private Message msgSave;
	
	private Message msgInterval;
	private Message msgShutter;
	private Message msgLoad;
	
	
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

        btnTimer.setOnCheckedChangeListener(onTimerCheckChange);
        btnShoot.setOnTouchListener(onShootClick);
        btnSaveConfig.setOnClickListener(onSaveConfigClick);
        btnLoadConfig.setOnClickListener(onLoadConfigClick);
        
        connect();
        msgShoot = new Message("/gh2/shutter");
        msgShoot.add(0);
        
        msgTimer = new Message("/gh2/timelapse/start");
        msgTimer.add(0);
        
        msgInterval = new Message("/gh2/timelapse/interval");
        msgInterval.add(0.0f);
        
        msgShutter = new Message("/gh2/timelapse/shutter");
        msgShutter.add(0.0f);

        msgSave = new Message("/gh2/config/save");
        msgSave.add(0);

        msgLoad = new Message("/gh2/config/load");
        msgLoad.add(0);
    }
    
    public void connect() {
        try {
			slipOut = new SLIPUdpOut("192.168.2.100", 2000);
			connected = true;
		} catch (SocketException e) {
			Log.e("gh2_timelapse_socket", e.toString());
			displayToast("Error while trying to create socket.");
		} catch (UnknownHostException e) {
			Log.e("gh2_timelapse_socket", e.toString());
			displayToast("Couln'd find time lapse host.");
		}
    }
    
    public OnChangeListener onIntervalChange = new OnChangeListener() {
		
		@Override
		public void onChange() {
			msgTimer.set(timeInterval.getTime(), 0);
			try {
				slipOut.send(msgTimer);
			} catch (IOException e) {
				Log.e("gh2_timelapse_socket", e.toString());
				displayToast("Error while trying to send timer message.");
			}
		}
	};
    
    public OnChangeListener onShutterChange = new OnChangeListener() {
		
		@Override
		public void onChange() {
			msgTimer.set(timeShutter.getTime(), 0);
			try {
				slipOut.send(msgTimer);
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
				msgTimer.set(1, 0);
			else
				msgTimer.set(0, 0);
			try {
				slipOut.send(msgTimer);
			} catch (IOException e) {
				Log.e("gh2_timelapse_socket", e.toString());
				displayToast("Error while trying to send timer message.");
			}
			
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
			// TODO Auto-generated method stub
			
		}
	};
	
	public OnClickListener onLoadConfigClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
    
    private void displayToast(String msg) {
         Toast.makeText(getBaseContext(), msg, 10).show();   
    }   
}