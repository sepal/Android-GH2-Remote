package com.sepgil.app;

import android.widget.Button;
import android.widget.TextView;

public class IntervalField {
	float time;
	NumberField fldMinute;
	NumberField fldSecond;
	NumberField fldMilis;
	
	public IntervalField(Button btnAddMinute, Button btnAddSecond, Button btnAddMilis,
			Button btnSubMinute, Button btnSubSecond, Button btnSubMilis,
			TextView txtMinute, TextView txtSecond, TextView txtMilis){
		fldMinute = new NumberField(btnAddMinute,btnSubMinute, txtMinute, 0, 30);
		fldSecond = new NumberField(btnAddSecond,btnSubSecond, txtSecond, 0, 59);
		fldMilis = new NumberField(btnAddMilis,btnSubMilis, txtMilis, 0, 999);

		fldMinute.setOnChangeListener(onMinuteChange);
		fldSecond.setOnChangeListener(onSecondChange);
		fldMilis.setOnChangeListener(onMilisChange);
	}
	
	private synchronized void calcTime() {
		time = (float)fldMinute.getVal() * 60 + (float)fldSecond.getVal() + ((float)fldMilis.getVal()/1000f);
	}
	
	public OnIntervalChangeListener onMinuteChange = new OnIntervalChangeListener() {
		
		@Override
		public void onChange() {
			calcTime();
			if (onChange != null)
				onChange.onChange();
		}
	};
	
	public OnIntervalChangeListener onSecondChange = new OnIntervalChangeListener() {
		
		@Override
		public void onChange() {
			calcTime();
			if (onChange != null)
				onChange.onChange();
		}
	};
	
	public OnIntervalChangeListener onMilisChange = new OnIntervalChangeListener() {
		
		@Override
		public void onChange() {
			calcTime();
			if (onChange != null)
				onChange.onChange();
		}
	};
	
	public OnIntervalChangeListener onChange;
	
	public void setOnChangeListener(OnIntervalChangeListener listener) {
		onChange = listener;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		int min = (int) (time/60);
		int sec = (int)(time-min);
		int mil = (int)((time-min-sec)*1000);
		fldMinute.setVal(min);
		fldSecond.setVal(sec);
		fldMilis.setVal(mil);
	}
}
