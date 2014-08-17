package com.normal.order.userfragment;

import com.normal.ordering.R;
import com.normal.ordering.orderfragment.ConfiremOrder;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class MyOrder extends Activity{

	private ListView listview;
	private MyOrderAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myorder);
		
		listview=(ListView) this.findViewById(R.id.activity_myorder_listview);
		if(ConfiremOrder.myOrderList.size()==0){
			Toast.makeText(this, "你还没有点任何东西", Toast.LENGTH_SHORT).show();
		}else{
			listview=(ListView) this.findViewById(R.id.activity_myorder_listview);
			adapter=new MyOrderAdapter(this, R.layout.activity_myorderadapter, ConfiremOrder.myOrderList);
			listview.setAdapter(adapter);
		}
	}

	
}
