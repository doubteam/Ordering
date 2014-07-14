package com.normal.ordering.userfragment;

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
import com.normal.ordering.R;
import com.normal.ordering.entities.User;
import com.normal.ordering.main.MainActivity;
import com.normal.ordering.tools.IApplication;
import com.normal.ordering.tools.IsConnect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

	private EditText mLoginEmail;
	private EditText mLoginPassword;
	private Button btnLogin;
	private ProgressBar proBarLogin;
	private TextView textRegister;
	private static ProgressDialog progressDialog;
	private static User user;
	private static String getResults;
	// 相当于JSP里面的Cookie
	private SharedPreferences sp;
	private static final String SP_NAME = "ORDERING";
	private static final String SP_LOGIN_NAME = "ORDERING_LOGIN_NAME";
	private static final String SP_LOGIN_PASSWORD = "ORDERING_LOGIN_PASSWORD";
	private static final String SP_SUCCESS_LOGIN = "ORDERING_SUCCESS_LOGIN";
	Editor editor = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		initData();
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

	/**
	 * 初始化组件
	 */
	private void initView() {
		mLoginEmail = (EditText) this
				.findViewById(R.id.loginActivity_edt_loginemail);
		mLoginPassword = (EditText) this
				.findViewById(R.id.loginActivity_edt_loginpassword);
		btnLogin = (Button) this.findViewById(R.id.loginActivity_btn_login);
		proBarLogin = (ProgressBar) this.findViewById(R.id.login_progress);
		textRegister = (TextView) this
				.findViewById(R.id.loginActivity_text_register);
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("请等待");
		progressDialog.setMessage("正在登陆中.......");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialgo是否能够被取消
		progressDialog.setCancelable(true);
		// 是否明确进度
		progressDialog.setIndeterminate(true);
		// 初始化sp，并划定你的存储|取值的区域
		this.sp = this.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		this.initState();

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		editor = sp.edit();
		btnLogin.setOnClickListener(this);
		textRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 点击登陆按钮
		case R.id.loginActivity_btn_login:
			clickLoginBtn();
			break;
		// 点击注册
		case R.id.loginActivity_text_register:
			clickRegisterText();
			break;

		}
	}

	/**
	 * cookies 获取用户帐号，密码
	 */
	private void initState() {
		String defaultLoginName = this.sp.getString(SP_LOGIN_NAME, null);
		String defaultLoginPassword = this.sp
				.getString(SP_LOGIN_PASSWORD, null);
		// boolean isDefaultSuccessLogin = this.sp.getBoolean(SP_SUCCESS_LOGIN,
		// false);
		mLoginEmail.setText(defaultLoginName);
		mLoginPassword.setText(defaultLoginPassword);

	}

	private void clickLoginBtn() {
		if (IsConnect.isConnect(this)) { // 确定网络已经连接
			final String getLoginEmail = mLoginEmail.getText().toString();
			final String getLoginPassword = mLoginPassword.getText().toString();
			if (getLoginEmail.length() > 0 && getLoginPassword.length() > 0) {

				try {
					// progressDialog.show();
					proBarLogin.setVisibility(0);// 可见进度条0 4 8
					btnLogin.setEnabled(false);// 按钮变灰
					// 记住帐号，密码
					editor.putString(SP_LOGIN_NAME, getLoginEmail);
					// 安全性，密码要加密变成密文放入sp
					editor.putString(SP_LOGIN_PASSWORD, getLoginPassword);
					new Thread(new Runnable() {

						@Override
						public void run() {

							try {
								user = uploadUser(getLoginEmail,
										getLoginPassword);
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
				Toast.makeText(getApplicationContext(), "亲，都必填哦^_^",
						Toast.LENGTH_LONG).show();
			}

		}
	}

	private void clickRegisterText() {
		Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
		startActivity(intent);
	}

	private MyHandler myHandler = new MyHandler(this);

	private static class MyHandler extends Handler {

		private final WeakReference<Activity> mActivity;

		public MyHandler(LoginActivity loginActivity) {
			mActivity = new WeakReference<Activity>(loginActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			// 不可见进度条 GONE
			((LoginActivity) mActivity.get()).proBarLogin.setVisibility(8);
			// 恢复按钮颜色
			((LoginActivity) mActivity.get()).btnLogin.setEnabled(true);
			if (msg != null) {
				int flag = msg.what;

				if (progressDialog != null) {
					progressDialog.dismiss();// 关闭
				}

				if (flag == 1) {
					if (getResults.equals("failed")) {
						((LoginActivity) mActivity.get()).editor.putBoolean(
								SP_SUCCESS_LOGIN, false);
						((LoginActivity) mActivity.get()).editor.commit();
						Toast.makeText(mActivity.get().getApplicationContext(),
								"帐号或密码错误！", Toast.LENGTH_LONG).show();
					} else {// 登陆成功后的代码
						((LoginActivity) mActivity.get()).editor.putBoolean(
								SP_SUCCESS_LOGIN, true);
						((LoginActivity) mActivity.get()).editor.commit();
						Toast.makeText(mActivity.get().getApplicationContext(),
								"登陆成功", Toast.LENGTH_LONG).show();
						// 把用户信息写入application
						IApplication.getInstance().setUser(user);
						Intent intent = new Intent(mActivity.get(),
								MainActivity.class);
						Bundle bundle = new Bundle();
						// 告诉主Actcivity 启动哪个Fragment
						bundle.putString("gotoString", "UserFragment");
						intent.putExtras(bundle);
						mActivity.get().startActivity(intent);
					}
				}
			}
		}
	}
}
