package com.zte.thanksbook.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.zte.thanksbook.R;

public class NewAudioMessageActivity extends Activity {
	private boolean isTouching = false;
	
	private View micActionLittle;
	
	private View micActionBig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_audio_message);
		this.getActionBar().hide();
		
		this.findViewById(R.id.mic_btn).setOnTouchListener(onMicBtnListener);
		
		this.micActionLittle = NewAudioMessageActivity.this.findViewById(R.id.mic_action_little);
		this.micActionBig = NewAudioMessageActivity.this.findViewById(R.id.mic_action_big);
	}

	private View.OnTouchListener onMicBtnListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				Log.v(null, "按下录音按钮");
				isTouching = true;
				
				ImageButton micInner = (ImageButton)NewAudioMessageActivity.this.findViewById(R.id.mic_btn);
				micInner.setBackgroundResource(R.drawable.mic_action_inner);
				
				micActionLittle.setVisibility(View.VISIBLE);
				Animation anim = AnimationUtils.loadAnimation(NewAudioMessageActivity.this, R.anim.mic_record_anim);
				anim.setAnimationListener(animationListenerLittle);
				micActionLittle.startAnimation(anim);
			}
			else if (event.getAction() == KeyEvent.ACTION_UP) {
				Log.v(null, "松开录音按钮");
				isTouching = false;
				
				micActionLittle.setVisibility(View.INVISIBLE);
				micActionBig.setVisibility(View.INVISIBLE);
				ImageButton micInner = (ImageButton)NewAudioMessageActivity.this.findViewById(R.id.mic_btn);
				micInner.setBackgroundResource(R.drawable.mic_init);
			}
			return false;
		}
	};
	
	private AnimationListener animationListenerLittle = new AnimationListener() {
		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			if (isTouching)
			{
				micActionBig.setVisibility(View.VISIBLE);
	
				Animation anim = AnimationUtils.loadAnimation(NewAudioMessageActivity.this, R.anim.mic_record_anim_b);
				anim.setAnimationListener(animationListenerBig);
				micActionBig.startAnimation(anim);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub			
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub			
		}
	};
	
	private AnimationListener animationListenerBig = new AnimationListener() {
		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			if (isTouching)
			{				
				micActionBig.setVisibility(View.INVISIBLE);

				Animation anim = AnimationUtils.loadAnimation(NewAudioMessageActivity.this, R.anim.mic_record_anim);
				anim.setAnimationListener(animationListenerLittle);
				micActionLittle.startAnimation(anim);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub			
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub			
		}
	};

}
