package com.zte.thanksbook.activities;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class PageViewAdapter extends PagerAdapter {

	private List<View> views;
	private Context activity;
	
	public PageViewAdapter(List<View> views,Context activity)
	{
		this.views = views;
		this.activity = activity;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(views.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(views.get(position));
		return views.get(position);
	}

	@Override
	public int getCount() {
		int size = 0;
		if(views!=null&&views.size()>0)
		{
			size = views.size();
		}
		return size;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}

}
