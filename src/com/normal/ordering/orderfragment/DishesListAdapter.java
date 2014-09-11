package com.normal.ordering.orderfragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.normal.ordering.R;
import com.normal.ordering.entities.TheDishes;
import com.normal.ordering.main.MainActivity;
import com.normal.ordering.tools.AsyncViewTask;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class DishesListAdapter extends ArrayAdapter<TheDishes>{

	
	private LayoutInflater mInflater;
	private int mResourceId;
	private ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
	private ArrayList<Map<String, Object>> dishesList = new ArrayList<Map<String, Object>>();
	
	public DishesListAdapter(Context context,
			int textViewResourceId, ArrayList<Map<String,Object>> item) {
		super(context, textViewResourceId);
		
		this.mInflater=LayoutInflater.from(context);
		this.mResourceId=textViewResourceId;
		this.items=(ArrayList<Map<String,Object>>)item.clone();
	}
	@Override
	public int getCount() {
		
		return items.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Map<String, Object> item;
		item=items.get(position);
		final String goodsName = item.get("goodsName").toString();
		final String price = item.get("price").toString();
		final String imagePath=item.get("imagePath").toString();
		final String amount=item.get("amount").toString();
		LinearLayout view = null;
		if (convertView == null) {
			view = (LinearLayout) this.mInflater
					.inflate(this.mResourceId, null);
		} else {
			view = (LinearLayout) convertView;
		}

		TextView tvGoodsName = (TextView) view
				.findViewById(R.id.activity_disheslistadapter_item_txt_name);
		final ImageView image = (ImageView) view
				.findViewById(R.id.activity_disheslistadapter_item_img);
		final Button btnOrder=(Button)view.findViewById(R.id.activity_disheslistadapter_item_btn_order);
		
		btnOrder.setTag(position);

		tvGoodsName.setText(goodsName);
		btnOrder.setText("￥"+price);
		
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
			btnOrder.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					btnOrder.setBackgroundColor(Color.GRAY);
					Map<String,Object> getDishesList=new HashMap<String, Object>();
					getDishesList.put("goodsName", goodsName);
					getDishesList.put("price", price);
					getDishesList.put("number", 1);
					getDishesList.put("amount", amount);
					if(dishesList.size()==0){
						dishesList.add(getDishesList);
					}else{
						for(int i=0;i<dishesList.size();i++){
							if(dishesList.get(i).get("goodsName").equals(getDishesList.get("goodsName"))){
							}
							else{
								dishesList.add(getDishesList);
							}
						}
					}			
				}
			});
		return view;
	}
	public ArrayList<Map<String, Object>> getDishesList() {
		
		return dishesList;
		
	}
	
}
