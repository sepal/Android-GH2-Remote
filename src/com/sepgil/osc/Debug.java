package com.sepgil.osc;

public class Debug {
	/**
	 * Prints an byte array.
	 * 
	 * @param data
	 *            The byte array.
	 */
	public static void printBytes(byte[] data) {
		int rowCount = 0;
		for (int i = 0; i < data.length; i++) {
			String res = "";
			res += Integer.toHexString((data[i] < 0 ? 256 + data[i]	: data[i]));
			if (res.length() < 2) {
				res = res + " ";
				//res = "0" + res;
			}
			System.out.print(res);
			System.out.print(" (");
			System.out.print((char) data[i]);
			System.out.print(") ");
			System.out.print(" ");
			if (rowCount++ >= 3) {
				System.out.println();
				rowCount = 0;
			}
		}
	}
}
