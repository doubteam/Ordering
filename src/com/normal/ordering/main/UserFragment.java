package com.normal.ordering.main;

import com.normal.order.userfragment.MyOrder;
import com.normal.ordering.R;
import com.normal.ordering.entities.User;
import com.normal.ordering.tools.IApplication;
import com.normal.ordering.userfragment.LoginActivity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 个人中心--Fragment
 * 
 * @author Vaboon
 * @date 2014-6-2
 */
public class UserFragment extends Fragment implements OnClickListener {
	private Button btnLogin;
	private Button btnMyOrdering;
	private ImageView imgView;
	private TextView txtLoginName;
	private TextView txtIntegral;
	private static User user;
	private static final String TAG = "UserFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragmentView;
		if (IApplication.getInstance().getUser() != null) {
			fragmentView = inflater.inflate(R.layout.fragment_user_logined,
					container, false);
		} else {
			fragmentView = inflater.inflate(R.layout.fragment_user, container,
					false);
		}
		return fragmentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		/*
		 * 当user中有用户信息时(已经登陆)
		 */
		if (IApplication.getInstance().getUser() != null) {
			User user = IApplication.getInstance().getUser();
			initView(true);
			initData(user);
			/**
			 * 未登陆，但是SharedPreferences有正确的帐号密码。
			 */
		}

		else {
			/*
			 * user中没有用户信息时
			 */
			initView(false);
			initData(null);
		}

	}

	/**
	 * 初始化组件
	 */
	private void initView(boolean IsLogined) {

		if (IsLogined) {// 如果已经登陆
			imgView = (ImageView) getActivity().findViewById(
					R.id.fragment_user_logined_img_loginimgview);
			txtLoginName = (TextView) getActivity().findViewById(
					R.id.fragment_user_logined_text_loginname);
			txtIntegral = (TextView) getActivity().findViewById(
					R.id.fragment_user_logined_text_integral);
			btnMyOrdering = (Button) getActivity().findViewById(
					R.id.fragment_user_logined_btn_myordering);
		} else {
			btnLogin = (Button) getActivity().findViewById(
					R.id.frament_user_btn_login);

		}
	}

	/**
	 * 初始化数据
	 */
	private void initData(User user) {
		if (user != null) {// 已经登陆
			imgView.setImageResource(R.drawable.ic_launcher);
			txtLoginName.setText(user.getUserName());
			txtIntegral.setText(user.getIntegration());
			// 给布局对象设置监听
			btnMyOrdering.setOnClickListener(this);
		} else {
			btnLogin.setOnClickListener(this);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 点击登陆按钮
		case R.id.frament_user_btn_login:
			clickLoginBtn();
			break;
		case R.id.fragment_user_logined_btn_myordering:
			clickMyOrderingBtn();
			break;

		}
	}

	public void clickLoginBtn() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), LoginActivity.class);
		startActivity(intent);

	}

	public void clickMyOrderingBtn() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), MyOrder.class);
		startActivity(intent);
	}
}
