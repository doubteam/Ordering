package com.normal.ordering.tools;

import java.util.LinkedList;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.normal.ordering.entities.User;
import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

public class IApplication extends Application {

	private static IApplication iApplication;
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;
	public TextView mLocationResult, logMsg;
	public TextView trigger, exit;
	public Vibrator mVibrator;

	@Override
	public void onCreate() {
		super.onCreate();
		iApplication = this;
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());

		mVibrator = (Vibrator) getApplicationContext().getSystemService(
				Service.VIBRATOR_SERVICE);
	}

	public static IApplication getInstance() {
		return iApplication;
	}

	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 记录（存储）没有Activity对象－引用
	 */
	private static LinkedList<Activity> activities;

	public void addActivity(Activity _activity) {
		if (activities == null) {
			activities = new LinkedList<Activity>();
		}

		if (activities != null) {
			boolean flag = false;
			for (Activity activity : activities) {
				if (activity == _activity) {
					flag = true;
					break;
				}
			}

			if (!flag) {
				activities.add(_activity);
			}
		}
	}

	public void removeActivity(Activity _activity) {
		if (activities != null) {
			activities.remove(_activity);
		}
	}

	public void exitApp() {
		try{
			if (activities != null) {
				for (Activity activity : activities) {
					activity.finish();
				}
				activities.clear();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public int getActivitiesSize() {
		return activities.size();
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append(location.getAddrStr());
			}
			logMsg(sb.toString());
			Log.i("BaiduLocationApiDem", sb.toString());
		}

	}

	/**
	 * 显示请求字符串
	 * 
	 * @param str
	 */
	public void logMsg(String str) {
		try {
			
			if (mLocationResult != null)
				mLocationResult.setText(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
