package com.espian.ribotchallenge.loaders;

import android.content.Context;
import com.espian.ribotchallenge.data.RibotItem;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Alex Curran
 * Date: 26/04/2013
 */
public class TeamListLoader extends BaseHttpLoader<List<RibotItem>> {

	public static final String API_POINT = "http://theribots.nodejitsu.com/api/team";

	public TeamListLoader(Context context) {
		super(context, API_POINT);
	}

	@Override
	public List<RibotItem> parseInputStream(InputStream stream) {

		try {

			BufferedReader apiReader = new BufferedReader(new InputStreamReader(stream));
			String buffer, fullResponse = "";
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
			return ribotItems;

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
