package com.espian.ribotchallenge.loaders;

import android.content.Context;
import com.espian.ribotchallenge.data.RibotItem;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Author: Alex Curran
 * Date: 26/04/2013
 */
public class SingleItemLoader extends BaseHttpLoader<RibotItem> {

	public static final String API_POINT = "http://theribots.nodejitsu.com/api/team/%s";

	public SingleItemLoader(Context context, String id) {
		super(context, String.format(API_POINT, id));
	}

	@Override
	public RibotItem parseInputStream(InputStream stream) {

		try {

			String buffer, fullResponse = "";

			// Read the stream into a String
			BufferedReader apiReader = new BufferedReader(new InputStreamReader(stream));
			while ((buffer = apiReader.readLine()) != null) {
				fullResponse += buffer;
			}

			// Create the JSONObject and RibotItem from it
			JSONObject responseArray = new JSONObject(fullResponse);
			return new RibotItem(responseArray, false);

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

}
