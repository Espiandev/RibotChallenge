package com.espian.ribotchallenge;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.espian.ribotchallenge.data.RibotItem;
import com.espian.ribotchallenge.data.Ribotar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Alex Curran
 * Date: 26/04/2013
 */
public class RibotAdapter extends ArrayAdapter<RibotItem> {

	private List<Bitmap> cache;

	public RibotAdapter(Activity context) {
		super(context, R.layout.grid_team);
		cache = new ArrayList<Bitmap>(30);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) convertView = View.inflate(getContext(), R.layout.grid_team, null);

		Ribotar ribotar = getItem(position).getRibotar();

		TextView textView = (TextView) convertView.findViewById(R.id.textView);
		TextView textView1 = (TextView) convertView.findViewById(R.id.textView1);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
		textView.setText(getItem(position).getFullName(true));
		textView1.setText(getItem(position).getRole());
		imageView.setImageBitmap(ribotar.get(getContext(), false));
		imageView.setTag(position);

		// If we haven't tried to get a real Ribotar, start loading!
		if (!ribotar.memoryCacheExists()) {
			new FetchImageTask(imageView, position, ribotar).execute();
		} else {
			Log.d("RibotAdapter", "Have got bitmap from memCache");
		}

		return convertView;

	}

	public void flushCache() {
		//cache.clear();
	}

	public class FetchImageTask extends AsyncTask<String, Void, Bitmap> {

		public final String TAG = FetchImageTask.class.getName();
		public final String API_POINT = "http://theribots.nodejitsu.com/api/team/%s/ribotar";

		private final Ribotar mRibotar;
		private final ImageView mImageView;
		private final int mPosition;

		public FetchImageTask(ImageView textView, int position, Ribotar target) {
			mImageView = textView;
			mPosition = position;
			mRibotar = target;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (mImageView != null && bitmap != null && (Integer) mImageView.getTag() == mPosition) {
				mRibotar.setMemoryCache(bitmap);
				mImageView.setImageBitmap(bitmap);
			}
		}

		@Override
		public Bitmap doInBackground(String... params) {

			if (mRibotar.fileCacheExists(getContext())) {
				Log.d(TAG, "Cached image exists, get that");
				return BitmapFactory.decodeFile(new File(getContext().getCacheDir(), mRibotar.id + ".png").getAbsolutePath());
			} else {

				HttpURLConnection ribotarConnection = null;
				try {

					Log.d(TAG, "Loading Ribotar from network");
					String url = String.format(API_POINT, URLEncoder.encode(mRibotar.id));
					ribotarConnection = (HttpURLConnection) new URL(url).openConnection();
					Bitmap result = BitmapFactory.decodeStream(new BufferedInputStream(ribotarConnection.getInputStream()));

					// Now, we want to write to the cache file
					// Need to do it by bytes, as the InputStream has now been read
					Log.d(TAG, "Caching loaded bitmap for: " + mRibotar.id);
					FileOutputStream cacheWriter = new FileOutputStream(new File(getContext().getCacheDir(), mRibotar.id + ".png"));
					boolean cacheSuccess = result.compress(Bitmap.CompressFormat.PNG, 0, cacheWriter);
					if (!cacheSuccess) {
						// Couldn't write to cache, make sure the cache file is deleted so we can try next time
						new File(getContext().getCacheDir(), mRibotar.id + ".png").delete();
					}

					mRibotar.setMemoryCache(result);
					return result;

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (ribotarConnection != null) ribotarConnection.disconnect();
				}

			}

			return null;
		}
	}

}
