package com.zte.thanksbook.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zte.thanksbook.R;
import com.zte.thanksbook.db.ThanksMessageDAO;

public class MainActivity extends Activity implements OnClickListener {
    private NavigationFragment navigation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
		ListView thanksList = (ListView)this.findViewById(R.id.thanks_list);
		ThanksMessageDAO dao = new ThanksMessageDAO(MainActivity.this);
		Cursor messages = dao.queryThanksMessages();
		/*messages.moveToFirst();
		String a = messages.getString(messages.getColumnIndex("create_date"));
		String b = messages.getString(messages.getColumnIndex("message_text"));
		String c = messages.getString(messages.getColumnIndex("msg_type"));*/
		
		MyAdapter adapter = new MyAdapter(messages);
		thanksList.setAdapter(adapter);
		
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
		/*switch (item.getItemId()) {
		case R.id.action_filter:
			Intent intent = new Intent(this, SignActivity.class);
			startActivity(intent);
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}
	
	class MyAdapter extends BaseAdapter
	{
		private Cursor datas;

		public MyAdapter(Cursor datas)
		{
			this.datas = datas;
		}
		
		@Override
		public int getCount() {
			return datas.getCount();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			Log.i("------------", position+"");
			if(view==null)
			{
				String msgType = null;
				Log.i("-----------", "beforemove");
				datas.moveToPosition(position);
				Log.i("-----------", "aftermove");
				msgType = datas.getString(datas.getColumnIndex("msg_type"));
				Log.i("-----------", msgType);
				//文本类型
				if("text".equals(msgType))
				{
					view = getLayoutInflater().inflate(R.layout.scrollable_textmsg_item, parent, false);
					TextView createDate = (TextView)view.findViewById(R.id.create_date);
					createDate.setText(datas.getString(datas.getColumnIndex("create_date")));
					TextView msg = (TextView)view.findViewById(R.id.message_text);
					msg.setText(datas.getString(datas.getColumnIndex("message_text")));
					ImageView ozz = (ImageView)view.findViewById(R.id.ozz);
					ozz.setBackgroundResource(R.drawable.friend);
				}else if("video".equals(msgType))
				{
					
				}
			}
			return view;
		}
		
	}

}