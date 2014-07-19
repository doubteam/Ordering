package com.normal.ordering.orderfragment;

import java.util.ArrayList;
import java.util.List;
import com.normal.ordering.R;
import com.normal.ordering.entities.Store;
import com.normal.ordering.tools.AsyncViewTask;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderFragmentAdapter extends ArrayAdapter<Store> {
	
	private LayoutInflater mInflater;
	private int mResourceId;
	private List<String> imagePaths = new ArrayList<String>();

	public OrderFragmentAdapter(Context context, int textViewResourceId,
			List<Store> discountFood, List<String> imagePaths) {
		super(context, textViewResourceId, discountFood);
		this.mInflater = LayoutInflater.from(context);
		this.mResourceId = textViewResourceId;
		this.imagePaths.addAll(imagePaths);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Store mercchant = this.getItem(position);
		String merchantName = mercchant.getMerchantName();
		String merchantAddress = mercchant.getMerchantAddress();
		LinearLayout view = null;
		if (convertView == null) {
			view = (LinearLayout) this.mInflater
					.inflate(this.mResourceId, null);
		} else {
			view = (LinearLayout) convertView;
		}

		TextView txtContent = (TextView) view
				.findViewById(R.id.orderActivity_item_storeaddress);
		TextView txtName = (TextView) view
				.findViewById(R.id.orderActivity_item_name);
		ImageView image = (ImageView) view
				.findViewById(R.id.orderActivity_item_img);

		txtContent.setText(merchantAddress.toString());
		txtName.setText(merchantName);
		
			image.setTag((imagePaths.get(position).equals("")||imagePaths.get(position)==null)?null:imagePaths.get(position));
			new AsyncViewTask().execute(image);
			Log.i("Adapter", imagePaths.get(position)+"当前位置："+position + "   总个数:"
					+ this.getCount());
		
		return view;
	}

	@Override
	public Store getItem(int position) {
		return super.getItem(position);
	}
	

}
