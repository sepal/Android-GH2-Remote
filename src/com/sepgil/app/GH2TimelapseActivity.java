package com.sepgil.app;


import com.sepgil.osc.SLIPUdp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class GH2TimelapseActivity extends Activity {
	IntervalField timeInterval;
	IntervalField timeShutter;
	
	ToggleButton btnTimer;
	Button btnShutter;
	Button btnSaveConfig;
	Button btnLoadConfig;
	
	SLIPUdp slip;
	
	
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
        btnShutter = (Button)findViewById(R.id.btnShutter);
        btnSaveConfig = (Button)findViewById(R.id.btnSaveConfig);
        btnLoadConfig = (Button)findViewById(R.id.btnLoadConfig);

        btnTimer.setOnCheckedChangeListener(onTimerCheckChange);
        btnShutter.setOnClickListener(onShutterClick);
        btnSaveConfig.setOnClickListener(onSaveConfigClick);
        btnLoadConfig.setOnClickListener(onLoadConfigClick);
        
        slip = new SLIPUdp("10.0.", port)
    }
    
    public OnChangeListener onIntervalChange = new OnChangeListener() {
		
		@Override
		public void onChange() {
			
		}
	};
    
    public OnChangeListener onShutterChange = new OnChangeListener() {
		
		@Override
		public void onChange() {
			
		}
	};
	
	public OnCheckedChangeListener onTimerCheckChange = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	public OnClickListener onShutterClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
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
}