package com.espian.ribotchallenge;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Author: Alex Curran
 * Date: 26/04/2013
 */
public class Utilities {

	/**
	 * Creates a state-list drawable for buttons in the details fragment
	 * @param color the hexColor of the person
	 * @return
	 */
	public static StateListDrawable getButtonDrawable(int color) {

		StateListDrawable drawable = new StateListDrawable();
		drawable.setExitFadeDuration(300);
		drawable.addState(new int[] { android.R.attr.state_pressed}, new ColorDrawable(color));
		drawable.addState(new int[] { android.R.attr.state_empty}, new ColorDrawable(Color.TRANSPARENT));
		return drawable;

	}

}
