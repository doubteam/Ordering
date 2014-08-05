package com.normal.ordering.tools;

import com.normal.ordering.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyListViewFooter extends LinearLayout {

	public static final int STATE_NORMAL=0;
	public static final int STATE_READY=1;
	public static final int STATE_LOADING=2;
	
	private Context mContext;
	private View mContentView;
	private View mProgressBar;
	private TextView mHintView;
	
	public MyListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	public MyListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	public void setState(int state){
		mHintView.setVisibility(View.INVISIBLE);//初始状态不可见
		mProgressBar.setVisibility(View.INVISIBLE);
		if(state==STATE_LOADING){
			mProgressBar.setVisibility(View.VISIBLE);//加载的时候可见
		}
		else if(state==STATE_READY){
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.mylistview_footer_hint_ready);
		}
		else{
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText(R.string.mylistview_footer_hint_normal);
		}
	}
	/*
	 * 设置footer的高
	 */
	public void setBottomMargin(int height){
		if(height<0)return;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}
	public int getBottomMargin(){
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		return lp.bottomMargin;
	}
	
	
	public void normal() {
		mHintView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}

	/**
	 * 不能加载更多的时候隐藏footer
	 */
	public void hide() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}
	
	/**
	 * 显示footer
	 */
	public void show() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}
	
	/*
	 * 初始化
	 */
	private void initView(Context context) {
		mContext = context;
		RelativeLayout moreView = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.mylistview_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		mContentView = moreView.findViewById(R.id.mylistview_footer_content);
		mProgressBar = moreView.findViewById(R.id.mylistview_footer_progressbar);
		mHintView = (TextView)moreView.findViewById(R.id.mylistview_footer_hint_textview);
	}
}
