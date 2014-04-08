package com.zte.thanksbook.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.zte.thanksbook.R;

public class MainBottomFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =  inflater.inflate(R.layout.main_bottom_fragment, container,false);
		RelativeLayout addText = (RelativeLayout)view.findViewById(R.id.addThanksText);
		addText.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),NewTextMessageActivity.class);
				startActivity(intent);
			}
		});
		RelativeLayout addVideo = (RelativeLayout)view.findViewById(R.id.addThanksVideo);
		addVideo.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),NewAudioMessageActivity.class);
				startActivity(intent);
			}
		});
		return view;
	}
	
}
