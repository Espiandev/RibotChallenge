package com.espian.ribotchallenge;

import android.app.Activity;
import android.os.Bundle;
import com.espian.ribotchallenge.fragments.StudioFragment;

/**
 * Author: Alex Curran
 * Date: 28/04/2013
 */
public class StudioActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_deets);
		getFragmentManager().beginTransaction()
				.add(R.id.frame, new StudioFragment())
				.commit();
	}
}
