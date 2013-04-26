package com.espian.ribotchallenge.data;

import android.graphics.Color;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Class which encapsulates a single Ribot team member.
 * <p/>
 * Author: Alex Curran
 * Date: 26/04/2013
 */
public class RibotItem implements Parcelable {

	private final String id, firstName, lastName;
	private String nickname;
	private String role;
	private String description;
	private String twitter;
	private String favSweet;
	private String favSeason;
	private String location;
	private String email;

	private int hexColor = Color.parseColor("#33B535");
	private boolean isLightResponse;

	private Ribotar ribotar;

	/**
	 * Creates a new RibotItem from the given JSONObject
	 *
	 * @param json            Object to construct the RibotItem
	 * @param isLightResponse Whether the API call was from /team, not /team/:id (which has less data)
	 */
	public RibotItem(JSONObject json, boolean isLightResponse) throws IllegalArgumentException {
		try {

			// Absolutely required items
			id = json.getString("id");
			firstName = json.getString("firstName");
			lastName = json.getString("lastName");

			// Optional items also available in the lightweight response
			if (!json.isNull("hexColor")) hexColor = Color.parseColor(json.getString("hexColor"));
			if (!json.isNull("nickname")) nickname = json.getString("nickname");
			if (!json.isNull("role")) role = json.getString("role");

			if (!isLightResponse) {

				// Optional items available in a full response, from /team/:id
				if (!json.isNull("description")) description = json.getString("description");
				if (!json.isNull("twitter")) twitter = json.getString("twitter");
				if (!json.isNull("favSweet")) favSweet = json.getString("favSweet");
				if (!json.isNull("favSeason")) favSeason = json.getString("favSeason");
				if (!json.isNull("email")) email = json.getString("email");
				if (!json.isNull("location")) location = json.getString("location");

			}

		} catch (JSONException e) {
			// If we've got here, something has gone really wrong
			throw new IllegalArgumentException("Couldn't create RibotItem from JSON", e);
		}
	}

	public String getDescription() {
		return description;
	}

	public String getFavSeason() {
		return favSeason;
	}

	public String getFavSweet() {
		return favSweet;
	}

	public String getFirstName() {
		return firstName;
	}

	public int getHexColor() {
		return hexColor;
	}

	public String getId() {
		return id;
	}

	public boolean isLightResponse() {
		return isLightResponse;
	}

	public String getLastName() {
		return lastName;
	}

	/**
	 * Get the Ribot's full name
	 *
	 * @param withNickname Insert a nickname, if available
	 * @return Name in format 'Joe (Jimmy) Bloggs' or 'Joe Bloggs'
	 */
	public String getFullName(boolean withNickname) {
		if (withNickname && getNickname() != null) return firstName + " (" + nickname + ") " + lastName;
		else return firstName + " " + lastName;
	}

	public String getNickname() {
		return nickname;
	}

	public String getRole() {
		return role;
	}

	public String getTwitter() {
		return twitter;
	}

	public Uri getTwitterUri() {
		return twitter == null || twitter.isEmpty() ? null : Uri.parse("https://twitter.com/" + getTwitter());
	}

	public String getEmail() {
		return email;
	}

	public Uri getEmailUri() {
		return email == null || email.isEmpty() ? null : Uri.parse("mailto:" + email);
	}

	public String getLocation() {
		return location;
	}

	public Uri getLocationUri() {
		return location == null || location.isEmpty() ? null : Uri.parse(String.format("geo:0,0?q=%s", URLEncoder.encode(location)));
	}

	public Ribotar getRibotar() {
		if (ribotar != null) return ribotar;
		else return ribotar = new Ribotar(this);
	}

	@Override
	/**
	 * Overridden method, which makes the {@link Adapter} implementations easier
	 */
	public String toString() {
		return getFullName(true);
	}

	// Parcelable items

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(id);
		dest.writeString(firstName);
		dest.writeString(lastName);
		dest.writeString(nickname);
		dest.writeString(role);
		dest.writeInt(hexColor);
		dest.writeInt(isLightResponse ? 1 : 0);

		if (!isLightResponse) {

			dest.writeString(description);
			dest.writeString(twitter);
			dest.writeString(favSweet);
			dest.writeString(favSeason);
			dest.writeString(location);
			dest.writeString(email);

		}

	}

	public static final Parcelable.Creator<RibotItem> CREATOR
			= new Parcelable.Creator<RibotItem>() {
		public RibotItem createFromParcel(Parcel in) {
			return new RibotItem(in);
		}

		public RibotItem[] newArray(int size) {
			return new RibotItem[size];
		}
	};

	private RibotItem(Parcel in) {

		id = in.readString();
		firstName = in.readString();
		lastName = in.readString();
		nickname = in.readString();
		role = in.readString();
		hexColor = in.readInt();
		isLightResponse = in.readInt() == 1;

		if (!isLightResponse) {

			description = in.readString();
			twitter = in.readString();
			favSweet = in.readString();
			favSeason = in.readString();
			location = in.readString();
			email = in.readString();

		}
	}
}
