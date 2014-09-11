package com.normal.ordering.discountfragment;

import java.util.ArrayList;
import java.util.Map;
import com.normal.ordering.R;
import com.normal.ordering.main.MainActivity;
import com.normal.ordering.tools.AsyncViewTask;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DiscountAdapter extends ArrayAdapter<ArrayList<Map<String, Object>>> {

	private LayoutInflater mInflater;
	private int mResourceId;
	private ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
	
	public DiscountAdapter(Context context, int textViewResourceId,ArrayList<Map<String,Object>> item) {
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
		String storeId=item.get("storeId").toString();
		return Long.parseLong(storeId);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Map<String, Object> item;
		item=items.get(position);
		String storeName = item.get("storeName").toString();
		String discountText = item.get("discountText").toString();
		final String imagePath=item.get("imagePath").toString();
		LinearLayout view = null;
		if (convertView == null) {
			view = (LinearLayout) this.mInflater
					.inflate(this.mResourceId, null);
		} else {
			view = (LinearLayout) convertView;
		}

		TextView txtContent = (TextView) view
				.findViewById(R.id.discountActivity_item_discounttext);
		TextView txtName = (TextView) view
				.findViewById(R.id.discountActivity_item_name);
		final ImageView image = (ImageView) view
				.findViewById(R.id.discountActivity_item_img);

		txtContent.setText(discountText);
		txtName.setText(storeName);
		
		if(MainActivity.noImg==false){
			image.setTag((imagePath.equals("")||imagePath==null)?null:imagePath);
			new AsyncViewTask().execute(image);
		}
		else{
			image.setBackgroundResource(R.drawable.refresh_image);
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
