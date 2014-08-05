package com.normal.ordering.orderfragment;



import java.util.List;

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
	public ConfiremOrderAdapter(Context context,int textViewResourceId, List<TheDishes> theOrder) {
		super(context, textViewResourceId);
		this.mInflater=LayoutInflater.from(context);
		this.mResourceId=textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		TheDishes theOrder=this.getItem(position);
		String goodsName=theOrder.getStoreName();
		String price=theOrder.getPrice();
		String number=theOrder.getNumber();
		
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
		
		
	/*	editNumber.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			//	theOrder.setNumber(s.toString());
				
			}
		});*/
		return view;
	}

	
}
