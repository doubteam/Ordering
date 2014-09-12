package com.normal.ordering.main;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import com.normal.ordering.R;
import com.normal.ordering.discountfragment.DiscountAdapter;
import com.normal.ordering.entities.DiscountFood;
import com.normal.ordering.orderfragment.DishesList;
import com.normal.ordering.tools.IsConnect;
import com.normal.ordering.tools.MyListView;
import com.normal.ordering.tools.MyListView.MyListViewListener;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 活动--Fragment
 * 
 * @author Vaboon
 * @date 2014-6-2
 */

public class DiscountFragment extends Fragment implements MyListViewListener{
	private List<DiscountFood> discountFoodList = new ArrayList<DiscountFood>();
	private static ProgressDialog progressDialogDiscount;
	private String getResult;
	private MyListView listview;
	private DiscountAdapter adapter;
	private List<String> imagePath = new ArrayList<String>();
	private static String businessActivity;
	private ArrayList<Map<String, Object>> discountFoodItems = new ArrayList<Map<String, Object>>();
	private static int discountFragmentUpdateCounts=0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.fragment_discount,
				container, false);
		// 允许使用菜单
		setHasOptionsMenu(true);

		this.listview = (MyListView) fragmentView
				.findViewById(R.id.discount_fragment_list_view);
		
		progressDialogDiscount = new ProgressDialog(getActivity());
		progressDialogDiscount.setTitle("请等待");
		progressDialogDiscount.setMessage("数据加载中.......");
		progressDialogDiscount.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialgo是否能够被取消
		progressDialogDiscount.setCancelable(false);
		// 明确进度
		progressDialogDiscount.setIndeterminate(true);
		if (IsConnect.isConnect(getActivity())) {
			progressDialogDiscount.show();
			getDiscountFoodList();
		}
		listview.setOnItemClickListener(new btnGetDishes());
		
		return fragmentView;
	}
	
	public class btnGetDishes implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			
			Intent intent=new Intent();
			long storeId=adapter.getItemId(position);
			intent.setClass(getActivity(), DishesList.class);
			intent.putExtra("storeId", storeId+"");
			startActivity(intent);
			
		}	
	}
	
	/**
	 * 拿打折活动表单
	 */
	private void getDiscountFoodList() {
		if (IsConnect.isConnect(getActivity())) {// 判断是否联网
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

		private final WeakReference<Fragment> mActivity;

		public MyHandler(DiscountFragment discountActivity) {
			mActivity = new WeakReference<Fragment>(discountActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			if (msg != null) {
				int flag = msg.what;

				if (progressDialogDiscount != null) {
					progressDialogDiscount.dismiss();
				}

				if (flag == 1) {
					((DiscountFragment) mActivity.get()).initList();// 初始化数据
					((DiscountFragment) mActivity.get()).onLoad();
					((DiscountFragment) mActivity.get()).listview
							.setSelection(0);
				}
				if (flag == 2) {
					Toast.makeText(mActivity.get().getActivity(), "数据获取失败",
							Toast.LENGTH_SHORT).show();
					((DiscountFragment) mActivity.get()).onLoad();
				}
				if (flag == 0) {
					Toast.makeText(mActivity.get().getActivity(), "网络超时，请稍后再试",
							Toast.LENGTH_SHORT).show();
					((DiscountFragment) mActivity.get()).onLoad();
				}
				if (flag == 3) {
					((DiscountFragment) mActivity.get()).onLoad();
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
					"http://www.doubteam.com:81/Ordering/GetBusinessActivityList.action");
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
								.getString("storeName");
						// 获取促销标题
						String discountText = businessList.getJSONObject(i)
								.getString("title");
						String storeId=businessList.getJSONObject(i).getString("storeId");
						DiscountFood discountFood = new DiscountFood(storeName,
								discountText,storeId);
						discountFoodList.add(discountFood);		
						imagePath.add(businessList.getJSONObject(i).getString(
								"image"));
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

	/**
	 * 适配器 初始化List
	 */
	private void initList() {

		discountFragmentUpdateCounts=0;
		getItem();
		listview.setPullLoadEnable(true);
		adapter = new DiscountAdapter(getActivity(),
				R.layout.fragment_discount_listview_content,discountFoodItems);
		listview.setAdapter(adapter);
		listview.setMyListViewListener(this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Toast.makeText(getActivity(), "menu text is " + item.getTitle(), Toast.LENGTH_SHORT)
				.show();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRefresh() {
		myHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				discountFoodList.clear();
				discountFoodItems.clear();
				getDiscountFoodList();
				discountFragmentUpdateCounts=0;
				getItem();
				adapter = new DiscountAdapter(getActivity(),
						R.layout.fragment_discount_listview_content,
						discountFoodItems);
				listview.setAdapter(adapter);
				onLoad();
			}
		}, 1000);
		
	}

	@Override
	public void onLoadMore() {
		myHandler.postDelayed(new Runnable() {
					
			@Override
			public void run() {
				getItem();
				adapter.notifyDataSetChanged();
		
				adapter = new DiscountAdapter(getActivity(),
						R.layout.fragment_discount_listview_content,
						discountFoodItems);
				listview.setAdapter(adapter);
				onLoad();				
			}
		}, 1000);
				
	}
	@SuppressLint("SimpleDateFormat")
	private void onLoad() {
		listview.stopRefresh();
		listview.stopLoadMore();
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		String date = format.format(new Date());
		listview.setRefreshTime(date);
	}
	Map<String, Object> item = new HashMap<String, Object>();
	public void getItem(){
		int length=discountFoodList.size();
		for (int i = 0; i <10; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			if(discountFragmentUpdateCounts < length){
				DiscountFood strFood=discountFoodList.get(discountFragmentUpdateCounts);
				String strImg=imagePath.get(discountFragmentUpdateCounts);
				item.put("storeName", strFood.getStoreName());
				item.put("discountText", strFood.getDiscountText());
				item.put("storeId", strFood.getStoreId());
				item.put("imagePath", strImg);
				discountFoodItems.add(item);
				discountFragmentUpdateCounts++;
			}else{
			}
		}
	}
	
}
