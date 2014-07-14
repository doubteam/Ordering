package com.normal.ordering.whatsnews;

import java.util.ArrayList;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**WhatsNews的适配器。可酌情修改。
 * @author VBaboon
 * @date 2014-7-14
 */
public class NewsPagerAdapter extends PagerAdapter{

	
	private ArrayList<View> views;
	private ArrayList<String> titles;
	
	
	public NewsPagerAdapter(ArrayList<View> views,ArrayList<String> titles){
		
		this.views = views;
		this.titles = titles;
	}
	
	@Override
	public int getCount() {
		return this.views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	public void destroyItem(View container, int position, Object object) {
		((ViewPager)container).removeView(views.get(position));
	}
	
	//页面view
	public Object instantiateItem(View container, int position) {
		((ViewPager)container).addView(views.get(position));
		return views.get(position);
	}
}
