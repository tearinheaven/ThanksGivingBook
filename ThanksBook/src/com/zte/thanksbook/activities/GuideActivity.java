package com.zte.thanksbook.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zte.thanksbook.R;

public class GuideActivity extends Activity implements OnPageChangeListener{
	
	private List<View> views;
	private LinearLayout dots;
	private int currentDotIndex;

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
		Button signBtn = (Button)page3.findViewById(R.id.sign);
		signBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent signIntent = new Intent(GuideActivity.this,SignActivity.class);
				signIntent.putExtra("signType", SignActivity.TYPE_SIGN_UP);
				startActivity(signIntent);
			}
		});
		/*Button loginBtn = (Button)page3.findViewById(R.id.login);
		loginBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent loginIntent = new Intent(GuideActivity.this,SignActivity.class);
				loginIntent.putExtra("signType", SignActivity.TYPE_LOGIN_IN);
				startActivity(loginIntent);
			}
		});*/
		views.add(page3);
		ViewPager viewPager = (ViewPager)this.findViewById(R.id.viewPager);
		PageViewAdapter adapter = new PageViewAdapter(views,this);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);
		dots = (LinearLayout)this.findViewById(R.id.dots);
		changeDots(0);
	}
	
	private void changeDots(int index)
	{
		for(int i=0;i<views.size();i++)
		{
			ImageView img = (ImageView)dots.getChildAt(i);
			if(i!=index)
			{
				img.setBackgroundResource(R.drawable.dot_2);
			}else
			{
				img.setBackgroundResource(R.drawable.dot_1);
			}
		}
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int index) {
		changeDots(index);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.guide, menu);
		return true;
	}

}