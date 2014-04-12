package com.zte.thanksbook.activities;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.zte.thanksbook.R;

/**
 * 新建感恩后退按钮页面
 * @author Administrator
 *
 */
public class NewTextMessageShadowFragment extends Fragment implements OnClickListener{

	private Activity activity = getActivity();
	private List<Uri> photos;
	
	public NewTextMessageShadowFragment(List<Uri> photos)
	{
		this.photos = photos;
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
		TextView date = (TextView)activity.findViewById(R.id.dateText);
		String dateStr = date.getText().toString();
		EditText msg = (EditText)activity.findViewById(R.id.msg_text);
		String msgStr = msg.getText().toString();
		
		
	}
	
	private void unsave()
	{
		
	}
	
	private void cancel()
	{
		
	}
	
}
