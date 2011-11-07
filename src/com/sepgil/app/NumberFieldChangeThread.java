package com.sepgil.app;

import com.sepgil.app.NumberField.StateNumberField;

import android.widget.TextView;

public class NumberFieldChangeThread  extends Thread{
	private NumberField field;
	
	
	public NumberFieldChangeThread (NumberField _field) {
		field = _field;
	}
	
	@Override
	public void run() {
		while (!this.isInterrupted() && field.getState() != StateNumberField.INACTIVE) {
			int val = field.getVal();
			if (field.getState() == StateNumberField.INCREASE) {
				field.increase();
			} else if (field.getState() == StateNumberField.DECREASE) {
				field.decrease();
			}
		}
	}

}
