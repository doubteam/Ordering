package com.normal.ordering.main;

import java.lang.ref.WeakReference;

import com.normal.ordering.R;
import com.normal.ordering.entities.User;
import com.normal.ordering.service.PushService;
import com.normal.ordering.tools.IApplication;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 该欢迎页每次进入APP都要启用，完成初始化、启动服务等功能，后续补全。
 * 
 * @author VBaboon
 * @date 2014-7-14
 */
public class Welcome extends Activity {
	private static String getResults;
	private User user;
	private TextView textView;
	private final String TAG = "Welcome";
	private PushService pushService;
	private SharedPreferences sp;
	private static final String SP_NAME = "ORDERING";
	private static final String SP_LOGIN_NAME = "ORDERING_LOGIN_NAME";
	private static final String SP_LOGIN_PASSWORD = "ORDERING_LOGIN_PASSWORD";
	private static final String SP_SUCCESS_LOGIN = "ORDERING_SUCCESS_LOGIN";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 不显示程序的标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 不显示系统的标题栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welcome);
		initView();
		initData();
		// 未登陆但是SharedPreferences有正确的帐号和密码
		if (IApplication.getInstance().getUser() == null
				&& sp.getBoolean(SP_SUCCESS_LOGIN, false)) {
			userLogin();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					Thread.sleep(5000);
					getResults = "success";
					myHandler.sendEmptyMessage(1);
				} catch (Exception e) {
					myHandler.sendEmptyMessage(0);
					e.printStackTrace();
				}

			}
		}).start();
	}

	private void initView() {
		textView = (TextView) findViewById(R.id.welcome_app_name);
	}

	private void initData() {
		// 初始化sp，并划定你的存储|取值的区域
		this.sp = this.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
	}

	private MyHandler myHandler = new MyHandler(this);

	private static class MyHandler extends Handler {

		private final WeakReference<Activity> mActivity;

		public MyHandler(Welcome welcome) {
			mActivity = new WeakReference<Activity>(welcome);
		}

		@Override
		public void handleMessage(Message msg) {
			if (msg != null) {
				int flag = msg.what;
				if (flag == 1) {
					if (getResults.equals("failed")) {
					} else {
						// 把用户信息写入application
						// IApplication.getInstance().setUser(user);
						Intent intent = new Intent(mActivity.get(),
								MainActivity.class);
						mActivity.get().startActivity(intent);
						// 防止返回到这个页面
						mActivity.get().finish();

					}
				}
			}
		}
	}

	/**
	 * 自动登陆
	 */
	private void userLogin() {

		/**
		 * 隐式的启动Service 如果在同一个包中。两者都可以用。在不同包时。只能用隐式启动
		 * 
		 * Intent intent = new Intent(PushService.ACTION);
		 */
		Intent intent = new Intent(this, PushService.class);
		this.bindService(intent, conn, Context.BIND_AUTO_CREATE);
		Log.d(TAG, "绑定服务");

	}

	/**
	 * 服务链接
	 */
	private ServiceConnection conn = new ServiceConnection() {
		// 链接中执行的操作

		// 链接成功执行的操作
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d(TAG, "onServiceConnected");
			pushService = ((PushService.ServiceBinder) service).getService();
			// progressDialog.show();
			pushService.onLogin(sp.getString(SP_LOGIN_NAME, null),
					sp.getString(SP_LOGIN_PASSWORD, null));
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d(TAG, "onServiceDisconnected");
			pushService = null;
		}
	};
}
