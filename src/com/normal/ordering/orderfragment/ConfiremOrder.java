package com.normal.ordering.orderfragment;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import com.normal.ordering.userfragment.MyOrder;
import com.normal.ordering.R;
import com.normal.ordering.main.MainActivity;
import com.normal.ordering.tools.IApplication;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ConfiremOrder extends Activity {

	private String storeId;//商家Id
	private ListView listview;
	private Button btnConfiremOrder;
	private Button btnBooking;
	public TextView txtTotalPrice;
	private ConfiremOrderAdapter adapter;
	private ArrayList<Map<String, Object>> foodList = new ArrayList<Map<String, Object>>();
	private String userName;
	public static ArrayList<Map<String, Object>> myOrderList = new ArrayList<Map<String, Object>>();
	public static ArrayList<Map<String, Object>> myBookingTime = new ArrayList<Map<String, Object>>();
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confiremorder);
		
		
		this.listview=(ListView) this.findViewById(R.id.activity_confiremorder_listview);
		Intent intent=getIntent();
		foodList=(ArrayList<Map<String, Object>>) intent.getSerializableExtra("foodList");
		storeId=intent.getStringExtra("storeId");
		adapter=new ConfiremOrderAdapter(this, R.layout.activity_confiremorderadapter, foodList);
		listview.setAdapter(adapter);
		this.btnBooking=(Button) this.findViewById(R.id.activity_confiremorder_btn_booking);
		this.btnConfiremOrder=(Button) this.findViewById(R.id.activity_confiremorder_btn_confiremorder);
		btnConfiremOrder.setOnClickListener(new confiremOrder());
		btnBooking.setOnClickListener(new btnBooking());
		listview.setOnItemClickListener(new listviewClickListener());
		IApplication.getInstance().addActivity(this);
	}
	
	public class btnBooking implements OnClickListener{


		@Override
		public void onClick(View view) {
			if(IApplication.getInstance().getUser()!=null){
				if(myOrderList.size()!=0){
					myOrderList.clear();
				}
				if(myBookingTime.size()!=0){
					myBookingTime.clear();
				}
				LayoutInflater inflater = (LayoutInflater)ConfiremOrder
						.this.getSystemService(LAYOUT_INFLATER_SERVICE);
				final View dataTime=inflater.inflate(R.layout.datatime_activity, null);
				final EditText editYear=(EditText) dataTime.findViewById(R.id.datatimg_activity_year);
				final EditText editMonth=(EditText) dataTime.findViewById(R.id.datatimg_activity_month);
				final EditText editDay=(EditText) dataTime.findViewById(R.id.datatimg_activity_day);
				final EditText editHour=(EditText) dataTime.findViewById(R.id.datatimg_activity_hour);
				new TimeTextWatcher(editHour).checkEditText();
				new TimeTextWatcher(editYear).checkEditText();
				new TimeTextWatcher(editMonth).checkEditText();
				new TimeTextWatcher(editDay).checkEditText();
				AlertDialog alert=new AlertDialog.Builder(ConfiremOrder.this)
										.setTitle("请设置时间")
										.setView(dataTime)
										.setNegativeButton("确定", new DialogInterface.OnClickListener() {
											
											@SuppressWarnings("unchecked")
											@Override
											public void onClick(DialogInterface dialog, int which) {
												StringBuffer sb=new StringBuffer();
												String year=editYear.getText().toString();
												sb.append(year+"-");
												String month=editMonth.getText().toString();
												sb.append(month+"-");
												String day=editMonth.getText().toString();
												sb.append(day+" :");
												String hour=editHour.getText().toString();
												sb.append(hour);
												String str=sb.toString();
												Map<String,Object> time=new HashMap<String, Object>();
												time.put("time", str);
												myBookingTime.add(time);
												foodList.clear();
												foodList=(ArrayList<Map<String, Object>>) adapter.getItems().clone();
												myOrderList.addAll(foodList);
												Intent intent = new Intent();
												userName=IApplication.getInstance().getUser().getUserName();
												intent.setClass(ConfiremOrder.this, MyOrder.class);
												startActivity(intent);
											}
										})
										.setPositiveButton("取消", null)
										.create();
				alert.show();
			}else{
				AlertDialog alert=new AlertDialog.Builder(ConfiremOrder.this).create();
				alert.setTitle("提示");
				alert.setMessage("你还没有登录");
				alert.setButton(DialogInterface.BUTTON_NEGATIVE, "登录", listener);
				alert.setButton(DialogInterface.BUTTON_POSITIVE, "取消", listener);
				alert.show();
			}
		}
	}
	
	public class listviewClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, final int position,
				long arg3) {
			LayoutInflater inflater = (LayoutInflater)ConfiremOrder
					.this.getSystemService(LAYOUT_INFLATER_SERVICE);
			final View layoutmore=inflater.inflate(R.layout.dialogview_more,null);
			String amount=adapter.getDishesAmount(position);
			TextView restnumber=(TextView) layoutmore.findViewById(R.id.dialogview_restnumber);
			restnumber.setText(amount+"份");
			String []itemList=new String[]{"删除美食","1份","2份","3份"};			
			AlertDialog alert=new AlertDialog.Builder(ConfiremOrder.this)
					.setTitle("修改美食")
					.setItems(itemList, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch(which){
							case 0:
								Map<String,Object> itemMap=adapter.getItemMap(position);
								foodList.remove(itemMap);
								if(foodList.size()==0){
									Intent intent=new Intent();
									intent.setClass(ConfiremOrder.this, DishesList.class);
									intent.putExtra("storeId", storeId);
									startActivity(intent);
								}else{
									adapter=new ConfiremOrderAdapter
											(ConfiremOrder.this, R.layout.activity_confiremorderadapter, foodList);
									listview.setAdapter(adapter);
								}
								break;
							case 1:
								Map<String,Object> map0=adapter.getItemMap(position);
								map0.put("number", 1);
								foodList.set(position, map0);
								adapter.notifyDataSetChanged();
								break;
							case 2:
								Map<String,Object> map1=adapter.getItemMap(position);
								map1.put("number", 2);
								foodList.set(position, map1);
								adapter.notifyDataSetChanged();
								break;
							case 3:
								Map<String,Object> map2=adapter.getItemMap(position);
								map2.put("number", 3);
								foodList.set(position, map2);
								adapter.notifyDataSetChanged();
								break;
							}
						}
						
					})
					.setView(layoutmore)
					.setNegativeButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							EditText editNumber=(EditText) layoutmore.findViewById(R.id.dialogview_txt_more);
							String moreNumber=editNumber.getText().toString();
							if(moreNumber.length()>0){
								Map<String,Object> map=adapter.getItemMap(position);
								map.put("number", moreNumber);
								foodList.set(position, map);
								adapter.notifyDataSetChanged();
							}
						}
					})
					.setPositiveButton("取消", null)
					.create();
			alert.show();
		}
		
	}
	
	public class confiremOrder implements OnClickListener{

		@SuppressWarnings("unchecked")
		@Override
		public void onClick(View v) {
			if(IApplication.getInstance().getUser()!=null){
				if(myOrderList.size()!=0){
					myOrderList.clear();
				}
				if(myBookingTime.size()!=0){
					myBookingTime.clear();
				}
				Map<String,Object> time=new HashMap<String, Object>();
				time.put("time", "已在餐厅");
				myBookingTime.add(time);
				foodList.clear();
				foodList=(ArrayList<Map<String, Object>>) adapter.getItems().clone();
				myOrderList.addAll(foodList);
				Intent intent = new Intent();
				userName=IApplication.getInstance().getUser().getUserName();
				intent.setClass(ConfiremOrder.this, MyOrder.class);
				startActivity(intent);
	//			Toast.makeText(getBaseContext(), foodList+"", Toast.LENGTH_SHORT).show();
				/*try {
					uploadOrder();
				} catch (IOException e) {
					e.printStackTrace();
				}*/
			}else{
				AlertDialog alert=new AlertDialog.Builder(ConfiremOrder.this).create();
				alert.setTitle("提示");
				alert.setMessage("你还没有登录");
				alert.setButton(DialogInterface.BUTTON_NEGATIVE, "登录", listener);
				alert.setButton(DialogInterface.BUTTON_POSITIVE, "取消", listener);
				alert.show();
			}
		}
		
	} 
	
	DialogInterface.OnClickListener listener=new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch(which){
			case DialogInterface.BUTTON_NEGATIVE:
				Intent intent=new Intent();
				intent.setClass(ConfiremOrder.this, MainActivity.class);
				intent.putExtra("gotoString", "UserFragment");
				startActivity(intent);
				break;
			case DialogInterface.BUTTON_POSITIVE:
				break;
			default:
				break;
			}
		}
	};
	/**
	 * 
	 * @param in
	 * @return json
	 * @throws Exception
	 */
	private static String parseResponseResults(InputStream in) throws Exception {
		String results = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		try {
			while ((len = in.read(data)) != -1) {
				byteArrayOutputStream.write(data, 0, len);
			}
			results = new String(byteArrayOutputStream.toByteArray());
		} finally {
			if (byteArrayOutputStream != null) {
				byteArrayOutputStream.close();
			}
		}
		return results;
	}
	/**
	 * Map<Key, value>
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static StringBuffer setPostPassParams(Map<String, String> params) throws UnsupportedEncodingException {
		StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
		        try {
		            for(Map.Entry<String, String> entry : params.entrySet()) {
		                 stringBuffer.append(entry.getKey())
		                             .append("=")
		                             .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
		                            .append("&");
		             }
		             stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
		         } catch (Exception e) {
		            e.printStackTrace();
		         }
		         return stringBuffer;
	}
	private void uploadOrder() throws IOException{
		URL url = null;
		HttpURLConnection urlConnection = null;
		OutputStream out = null;
		// 对于URLConnection来说，从服务器接手数据是输入流
		InputStream in = null;
		String results = null;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("id", storeId);
			params.put("foodList",myOrderList.toString());
			params.put("userName",userName);
			byte[] data = setPostPassParams(params).toString().getBytes();
			url = new URL(
					"http://www.doubteam.com:81/Ordering/GetFoodList.action");
			urlConnection = (HttpURLConnection) url.openConnection();
			// 请求连接超时
			urlConnection.setConnectTimeout(10 * 1000);
			// 响应超时
			urlConnection.setReadTimeout(10 * 1000);
			// 当前连接可以读取数据
			urlConnection.setDoInput(true);
			// 当前连接可以写入数据
			urlConnection.setDoOutput(true);
			// 请求方式为POST
			urlConnection.setRequestMethod("POST");
			// 取消缓存
			urlConnection.setUseCaches(false);
			// 常规text/html提交
			urlConnection.setRequestProperty("Content-Type",
								"application/x-www-form-urlencoded");
			
			urlConnection.setRequestProperty("Content-Length",
					String.valueOf(data.length));
			urlConnection.connect();
			// 在这个连接上获得输出流
			out = urlConnection.getOutputStream();
			// 向服务器去传递数据
			out.write(data);
			out.flush();
			int responseCode = urlConnection.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				throw new Exception("服务器出错");
			} else {
				in = urlConnection.getInputStream();

				results = parseResponseResults(in);

				/**
				 * 解析服务器传回来的JSON数据 把它封装到List中
				 */
				JSONObject result = new JSONObject(results);

				if (result.getString("result").equals("success")) {
					Toast.makeText(getBaseContext(), "订餐成功", Toast.LENGTH_SHORT).show();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (in != null) {
				in.close();
			}
			if (out != null) {

				out.close();
			}
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
	}
}
