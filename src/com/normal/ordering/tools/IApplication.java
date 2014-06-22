package com.normal.ordering.tools;

import java.util.LinkedList;

import android.app.Activity;
import android.app.Application;

public class IApplication extends Application {

	private static IApplication iApplication;

	@Override
	public void onCreate() {
		super.onCreate();
		iApplication = this;
	}

	public static IApplication getInstance() {
		return iApplication;
	}

	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 记录（存储）没有Activity对象－引用
	 */
	private static LinkedList<Activity> activities;

	public void addActivity(Activity _activity) {
		if (activities == null) {
			activities = new LinkedList<Activity>();
		}
		
		if(activities != null) {
			boolean flag = false;
			for (Activity activity : activities) {
				if(activity == _activity) {
					flag = true;
					break;
				}
			}
			
			if(!flag) {
				activities.add(_activity);
			}
		}
	}
	
	public void removeActivity(Activity _activity) {
		if(activities != null) {
			activities.remove(_activity);
		}
	}
	
	public void exitApp() {
		if(activities != null) {
			for (Activity activity : activities) {
				activity.finish();
			}
			activities.clear();
		}
	}
	
	public int getActivitiesSize() {
		return activities.size();
	}

}
