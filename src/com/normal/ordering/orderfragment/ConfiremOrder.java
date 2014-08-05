package com.normal.ordering.orderfragment;


import java.util.ArrayList;
import java.util.List;

import com.normal.ordering.R;
import com.normal.ordering.entities.TheDishes;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ConfiremOrder extends Activity {

	private String storeId;//商家Id
	private ListView listview;
	private Button btnConfiremOrder;
	public TextView txtTotalPrice;
	private ConfiremOrderAdapter adapter;
	public static String totalPrice;
	private List<TheDishes> theOrderList=new ArrayList<TheDishes>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confiremorder);
		
		
		this.listview=(ListView) this.findViewById(R.id.activity_confiremorder_listview);
		this.txtTotalPrice=(TextView) this.findViewById(R.id.activity_confiremorder_text_totalprice);
		this.btnConfiremOrder=(Button) this.findViewById(R.id.activity_confiremorder_btn_confiremorder);
		for (int i = 0; i < 8; i++) {
			TheDishes theOrder=new TheDishes("Icon"+i, "2", String.valueOf(i+1));
			theOrderList.add(theOrder);
		}
		adapter=new ConfiremOrderAdapter(this, R.layout.activity_confiremorderadapter, theOrderList);
		listview.setAdapter(adapter);
		btnConfiremOrder.setOnClickListener(new confiremOrder());
		totalPrice=getTotalPrice();
		txtTotalPrice.setText(totalPrice);
		Toast.makeText(this, theOrderList.get(1).getStoreName(), Toast.LENGTH_LONG).show();
	}
	
	public String getTotalPrice(){
		int culPrice=0;
		for(int i=0;i<theOrderList.size();i++){
			TheDishes getTotalPrice=(TheDishes)theOrderList.get(i);
			int price=Integer.parseInt(getTotalPrice.getPrice());
			int number=Integer.parseInt(getTotalPrice.getNumber());
			culPrice+=price*number;
		}
		totalPrice=""+culPrice;
		return totalPrice;
	}
	
	
	
	public class confiremOrder implements OnClickListener{

		@Override
		public void onClick(View v) {
			
			
		}
		
	} 
	
}
