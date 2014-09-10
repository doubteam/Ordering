package com.normal.ordering.main;

import com.normal.ordering.R;
import com.normal.ordering.service.PushService;
import com.normal.ordering.tools.IApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * 主界面 兼容SmartBar 使用了ActionBar
 * 
 * @author Vaboon
 * @date 2014-6-2
 */
public class MainActivity extends Activity implements OnClickListener {
	private DiscountFragment discountFragment;
	private OrderFragment orderFragment;
	private UserFragment userFragment;
	private MoreFragment moreFragment;
	// 定义布局对象
	private FrameLayout discountFrameLayout, orderFrameLayout, userFrameLayout,
			moreFrameLayout;
	private ImageView userImageView, moreImageView;
	private static final String TAG = "MainActivity";
	private ProgressDialog progressDialog;
	private Dialog alertDialog;
	public static boolean noImg=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		setContentView(R.layout.activity_main);
		IApplication.getInstance().addActivity(this);
		initView();
		initData();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			// 获取需要到哪个fragment
			String gotoString = bundle.getString("gotoString");
			// 确认是从登陆界面登陆成功跳转过来的
			if (gotoString != null) {
				if (gotoString.equals("UserFragment")) {
					clickBottomTabUserBtn();

				}
			}else {
				// 初始化默认为选中点击了“打折”按钮
				clickBottomTabDiscountBtn();
			}
		} else {
			// 初始化默认为选中点击了“打折”按钮
			clickBottomTabDiscountBtn();

		}
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		// 实例化布局对象
		discountFrameLayout = (FrameLayout) findViewById(R.id.bottom_tab_discount);
		orderFrameLayout = (FrameLayout) findViewById(R.id.bottom_tab_order);
		userFrameLayout = (FrameLayout) findViewById(R.id.bottom_tab_user);
		moreFrameLayout = (FrameLayout) findViewById(R.id.bottom_tab_more);

		// 实例化图片组件对象
		userImageView = (ImageView) findViewById(R.id.bottom_tab_user_image_view);
		moreImageView = (ImageView) findViewById(R.id.bottom_tab_more_image_view);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// 给布局对象设置监听
		discountFrameLayout.setOnClickListener(this);
		orderFrameLayout.setOnClickListener(this);
		userFrameLayout.setOnClickListener(this);
		moreFrameLayout.setOnClickListener(this);
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("请等待");
		progressDialog.setMessage("数据加载中.......");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialgo是否能够被取消
		progressDialog.setCancelable(false);
		// 明确进度
		progressDialog.setIndeterminate(true);
		alertDialog = new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("确认要关闭吗？")
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						IApplication.getInstance().exitApp();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).create();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 点击打折按钮
		case R.id.bottom_tab_discount:
			Log.d(TAG, "点击了打折按钮");
			clickBottomTabDiscountBtn();
			break;
		// 点击订餐按钮
		case R.id.bottom_tab_order:
			Log.d(TAG, "点击了订餐按钮");
			clickBottomTabOrderBtn();
			break;
		// 点击个人按钮
		case R.id.bottom_tab_user:
			Log.d(TAG, "点击了个人按钮");
			clickBottomTabUserBtn();
			break;
		// 点击更多按钮
		case R.id.bottom_tab_more:
			Log.d(TAG, "点击了更多按钮");
			clickBottomTabMoreBtn();
			break;

		}
	}

	/**
	 * 点击了“打折”按钮
	 */
	private void clickBottomTabDiscountBtn() {
		// 实例化Fragment页面
		discountFragment = new DiscountFragment();
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = this.getFragmentManager()
				.beginTransaction();
		// 替换当前的页面
		fragmentTransaction.replace(R.id.frame_content, discountFragment);
		// 事务管理提交
		fragmentTransaction.commit();
		discountFrameLayout.setSelected(true);
		orderFrameLayout.setSelected(false);
		userFrameLayout.setSelected(false);
		userImageView.setSelected(false);
		moreFrameLayout.setSelected(false);
		moreImageView.setSelected(false);
	}

	/**
	 * 点击了“订餐”按钮
	 */
	private void clickBottomTabOrderBtn() {
		// 实例化Fragment页面
		orderFragment = new OrderFragment();
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = this.getFragmentManager()
				.beginTransaction();
		// 替换当前的页面
		fragmentTransaction.replace(R.id.frame_content, orderFragment);
		// 事务管理提交
		fragmentTransaction.commit();

		discountFrameLayout.setSelected(false);

		orderFrameLayout.setSelected(true);

		userFrameLayout.setSelected(false);
		userImageView.setSelected(false);

		moreFrameLayout.setSelected(false);
		moreImageView.setSelected(false);
	}

	/**
	 * 点击了“个人”按钮
	 */
	private void clickBottomTabUserBtn() {
		// 实例化Fragment页面
		userFragment = new UserFragment();
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = this.getFragmentManager()
				.beginTransaction();
		// 替换当前的页面
		fragmentTransaction.replace(R.id.frame_content, userFragment);
		// 事务管理提交
		fragmentTransaction.commit();

		discountFrameLayout.setSelected(false);

		orderFrameLayout.setSelected(false);

		userFrameLayout.setSelected(true);
		userImageView.setSelected(true);

		moreFrameLayout.setSelected(false);
		moreImageView.setSelected(false);
	}

	/**
	 * 点击了“更多”按钮
	 */
	private void clickBottomTabMoreBtn() {
		// 实例化Fragment页面
		moreFragment = new MoreFragment();
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = this.getFragmentManager()
				.beginTransaction();
		// 替换当前的页面
		fragmentTransaction.replace(R.id.frame_content, moreFragment);
		// 事务管理提交
		fragmentTransaction.commit();

		discountFrameLayout.setSelected(false);

		orderFrameLayout.setSelected(false);

		userFrameLayout.setSelected(false);
		userImageView.setSelected(false);

		moreFrameLayout.setSelected(true);
		moreImageView.setSelected(true);
	}

	// /**
	// * 自动登陆
	// */
	// private void userLogin() {
	//
	// /**
	// * 隐式的启动Service 如果在同一个包中。两者都可以用。在不同包时。只能用隐式启动
	// *
	// * Intent intent = new Intent(PushService.ACTION);
	// */
	// Intent intent = new Intent(this, PushService.class);
	// this.bindService(intent, conn, Context.BIND_AUTO_CREATE);
	// Log.d(TAG, "绑定服务");
	//
	// }

	// /**
	// * 服务链接
	// */
	// private ServiceConnection conn = new ServiceConnection() {
	// // 链接中执行的操作
	//
	// // 链接成功执行的操作
	// @Override
	// public void onServiceConnected(ComponentName name, IBinder service) {
	// Log.d(TAG, "onServiceConnected");
	// pushService = ((PushService.ServiceBinder) service).getService();
	// // progressDialog.show();
	// pushService.onLogin(sp.getString(SP_LOGIN_NAME, null),
	// sp.getString(SP_LOGIN_PASSWORD, null));
	// }
	//
	// @Override
	// public void onServiceDisconnected(ComponentName name) {
	// Log.d(TAG, "onServiceDisconnected");
	// pushService = null;
	// }
	// };

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 捕捉返回键
			Log.d(TAG, "返回键");
			alertDialog.show();
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// this.unbindService(conn);
		// 关闭服务
		Intent intent = new Intent(this, PushService.class);
		this.stopService(intent);
		Log.v("Services", "out");
	}
}
