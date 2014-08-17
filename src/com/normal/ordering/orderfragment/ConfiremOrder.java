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
import com.normal.ordering.R;
import com.normal.ordering.entities.TheDishes;
import com.normal.ordering.entities.User;
import com.normal.ordering.main.MainActivity;
import com.normal.ordering.main.UserFragment;
import com.normal.ordering.tools.IApplication;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
	private ArrayList<Map<String, Object>> foodList = new ArrayList<Map<String, Object>>();
	private String userName;
	private User user;
	public static ArrayList<Map<String, Object>> myOrderList = new ArrayList<Map<String, Object>>();
	
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
		this.btnConfiremOrder=(Button) this.findViewById(R.id.activity_confiremorder_btn_confiremorder);
		btnConfiremOrder.setOnClickListener(new confiremOrder());
		user=IApplication.getInstance().getUser();
	}
	public class confiremOrder implements OnClickListener{

		@SuppressWarnings("unchecked")
		@Override
		public void onClick(View v) {
			if(IApplication.getInstance().getUser()!=null){
			user=IApplication.getInstance().getUser();
			if(myOrderList.size()!=0){
				myOrderList.clear();
			}
			myOrderList=(ArrayList<Map<String, Object>>) adapter.getItems().clone();
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
	public void getMyOrder(ArrayList<Map<String, Object>> foodList ){
		for(int i=0;i<foodList.size();i++){
			TheDishes myOrder;
		}
	}
}
