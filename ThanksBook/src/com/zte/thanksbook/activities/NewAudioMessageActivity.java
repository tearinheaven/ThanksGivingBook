package com.zte.thanksbook.activities;

import java.io.IOException;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
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
import com.zte.thanksbook.util.AudioRecorder;
import com.zte.thanksbook.util.AudioRecorder.AudioRecorderListener;
import com.zte.thanksbook.util.PreferenceUtil;
import com.zte.thanksbook.util.TGUtil;

public class NewAudioMessageActivity extends Activity {
	//是否按住录音按钮
	private boolean isTouching = false;
	
	private View micActionLittle;
	
	private View micActionBig;
	
	private Button micBtn;
	
	private TextView micTip;
	
	/**
	 * 录音工具实例
	 */
	private AudioRecorder audioRecorder;
	
	/**
	 * 录音播放工具实例
	 */
	private MediaPlayer audioPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_audio_message);
		this.getActionBar().hide();
		
		this.micBtn = (Button)this.findViewById(R.id.mic_btn);
		micBtn.setOnTouchListener(onMicBtnListener);
		
		//重录
		this.findViewById(R.id.mic_re_record).setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				audioRecorder.release();
				NewAudioMessageActivity.this.recreate();
				
				return false;
			}
		});
		
		//播放
		this.findViewById(R.id.mic_view).setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//Log.v("asdfasdfd", "touch!!!!!");
				if (audioPlayer!= null)
				{
					if (audioPlayer.isPlaying())
					{
						//暂停
			        	ImageView micInner = (ImageView)NewAudioMessageActivity.this.findViewById(R.id.mic_view);
			        	micInner.setBackgroundResource(R.drawable.mic_play);
			        	
						audioPlayer.pause();
						Log.v("ThanksBook", "pause!!!!!");
					}
					else
					{
						//继续播放
			        	ImageView micInner = (ImageView)NewAudioMessageActivity.this.findViewById(R.id.mic_view);
			        	micInner.setBackgroundResource(R.drawable.mic_pause);
			        	
						audioPlayer.start();
						Log.v("ThanksBook", "start!!!!!");
					}
				}
				else 
				{
					if (!TGUtil.isEmpty(audioRecorder.getFileName()))
					{
						audioPlayer = new MediaPlayer();
				        try {
				        	ImageView micInner = (ImageView)NewAudioMessageActivity.this.findViewById(R.id.mic_view);
				        	micInner.setBackgroundResource(R.drawable.mic_pause);
				        	
				        	audioPlayer.setDataSource(audioRecorder.getFileName());
				        	audioPlayer.setOnCompletionListener(audioCompletionListener);
				            audioPlayer.prepare();
				        	audioPlayer.start();
				        } catch (IOException e) {
				            Log.e("ThanksBook", "播放 prepare() 失败");
				            e.printStackTrace();
				        }
					}
				}
			}
		});
		
		//取消
		this.findViewById(R.id.mic_btn_cancel).setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (!TGUtil.isEmpty(audioRecorder.getFileName()))
				{
					ThreeButtonShadowFragment shadow = new ThreeButtonShadowFragment(new String[] {"保存草稿", "取消发布", "返回录音"}, cancelLinstener);
					FragmentManager fragManager = getFragmentManager();
			        FragmentTransaction tran = fragManager.beginTransaction();
			        tran.replace(R.id.mic_shadow, shadow);
			        tran.addToBackStack(null);
			        tran.commit();
				}
				else
				{
					NewAudioMessageActivity.this.finish();
				}
			}
		});
		
		this.micActionLittle = NewAudioMessageActivity.this.findViewById(R.id.mic_action_little);
		this.micActionBig = NewAudioMessageActivity.this.findViewById(R.id.mic_action_big);
		
		this.micTip = (TextView)this.findViewById(R.id.mic_tip);
		
		String userName = PreferenceUtil.getStringPre(this, PreferenceUtil.USER_NAME, null);
		if (!TGUtil.isEmpty(userName))
		{
			//初始化录音实例
			this.audioRecorder = new AudioRecorder(userName, audioRecorderListener);
		}
		else
		{
			Log.e("ThanksBook", "用户名读取失败");
		}
	}

	private View.OnTouchListener onMicBtnListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {					
				Log.v(null, "按下录音按钮");			
				
				if (audioRecorder.isMaxDuration())
				{
					TGUtil.showToast(NewAudioMessageActivity.this, "已达到最大录音时长", 3000);
					return false;
				}
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

				audioRecorder.startRecording();
			}
			else if (event.getAction() == KeyEvent.ACTION_UP) {
				Log.v(null, "松开录音按钮");		
				
				audioRecorder.stopRecording();
				
			}
			return false;
		}
	};
	
	private AudioRecorderListener audioRecorderListener = new AudioRecorderListener() {
		//录音结束回调函数
		@Override
		public void onRecorderStop() {
			isTouching = false;		
			
			int duration = (int)(audioRecorder.getDuration() / new Long(1000));
			//录音太短
			if (duration < 1)
			{
				TGUtil.showToast(NewAudioMessageActivity.this, "录音太短", 3000);
				audioRecorder.release();
				NewAudioMessageActivity.this.recreate();
			}
			else
			{				
				String durString = String.valueOf(duration) + "''";
				((TextView)NewAudioMessageActivity.this.findViewById(R.id.mic_record_second)).setText(durString);

				micBtn.setBackgroundResource(R.drawable.border_corner_mic);
				micBtn.setText("按住继续录音");
				micTip.setText("");
				
				micActionLittle.setVisibility(View.INVISIBLE);
				micActionBig.setVisibility(View.INVISIBLE);
				
				ImageView micInner = (ImageView)NewAudioMessageActivity.this.findViewById(R.id.mic_view);
				micInner.setBackgroundResource(R.drawable.mic_play);
				
				NewAudioMessageActivity.this.findViewById(R.id.mic_pause_big).setVisibility(View.VISIBLE);
				NewAudioMessageActivity.this.findViewById(R.id.mic_re_record).setVisibility(View.VISIBLE);
				((TextView)NewAudioMessageActivity.this.findViewById(R.id.mic_play_tip)).setText("点击按钮播放");
				
				if (audioRecorder.isMaxDuration())
				{
					TGUtil.showToast(NewAudioMessageActivity.this, "已达到最大录音时长", 3000);
				}
			}
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
	
	private OnCompletionListener audioCompletionListener = new OnCompletionListener() {
		@Override
		public void onCompletion(android.media.MediaPlayer arg0) 
		{
        	ImageView micInner = (ImageView)NewAudioMessageActivity.this.findViewById(R.id.mic_view);
        	micInner.setBackgroundResource(R.drawable.mic_play);
        	
			audioPlayer.release();
			audioPlayer = null;
		}
	};
	
	//取消按钮动作
	private ThanksShadowLinstener cancelLinstener = new ThanksShadowLinstener() {
		//取消 -- 保存草稿
		@Override
		public void mainAction() {
			// TODO Auto-generated method stub
			
		}

		//取消 -- 取消发布
		@Override
		public void subAction() {
			//释放资源
			if (audioPlayer!= null)
			{
				audioPlayer.release();
				audioPlayer = null;
			}

			if (!TGUtil.isEmpty(audioRecorder.getFileName()))
			{
				audioRecorder.release();
			}

			//销毁当前的Activity
			NewAudioMessageActivity.this.finish();
		}

		//取消 -- 返回
		@Override
		public void cancelAction() {
			// TODO Auto-generated method stub
			
		}
	};

}
