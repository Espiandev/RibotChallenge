package com.espian.ribotchallenge.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import com.espian.ribotchallenge.R;

import java.io.File;

/**
 * Author: Alex Curran
 * Date: 26/04/2013
 */
public class Ribotar {

	private int hexColor;
	public String id;
	private Bitmap memoryCache;

	public Ribotar(RibotItem owner) {
		hexColor = owner.getHexColor();
		id = owner.getId();
	}

	public boolean fileCacheExists(Context context) {
		return new File(context.getCacheDir(), id + ".png").exists();
	}

	public boolean memoryCacheExists() {
		return memoryCache != null;
	}

	public Bitmap get(Context context, boolean includeFileCache) {
		if (memoryCache != null) return memoryCache;
		if (includeFileCache && fileCacheExists(context)) return getFromFile(context);
		return getBasic(context);
	}

	public Bitmap getFromFile(Context context) {
		return BitmapFactory.decodeFile(new File(context.getCacheDir(), id + ".png").getAbsolutePath());
	}

	/**
	 * Generate a basic Ribotar image
	 * @param context Context to create the image from
	 * @return A quick Ribotar image - simply the Ribot logo over the person's hexCode
	 */
	private Bitmap getBasic(Context context) {
		Drawable basicOverlay = context.getResources().getDrawable(R.drawable.ribotar_basic);
		Bitmap b = Bitmap.createBitmap(basicOverlay.getIntrinsicWidth(), basicOverlay.getIntrinsicHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(b);
		canvas.drawColor(hexColor != 0 ? hexColor : Color.parseColor("#33B535"));
		basicOverlay.setBounds(0, 0, basicOverlay.getIntrinsicWidth(), basicOverlay.getIntrinsicHeight());
		basicOverlay.draw(canvas);
		canvas = null;

		return b;
	}

	public void setMemoryCache(Bitmap memoryCache) {
		this.memoryCache = memoryCache;
	}

	public Bitmap getMemoryCache() {
		return memoryCache;
	}
}
