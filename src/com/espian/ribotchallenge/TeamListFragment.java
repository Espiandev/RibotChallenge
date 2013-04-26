package com.espian.ribotchallenge;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import com.espian.ribotchallenge.data.RibotItem;
import com.espian.ribotchallenge.loaders.TeamListLoader;

import java.util.List;

/**
 * Author: Alex Curran
 * Date: 26/04/2013
 */
public class TeamListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<RibotItem>> {

	RibotAdapter mAdapter;

	public void onActivityCreated(Bundle saved) {
		super.onActivityCreated(saved);
		getLoaderManager().initLoader(-1, null, this).forceLoad();
		mAdapter = new RibotAdapter(getActivity());
		getListView().setAdapter(mAdapter);
	}

	@Override
	public Loader<List<RibotItem>> onCreateLoader(int id, Bundle args) {
		return new TeamListLoader(getActivity());
	}

	@Override
	public void onStop() {
		super.onStop();
		mAdapter.flushCache();
	}

	@Override
	public void onLoadFinished(Loader<List<RibotItem>> loader, List<RibotItem> data) {
		mAdapter.addAll(data);
		if (isResumed()) setListShown(true);
		else setListShownNoAnimation(true);
	}

	@Override
	public void onLoaderReset(Loader<List<RibotItem>> loader) {
		loader.abandon();
	}
}
