package com.normal.ordering.userfragment;

import com.normal.ordering.R;
import com.normal.ordering.main.MainActivity;
import com.normal.ordering.orderfragment.ConfiremOrder;
import com.normal.ordering.tools.IApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Toast;

public class MyOrder extends Activity{

	private ListView listview;
	private MyOrderAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myorder);
		
		IApplication.getInstance().addActivity(this);
		listview=(ListView) this.findViewById(R.id.activity_myorder_listview);
		if(ConfiremOrder.myOrderList.size()==0){
			Toast.makeText(this, "你还没有点任何东西", Toast.LENGTH_SHORT).show();
		}else{
			listview=(ListView) this.findViewById(R.id.activity_myorder_listview);
			adapter=new MyOrderAdapter(this, R.layout.activity_myorderadapter, ConfiremOrder.myOrderList);
			listview.setAdapter(adapter);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(this, MainActivity.class);
			intent.putExtra("gotoString", "UserFragment");
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}

	
}
