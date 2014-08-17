package com.normal.ordering.orderfragment;

import java.util.ArrayList;
import java.util.Map;
import com.normal.ordering.R;
import com.normal.ordering.main.MainActivity;
import com.normal.ordering.main.MoreFragment;
import com.normal.ordering.tools.AsyncViewTask;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderFragmentAdapter extends ArrayAdapter<ArrayList<Map<String, Object>>> {
	
	private LayoutInflater mInflater;
	private int mResourceId;
	private ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();

	@SuppressWarnings("unchecked")
	public OrderFragmentAdapter(Context context, int textViewResourceId,ArrayList<Map<String,Object>> item) {
		super(context, textViewResourceId);
		this.mInflater = LayoutInflater.from(context);
		this.mResourceId = textViewResourceId;
		items=(ArrayList<Map<String,Object>>)item.clone();

	}
	


	@Override
	public int getCount() {

		return items.size();
	}



	@Override
	public long getItemId(int position) {

		Map<String, Object> item;
		item=items.get(position);
		return Long.parseLong(item.get("storeId").toString());
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Map<String, Object> item;
		item=items.get(position);
		String storeName = item.get("storeName").toString();
		String storeAddress = item.get("storeAddress").toString();
		final String imagePath=item.get("imagePath").toString();
		LinearLayout view = null;
		if (convertView == null) {
			view = (LinearLayout) this.mInflater
					.inflate(this.mResourceId, null);
		} else {
			view = (LinearLayout) convertView;
		}

		TextView txtStoreAddress = (TextView) view
				.findViewById(R.id.orderActivity_item_storeaddress);
		TextView txtStoreName = (TextView) view
				.findViewById(R.id.orderActivity_item_name);
		final ImageView image = (ImageView) view
				.findViewById(R.id.orderActivity_item_img);

		txtStoreAddress.setText(storeAddress);
		txtStoreName.setText(storeName);
		
		if(MainActivity.noImg==false){
			image.setTag((imagePath.equals("")||imagePath==null)?null:imagePath);
			new AsyncViewTask().execute(image);
		}
		else{
			image.setBackgroundResource(R.drawable.action_bar_bg);
			image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					image.setTag((imagePath.equals("")||imagePath==null)?null:imagePath);
					new AsyncViewTask().execute(image);
				}
			});
			
		}
			Log.i("Adapter", imagePath+"当前位置："+position + "   总个数:"
					+ this.getCount());
		
		return view;
	}

	

}
