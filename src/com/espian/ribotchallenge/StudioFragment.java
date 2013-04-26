package com.espian.ribotchallenge;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.espian.ribotchallenge.loaders.StudioImageLoader;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Author: Alex Curran
 * Date: 26/04/2013
 */
public class StudioFragment extends Fragment implements LoaderManager.LoaderCallbacks {

	public static final int LOADER_STUDIO = 0;
	public static final int LOADER_IMAGES = 0;

	private List<Bitmap> images;
	private ImageView imageViewLower, imageViewUpper;
	private boolean isShowingUpper = false;
	private int counter = 0;
	private Timer fadeTimer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.frag_studio, null);
		imageViewLower = (ImageView) v.findViewById(R.id.imageView);
		imageViewUpper = (ImageView) v.findViewById(R.id.imageView1);
		return super.onCreateView(inflater, container, savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(LOADER_IMAGES, null, this).forceLoad();

	}

	@Override
	public Loader onCreateLoader(int id, Bundle args) {
		if (id == LOADER_IMAGES) return new StudioImageLoader(getActivity());
		return null;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (fadeTimer != null) fadeTimer.cancel();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (fadeTimer != null && images != null) fadeTimer.scheduleAtFixedRate(new TransitionTimerTask(), 0, 5000);
	}

	@Override
	public void onLoadFinished(Loader loader, Object data) {
		if (loader.getId() == LOADER_IMAGES) {
			images = (List<Bitmap>) data;
			fadeTimer = new Timer();
			fadeTimer.scheduleAtFixedRate(new TransitionTimerTask(), 0, 5000);
		}
	}

	@Override
	public void onLoaderReset(Loader loader) {
	}

	public class TransitionTimerTask extends TimerTask {

		@Override
		public void run() {
			if (isShowingUpper) {
				imageViewLower.setImageBitmap(images.get(counter % images.size()));
				ObjectAnimator.ofFloat(imageViewUpper, "alpha", 1f, 0f).start();
			} else {
				imageViewUpper.setImageBitmap(images.get(counter % images.size()));
				ObjectAnimator.ofFloat(imageViewUpper, "alpha", 0f, 1f).start();
			}
			isShowingUpper = !isShowingUpper;
			counter++;
		}
	}

}
