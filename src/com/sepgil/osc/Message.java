package com.sepgil.osc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Message extends Packet {
	private String address;
	private ArrayList<Object> arguments;
	private String argumentString;
	
	/**
	 * Create a new message and set the address.
	 * @param address
	 */
	public Message(String address) {
		this.address = address;
		arguments =  new ArrayList<Object>();
		generated = false;
		argumentString = ",";
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
		generated = false;
	}

	public void add(Float f) {
		arguments.add(f);
		argumentString += "f";
		generated = false;
	}
	
	public void add(Integer i) {
		arguments.add(i);
		argumentString += "i";
		generated = false;
	}
	
	public void add(String str) {
		arguments.add(str);
		argumentString += "s";
		generated = false;
	}
	
	public void replaceArgString(int index, char element) {
		String left = argumentString.substring(0, index+1);
		String right = argumentString.substring(index+2, argumentString.length());
		argumentString = left + element + right;
		generated = false;
		
	}
	
	public void set(Integer val, int index) {
		//if (index < arguments.size() && index >= 0) {
		System.out.println(arguments.size());
		System.out.println(index);
			arguments.set(index, val);
			replaceArgString(index, 'i');
		//}
	}
	
	public void set(Float val, int index) {
		//if (index < arguments.size() && index >= 0) {
		System.out.println(arguments.size());
		System.out.println(index);
			arguments.set(index, val);
			replaceArgString(index, 'f');
		//}
	}
	
	public Object getArgument(int index) {
		return  arguments.get(index);
	}
	
	public Object[] getArguments() {
		return arguments.toArray();
	}
	
	@Override
	protected void generateBytes() throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byteStream.write(generateBytes(address));
		byteStream.write(generateBytes(argumentString));
		
		for (Object arg : arguments) {
			if (arg.getClass().getName().equals("java.lang.Float")) {
				byteStream.write(generateBytes((Float)arg));
			} else if (arg.getClass().getName().equals("java.lang.Integer")) {
				byteStream.write(generateBytes((Integer)arg));
			} else if(arg.getClass().getName().equals("java.lang.String")) {
				byteStream.write(generateBytes((String)arg));
			}
		}
		data = byteStream.toByteArray();
		generated = true;
	}
}