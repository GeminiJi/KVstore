package com.dynamo.store;

import java.io.Serializable;


public class KeyValuePair implements Serializable {
	private String key;
	private String val;
	// private DateTime dateTime;

	public KeyValuePair(String key, String val) {
		this.key = key;
		this.val = val;
		// this.dateTime = new DateTime();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	// public DateTime getDateTime() {
	// return dateTime;
	// }
	//
	// public void setDateTime(DateTime dateTime) {
	// this.dateTime = dateTime;
	// }
	//
	// @Override
	// public String toString() {
	// return "KeyValuePair [key=" + key + ", val=" + val + ", dateTime=" + dateTime
	// + "]";
	// }
}
