package com.normal.ordering.orderfragment;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.normal.ordering.R;
import com.normal.ordering.entities.TheDishes;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ConfiremOrderAdapter extends ArrayAdapter<TheDishes> {

	private LayoutInflater mInflater;
	private int mResourceId;
	private ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
	public ConfiremOrderAdapter(Context context,int textViewResourceId, ArrayList<Map<String, Object>> item) {
		super(context, textViewResourceId);
		this.mInflater=LayoutInflater.from(context);
		this.mResourceId=textViewResourceId;
		items=(ArrayList<Map<String, Object>>) item.clone();
	}
	

	@Override
	public int getCount() {
		return items.size();
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
	
		Map<String, Object> item;
		item=items.get(position);
		String goodsName = item.get("goodsName").toString();
		String price = item.get("price").toString();
		String number=item.get("number").toString();
		LinearLayout view = null;
		if (convertView == null) {
			view = (LinearLayout) this.mInflater
					.inflate(this.mResourceId, null);
		} else {
			view = (LinearLayout) convertView;
		}

		
		
		TextView txtGoodsName=(TextView)view.findViewById(R.id.activity_confiremorderadapter_text_goodsname);
		EditText editNumber=(EditText)view.findViewById(R.id.activity_confiremoderadapter_edit_goodsnumber);
		TextView txtPrice=(TextView)view.findViewById(R.id.activity_confiremorderadapter_text_goodsprice);
		
		
		
		txtGoodsName.setText(goodsName);
		editNumber.setText(number);
		txtPrice.setText(price);
		
		
		editNumber.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				items.get(position).put("number", s);
			}
		});
		return view;
	}


	public ArrayList<Map<String, Object>> getItems() {
		return items;
	}

	
}
