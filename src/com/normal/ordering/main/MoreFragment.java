package com.normal.ordering.main;

import com.normal.ordering.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		addPreferencesFromResource(R.xml.preferences);
		return super.onCreateView(inflater, container, savedInstanceState);

	}

}
