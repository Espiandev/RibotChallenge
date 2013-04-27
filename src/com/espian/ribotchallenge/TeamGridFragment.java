package com.espian.ribotchallenge;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.espian.ribotchallenge.data.RibotItem;
import com.espian.ribotchallenge.loaders.TeamListLoader;

import java.util.List;

/**
 * Author: Alex Curran
 * Date: 26/04/2013
 */
public class TeamGridFragment extends GridFragment implements LoaderManager.LoaderCallbacks<List<RibotItem>>,
		AdapterView.OnItemClickListener {

	RibotAdapter mAdapter;

	public void onActivityCreated(Bundle saved) {
		super.onActivityCreated(saved);
		getLoaderManager().initLoader(RibotMainActivity.LOADER_TEAM, null, this).forceLoad();
		mAdapter = new RibotAdapter(getActivity());
		getGridView().setNumColumns(2);
		getGridView().setAdapter(mAdapter);
		getGridView().setOnItemClickListener(this);
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

		if (data != null) mAdapter.addAll(data);
		else mAdapter.clear();

		if (isResumed()) setListShown(true);
		else setListShownNoAnimation(true);
	}

	@Override
	public void onLoaderReset(Loader <List<RibotItem>> loader) {
		loader.cancelLoad();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		startActivity(new Intent(getActivity(), PersonDetailActivity.class).putExtra("ribot", mAdapter.getItem(position)));
	}
}
