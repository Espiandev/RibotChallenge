package com.espian.ribotchallenge;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.Loader;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.espian.ribotchallenge.data.RibotItem;
import com.espian.ribotchallenge.loaders.SingleItemLoader;

/**
 * Author: Alex Curran
 * Date: 26/04/2013
 */
public class PersonDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<RibotItem> {

	RibotItem ribotItem;
	ImageView heroImage;
	ImageButton emailButton, twitterButton, mapButton;
	LoadHideHelper hideButtonLayout, hideExtendedLayout;
	TextView description, sweetLabel, sweet, seasonLabel, season;

	public static PersonDetailFragment showDeets(RibotItem ribot) {
		PersonDetailFragment pdf = new PersonDetailFragment();
		Bundle b = new Bundle();
		b.putParcelable("ribot", ribot);
		pdf.setArguments(b);
		return pdf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.frag_deets, null);

		ribotItem = getArguments().getParcelable("ribot");

		// We can fill these in immediately from the light response
		((TextView) v.findViewById(R.id.desc_name)).setText(ribotItem.getFullName(true));
		setText((TextView) v.findViewById(R.id.desc_role), null, ribotItem.getRole());

		// Retain a reference to the hero image, in case the ribotar hasn't loaded
		(heroImage = (ImageView) v.findViewById(R.id.desc_image)).setBackgroundColor(ribotItem.getHexColor());

		// Annoyingly, we must create a separate drawable for each button, else strange things happen
		(emailButton    = (ImageButton) v.findViewById(R.id.imageButton)).setBackground(Utilities.getButtonDrawable(ribotItem.getHexColor()));
		(twitterButton  = (ImageButton) v.findViewById(R.id.imageButton1)).setBackground(Utilities.getButtonDrawable(ribotItem.getHexColor()));
		(mapButton      = (ImageButton) v.findViewById(R.id.imageButton2)).setBackground(Utilities.getButtonDrawable(ribotItem.getHexColor()));

		// Get all the items found in the extended layout
		description     = (TextView) v.findViewById(R.id.desc_desc);
		sweet           = (TextView) v.findViewById(R.id.desc_sweet);
		sweetLabel      = (TextView) v.findViewById(R.id.labelSweet);
		season          = (TextView) v.findViewById(R.id.desc_season);
		seasonLabel     = (TextView) v.findViewById(R.id.labelSeason);

		// Associate our LoadHideHelpers
		hideButtonLayout    = new LoadHideHelper(v.findViewById(R.id.buttonLayout));
		hideExtendedLayout  = new LoadHideHelper(v.findViewById(R.id.extendedLayout));

		return v;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Load as many details from the light response as we can, and get the rest ASAP
		getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(ribotItem.getHexColor()));

		heroImage.setImageBitmap(ribotItem.getRibotar().get(getActivity(), true));
		getLoaderManager().initLoader(0, null, this).forceLoad();

	}

	@Override
	public Loader<RibotItem> onCreateLoader(int id, Bundle args) {
		getActivity().setProgressBarIndeterminateVisibility(true);
		return new SingleItemLoader(getActivity(), ribotItem.getId());
	}

	@Override
	public void onLoadFinished(Loader<RibotItem> loader, final RibotItem data) {

		getActivity().setProgressBarIndeterminateVisibility(false);

		if (data != null) {

			// Check the button states
			setButtonClick(twitterButton, data.getTwitterUri());
			setButtonClick(emailButton, data.getEmailUri());
			setButtonClick(mapButton, data.getLocationUri());
			hideButtonLayout.show();

			// Set/hide extra data
			setText(description, null, data.getDescription());
			setText(sweet, sweetLabel, data.getFavSweet());
			setText(season, seasonLabel, data.getFavSeason());
			hideExtendedLayout.show();

		} else Toast.makeText(getActivity(), "Couldn't load data", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onLoaderReset(Loader <RibotItem> loader) {
		loader.abandon();
	}

	/**
	 * set a textView's text, or hide it (and associated label) if there's no text
	 * @param textView
	 * @param labelView
	 * @param text
	 */
	public void setText(TextView textView, TextView labelView, String text) {
		if (text != null && !text.isEmpty()) {
			textView.setText(text);
		} else {
			textView.setVisibility(View.GONE);
			if (labelView != null) labelView.setVisibility(View.GONE);
		}
	}

	/**
	 * Associate a ACTION_VIEW Intent with a button's click, or hide the button if the Uri is null
	 * @param button The button to associate
	 * @param viewRi Seriously, say it out loud.
	 */
	public void setButtonClick(View button, final Uri viewRi) {
		if (viewRi != null) {
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						startActivity(new Intent(Intent.ACTION_VIEW).setData(viewRi));
					} catch (ActivityNotFoundException anfe) {
						Toast.makeText(getActivity(), "No handler for URI: " + viewRi.toString(), Toast.LENGTH_SHORT).show();
					}
				}
			});
		} else button.setVisibility(View.GONE);
	}

}
