package com.espian.ribotchallenge.loaders;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Author: Alex Curran
 * Date: 27/04/2013
 */
public class StudioDataLoader extends BaseHttpLoader<JSONObject> {

	public static final String API_POINT = "http://theribots.nodejitsu.com/api/studio";

	public StudioDataLoader(Context context) {
		super(context, API_POINT);
	}

	@Override
	public JSONObject parseInputStream(InputStream stream) {

		try {

			String buffer, fullResponse = "";
			BufferedReader apiReader = new BufferedReader(new InputStreamReader(stream));
			while ((buffer = apiReader.readLine()) != null) {
				fullResponse += buffer;
			}
			return new JSONObject(fullResponse);

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (JSONException je) {
			je.printStackTrace();
		}
		return null;

	}

}
