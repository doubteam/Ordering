package com.normal.ordering.userfragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

import com.normal.ordering.R;
import com.normal.ordering.tools.IsConnect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener {
	private EditText mRegisterEmail;
	private EditText mRegisterPassword;
	private EditText mRegisterName;
	private Button btnResgister;
	private ProgressBar proBarRegister;
	private static String getResults;
	private static ProgressDialog progressDialog;
	private static final String TAG = "MainActivity";

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

	private void uploadUser(String useremail, String userpassword,
			String username) throws Exception {
		URL url = null;
		HttpURLConnection urlConnection = null;
		// 对于URLConnection来说，向服务器发送数据是输出流
		OutputStream out = null;
		// 对于URLConnection来说，从服务器接手数据是输入流
		InputStream in = null;
		String results = null;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("loginName", useremail);
			params.put("&loginPassword", userpassword);
			params.put("&trueName", username);
			System.out.println(params);
			byte[] data = setPostPassParams(params).toString().getBytes();
			url = new URL("http://www.doubteam.com/Ordering/register.action");
			urlConnection = (HttpURLConnection) url.openConnection();
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
				// 是从服务器接收的数据
			}
			in = urlConnection.getInputStream();

			results = parseResponseResults(in);

			/**
			 * 解析服务器传回来的JSON数据 把它封装到List中
			 */
			JSONObject array = new JSONObject(results);
			getResults = array.getString("result");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initView();
		initData();

	}

	private void initView() {
		mRegisterEmail = (EditText) this
				.findViewById(R.id.mainactivity_edit_registeremail);
		mRegisterPassword = (EditText) this
				.findViewById(R.id.mainactivity_edit_registerpassword);
		mRegisterName = (EditText) this
				.findViewById(R.id.mainactivity_edit_registername);
		btnResgister = (Button) this
				.findViewById(R.id.mainactivity_btn_register);
		proBarRegister = (ProgressBar) this
				.findViewById(R.id.register_progress);
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("请等待");
		progressDialog.setMessage("正在注册中.......");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialgo是否能够被取消
		progressDialog.setCancelable(true);
		// 是否明确进度
		progressDialog.setIndeterminate(true);
	}

	private void initData() {
		btnResgister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 点击注册
		case R.id.mainactivity_btn_register:
			clickRegisterBtn();
			break;

		}
	}

	private void clickRegisterBtn() {
		if (IsConnect.isConnect(getApplicationContext())) { // 确定网络已经连接
			final String getResigsterEmail = mRegisterEmail.getText()
					.toString().trim();
			final String getRegisterPassword = mRegisterPassword.getText()
					.toString().trim();
			final String getResgisterUserName = mRegisterName.getText()
					.toString().trim();
			Log.d(TAG, "提交值：" + "email:" + getResigsterEmail + "password:"
					+ getRegisterPassword + "name:" + getResgisterUserName);
			if (getResigsterEmail.length() == 0
					|| getRegisterPassword.length() == 0
					|| getResgisterUserName.length() == 0) {
				Toast.makeText(getApplicationContext(), "亲，都必填哦^_^",
						Toast.LENGTH_LONG).show();
				return;

			}
			// 邮箱格式正确
			if (getResigsterEmail.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
				proBarRegister.setVisibility(0);// 可见进度条0 4 8
				btnResgister.setEnabled(false);// 按钮变灰
				try {
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								uploadUser(getResigsterEmail,
										getRegisterPassword,
										getResgisterUserName);
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
			} else {
				Toast.makeText(getApplicationContext(), "邮箱格式错误",
						Toast.LENGTH_LONG).show();
			}

		}
	}

	private MyHandler myHandler = new MyHandler(this);

	private static class MyHandler extends Handler {

		private final WeakReference<Activity> mActivity;

		public MyHandler(RegisterActivity activity) {
			mActivity = new WeakReference<Activity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			// 不可见进度条 GONE
			((RegisterActivity) mActivity.get()).proBarRegister
					.setVisibility(8);
			// 恢复按钮颜色
			((RegisterActivity) mActivity.get()).btnResgister.setEnabled(true);
			if (msg != null) {
				int flag = msg.what;

				if (progressDialog != null) {
					progressDialog.dismiss();
				}

				if (flag == 1) {
					Toast.makeText(mActivity.get().getApplicationContext(),
							getResults, Toast.LENGTH_LONG).show();
				}

			}
		}
	}

}
