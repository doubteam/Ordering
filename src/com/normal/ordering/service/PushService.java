package com.normal.ordering.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.normal.ordering.entities.User;
import com.normal.ordering.tools.IApplication;
import com.normal.ordering.tools.IsConnect;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;

import android.os.IBinder;

import android.util.Log;

public class PushService extends Service {
	private static final String TAG = "PushService";

	public static final String ACTION = "com.normal.ordering.PUSH_SERVICE";
	private SharedPreferences sp;
	private static String getResults;
	private static User user;
	private static final String SP_NAME = "ORDERING";
	private static final String SP_LOGIN_NAME = "ORDERING_LOGIN_NAME";
	private static final String SP_LOGIN_PASSWORD = "ORDERING_LOGIN_PASSWORD";
	private static final String SP_SUCCESS_LOGIN = "ORDERING_SUCCESS_LOGIN";
	private ServiceBinder serviceBinder = new ServiceBinder();

	/**
	 * 绑定方法，结构必须保留 意思：将一个Service是生命周期和其他组件关联
	 * 
	 */
	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onbind");

		return serviceBinder;
	}

	/**
	 * 解绑
	 */
	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(TAG, "onUnbind");
		return super.onUnbind(intent);
	}

	/**
	 * 创建
	 */
	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		// 初始化sp，并划定你的存储|取值的区域
		this.sp = this.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
	}

	/**
	 * 销毁
	 */
	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
	}

	public void onLogin(final String loginName, final String loginPassword) {
		if (IsConnect.isConnect(this)) {// 保证联网状态
			Log.d(TAG, "onLogin:" + loginName + "    " + loginPassword);
			try {
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							user = uploadUser(loginName, loginPassword);
							Thread.sleep(1000);
							myHandler.sendEmptyMessage(1);
						} catch (Exception e) {
							myHandler.sendEmptyMessage(0);
							e.printStackTrace();
						}
					}
				}).start();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 启动（重启） 在Android v2.0后出现，代替了以前的onStart()方法。
	 * 它提供和onStart()一样的功能，但允许编程者告诉系统，如果在显示的调用stopService()或stopSelf()之前终止了服务，
	 * 那么该如何重启Service，它的返回值决定了这种方式：
	 * 
	 * START_STICKY：如果service进程被kill掉，保留service的状态为开始状态
	 * ，但不保留递送的intent对象。随后系统会尝试重新创建service
	 * ，由于服务状态为开始状态，所以创建服务后一定会调用onStartCommand
	 * (Intent,int,int)方法。如果在此期间没有任何启动命令被传递到service，那么参数Intent将为null。
	 * 
	 * START_NOT_STICKY：“非粘性的”。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，
	 * 系统不会自动重启该服务。
	 * 
	 * START_REDELIVER_INTENT：重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉
	 * ，系统会自动重启该服务，并将Intent的值传入。
	 * 
	 * START_STICKY_COMPATIBILITY：START_STICKY的兼容版本，但不保证服务被kill后一定能重启。
	 * 
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");
		Log.d(TAG, "flags:" + flags + "");
		Log.d(TAG, "startId:" + startId + "");
		// SharedPreferences有正确的帐号和密码
		if (sp.getBoolean(SP_SUCCESS_LOGIN, false)) {
			onLogin(sp.getString(SP_LOGIN_NAME, null),
					sp.getString(SP_LOGIN_PASSWORD, null));
		}
		return Service.START_STICKY;
	}

	// 此方法是为了可以在Acitity中获得服务的实例
	public class ServiceBinder extends Binder {
		public PushService getService() {
			Log.d(TAG, "getService()");
			return PushService.this;
		}
	}

	/**
	 * 发送POST请求
	 * 
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static StringBuffer setPostPassParams(Map<String, String> params)
			throws UnsupportedEncodingException {
		StringBuffer stringBuffer = new StringBuffer(); // 存储封装好的请求体信息
		try {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				stringBuffer.append(entry.getKey()).append("=")
						.append(URLEncoder.encode(entry.getValue(), "UTF-8"))
						.append("&");
			}
			stringBuffer.deleteCharAt(stringBuffer.length() - 1); // 删除最后的一个"&"
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer;
	}

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

	private User uploadUser(String useremail, String userpassword)
			throws Exception {
		URL url = null;
		HttpURLConnection urlConnection = null;
		OutputStream out = null;
		// 对于URLConnection来说，从服务器接手数据是输入流
		InputStream in = null;
		String results = null;
		User user = null;
		Log.d(TAG, "正在登陆");
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("loginName", useremail);
			params.put("&loginPassword", userpassword);
			byte[] data = setPostPassParams(params).toString().getBytes();
			url = new URL("http://www.doubteam.com/Ordering/login.action");
			urlConnection = (HttpURLConnection) url.openConnection();
			// 请求连接超时
			urlConnection.setConnectTimeout(20 * 1000);
			// 响应超时
			urlConnection.setReadTimeout(20 * 1000);
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
			}
			in = urlConnection.getInputStream();

			results = parseResponseResults(in);

			/**
			 * 解析服务器传回来的JSON数据
			 */
			JSONObject array = new JSONObject(results);
			getResults = array.getString("result");
			JSONObject userInformation = array.getJSONObject("user");
			user = new User();
			user.setUserEmail(userInformation.getString("loginName"));
			user.setUserName(userInformation.getString("trueName"));
			user.setVip(userInformation.getString("vip"));
			user.setIntegration(userInformation.getString("integration"));
			user.setUserPassword(userInformation.getString("loginPassword"));

		} catch (Exception e) {

			// TODO Auto-generated catch block
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
		return user;
	}

	private MyHandler myHandler = new MyHandler(this);

	private static class MyHandler extends Handler {

		private final WeakReference<PushService> mActivity;

		public MyHandler(PushService pushService) {
			mActivity = new WeakReference<PushService>(pushService);
		}

		public void handleMessage(Message msg) {
			if (msg != null) {
				int flag = msg.what;
				if (flag == 1) {// 登陆功能
					if (getResults.equals("failed")) {// 登陆失败
						Log.d(TAG, "登陆失败");
					} else {// 登陆成功后的代码
						// 把用户信息写入application
						IApplication.getInstance().setUser(user);
						Log.d(TAG, "登陆成功");
						// 发送特定action的广播
						Intent intent = new Intent();
						intent.setAction("android.intent.action.Ordering.Broadcast");
						intent.putExtra("loaded", true);
						mActivity.get().sendBroadcast(intent);

					}
				} else {// 服务器链接失败等原因

				}
			}
		}
	}
}