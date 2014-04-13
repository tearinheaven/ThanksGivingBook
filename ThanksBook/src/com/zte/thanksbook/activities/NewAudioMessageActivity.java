package com.zte.thanksbook.activities;

import java.io.IOException;

import android.app.Activity;
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
import com.zte.thanksbook.util.PreferenceUtil;
import com.zte.thanksbook.util.TGUtil;

public class NewAudioMessageActivity extends Activity {
	//�Ƿ�ס¼����ť
	private boolean isTouching = false;
	//�Ƿ���¼���ļ�
	private boolean hasAudio = false;
	
	private View micActionLittle;
	
	private View micActionBig;
	
	private Button micBtn;
	
	private TextView micTip;
	
	/**
	 * ¼������ʵ��
	 */
	private AudioRecorder audioRecorder;
	
	/**
	 * ¼�����Ź���ʵ��
	 */
	private MediaPlayer audioPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_audio_message);
		this.getActionBar().hide();
		
		this.micBtn = (Button)this.findViewById(R.id.mic_btn);
		micBtn.setOnTouchListener(onMicBtnListener);
		
		//��¼
		this.findViewById(R.id.mic_re_record).setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				audioRecorder.release();
				NewAudioMessageActivity.this.recreate();
				
				return false;
			}
		});
		
		//����
		this.findViewById(R.id.mic_view).setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Log.v("asdfasdfd", "touch!!!!!");
				if (audioPlayer!= null)
				{
					if (audioPlayer.isPlaying())
					{
						//��ͣ
						audioPlayer.pause();
						Log.v("asdfasdfd", "pause!!!!!");
					}
					else
					{
						//��������
						audioPlayer.start();
						Log.v("asdfasdfd", "start!!!!!");
					}
				}
				else 
				{
					if (!TGUtil.isEmpty(audioRecorder.getFileName()))
					{
						audioPlayer = new MediaPlayer();
				        try {
				        	audioPlayer.setDataSource(audioRecorder.getFileName());
				        	audioPlayer.setOnCompletionListener(audioCompletionListener);
				            audioPlayer.prepare();
				        	audioPlayer.start();
				        } catch (IOException e) {
				            Log.e("ThanksBook", "���� prepare() ʧ��");
				            e.printStackTrace();
				        }
					}
				}
			}
		});
		
		this.micActionLittle = NewAudioMessageActivity.this.findViewById(R.id.mic_action_little);
		this.micActionBig = NewAudioMessageActivity.this.findViewById(R.id.mic_action_big);
		
		this.micTip = (TextView)this.findViewById(R.id.mic_tip);
		
		String userName = PreferenceUtil.getStringPre(this, PreferenceUtil.USER_NAME, null);
		if (!TGUtil.isEmpty(userName))
		{
			//��ʼ��¼��ʵ��
			this.audioRecorder = new AudioRecorder(userName);
		}
		else
		{
			Log.e("ThanksBook", "�û�����ȡʧ��");
		}
	}

	private View.OnTouchListener onMicBtnListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				Log.v(null, "����¼����ť");
				isTouching = true;
				
				micBtn.setBackgroundResource(R.drawable.mic_recording);
				micBtn.setText("");
				micTip.setText("�ɿ���ͣ");
				
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
				Log.v(null, "�ɿ�¼����ť");
				isTouching = false;				
				
				audioRecorder.stopRecording();
				int duration = (int)(audioRecorder.getDuration() / new Long(1000));
				//¼��̫��
				if (duration < 1)
				{
					TGUtil.showToast(NewAudioMessageActivity.this, "¼��̫��", 3000);
					audioRecorder.release();
					NewAudioMessageActivity.this.recreate();
				}
				else
				{				
					String durString = String.valueOf(duration) + "''";
					((TextView)NewAudioMessageActivity.this.findViewById(R.id.mic_record_second)).setText(durString);
	
					micBtn.setBackgroundResource(R.drawable.border_corner_mic);
					micBtn.setText("��ס����¼��");
					micTip.setText("");
					
					micActionLittle.setVisibility(View.INVISIBLE);
					micActionBig.setVisibility(View.INVISIBLE);
					
					hasAudio = true;
					ImageView micInner = (ImageView)NewAudioMessageActivity.this.findViewById(R.id.mic_view);
					micInner.setBackgroundResource(R.drawable.mic_play);
					
					NewAudioMessageActivity.this.findViewById(R.id.mic_pause_big).setVisibility(View.VISIBLE);
					NewAudioMessageActivity.this.findViewById(R.id.mic_re_record).setVisibility(View.VISIBLE);
					((TextView)NewAudioMessageActivity.this.findViewById(R.id.mic_play_tip)).setText("�����ť����");
				}
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
	
	private OnCompletionListener audioCompletionListener = new OnCompletionListener() {
		@Override
		public void onCompletion(android.media.MediaPlayer arg0) 
		{
			audioPlayer.release();
			audioPlayer = null;
		}
	};

}
