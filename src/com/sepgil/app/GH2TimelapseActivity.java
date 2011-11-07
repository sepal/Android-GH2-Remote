package com.sepgil.app;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class GH2TimelapseActivity extends Activity {
	NumberField test;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button btn1 = (Button)findViewById(R.id.btnIntervalMinuteAdd);
        Button btn2 = (Button)findViewById(R.id.btnIntervalMinuteSub);
        TextView edit = (TextView)findViewById(R.id.txtIntervalMinute);
        test = new NumberField(btn1, btn2, edit);
    }
}