package com.espian.ribotchallenge.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import com.espian.ribotchallenge.data.RibotItem;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Alex Curran
 * Date: 26/04/2013
 */
public class TeamListLoader extends AsyncTaskLoader<List<RibotItem>> {

	public static final String API_POINT = "http://theribots.nodejitsu.com/api/team";

	public TeamListLoader(Context context) {
		super(context);
	}

	@Override
	public List<RibotItem> loadInBackground() {

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
			JSONArray responseArray = new JSONArray(fullResponse);
			List<RibotItem> ribotItems = new ArrayList<RibotItem>();
			int responseCount = responseArray.length();
			for (int i = 0; i < responseCount; i++) {

				try {
					ribotItems.add(new RibotItem(responseArray.getJSONObject(i), true));
				} catch (IllegalArgumentException iae) {
					// Just ignore this entry if it's borked, but print stack
					iae.printStackTrace();
				}

			}
			apiConnection.disconnect();
			return ribotItems;

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
