package com.espian.ribotchallenge.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Author: Alex Curran
 * Date: 27/04/2013
 */
public abstract class BaseHttpLoader<D> extends AsyncTaskLoader<D> {

	private String mApiPoint;

	public BaseHttpLoader(Context context, String apiPoint) {
		super(context);
		mApiPoint = apiPoint;
	}

	@Override
	public D loadInBackground() {

		//TODO: deal with connection errors
		HttpURLConnection apiConnection = null;
		String fullResponse = "";
		try {

			apiConnection = (HttpURLConnection) new URL(mApiPoint).openConnection();
			D result = parseInputStream(apiConnection.getInputStream());

			apiConnection.disconnect();
			return result;

		} catch (IOException e) {
			Log.d("TeamListLoader", fullResponse);
			e.printStackTrace();
		} finally {
			if (apiConnection != null) apiConnection.disconnect();
		}

		return null;

	}

	public abstract D parseInputStream(InputStream stream);

}
