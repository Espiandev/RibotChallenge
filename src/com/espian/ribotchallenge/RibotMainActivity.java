package com.espian.ribotchallenge;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class RibotMainActivity extends Activity
{
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
		getFragmentManager().beginTransaction()
				.replace(R.id.mainFrame, new StudioFragment())
				.addToBackStack(null)
				.commit();
		return true;
	}

}
