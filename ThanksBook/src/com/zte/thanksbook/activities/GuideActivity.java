package com.zte.thanksbook.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.zte.thanksbook.R;

public class GuideActivity extends Activity implements OnPageChangeListener{
	
	private List<View> views;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		this.getActionBar().hide();
		LayoutInflater inflater = LayoutInflater.from(this);
		views = new ArrayList<View>();
		View page1 = inflater.inflate(R.layout.guide_one, null);
		views.add(page1);
		View page2 = inflater.inflate(R.layout.guide_two, null);
		views.add(page2);
		View page3 = inflater.inflate(R.layout.guide_three, null);
		views.add(page3);
		ViewPager viewPager = (ViewPager)this.findViewById(R.id.viewPager);
		PageViewAdapter adapter = new PageViewAdapter(views,this);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.guide, menu);
		return true;
	}

}