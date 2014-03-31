package com.zte.thanksbook.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;

import com.zte.thanksbook.R;

public class BootStrapActivity extends Activity {

	private boolean isFirstUse = true;
	private static final String IS_FIRST_NAME = "first_use";
	private static final int TO_GUIDE_PAGE = 1;
	private static final int TO_MAIN_PAGE = 2;
	private static final int DELAY_MILLS = 3000;
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what)
			{
			case TO_GUIDE_PAGE:
				Intent toGuide = new Intent(BootStrapActivity.this,GuideActivity.class);
				startActivity(toGuide);
				finish();
				break;
			case TO_MAIN_PAGE:
				Intent toMain = new Intent(BootStrapActivity.this,MainActivity.class);
				startActivity(toMain);
				finish();
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boot_strap);
		this.getActionBar().hide();
		SharedPreferences  sharePre = this.getSharedPreferences(IS_FIRST_NAME, MODE_PRIVATE);
		isFirstUse = sharePre.getBoolean(IS_FIRST_NAME, true);
		if(!isFirstUse)
		{
			handler.sendEmptyMessageDelayed(TO_MAIN_PAGE, DELAY_MILLS);
		}else
		{
			handler.sendEmptyMessageDelayed(TO_GUIDE_PAGE, DELAY_MILLS);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.boot_strap, menu);
		return true;
	}

}
