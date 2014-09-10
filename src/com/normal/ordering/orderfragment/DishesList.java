package com.normal.ordering.orderfragment;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import com.normal.ordering.R;
import com.normal.ordering.entities.TheDishes;
import com.normal.ordering.tools.IApplication;
import com.normal.ordering.tools.IsConnect;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class DishesList extends Activity {
	
	private ListView listview;
	private DishesListAdapter adapter;
	private List<TheDishes> dishesList=new ArrayList<TheDishes>();
	private String getResult;
	private String getFoodList;
	private String storeId;
	private List<String> imagePath = new ArrayList<String>();
	private ArrayList<Map<String, Object>> dishesItems = new ArrayList<Map<String, Object>>();
	private ArrayList<Map<String, Object>> foodList = new ArrayList<Map<String, Object>>();
	private Button btnConfirem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disheslist);
		IApplication.getInstance().addActivity(this);
		btnConfirem=(Button)this.findViewById(R.id.activity_disheslist_btn_confirem);
		btnConfirem.setVisibility(View.GONE);
		listview=(ListView) this.findViewById(R.id.activity_disheslist_listview);
		Intent intent=getIntent();
		storeId=intent.getStringExtra("storeId").toString();
		getDishesList();
		if(dishesList.size()!=0){
			btnConfirem=(Button)this.findViewById(R.id.activity_disheslist_btn_confirem);
			btnConfirem.setOnClickListener(new btnConfiremTheOrder());
		}
	}
	public class btnConfiremTheOrder implements OnClickListener{

		@SuppressWarnings("unchecked")
		@Override
		public void onClick(View v) {
			foodList=(ArrayList<Map<String,Object>>)adapter.getDishesList().clone();
			if(foodList.size()==0){
				Toast.makeText(getBaseContext(), "你还没有选择任何东西",Toast.LENGTH_LONG).show();
			}else{
				Intent intent=new Intent();
				intent.putExtra("foodList", foodList);
				intent.putExtra("storeId", storeId);
				intent.setClass(DishesList.this, ConfiremOrder.class);
				startActivity(intent);
			}
		}
		
	}
	
	private void getDishesList() {
		if (IsConnect.isConnect(this)) {// 判断是否联网
			try {
				new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							downloadDishes();
							Thread.sleep(1000);// 线程睡眠1S
							if (getResult.equals("success")) {// 获取数据成功再更新
								myHandler.sendEmptyMessage(1);
							} else {
								myHandler.sendEmptyMessage(2);
							}
						} catch (Exception e) {
							myHandler.sendEmptyMessage(0);
							e.printStackTrace();
						}

					}
				}).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			myHandler.sendEmptyMessage(3);
		}
	}
	
	private MyHandler myHandler = new MyHandler(this);

	private static class MyHandler extends Handler {

		private final WeakReference<Activity> mActivity;

		public MyHandler(DishesList dishesList) {
			mActivity = new WeakReference<Activity>(dishesList);
		}

		@Override
		public void handleMessage(Message msg) {
			if (msg != null) {
				int flag = msg.what;

				if (flag == 1) {
					((DishesList)mActivity.get()).initList();// 初始化数据
					((DishesList)mActivity.get()).listview.setSelection(0);
				}
				if (flag == 2) {
					Toast.makeText(mActivity.get(), "数据获取失败",
							Toast.LENGTH_SHORT).show();
				}
				if (flag == 0) {
					Toast.makeText(mActivity.get(), "网络超时，请稍后再试",
							Toast.LENGTH_SHORT).show();
				}
				if (flag == 3) {
				}
			}
		}
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
	
	
	
	public void initList() {
		getItem();
		adapter = new DishesListAdapter(this,
				R.layout.activity_diseheslistadapter,dishesItems);
		listview.setAdapter(adapter);
		if(dishesList.size()!=0){
			btnConfirem.setVisibility(View.VISIBLE);
			btnConfirem.setOnClickListener(new btnConfiremTheOrder());
		}else{
			Toast.makeText(this, "此商店还没有上架任何美食", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 下载打折信息
	 * 
	 * @throws Exception
	 */
	private void downloadDishes() throws Exception {
		URL url = null;
		HttpURLConnection urlConnection = null;
		OutputStream out = null;
		// 对于URLConnection来说，从服务器接手数据是输入流
		InputStream in = null;
		String results = null;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("id", storeId);
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

				getResult = result.getString("result");
				if (getResult.equals("success")) {
					getFoodList = result.getString("foodList");
					JSONArray foodList = new JSONArray(getFoodList);

					for (int i = 0; i < foodList.length(); i++) {
						String goodsName = foodList.getJSONObject(i)
								.getString("dishes");
						String price = foodList.getJSONObject(i)
								.getString("price");
						String imgPath=foodList.getJSONObject(i).getString("picture");
						TheDishes theDishes = new TheDishes(goodsName,
								price);
						imagePath.add(imgPath);
						dishesList.add(theDishes);		
						Toast.makeText(this, dishesList+"  "+imgPath, Toast.LENGTH_SHORT).show();
					}
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
	public void getItem(){
		int length=dishesList.size();
		Map<String, Object> item = new HashMap<String, Object>();
			for(int i=0;i<length;i++){
				TheDishes strDishes=dishesList.get(i);
				String strImg=imagePath.get(i);
				item.put("goodsName", strDishes.getGoodsName());
				item.put("price", strDishes.getPrice());
				item.put("imagePath", strImg);
				dishesItems.add(item);
			}
	}
}
