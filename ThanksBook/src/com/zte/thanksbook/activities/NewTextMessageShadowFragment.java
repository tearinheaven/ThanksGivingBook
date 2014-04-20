package com.zte.thanksbook.activities;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zte.thanksbook.R;
import com.zte.thanksbook.db.ThanksMessageDAO;
import com.zte.thanksbook.entity.ThanksMessageEntity;

/**
 * 新建感恩后退按钮页面
 * @author Administrator
 *
 */
public class NewTextMessageShadowFragment extends Fragment implements OnClickListener{

	private Context context;
	private List<Uri> photos;
	private String date;
	private String thanksTo;
	private String msg;
	
	private static final int TOAST_TIME_SECOND = 2*1000;
	
	public NewTextMessageShadowFragment(String date,String thanksTo,String msg,List<Uri> photos)
	{
		this.photos = photos;
		this.date = date;
		this.thanksTo = thanksTo;
		this.msg = msg;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.new_text_shadow, container,false);
		TextView save = (TextView)view.findViewById(R.id.save);
		save.setOnClickListener(this);
		TextView unsave = (TextView)view.findViewById(R.id.unsave);
		unsave.setOnClickListener(this);
		TextView cancel = (TextView)view.findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View view) {
		switch(view.getId())
		{
		case R.id.save:
			this.save();
			break;
		case R.id.unsave:
			this.unsave();
			break;
		case R.id.cancel:
			default:
			this.cancel();
		}
	}
	
	private void save()
	{
		ThanksMessageEntity msgEntity = new ThanksMessageEntity();
		msgEntity.setImgs(photos);
		msgEntity.setCreateBy(1l);
		msgEntity.setMessageText(msg);
		msgEntity.setThanksTo("");
		context = this.getActivity();
		SaveMsgTask task = new SaveMsgTask();
		task.execute(msgEntity);
		Activity fatherActivity = (Activity)context;
		FragmentManager manager = fatherActivity.getFragmentManager();
		FragmentTransaction tran = manager.beginTransaction();
		Fragment fragment = manager.findFragmentById(R.id.new_text_message);
		tran.remove(fragment);
		tran.commit();
	}
	
	private void unsave()
	{
		context = this.getActivity();
		Activity fatherActivity = (Activity)context;
		Intent intent = new Intent(fatherActivity,MainActivity.class);
		fatherActivity.startActivity(intent);
	}
	
	private void cancel()
	{
		context = this.getActivity();
		Activity fatherActivity = (Activity)context;
		FragmentManager manager = fatherActivity.getFragmentManager();
		FragmentTransaction tran = manager.beginTransaction();
		Fragment fragment = manager.findFragmentById(R.id.new_text_message);
		tran.remove(fragment);
		tran.commit();
	}
	
	/**
	 * 异步保存草稿类
	 * @author huangjianxin
	 *
	 */
	class SaveMsgTask extends AsyncTask<ThanksMessageEntity, Void, Boolean>
	{
		@Override
		protected Boolean doInBackground(ThanksMessageEntity... msgs) {
			boolean result = false;
			ThanksMessageEntity msg = msgs[0];
			ThanksMessageDAO msgDAO = new ThanksMessageDAO(context);
			result = msgDAO.addThanksMessage(msg);
			Log.i("save", "saveSuccess");
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(result)
			{
				Toast.makeText(context, "保存成功", TOAST_TIME_SECOND).show();
			}else
			{
				Toast.makeText(context, "保存失败", TOAST_TIME_SECOND).show();
			}
		}
	}
	
}



