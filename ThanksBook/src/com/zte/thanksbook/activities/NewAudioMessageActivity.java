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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zte.thanksbook.R;

public class NewAudioMessageActivity extends Activity {
	//是否按住录音按钮
	private boolean isTouching = false;
	//是否有录音文件
	private boolean hasAudio = false;
	
	private View micActionLittle;
	
	private View micActionBig;
	
	private Button micBtn;
	
	private TextView micTip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_audio_message);
		this.getActionBar().hide();
		
		this.micBtn = (Button)this.findViewById(R.id.mic_btn);
		micBtn.setOnTouchListener(onMicBtnListener);
		
		this.micActionLittle = NewAudioMessageActivity.this.findViewById(R.id.mic_action_little);
		this.micActionBig = NewAudioMessageActivity.this.findViewById(R.id.mic_action_big);
		
		this.micTip = (TextView)this.findViewById(R.id.mic_tip);
	}

	private View.OnTouchListener onMicBtnListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				Log.v(null, "按下录音按钮");
				isTouching = true;
				
				micBtn.setBackgroundResource(R.drawable.mic_recording);
				micBtn.setText("");
				micTip.setText("松开暂停");
				
				NewAudioMessageActivity.this.findViewById(R.id.mic_pause_big).setVisibility(View.INVISIBLE);
				NewAudioMessageActivity.this.findViewById(R.id.mic_re_record).setVisibility(View.INVISIBLE);
				((TextView)NewAudioMessageActivity.this.findViewById(R.id.mic_play_tip)).setText("");
				((TextView)NewAudioMessageActivity.this.findViewById(R.id.mic_record_second)).setText("");
								
				ImageView micInner = (ImageView)NewAudioMessageActivity.this.findViewById(R.id.mic_view);
				micInner.setBackgroundResource(R.drawable.mic_action_inner);
				
				micActionLittle.setVisibility(View.VISIBLE);
				Animation anim = AnimationUtils.loadAnimation(NewAudioMessageActivity.this, R.anim.mic_record_anim);
				anim.setAnimationListener(animationListenerLittle);
				micActionLittle.startAnimation(anim);
			}
			else if (event.getAction() == KeyEvent.ACTION_UP) {
				Log.v(null, "松开录音按钮");
				isTouching = false;				

				micBtn.setBackgroundResource(R.drawable.border_corner_mic);
				micBtn.setText("按住继续录音");
				micTip.setText("");
				
				micActionLittle.setVisibility(View.INVISIBLE);
				micActionBig.setVisibility(View.INVISIBLE);
				
				hasAudio = true;
				ImageView micInner = (ImageView)NewAudioMessageActivity.this.findViewById(R.id.mic_view);
				micInner.setBackgroundResource(R.drawable.mic_play);
				
				NewAudioMessageActivity.this.findViewById(R.id.mic_pause_big).setVisibility(View.VISIBLE);
				NewAudioMessageActivity.this.findViewById(R.id.mic_re_record).setVisibility(View.VISIBLE);
				((TextView)NewAudioMessageActivity.this.findViewById(R.id.mic_play_tip)).setText("点击按钮播放");
				((TextView)NewAudioMessageActivity.this.findViewById(R.id.mic_record_second)).setText("14''");
				
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
