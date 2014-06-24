package com.normal.ordering.main;

import com.normal.ordering.R;
import com.normal.ordering.entities.User;
import com.normal.ordering.tools.IApplication;
import com.normal.ordering.userfragment.LoginActivity;

import android.support.v4.app.Fragment;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * 个人中心--Fragment
 * 
 * @author Vaboon
 * @date 2014-6-2
 */
public class UserFragment extends Fragment implements OnClickListener {
	private Button btnLogin;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.fragment_user, container,
				false);
		return fragmentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		initData();
		if(IApplication.getInstance().getUser()!=null){
			User user=IApplication.getInstance().getUser();
		Toast.makeText(getActivity(), user.getUserName()+ "",
				1000).show();
		
		}
		//
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		btnLogin = (Button) getActivity().findViewById(
				R.id.frament_user_btn_login);

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// 给布局对象设置监听
		btnLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 点击登陆按钮
		case R.id.frament_user_btn_login:
			clickLoginBtn();
			break;

		}
	}

	public void clickLoginBtn() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), LoginActivity.class);
		startActivity(intent);
	}

}
