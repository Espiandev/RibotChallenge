package com.espian.ribotchallenge.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Allows safe calls to a JSONObject. If the mapping doesn't exist, the method will
 * simply return null (or an empty value) rather than throwing an Exception
 * @author Alex Curran
 *
 */
public class SafeJsonObject {

	private JSONObject mObject;
	private boolean mReturnNull;

	/**
	 * Construct a new SafeJsonObject
	 * @param object The JSONObject to wrap around
	 * @param returnNull Whether a failed mapping look-up should return a null value
	 * or an empty one.
	 */
	public SafeJsonObject(JSONObject object, boolean returnNull) {
		mObject = object;
	}

	public String getString(String key) {
		try {
			return mObject.getString(key);
		} catch (JSONException je) {
			return mReturnNull ? null : "";
		}
	}

}
