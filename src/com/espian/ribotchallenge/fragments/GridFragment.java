package com.espian.ribotchallenge.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.espian.ribotchallenge.R;

/**
 * Author: Alex Curran
 * Date: 26/04/2013
 */
public class GridFragment extends Fragment {

	ListAdapter mAdapter;
	GridView mList;
	View mEmptyView;
	//View mStandardEmptyView;
	View mProgressContainer;
	View mListContainer;
	CharSequence mEmptyText;
	boolean mListShown;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.grid_fragment, null);
	}

	/**
	 * Provide the cursor for the list view.
	 */
	public void setAdapter(ListAdapter adapter) {
		boolean hadAdapter = mAdapter != null;
		mAdapter = adapter;
		if (mList != null) {
			mList.setAdapter(adapter);
			if (!mListShown && !hadAdapter) {
				// The list was hidden, and previously didn't have an
				// adapter.  It is now time to show it.
				setListShown(true, getView().getWindowToken() != null);
			}
		}
	}

	/**
	 * Get the activity's list view widget.
	 */
	public GridView getGridView() {
		ensureList();
		return mList;
	}

	/**
	 * Control whether the list is being displayed.  You can make it not
	 * displayed if you are waiting for the initial data to show in it.  During
	 * this time an indeterminant progress indicator will be shown instead.
	 *
	 * <p>Applications do not normally need to use this themselves.  The default
	 * behavior of ListFragment is to start with the list not being shown, only
	 * showing it once an adapter is given with {@link #setAdapter(ListAdapter)}.
	 * If the list at that point had not been shown, when it does get shown
	 * it will be do without the user ever seeing the hidden state.
	 *
	 * @param shown If true, the list view is shown; if false, the progress
	 * indicator.  The initial value is true.
	 */
	public void setListShown(boolean shown) {
		setListShown(shown, true);
	}

	/**
	 * Like {@link #setListShown(boolean)}, but no animation is used when
	 * transitioning from the previous state.
	 */
	public void setListShownNoAnimation(boolean shown) {
		setListShown(shown, false);
	}

	/**
	 * Control whether the list is being displayed.  You can make it not
	 * displayed if you are waiting for the initial data to show in it.  During
	 * this time an indeterminant progress indicator will be shown instead.
	 *
	 * @param shown If true, the list view is shown; if false, the progress
	 * indicator.  The initial value is true.
	 * @param animate If true, an animation will be used to transition to the
	 * new state.
	 */
	private void setListShown(boolean shown, boolean animate) {
		ensureList();
		if (mProgressContainer == null) {
			throw new IllegalStateException("Can't be used with a custom content view");
		}
		if (mListShown == shown) {
			return;
		}
		mListShown = shown;
		if (shown) {
			if (animate) {
				mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_out));
				mListContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_in));
			} else {
				mProgressContainer.clearAnimation();
				mListContainer.clearAnimation();
			}
			mProgressContainer.setVisibility(View.GONE);
			mListContainer.setVisibility(View.VISIBLE);
		} else {
			if (animate) {
				mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_in));
				mListContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_out));
			} else {
				mProgressContainer.clearAnimation();
				mListContainer.clearAnimation();
			}
			mProgressContainer.setVisibility(View.VISIBLE);
			mListContainer.setVisibility(View.GONE);
		}
	}

	/**
	 * Get the ListAdapter associated with this activity's ListView.
	 */
	public ListAdapter getListAdapter() {
		return mAdapter;
	}

	private void ensureList() {
		if (mList != null) {
			return;
		}
		View root = getView();
		if (root == null) {
			throw new IllegalStateException("Content view not yet created");
		}
		if (root instanceof ListView) {
			mList = (GridView)root;
		} else {
			mEmptyView = root.findViewById(R.id.empty);
			mProgressContainer = root.findViewById(R.id.progressContainer);
			mListContainer = root.findViewById(R.id.listContainer);
			View rawListView = root.findViewById(R.id.gridView);
			if (!(rawListView instanceof GridView)) {
				throw new RuntimeException(
						"Content has view with id attribute 'android.R.id.list' "
								+ "that is not a ListView class");
			}
			mList = (GridView)rawListView;
			if (mList == null) {
				throw new RuntimeException(
						"Your content must have a ListView whose id attribute is " +
								"'android.R.id.list'");
			}
			if (mEmptyView != null) {
				mList.setEmptyView(mEmptyView);
			}
		}
		mListShown = true;
		if (mAdapter != null) {
			ListAdapter adapter = mAdapter;
			mAdapter = null;
			setAdapter(adapter);
		} else {
			// We are starting without an adapter, so assume we won't
			// have our data right away and start with the progress indicator.
			if (mProgressContainer != null) {
				setListShown(false, false);
			}
		}
	}


}
