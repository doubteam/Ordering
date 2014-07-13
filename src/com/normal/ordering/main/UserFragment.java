package com.normal.ordering.main;

import com.normal.ordering.R;
import com.normal.ordering.entities.User;
import com.normal.ordering.service.PushService;
import com.normal.ordering.tools.IApplication;
import com.normal.ordering.userfragment.LoginActivity;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
	private SharedPreferences sp;
	private static User user;
	private static final String SP_NAME = "ORDERING";
	private static final String SP_LOGIN_NAME = "ORDERING_LOGIN_NAME";
	private static final String SP_LOGIN_PASSWORD = "ORDERING_LOGIN_PASSWORD";
	private static final String SP_SUCCESS_LOGIN = "ORDERING_SUCCESS_LOGIN";
	private ProgressDialog progressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragmentView;
		// 初始化sp，并划定你的存储|取值的区域
		this.sp = this.getActivity().getSharedPreferences(SP_NAME,
				Context.MODE_PRIVATE);
		if (IApplication.getInstance().getUser() != null
				|| sp.getBoolean(SP_SUCCESS_LOGIN, false)) {
			Log.d("3", sp.getString(SP_LOGIN_PASSWORD, null)+sp.getBoolean(SP_SUCCESS_LOGIN, false)+"");
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
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setTitle("请等待");
		progressDialog.setMessage("数据加载中.......");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialgo是否能够被取消
		progressDialog.setCancelable(false);
		// 明确进度
		progressDialog.setIndeterminate(true);
		/*
		 * 当user中有用户信息时(已经登陆)
		 */
		if (IApplication.getInstance().getUser() != null) {
			User user = IApplication.getInstance().getUser();
			Toast.makeText(getActivity(), user.getUserName() + "", 1000).show();
			initView(true);
			initData(user);
			/**
			 * 未登陆，但是SharedPreferences有正确的帐号密码。
			 */
		} else if (IApplication.getInstance().getUser() == null
				&& sp.getBoolean(SP_SUCCESS_LOGIN, false)) {
			/**
			 * 隐式的启动Service 如果在同一个包中。两者都可以用。在不同包时。只能用隐式启动
			 * 
			 * Intent intent = new Intent(PushService.ACTION);
			 */
			// TODO: 这里加入之前的数据，写入数据库的数据来初始化界面，不然太难看。后续加入
			Intent intent = new Intent(getActivity().getApplicationContext(), PushService.class);
			intent.putExtra("ServiceContent", "Login");
			this.getActivity().startService(intent);
			Log.d("3", "启动服务");
			progressDialog.show();
			

		} else {
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

	}

}
