package com.normal.ordering.orderfragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.normal.ordering.R;
import com.normal.ordering.discountfragment.MyListView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SimpleAdapter;

public class ConfiremOrder extends Activity {

	private String storeId;//商家Id
	private MyListView listview;
	private Button btnCinfiremOrder;
	private ConfiremOrderAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confiremorder);
		
		
		this.listview=(MyListView) this.findViewById(R.id.activity_confiremorder_listview);
		this.btnCinfiremOrder=(Button) this.findViewById(R.id.activity_confiremorder_btn_confiremorder);
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 8; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("textItem", "icon" + i);// 按序号添加ItemText
			items.add(item);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, items,
				R.layout.activity_confiremorder, new String[] {
						"textItem" }, new int[] {
						R.id.activity_confiremorderadapter_text_goodsname });
	}

	
}
