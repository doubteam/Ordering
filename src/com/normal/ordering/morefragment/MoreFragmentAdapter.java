package com.normal.ordering.morefragment;

import java.util.LinkedHashMap;
import java.util.Map;

import com.normal.ordering.R;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;

public class MoreFragmentAdapter extends BaseAdapter {

	public final ArrayAdapter<String> headers;
	public Button viewButton;
	public final static int TYPE_SECTION_HEADER = 0;
	public final String TAG="MoreFragmentAdapter";
	public final Map<String, Adapter> sections = new LinkedHashMap<String, Adapter>();

	public MoreFragmentAdapter(Context context) {
		headers = new ArrayAdapter<String>(context,
				R.layout.fragment_more_header);
		Log.d(TAG, "MoreFragmentAdapter");
	}

	public void addSection(String section, Adapter adapter) {
		this.headers.add(section);
		this.sections.put(section, adapter);
		Log.d(TAG, "addSection");
	}

	@Override
	public int getCount() {
		
		int total = 0;
		for (Adapter adapter : this.sections.values())
			total += adapter.getCount() + 1;
		Log.d(TAG, "getCount:"+total);
		return total;
	}

	public int getViewTypeCount() {
		int total = 1;
		for (Adapter adapter : this.sections.values())
			total += adapter.getViewTypeCount();
		Log.d(TAG, "getViewTypeCount:"+total);
		return total;
	}

	public int getItemViewType(int position) {
		Log.d(TAG, "getItemViewType");
		int type = 1;
		for (Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;
			if (position == 0)
				return TYPE_SECTION_HEADER;
			if (position < size)
				return type + adapter.getItemViewType(position - 1);
			position -= size;
			type += adapter.getViewTypeCount();
		}
		return -1;
	}

	public boolean areAllItemsSelectable() {
		Log.d(TAG, "areAllItemsSelectable");
		return false;
	}

	public boolean isEnabled(int position) {
		Log.d(TAG, "isEnabled");
		return (getItemViewType(position) != TYPE_SECTION_HEADER);
	}

	@Override
	public Object getItem(int position) {
		Log.d(TAG, "getItem");
		for (Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;
			if (position == 0)
				return section;
			if (position < size)
				return adapter.getItem(position - 1);
			position -= size;
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		Log.d(TAG, "getItemId");
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "View getView");
		int sectionnum = 0;
		for (Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if (position == 0)
				return headers.getView(sectionnum, convertView, parent);
			if (position < size)
				return adapter.getView(position - 1, convertView, parent);

			// otherwise jump into next section
			position -= size;
			sectionnum++;
		}
		return null;
	}

}
