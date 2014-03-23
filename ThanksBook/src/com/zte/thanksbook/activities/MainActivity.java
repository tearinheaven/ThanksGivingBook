package com.zte.thanksbook.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.zte.thanksbook.R;

public class MainActivity extends Activity implements OnClickListener {
    private NavigationFragment navigation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
		MainBottomFragment bottomFragment = new MainBottomFragment();
		FragmentManager fragManager = this.getFragmentManager();
		FragmentTransaction fragTransaction = fragManager.beginTransaction();
		fragTransaction.replace(R.id.main_bottom,bottomFragment);
		fragTransaction.commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.actionbar_layout);
		//actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		this.findViewById(R.id.actionbar_menu_tip).setOnClickListener(this);
		
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		boolean isMenuShow = false;
		if (this.navigation!=null)
		{
			isMenuShow = this.navigation.isMenuShow();
		}
		
		if (isMenuShow) 
		{
			this.navigation.toggleMenu(this.findViewById(R.id.actionbar_menu_tip));
		}
		else
		{			
			navigation = new NavigationFragment();
	        FragmentManager fragManager = getFragmentManager();
	        FragmentTransaction tran = fragManager.beginTransaction();
	        tran.replace(R.id.thanks_nav, navigation);
	        tran.addToBackStack(null);
	        tran.commit();
			this.navigation.toggleMenu(this.findViewById(R.id.actionbar_menu_tip));
		}
		
		return;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_filter:
			Intent intent = new Intent(this, SignActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}