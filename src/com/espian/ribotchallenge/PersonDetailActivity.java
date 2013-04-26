package com.espian.ribotchallenge;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import com.espian.ribotchallenge.data.RibotItem;

/**
 * Author: Alex Curran
 * Date: 26/04/2013
 */
public class PersonDetailActivity extends Activity {

	public void onCreate(Bundle saved) {
		super.onCreate(saved);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.act_deets);
		getFragmentManager().beginTransaction().add(R.id.frame,
				PersonDetailFragment.showDeets((RibotItem) getIntent().getParcelableExtra("ribot")))
				.commit();
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}


}
