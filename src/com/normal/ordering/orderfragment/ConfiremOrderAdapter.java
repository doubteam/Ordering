package com.normal.ordering.orderfragment;



import java.util.ArrayList;
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


public class ConfiremOrderAdapter extends ArrayAdapter<ArrayList<Map<String, Object>>> {

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

	public Map<String , Object> getItemMap(int position){
		Map<String, Object> item;
		item=items.get(position);
		return item;
	}

	public String getDishesAmount(int position){
		Map<String, Object> item;
		item=items.get(position);
		String amount=item.get("amount").toString();
		return amount;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
	
		Map<String, Object> item;
		item=items.get(position);
		String goodsName = item.get("goodsName").toString();
		String price = item.get("singlePrice").toString();
		String number=item.get("number").toString();
		String totalPrice=(Integer.parseInt(price)*Integer.parseInt(number))+"";
		LinearLayout view = null;
		if (convertView == null) {
			view = (LinearLayout) this.mInflater
					.inflate(this.mResourceId, null);
		} else {
			view = (LinearLayout) convertView;
		}

		
		
		TextView txtGoodsName=(TextView)view.findViewById(R.id.activity_confiremorderadapter_text_goodsname);
		TextView txtNumber=(TextView)view.findViewById(R.id.activity_confiremoderadapter_txt_goodsnumber);
		TextView txtPrice=(TextView)view.findViewById(R.id.activity_confiremorderadapter_text_goodsprice);
		
		
		
		txtGoodsName.setText(goodsName);
		txtNumber.setText(number);
		txtPrice.setText(totalPrice);
		
		return view;
	}


	public ArrayList<Map<String, Object>> getItems() {
		return items;
	}

	
}
