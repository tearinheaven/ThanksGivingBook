package com.zte.thanksbook.activities;

import com.zte.thanksbook.R;
import com.zte.thanksbook.util.TGUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
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

@SuppressLint("ValidFragment")
public class ThreeButtonShadowFragment extends Fragment {
	private ThanksShadowLinstener btnLinstener;
	private String[] btnTexts;

	protected ThreeButtonShadowFragment() {
	}

	public ThreeButtonShadowFragment(String[] btnTexts,
			ThanksShadowLinstener btnLinstener) {
		this.btnLinstener = btnLinstener;
		this.btnTexts = btnTexts;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.three_button_shadow, container,
				false);

		if (null != this.btnTexts) {
			((Button) v.findViewById(R.id.shadow_btn_main))
					.setText(this.btnTexts[0]);
			((Button) v.findViewById(R.id.shadow_btn_sub))
					.setText(this.btnTexts[1]);
			((Button) v.findViewById(R.id.shadow_btn_cancel))
					.setText(this.btnTexts[2]);
		}

		if (null != this.btnLinstener) {
			// 主按钮事件注册
			v.findViewById(R.id.shadow_btn_main).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {							
							btnLinstener.mainAction();
							
							FragmentTransaction ft = ThreeButtonShadowFragment.this.getActivity()
									.getFragmentManager().beginTransaction();
							ft.remove(ThreeButtonShadowFragment.this).commit();
						}
					});

			// 次按钮事件注册
			v.findViewById(R.id.shadow_btn_sub).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							btnLinstener.subAction();
							
							FragmentTransaction ft = ThreeButtonShadowFragment.this.getActivity()
									.getFragmentManager().beginTransaction();
							ft.remove(ThreeButtonShadowFragment.this).commit();
						}
					});

			// 取消按钮事件注册
			v.findViewById(R.id.shadow_btn_cancel).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							btnLinstener.cancelAction();
							
							FragmentTransaction ft = ThreeButtonShadowFragment.this.getActivity()
									.getFragmentManager().beginTransaction();
							ft.remove(ThreeButtonShadowFragment.this).commit();
						}
					});
		}

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
	}
}