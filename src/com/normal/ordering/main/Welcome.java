package com.normal.ordering.main;

import java.lang.ref.WeakReference;

import com.normal.ordering.R;
import com.normal.ordering.entities.User;
import com.normal.ordering.tools.IApplication;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					Thread.sleep(5000);
					getResults="success";
					myHandler.sendEmptyMessage(1);
				} catch (Exception e) {
					myHandler.sendEmptyMessage(0);
					e.printStackTrace();
				}

			}
		}).start();
	}

	private void initView() {
		textView=(TextView) findViewById(R.id.welcome_app_name);
	}

	private void initData() {

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
						//防止返回到这个页面
						mActivity.get().finish();
						

					}
				}
			}
		}
	}
}
