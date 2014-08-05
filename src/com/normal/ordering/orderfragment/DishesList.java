package com.normal.ordering.orderfragment;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.normal.ordering.R;
import com.normal.ordering.entities.TheDishes;
import com.normal.ordering.tools.IsConnect;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.Toast;

public class DishesList extends Activity {
	
	private ListView listview;
	private DishesListAdapter adapter;
	private List<TheDishes> dishesList=new ArrayList<TheDishes>();
	private List<TheDishes> confiremDishes=new ArrayList<TheDishes>();
	private static String getResult;
	private static String businessActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disheslist);
		
		listview=(ListView) this.findViewById(R.id.activity_disheslist_listview);
	}

	private void getDiscountFoodList() {
		if (IsConnect.isConnect(this)) {// 判断是否联网
			try {
				new Thread(new Runnable() {

					@Override
					public void run() {

						try {
							downloadDiscountFood();
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
					initList();// 初始化数据
					this.listview
							.setSelection(0);
				}
				if (flag == 2) {
					Toast.makeText(DishesList, "数据获取失败",
							1000).show();
				}
				if (flag == 0) {
					Toast.makeText(mActivity.get().getActivity(), "网络超时，请稍后再试",
							1000).show();
				}
				if (flag == 3) {
				}
			}
		}
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
	private void initList() {
		adapter = new DishesListAdapter(this,
				R.layout.activity_diseheslistadapter,
				new ArrayList<TheDishes>(dishesList));
		listview.setAdapter(adapter);
		dishesList.clear();
		getDiscountFoodList();
		/*listview.setOnRefreshListener(new OnRefreshListener()
		// 刷新
		{
			@Override
			public void onRefresh() {
				getDiscountFoodList();
			}

		});*/
	}

	/**
	 * 下载打折信息
	 * 
	 * @throws Exception
	 */
	private void downloadDiscountFood() throws Exception {
		URL url = null;
		HttpURLConnection urlConnection = null;
		OutputStream out = null;
		// 对于URLConnection来说，从服务器接手数据是输入流
		InputStream in = null;
		String results = null;
		try {
			url = new URL(
					"http://www.doubteam.com/Ordering/GetBusinessList.action");
			urlConnection = (HttpURLConnection) url.openConnection();
			// 请求连接超时
			urlConnection.setConnectTimeout(10 * 1000);
			// 响应超时
			urlConnection.setReadTimeout(10 * 1000);
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
					businessActivity = result.getString("businessActivityList");
					JSONArray businessList = new JSONArray(businessActivity);

					for (int i = 0; i < businessList.length(); i++) {
						// 获取商店名称
						String storeName = businessList.getJSONObject(i)
								.getString("businessName");
						// 获取促销标题
						String discountText = businessList.getJSONObject(i)
								.getString("title");
						TheDishes theDishes = new TheDishes(storeName,
								discountText);
						dishesList.add(theDishes);		
						
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
}
