package com.espian.ribotchallenge;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import com.espian.ribotchallenge.data.RibotItem;
import com.espian.ribotchallenge.fragments.PersonDetailFragment;

/**
 * Author: Alex Curran
 * Date: 26/04/2013
 */
public class PersonDetailActivity extends Activity {

	@Override
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) finish();
		return super.onOptionsItemSelected(item);
	}
}
