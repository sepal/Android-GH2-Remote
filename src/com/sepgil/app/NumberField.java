package com.sepgil.app;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NumberField {
	public enum StateNumberField{
		INACTIVE, INCREASE, DECREASE
	}
	private Button btnAdd;
	private Button btnSub;
	private TextView txtNumber;
	private int min, max;
	
	private StateNumberField state; 
	
	public NumberField (Button _btnAdd, Button _btnSub, TextView _txtNumber, int _min, int _max) {
		btnAdd = _btnAdd;
		btnSub= _btnSub;
		txtNumber = _txtNumber;
		min = _min;
		max = _max;
		
		btnAdd.setOnTouchListener(onAdd);
		//btnAdd.setOnClickListener(onAdd);
		btnSub.setOnClickListener(onSubstract);
	}
	
	public synchronized void setVal(int number) {
		txtNumber.setText(""+number);
	}
	
	
	public int getVal() {
		return Integer.parseInt(txtNumber.getText().toString());
	}
	
	public StateNumberField getState() {
		return state;
	}
	
	public void increase() {
		int val = getVal();
		if (val<=max) {
			val--;
			setVal(val++);
		}
	}
	
	public void decrase() {
		int val = getVal();
		if (val>=0) {
			val--;
			setVal(val++);
		}
	}
	
	public OnTouchListener onAdd =  new OnTouchListener() {

		@Override
		public boolean onTouch(android.view.View arg0, android.view.MotionEvent arg1) {
			switch ( arg1.getAction() ) {
				case MotionEvent.ACTION_DOWN:
					state = StateNumberField.INCREASE;
					System.out.println("down");
					btnAdd.setBackgroundResource(R.drawable.btn_plus_focus);
					break;
				case MotionEvent.ACTION_UP:
					btnAdd.setBackgroundResource(R.drawable.btn_plus);
					state = StateNumberField.INACTIVE;
					System.out.println("up");
					break;
			}
			return true;
		}
		
	};
	
	public OnClickListener onSubstract =  new OnClickListener() {

		@Override
		public void onClick(View arg0) {
		}
		
	};
}
