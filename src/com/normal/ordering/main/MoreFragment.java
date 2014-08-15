package com.normal.ordering.main;

import com.normal.ordering.R;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * 更多--Fragment
 * 
 * @author Vaboon
 * @date 2014-6-2
 */
public class MoreFragment extends PreferenceFragment implements 
Preference.OnPreferenceClickListener,
Preference.OnPreferenceChangeListener{

	private CheckBoxPreference smsprompt_preference;		//消息提示
	private CheckBoxPreference image_preference;			//是否显示图片
	private Preference share_preference;					//分享
	private Preference invite_friends_preference;			//邀请好友
	private ListPreference set_typeface_preference;			//设置字体
	private Preference empty_cache_preference;				//清空缓存
	private EditTextPreference option_feedback_preference;	//提意见
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		addPreferencesFromResource(R.xml.preferences);
		
		/*
		 * 更具KEY找到value
		 */
		smsprompt_preference=(CheckBoxPreference) findPreference("smsprompt_preference");
		image_preference=(CheckBoxPreference) findPreference("image_preference");
		share_preference=(Preference) findPreference("share_preference");
		invite_friends_preference=(Preference) findPreference("invite_friends_preference");
		set_typeface_preference=(ListPreference) findPreference("set_typeface_preference");
		empty_cache_preference=(Preference) findPreference("empty_cache_preference");
		option_feedback_preference=(EditTextPreference) findPreference("option_feedback_preference");
		/*
		 * 设置监听
		 */
		smsprompt_preference.setOnPreferenceClickListener(this);
		smsprompt_preference.setOnPreferenceChangeListener(this);
		image_preference.setOnPreferenceClickListener(this);
		image_preference.setOnPreferenceChangeListener(this);
		share_preference.setOnPreferenceClickListener(this);
		invite_friends_preference.setOnPreferenceClickListener(this);
		set_typeface_preference.setOnPreferenceClickListener(this);
		set_typeface_preference.setOnPreferenceChangeListener(this);
		empty_cache_preference.setOnPreferenceClickListener(this);
		option_feedback_preference.setOnPreferenceClickListener(this);
		option_feedback_preference.setOnPreferenceChangeListener(this);
		
		SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
		return super.onCreateView(inflater, container, savedInstanceState);

	}
	/*
	 * 点击按钮时的操作
	 */
	public void operatePreference(Preference preference){
		if(preference.getKey().equals("smsprompt_preference")){
			
		}
		else if(preference.getKey().equals("image_preference")){
			MainActivity.noImg=image_preference.isChecked();
			Toast.makeText(getActivity(), MainActivity.noImg+"", Toast.LENGTH_SHORT).show();
			Log.i("hello", MainActivity.noImg+"");
			
		}
		else if(preference.getKey().equals("share_preference")){}
		else if(preference.getKey().equals("invite_friends_preference")){}
		else if(preference.getKey().equals("set_typeface_preference")){}
		else if(preference.getKey().equals("empty_cache_preference")){
			Toast.makeText(getActivity(), "已清理缓存", Toast.LENGTH_SHORT).show();
		}
		else if(preference.getKey().equals("option_feedback_preference")){}
	}
	
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		
		if(preference.getKey().equals("smsprompt_preference")){
			return true;
		}
		else if(preference.getKey().equals("image_preference")){
			return true;
		}
		else if(preference.getKey().equals("set_typeface_preference")){
			return true;
		}
		else if(preference.getKey().equals("option_feedback_preference")){
			return true;
		}
		return true;
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
	
		operatePreference(preference);

		return false;
	}
	
}
