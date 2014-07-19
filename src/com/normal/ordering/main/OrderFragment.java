package com.normal.ordering.main;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import com.normal.ordering.R;
import com.normal.ordering.discountfragment.MyListView;
import com.normal.ordering.discountfragment.MyListView.OnRefreshListener;
import com.normal.ordering.entities.Store;
import com.normal.ordering.orderfragment.OrderFragmentAdapter;
import com.normal.ordering.tools.IsConnect;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
/**订餐--Fragment
 * @author Vaboon
 * @date 2014-6-2
 */
public class OrderFragment extends Fragment {
	
	private List<Store> storeList = new ArrayList<Store>();
	private TextView txtLocation;
	private ImageView imgRefresh;
	private MyListView listview;
	private static String getResult;
	private static ProgressDialog progressDialog;
	private List<String> imagePath = new ArrayList<String>();
	private static String storeActivity;
	private int merchantCounts;
	private OrderFragmentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_order, container, false);
       /* TextView text = (TextView) fragmentView.findViewById(android.R.id.text1);
        text.setText("Ordering");*/
        
        txtLocation=(TextView) fragmentView.findViewById(R.id.fragment_order_txt_location);
        imgRefresh=(ImageView) fragmentView.findViewById(R.id.fragment_order_img_refresh);
        imgRefresh.setImageResource(R.drawable.refresh_location);
        txtLocation.setText("当前位置");
        
        /*
         * 刷新当前位置
         */
        imgRefresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
        
        listview=(MyListView)fragmentView.findViewById(R.id.fragment_order_listview);
		// 准备要添加的数据条目
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 8; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("imageItem", R.drawable.refresh_image);// 添加图像资源的ID
			item.put("textItem", "icon" + i);// 按序号添加ItemText
			items.add(item);
		}

		// 实例化一个适配器
		final SimpleAdapter adapter = new SimpleAdapter(this.getActivity(), items,
				R.layout.fragment_order_listview_content, new String[] {
						"imageItem", "textItem" }, new int[] {
						R.id.orderActivity_item_img,
						R.id.orderActivity_item_name });
		// 为GridView设置适配器
		listview.setAdapter(adapter);
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
		 */
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg, int position,
					long duration) {
				String item = (String)adapter.getItem(position);
				Toast.makeText(getActivity(), position, Toast.LENGTH_SHORT).show();
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
					((OrderFragment) mActivity.get()).listview
							.onRefreshComplete();// 刷新完成
					((OrderFragment) mActivity.get()).listview
							.setSelection(0);
				}
				if (flag == 2) {
					Toast.makeText(mActivity.get().getActivity(), "数据获取失败",
							1000).show();
					((OrderFragment) mActivity.get()).listview
							.onRefreshComplete();// 刷新完成
				}
				if (flag == 0) {
					Toast.makeText(mActivity.get().getActivity(), "网络超时，请稍后再试",
							1000).show();
					((OrderFragment) mActivity.get()).listview
							.onRefreshComplete();// 刷新完成
				}
				if (flag == 3) {
					((OrderFragment) mActivity.get()).listview
							.onRefreshComplete();// 刷新完成
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

		merchantCounts = imagePath.size();
		adapter = new OrderFragmentAdapter(getActivity(),
				R.layout.fragment_order_listview_content,
				new ArrayList<Store>(storeList), imagePath);
		listview.setAdapter(adapter);
		storeList.clear();
		listview.setOnRefreshListener(new OnRefreshListener()
		// 刷新
		{
			@Override
			public void onRefresh() {
				getDiscountFoodList();
			}

		});
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
/*
 * 刷新地理位置
 */
	
}
