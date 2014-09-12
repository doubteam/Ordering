package com.normal.ordering.userfragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.normal.ordering.R;

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

public class MyOrderAdapter extends ArrayAdapter<ArrayList<Map<String, Object>>> {
	
	private ArrayList<Map<String, Object>> items=new java.util.ArrayList<Map<String,Object>>();
	private LayoutInflater mInflater;
	private int mResourceId;
	@SuppressWarnings("unchecked")
	public MyOrderAdapter(Context context, int textViewResourceId, ArrayList<Map<String, Object>> item) {
		super(context, textViewResourceId);
		this.mInflater = LayoutInflater.from(context);
		this.mResourceId = textViewResourceId;
		items=(ArrayList<Map<String, Object>>) item.clone();
		
	}
	@Override
	public int getCount() {
		
		return items.size();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Map<String, Object> item;
		item=items.get(position);
		String goodsName = item.get("goodsName").toString();
		String price = item.get("totalPrice").toString();
		String number=item.get("number").toString();
		LinearLayout view = null;
		if (convertView == null) {
			view = (LinearLayout) this.mInflater
					.inflate(this.mResourceId, null);
		} else {
			view = (LinearLayout) convertView;
		}

		
		
		TextView txtGoodsName=(TextView)view.findViewById(R.id.activity_myorderadapter_text_goodsname);
		TextView editNumber=(TextView)view.findViewById(R.id.activity_myorderadapter_text_goodsnumber);
		TextView txtPrice=(TextView)view.findViewById(R.id.activity_myorderadapter_text_goodsprice);
		
		
		
		txtGoodsName.setText(goodsName);
		editNumber.setText(number);
		txtPrice.setText(price);
		
		return view;
	}
	

}
