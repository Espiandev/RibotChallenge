package com.espian.ribotchallenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.espian.ribotchallenge.fragments.TeamGridFragment;

public class RibotMainActivity extends Activity {

	public static final int LOADER_TEAM = 1;
	public static final int LOADER_STUDIO = 3;
	public static final int LOADER_IMAGES = 4;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
	    getFragmentManager().beginTransaction()
			    .add(R.id.mainFrame, new TeamGridFragment())
			    .commit();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Find us").setIcon(R.drawable.ic_light_action_place).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// As there's only one menu item, we don't have to be specific
		startActivity(new Intent(this, StudioActivity.class));
		return true;
	}

}
