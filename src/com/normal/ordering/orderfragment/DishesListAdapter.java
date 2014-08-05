package com.normal.ordering.orderfragment;

import java.util.List;

import com.normal.ordering.entities.TheDishes;

import android.content.Context;
import android.widget.ArrayAdapter;

public class DishesListAdapter extends ArrayAdapter<TheDishes>{

	public DishesListAdapter(Context context,
			int textViewResourceId, List<TheDishes> objects) {
		super(context, textViewResourceId, objects);
		
	}

}
