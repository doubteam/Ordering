package com.normal.ordering.main;

import com.normal.ordering.R;
import com.normal.ordering.entities.User;
import com.normal.ordering.service.PushService;
import com.normal.ordering.tools.IApplication;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 该欢迎页每次进入APP都要启用，完成初始化、启动服务等功能，后续补全。
 * 
 * @author VBaboon
 * @date 2014-7-14
 */
public class Welcome extends Activity {
	private TextView textView;
	private final String TAG = "Welcome";

	private MyReceiver receiver;

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

	}

	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart()");
		// 启动服务
		Intent intent = new Intent(this, PushService.class);
		// 这里不再使用bindService,而使用startService
		startService(intent);
		Log.d(TAG, "启动服务");
	}

	private void initView() {
		textView = (TextView) findViewById(R.id.welcome_app_name);
	}

	private void initData() {

		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.Ordering.Broadcast");
		// 注册
		registerReceiver(receiver, filter);
		Log.d(TAG, "注册广播接受着");
	}

	/**
	 * 广播接收器
	 * 
	 * @author user
	 * 
	 */
	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			boolean loaded = bundle.getBoolean("loaded");
			if (loaded) {// 加载完
				intent.setClass(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				finish();//防止返回到这页
			}

		}
	}

	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		// 不要忘了这一步
		unregisterReceiver(receiver);
		Log.d(TAG, "注销广播接受者");
	}
}
