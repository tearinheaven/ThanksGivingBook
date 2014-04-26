package com.zte.thanksbook.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zte.thanksbook.R;
import com.zte.thanksbook.db.ThanksMessageDAO;

public class MainActivity extends Activity implements OnClickListener {
    private NavigationFragment navigation;
    private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		options = new DisplayImageOptions.Builder()
		//.showImageOnLoading(R.drawable.ic_stub)
		//.showImageForEmptyUri(R.drawable.ic_empty)
		//.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisc(false)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		ListView thanksList = (ListView)this.findViewById(R.id.thanks_list);
		ThanksMessageDAO dao = new ThanksMessageDAO(MainActivity.this);
		Cursor messages = dao.queryThanksMessages();
		
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
				datas.moveToPosition(position);
				msgType = datas.getString(datas.getColumnIndex("msg_type"));
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
					ThanksMessageDAO dao = new ThanksMessageDAO(MainActivity.this);
					List<Long> imgIds = dao.queryImagesByMsgId(datas.getLong(datas.getColumnIndex("id")));
					if(imgIds!=null&&imgIds.size()>0)
					{
						List<String> imgFileNames = new ArrayList<String>();
						GridView gridView = (GridView)view.findViewById(R.id.imgs);
						for(Long imgId:imgIds)
						{
							String imgPath = dao.queryThumbnailById(imgId);
							String imgFileName = "thumbnail_"+imgId+".PNG";
							Log.i("---------", imgPath);
							imgFileNames.add("file:"+imgPath);
						}
						gridView.setAdapter(new GridViewAdapter(imgFileNames));
					}
				}else if("video".equals(msgType))
				{
					
				}
			}
			return view;
		}
		
		class GridViewAdapter extends BaseAdapter
		{
			List<String> imgFilePath;
			
			public GridViewAdapter(List<String> imgFilePath)
			{
				this.imgFilePath = imgFilePath;
			}
			
			@Override
			public int getCount() {
				return imgFilePath.size();
			}

			@Override
			public Object getItem(int arg0) {
				return arg0;
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public View getView(int index, View arg1, ViewGroup arg2) {
				ImageView imageView = (ImageView)arg1;
				if(imageView==null)
				{
					imageView = new ImageView(MainActivity.this);
		            imageView.setLayoutParams(new GridView.LayoutParams(125, 125));
		            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		            imageView.setPadding(8, 8, 8, 8);
				}
				imageView.setBackgroundResource(R.drawable.my_pic);
				imageLoader.displayImage(imgFilePath.get(index), imageView, options, null);
				return imageView;
			}
			
		}
		
	}

}