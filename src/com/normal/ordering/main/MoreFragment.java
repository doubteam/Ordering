package com.normal.ordering.main;

import com.normal.ordering.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 更多--Fragment
 * 
 * @author Vaboon
 * @date 2014-6-2
 */
public class MoreFragment extends PreferenceFragment {

	public static boolean noImg=false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		addPreferencesFromResource(R.xml.preferences);
		
		getPreferenceManager();
		SharedPreferences spfs=PreferenceManager.getDefaultSharedPreferences(getActivity());
		noImg=spfs.getBoolean("image_preference", true);
		
		return super.onCreateView(inflater, container, savedInstanceState);

	}

}
