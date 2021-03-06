package com.zte.thanksbook.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;

import com.zte.thanksbook.R;
import com.zte.thanksbook.util.PreferenceUtil;

public class BootStrapActivity extends Activity {

	private boolean isFirstUse = true;
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
		isFirstUse = PreferenceUtil.getBooleanPre(this, PreferenceUtil.IS_FIRST_USE, true);
		String userName = PreferenceUtil.getStringPre(this, PreferenceUtil.USER_NAME, null);
		if(!isFirstUse)
		{
			Log.v("ThanksBook", "UserName:" + (userName==null ? "" : userName));
		}
		if(!isFirstUse && userName!=null)
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
