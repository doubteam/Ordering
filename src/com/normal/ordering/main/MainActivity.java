package com.normal.ordering.main;

import com.normal.ordering.R;
import com.normal.ordering.entities.User;
import com.normal.ordering.tools.SmartBarUtils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 主界面 兼容SmartBar 使用了ActionBar
 * 
 * @author Vaboon
 * @date 2014-6-2
 */
public class MainActivity extends FragmentActivity implements OnClickListener {
	private DiscountFragment discountFragment;
	private OrderFragment orderFragment;
	private UserFragment userFragment;
	private MoreFragment moreFragment;
	public User user;
	// 定义布局对象
	private FrameLayout discountFrameLayout, orderFrameLayout, userFrameLayout,
			moreFrameLayout;
	private ImageView userImageView, moreImageView;
	private TextView discounTextView;
	private int lastOnclick;// 上次点击的按钮ID

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initData();
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
		// 获取需要到哪个fragment
		String gotoString = bundle.getString("gotoString");
		user = (User) bundle.getSerializable("user");
		// 确认是从登陆界面登陆成功跳转过来的
		if (gotoString != null && user != null) {
			if(gotoString.equals("UserFragment")){
				clickBottomTabUserBtn();
				lastOnclick = R.id.bottom_tab_user;
			}
		}} else {
			// 初始化默认为选中点击了“打折”按钮
			clickBottomTabDiscountBtn();
			lastOnclick = R.id.bottom_tab_discount;
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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 点击打折按钮
		case R.id.bottom_tab_discount:
			clickBottomTabDiscountBtn();
			break;
		// 点击订餐按钮
		case R.id.bottom_tab_order:
			clickBottomTabOrderBtn();
			break;
		// 点击个人按钮
		case R.id.bottom_tab_user:
			clickBottomTabUserBtn();
			break;
		// 点击更多按钮
		case R.id.bottom_tab_more:
			clickBottomTabMoreBtn();
			break;

		}
		lastOnclick = v.getId();// 记录点击过后的ID
	}

	/**
	 * 点击了“打折”按钮
	 */
	private void clickBottomTabDiscountBtn() {
		// 实例化Fragment页面
		discountFragment = new DiscountFragment();
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
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
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
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
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
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
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
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

	/**
	 * 改变显示的按钮图片为正常状态
	 */
	private void changeButtonImage() {
		// plusImageView.setImageResource(R.drawable.toolbar_plus);
		// toggleImageView.setImageResource(R.drawable.toolbar_btn_normal);
	}
}
