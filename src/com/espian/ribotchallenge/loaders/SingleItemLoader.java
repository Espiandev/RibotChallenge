package com.espian.ribotchallenge.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import com.espian.ribotchallenge.data.RibotItem;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Author: Alex Curran
 * Date: 26/04/2013
 */
public class SingleItemLoader extends AsyncTaskLoader<RibotItem> {

	public static final String API_POINT = "http://theribots.nodejitsu.com/api/team/%s";

	private String mId;

	public SingleItemLoader(Context context, String id) {
		super(context);
		mId = id;
	}

	@Override
	public RibotItem loadInBackground() {

		//TODO: deal with connection errors
		HttpURLConnection apiConnection = null;
		String fullResponse = "";
		try {

			apiConnection = (HttpURLConnection) new URL(String.format(API_POINT, mId)).openConnection();
			String buffer;
			// TODO: check response is JSON with header
			BufferedReader apiReader = new BufferedReader(new InputStreamReader(apiConnection.getInputStream()));
			while ((buffer = apiReader.readLine()) != null) {
				fullResponse += buffer;
			}
			JSONObject responseArray = new JSONObject(fullResponse);
			RibotItem item = new RibotItem(responseArray, false);

			apiConnection.disconnect();
			return item;

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
