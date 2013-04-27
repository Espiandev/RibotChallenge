package com.espian.ribotchallenge;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.espian.ribotchallenge.loaders.StudioDataLoader;
import com.espian.ribotchallenge.loaders.StudioImageLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Author: Alex Curran Date: 26/04/2013
 */
public class StudioFragment extends Fragment implements
		LoaderManager.LoaderCallbacks {

	private List<Bitmap> images;
	private ImageView imageViewLower, imageViewUpper;
	private TextView addressText;
	private boolean isShowingUpper = false;
	private int counter = 0;
	private Timer fadeTimer;
	private String[] urls;

	// TODO: ISSUE WITH ROTATED IMAGE

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.frag_studio, null);
		imageViewLower = (ImageView) v.findViewById(R.id.imageView);
		imageViewUpper = (ImageView) v.findViewById(R.id.imageView1);
		addressText = (TextView) v.findViewById(R.id.studio_address);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(RibotMainActivity.LOADER_STUDIO, null,
				this).forceLoad();

	}

	@Override
	public Loader onCreateLoader(int id, Bundle args) {
		if (id == RibotMainActivity.LOADER_IMAGES)
			return new StudioImageLoader(getActivity(), urls);
		else
			return new StudioDataLoader(getActivity());
	}

	@Override
	public void onPause() {
		super.onPause();
		if (fadeTimer != null) {
			fadeTimer.cancel();
			fadeTimer = null;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (fadeTimer == null && images != null) {
			fadeTimer = new Timer();
			fadeTimer.scheduleAtFixedRate(new TransitionTimerTask(), 0, 5000);
		}
	}

	@Override
	public void onLoadFinished(Loader loader, Object data) {
		if (loader.getId() == RibotMainActivity.LOADER_IMAGES && data != null) {

			images = (List<Bitmap>) data;
			fadeTimer = new Timer();
			fadeTimer.scheduleAtFixedRate(new TransitionTimerTask(), 0, 5000);

		} else {

			if (data != null) {

				JSONObject studioData = (JSONObject) data;
				try {

					// Set the address
					addressText.setText(constructAddress(studioData));

					JSONArray jsonUrls = studioData.getJSONArray("photos");
					urls = new String[jsonUrls.length()];
					for (int i = 0; i < jsonUrls.length(); i++) {
						urls[i] = (String) jsonUrls.get(i);
					}
					getLoaderManager().initLoader(
							RibotMainActivity.LOADER_IMAGES, null, this)
							.forceLoad();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		}
	}

	@Override
	public void onLoaderReset(Loader loader) {
	}

	private String constructAddress(JSONObject object) {
		String address = "";
		// Concatenate each line of the address, appended with a new line where appropriate
		SafeJson json = new SafeJson(object, false);
		address += json.getString("addressNumber") + " " + json.getString("street") + "\n";
		address += json.getString("city") + "\n";
		address += json.getString("county") + "\n";
		address += json.getString("country") + "\n";
		address += json.getString("postcode") + "\n";
		return address;
	}

	public class TransitionTimerTask extends TimerTask {

		@Override
		public void run() {
			Log.d("TransitionTimerTask", "Ticked: " + counter);
			if (imageViewLower != null && images != null) {

				if (isShowingUpper) {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							imageViewLower.setImageBitmap(images.get(counter
									% images.size()));
							ObjectAnimator
									.ofFloat(imageViewUpper, "alpha", 1f, 0f)
									.setDuration(1000).start();
						}
					});
				} else {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							imageViewUpper.setImageBitmap(images.get(counter
									% images.size()));
							ObjectAnimator
									.ofFloat(imageViewUpper, "alpha", 0f, 1f)
									.setDuration(1000).start();
						}
					});
				}
			}
			isShowingUpper = !isShowingUpper;
			counter++;
		}
	}

	/**
	 * Allows safe calls to a JSONObject. If the mapping doesn't exist, the method will
	 * simply return null (or an empty value) rather than throwing an Exception
	 * @author Alex Curran
	 *
	 */
	public class SafeJson {
		
		private JSONObject mObject;
		private boolean mReturnNull;
		
		/**
		 * Construct a new SafeJsonObject
		 * @param object The JSONObject to wrap around
		 * @param returnNull Whether a failed mapping look-up should return a null value
		 * or an empty one.
		 */
		public SafeJson(JSONObject object, boolean returnNull) {
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
	
}
