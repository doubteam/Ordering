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

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.normal.ordering.R;
import com.normal.ordering.entities.Store;
import com.normal.ordering.orderfragment.ConfiremOrder;
import com.normal.ordering.orderfragment.OrderFragmentAdapter;
import com.normal.ordering.tools.IsConnect;
import com.normal.ordering.tools.IApplication;
import com.normal.ordering.tools.IApplication.MyLocationListener;
import com.normal.ordering.tools.LocationLoading;
import com.normal.ordering.tools.MyListView;
import com.normal.ordering.tools.MyListView.MyListViewListener;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 订餐--Fragment
 * 
 * @author Vaboon
 * @date 2014-6-2
 */
public class OrderFragment extends Fragment implements MyListViewListener{


	private LocationLoading locationLoding;
	private List<Store> storeList = new ArrayList<Store>();
	private TextView txtLocation;
	private Button imgRefresh;
	private MyListView listview;
	private static String getResult;
	private static ProgressDialog progressDialog;
	private List<String> imagePath = new ArrayList<String>();
	private static String storeActivity;
	private int storeCounts;
	private OrderFragmentAdapter adapter;
	private SimpleAdapter adapters;
	
	private ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
	private static int updateCounts1=0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.fragment_order,
				container, false);
		/*
		 * TextView text = (TextView)
		 * fragmentView.findViewById(android.R.id.text1);
		 * text.setText("Ordering");
		 */

		InitLocation();
		txtLocation = (TextView) fragmentView
				.findViewById(R.id.fragment_order_txt_location);
		imgRefresh = (Button) fragmentView
				.findViewById(R.id.fragment_order_btn_refreshlocation);
		((IApplication) getActivity().getApplication()).mLocationResult=txtLocation;
		//停止定位
		locationLoding.mLocationClient.stop();
		/*
		 * 刷新当前位置
		 */
		imgRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InitLocation();
				((IApplication) getActivity().getApplication()).mLocationResult=txtLocation;
				locationLoding.mLocationClient.stop();
			}
		});

		listview = (MyListView) fragmentView
				.findViewById(R.id.fragment_order_listview);
		// 准备要添加的数据条目
		/*List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 8; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("imageItem", R.drawable.refresh_image);// 添加图像资源的ID
			item.put("textItem", "icon" + i);// 按序号添加ItemText
			items.add(item);
		}

		// 实例化一个适配器
		final SimpleAdapter adapter = new SimpleAdapter(this.getActivity(),
				items, R.layout.fragment_order_listview_content, new String[] {
						"imageItem", "textItem" }, new int[] {
						R.id.orderActivity_item_img,
						R.id.orderActivity_item_name });*/
		/*
		 * adapter=new OrderFragmentAdapter(this.getActivity(),
		 * R.layout.fragment_order_listview_content, storeList, imagePath);
		 */

		getItem();
		listview.setPullLoadEnable(true);
		adapters=new SimpleAdapter(this.getActivity(),items,
				R.layout.fragment_discount_gridview_content,new String[]{
			"imageItem","textItem"},new int[]{
			R.id.fragment_discount_gridview_img,R.id.fragment_discount_gridview_text
		});
		// 为GridView设置适配器
		listview.setAdapter(adapters);
		listview.setMyListViewListener(this);
		
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setTitle("请等待");
		progressDialog.setMessage("数据加载中.......");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialgo是否能够被取消
		progressDialog.setCancelable(false);
		// 明确进度
		progressDialog.setIndeterminate(true);
		if (IsConnect.isConnect(getActivity())) {
			progressDialog.show();
			getDiscountFoodList();
		}
		/*
		 * 点击事件还有问题
		 * 当你有内容后，就不会报错了。。。如果需要判断，需要重载方法。。。
		 */
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg,
					int position, long id) {
				// String item =(String) adapter.getItem(position);
				Intent intent=new Intent();
				intent.setClass(getActivity(), ConfiremOrder.class);
				startActivity(intent);
				
			}
		});

		return fragmentView;
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
			txtLocation.setText("无法获取到当前位置");
			myHandler.sendEmptyMessage(3);
		}
	}

	private MyHandler myHandler = new MyHandler(this);

	private static class MyHandler extends Handler {

		private final WeakReference<Fragment> mActivity;

		public MyHandler(OrderFragment orderFragment) {
			mActivity = new WeakReference<Fragment>(orderFragment);
		}

		@Override
		public void handleMessage(Message msg) {
			if (msg != null) {
				int flag = msg.what;

				if (progressDialog != null) {
					progressDialog.dismiss();
				}

				if (flag == 1) {
					((OrderFragment) mActivity.get()).initList();// 初始化数据
					((OrderFragment) mActivity.get()).onLoad();
				//	((OrderFragment) mActivity.get()).listview
				//			.onRefreshComplete();// 刷新完成
					((OrderFragment) mActivity.get()).listview.setSelection(0);
				}
				if (flag == 2) {
					Toast.makeText(mActivity.get().getActivity(), "数据获取失败",
							1000).show();
					((OrderFragment) mActivity.get()).onLoad();
				//	((OrderFragment) mActivity.get()).listview
				//			.onRefreshComplete();// 刷新完成
				}
				if (flag == 0) {
					Toast.makeText(mActivity.get().getActivity(), "网络超时，请稍后再试",
							1000).show();
					((OrderFragment) mActivity.get()).onLoad();
				//	((OrderFragment) mActivity.get()).listview
				//			.onRefreshComplete();// 刷新完成
				}
				if (flag == 3) {
					((OrderFragment) mActivity.get()).onLoad();
				//	((OrderFragment) mActivity.get()).listview
				//			.onRefreshComplete();// 刷新完成
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
					"http://www.doubteam.com/Ordering/BusinessActivity.action");
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
					storeActivity = result.getString("businessactivity");
					JSONArray businessList = new JSONArray(storeActivity);

					for (int i = 0; i < businessList.length(); i++) {
						// 获取商店名称
						String merchantName = businessList.getJSONObject(i)
								.getString("businessName");
						// 获取促销标题
						String merchantAddress = businessList.getJSONObject(i)
								.getString("title");
						Store merchant = new Store(merchantName,
								merchantAddress);
						storeList.add(merchant);
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

		storeCounts = imagePath.size();
		adapter = new OrderFragmentAdapter(getActivity(),
				R.layout.fragment_order_listview_content, new ArrayList<Store>(
						storeList), imagePath);
		listview.setAdapter(adapter);
		storeList.clear();
		onLoad();
		/*listview.setOnRefreshListener(new OnRefreshListener()
		// 刷新
		{
			@Override
			public void onRefresh() {
				getDiscountFoodList();
			}

		});*/
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main, menu);
		// menu.add("Menu 1a").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		// menu.add("Menu 1b").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Toast.makeText(getActivity(), "menu text is " + item.getTitle(), 1000)
				.show();
		return super.onOptionsItemSelected(item);
	}

	// 获取地理位置
	private void InitLocation() {
		locationLoding=new LocationLoading();
		locationLoding.mLocationClient=((IApplication)getActivity().getApplication()).mLocationClient;
		locationLoding.getLocation();
		locationLoding.mLocationClient.start();
	}
	@Override
	public void onRefresh() {
		myHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				items.clear();
				updateCounts1=0;
				getItem();
				adapters=new SimpleAdapter(getActivity(),items,
						R.layout.fragment_discount_gridview_content,new String[]{
					"imageItem","textItem"},new int[]{
					R.id.fragment_discount_gridview_img,R.id.fragment_discount_gridview_text
				});
				listview.setAdapter(adapters);
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
//				adapters.notifyDataSetChanged();
				onLoad();
				
			}
		}, 1000);
				
	}
	private void onLoad() {
		listview.stopRefresh();
		listview.stopLoadMore();
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		String date = format.format(new Date());
		listview.setRefreshTime(date);
	}
	
	public void getItem(){
		for (int i = 0; i < 15; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("imageItem", R.drawable.refresh_image);// 添加图像资源的ID
			item.put("textItem", "icon" + updateCounts1);// 按序号添加ItemText
			items.add(item);
			updateCounts1++;
		}
	}

}
