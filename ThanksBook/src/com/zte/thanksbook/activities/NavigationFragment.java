package com.zte.thanksbook.activities;

import com.zte.thanksbook.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;

public class NavigationFragment extends Fragment implements AnimationListener {

	private Activity myActivity;
	
	private boolean isMenuShow;

	public boolean isMenuShow() {
		return isMenuShow;
	}

	public void setMenuShow(boolean isMenuShow) {
		this.isMenuShow = isMenuShow;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.thanks_nav, container, false);

		this.myActivity = this.getActivity();

		Button b = (Button) v.findViewById(R.id.nav_shadow);
		b.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (myActivity!=null)
				{
					toggleMenu(myActivity.findViewById(R.id.actionbar_menu_tip));
				}
			}
		});

		//设置选中状态，待完善
		v.findViewById(R.id.nav_menu_home).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setSelected(true);
				myActivity.findViewById(R.id.nav_menu_draft).setSelected(false);
				myActivity.findViewById(R.id.nav_menu_myinfo).setSelected(false);
				myActivity.findViewById(R.id.nav_menu_setup).setSelected(false);
			}
		});
		v.findViewById(R.id.nav_menu_draft).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setSelected(true);
				myActivity.findViewById(R.id.nav_menu_home).setSelected(false);
				myActivity.findViewById(R.id.nav_menu_myinfo).setSelected(false);
				myActivity.findViewById(R.id.nav_menu_setup).setSelected(false);
			}
		});
		v.findViewById(R.id.nav_menu_myinfo).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setSelected(true);
				myActivity.findViewById(R.id.nav_menu_home).setSelected(false);
				myActivity.findViewById(R.id.nav_menu_draft).setSelected(false);
				myActivity.findViewById(R.id.nav_menu_setup).setSelected(false);
			}
		});
		v.findViewById(R.id.nav_menu_setup).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setSelected(true);
				myActivity.findViewById(R.id.nav_menu_home).setSelected(false);
				myActivity.findViewById(R.id.nav_menu_draft).setSelected(false);
				myActivity.findViewById(R.id.nav_menu_myinfo).setSelected(false);
			}
		});

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		View v = this.getActivity().findViewById(R.id.nav_menu);
		Animation anim = AnimationUtils.loadAnimation(this.getActivity(), R.anim.nav_slow_in_anim);
		anim.setAnimationListener(this);
		v.startAnimation(anim);
		
		View v1 = this.getActivity().findViewById(R.id.nav_shadow);
		Animation anim1 = AnimationUtils.loadAnimation(this.getActivity(), R.anim.nav_fast_in_anim);
		v1.startAnimation(anim1);
	}

	public void toggleMenu(View v) {
		
		if (v!=null)
		{
			if (this.isMenuShow) 
			{
	
				Animation animation = new TranslateAnimation(
						-13, 0, v.getTop(), v.getTop());
				animation.setFillAfter(true);
				animation.setDuration(500);
				v.startAnimation(animation);
			}
			else
			{
				Animation animation = new TranslateAnimation(
						0, -13, v.getTop(), v.getTop());
				animation.setFillAfter(true);
				animation.setDuration(500);
				v.startAnimation(animation);			 
			}
		}
		
		if (this.isMenuShow)
		{
			View v2 = this.getActivity().findViewById(R.id.nav_menu);
			Animation anim2 = AnimationUtils.loadAnimation(this.getActivity(), R.anim.nav_slow_out_anim);
			anim2.setAnimationListener(this);
			v2.startAnimation(anim2);
			
			View v1 = this.getActivity().findViewById(R.id.nav_shadow);
			Animation anim1 = AnimationUtils.loadAnimation(this.getActivity(), R.anim.nav_fast_out_anim);
			v1.startAnimation(anim1);
		}
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		if (this.isMenuShow)
		{
			FragmentTransaction ft = this.getActivity().getFragmentManager()
					.beginTransaction();
			ft.remove(this).commit();
		}
		else
		{			
		}
		this.isMenuShow = this.isMenuShow ? false : true;
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
}