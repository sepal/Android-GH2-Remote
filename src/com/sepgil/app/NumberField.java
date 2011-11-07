package com.sepgil.app;


import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

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
		
		txtNumber.setFilters(new InputFilter[]{ new InputFilterMinMax(min, max)});
		
		btnAdd.setOnTouchListener(onAddTouch);
		btnSub.setOnTouchListener(onSubTouch);
		txtNumber.setOnKeyListener(onKeyListener);
	}
	
	public synchronized void setVal(int number) {
		if (number<max || number>min) {
			txtNumber.setText(""+number);
		}
	}
	
	
	public int getVal() {
		return Integer.parseInt(txtNumber.getText().toString());
	}
	
	public StateNumberField getState() {
		return state;
	}
	
	public void increase() {
		int val = getVal();
		if (val<max) {
			val++;
			setVal(val);
		}
	}
	
	public void decrease() {
		int val = getVal();
		if (val>min) {
			val--;
			setVal(val);
		}
	}
	
	public OnTouchListener onAddTouch =  new OnTouchListener() {

		@Override
		public boolean onTouch(android.view.View arg0, android.view.MotionEvent arg1) {
			switch ( arg1.getAction() ) {
				case MotionEvent.ACTION_DOWN:
					System.out.println(txtNumber.requestFocus());
					btnAdd.setBackgroundResource(R.drawable.btn_plus_focus);
					increase();
					break;
				case MotionEvent.ACTION_UP:
					btnAdd.setBackgroundResource(R.drawable.btn_plus);
					if (change != null)
						change.onChange();
					break;
			}
			return true;
		}
		
	};
	
	public OnTouchListener onSubTouch =  new OnTouchListener() {

		@Override
		public boolean onTouch(android.view.View arg0, android.view.MotionEvent arg1) {
			switch ( arg1.getAction() ) {
				case MotionEvent.ACTION_DOWN:
					txtNumber.requestFocus();
					btnSub.setBackgroundResource(R.drawable.btn_minus_focus);
					decrease();
					break;
				case MotionEvent.ACTION_UP:
					btnSub.setBackgroundResource(R.drawable.btn_minus);
					if (change != null)
						change.onChange();
					break;
			}
			return true;
		}
	};
	
	public OnKeyListener onKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (change != null)
				change.onChange();
			return false;
		}
	};
	
	public OnChangeListener change;
	
	
	public void setOnChangeListener(OnChangeListener listener) {
		change = listener;
	}
}
