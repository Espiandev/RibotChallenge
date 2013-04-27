package com.espian.ribotchallenge.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Author: Alex Curran
 * Date: 27/04/2013
 */
public class StudioDataLoader extends AsyncTaskLoader<JSONObject> {

	public static final String API_POINT = "http://theribots.nodejitsu.com/api/studio";

	public StudioDataLoader(Context context) {
		super(context);
	}

	@Override
	public JSONObject loadInBackground() {

		//TODO: deal with connection errors
		HttpURLConnection apiConnection = null;
		String fullResponse = "";
		try {

			apiConnection = (HttpURLConnection) new URL(API_POINT).openConnection();
			String buffer;
			// TODO: check response is JSON with header
			BufferedReader apiReader = new BufferedReader(new InputStreamReader(apiConnection.getInputStream()));
			while ((buffer = apiReader.readLine()) != null) {
				fullResponse += buffer;
			}
			JSONObject responseArray = new JSONObject(fullResponse);
			apiConnection.disconnect();
			return responseArray;

		} catch (IOException e) {
			Log.d("TeamListLoader", fullResponse);
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			if (apiConnection != null) apiConnection.disconnect();
		}

		return null;

	}

}
