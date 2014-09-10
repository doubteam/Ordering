package com.normal.ordering.tools;



import android.app.Activity;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

/*
 * 取得地理位置
 */
public class LocationLoading extends Activity{

	public LocationClient mLocationClient;
	private static final int UPDATA_TIME=10*60*1000;

	


	public void getLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("gcj02");
		option.setScanSpan(UPDATA_TIME);
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}
}
