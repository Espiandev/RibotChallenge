package com.espian.ribotchallenge.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Alex Curran
 * Date: 26/04/2013
 */
public class StudioImageLoader extends AsyncTaskLoader<List<Bitmap>> implements FilenameFilter {

	private String[] mUrls;

	public StudioImageLoader(Context context, String... urls) {
		super(context);
		mUrls = urls;
	}

	@Override
	public List<Bitmap> loadInBackground() {

		// First check if there is at least one cached image
		if (new File(getContext().getCacheDir(), "studio0.jpg").exists()) {
			File[] files = getContext().getCacheDir().listFiles(this);
			List<Bitmap> fileBitmaps = new ArrayList<Bitmap>();
			for (File file : files) {
				fileBitmaps.add(BitmapFactory.decodeFile(file.getAbsolutePath()));
			}
			return fileBitmaps;
		}

		// Else, load them off the network
		for (String url : mUrls) {

			List<Bitmap> loadedBitmaps = new ArrayList<Bitmap>();
			HttpURLConnection connection = null;
			try {

				connection = (HttpURLConnection) new URL(url).openConnection();
				Bitmap result = BitmapFactory.decodeStream(new BufferedInputStream(connection.getInputStream()));
				if (result != null) loadedBitmaps.add(result);

				// Now, we want to write to the cache file
				// Need to do it by bytes, as the InputStream has now been read
				int i = 0;
				File currentProbe;
				while ((currentProbe = new File(getContext().getCacheDir(), "studio" + i + ".jpg")).exists()) {
					i++;
				}
				FileOutputStream cacheWriter = new FileOutputStream(currentProbe);
				boolean cacheSuccess = result.compress(Bitmap.CompressFormat.JPEG, 0, cacheWriter);
				if (!cacheSuccess) {
					// Couldn't write to cache, make sure the cache file is deleted so we can try next time
					currentProbe.delete();
				}

				connection.disconnect();
				return loadedBitmaps;


			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (connection != null) connection.disconnect();
			}

		}

		return null;
	}

	@Override
	public boolean accept(File dir, String filename) {
		return filename.contains("studio");
	}
}
