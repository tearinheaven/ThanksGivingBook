package com.zte.thanksbook.activities;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zte.thanksbook.R;
import com.zte.thanksbook.util.PreferenceUtil;
import com.zte.thanksbook.util.TGUtil;

public class NewAudioMessageNextActivity extends Activity {
	private static final int TAKE_PHOTO = 1;

	private static final int PICK_PHOTO = 2;
	
	private static final int CROP_PHOTO = 3;
	
	public static final String INTENT_PHOTO_URI = "intent_photo_uri";
	
	/**
	 * ��ǰ���� ���ࡢѡ����Ƭ���ü���Ƭ
	 */
	private int currentAction;

	/**
	 * ��ǰ��ƬURI
	 */
	private Uri currentPhoto = null;

	/**
	 * ��һ��URI
	 */
	private Uri tmpphoto = null;

	/**
	 * ��Ƭ�ü�����
	 */
	private final int rateWidth = 1;
	private final int rateHeight = 1;
	/*private final int cropWidth = 250;
	private final int cropHeight = 250;*/
	
	/**
	 * �û���
	 */
	private String userName;
	
	private ImageButton imageBtn;
	private TextView	imageTip;
	private Button 		audioBtn;
	
	/**
	 * 
	 */
	private Intent intent;
	
	/**
	 * ¼�����Ź���ʵ��
	 */
	private MediaPlayer audioPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_audio_message_next);
		this.intent = this.getIntent();

		// ����������
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setDisplayShowCustomEnabled(true);
		bar.setCustomView(R.layout.actionbar_layout_child);
		// ���ذ�ť
		this.findViewById(R.id.actionbar_menu_back).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						releasePalyer();
						
						intent.putExtra(NewAudioMessageNextActivity.INTENT_PHOTO_URI, 
								currentPhoto!=null ? currentPhoto.getPath() : "");
						NewAudioMessageNextActivity.this.setResult(RESULT_OK, intent);
						NewAudioMessageNextActivity.this.finish();
					}
				});
		((TextView) this.findViewById(R.id.actionbar_title)).setText("�½��ж�");

		// �û�����ʼ��
		this.userName = PreferenceUtil.getStringPre(this,
				PreferenceUtil.USER_NAME, null);

		this.imageBtn = (ImageButton)this.findViewById(R.id.audio_message_btn);
		this.imageTip = (TextView)this.findViewById(R.id.audio_message_tip);
		// �����Ƭ		
		this.imageBtn.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String[] strs = new String[] {"����", "���", "ȡ��"};
						if (currentPhoto != null)
						{
							strs = new String[] {"����", "���", "ɾ����Ƭ"};
						}
						ThreeButtonShadowFragment shadow = new ThreeButtonShadowFragment(strs, chooseLinstener);
						FragmentManager fragManager = getFragmentManager();
				        FragmentTransaction tran = fragManager.beginTransaction();
				        tran.replace(R.id.audio_shadow, shadow);
				        tran.addToBackStack(null);
				        tran.commit();
					}
				});
		
		//��������ͣ
		this.audioBtn = (Button)this.findViewById(R.id.audio_message_play_btn);
		this.audioBtn.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						toggleAudio();
					}
				});
		
		//��ʼ������
		Bundle bundle = this.intent.getExtras();
		if (!TGUtil.isEmpty(bundle.getString(INTENT_PHOTO_URI)))
		{
			Log.v("ThanksBook", "��ʼ��ͼƬ");
			File file = new File(bundle.getString(INTENT_PHOTO_URI));
			this.currentPhoto = Uri.fromFile(file);
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(this.currentPhoto));
				this.imageBtn.setImageBitmap(bitmap);
				this.imageTip.setText("");
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	//�����Ƭ�¼�
	private ThanksShadowLinstener chooseLinstener = new ThanksShadowLinstener() {
		//����
		@Override
		public void mainAction() {
			toggleAudio();
			
			tmpphoto = getOutputUri();
			currentAction = TAKE_PHOTO;
			Intent intent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, tmpphoto);

			startActivityForResult(intent, TAKE_PHOTO);
		}

		//���
		@Override
		public void subAction() {
			toggleAudio();
			
			currentAction = PICK_PHOTO;
			Intent intent = new Intent();  
	        intent.setAction(Intent.ACTION_PICK);  
	        intent.setType("image/*");  
	        startActivityForResult(intent, PICK_PHOTO);
		}

		//ɾ����Ƭ
		@Override
		public void cancelAction() {
			//�����ǰ������Ƭ����ɾ��
			if (currentPhoto != null)
			{
				//this.imageBtn.setBackgroundColor(Color.rgb(230, 230, 230));
				File file = new File(currentPhoto.getPath());
				if (file.exists())
				{
					file.delete();
				}
				currentPhoto = null;
				imageBtn.setImageBitmap(null);
				imageTip.setText("��������Ƭ");
			}
		}
	};

	// ���ջ���ѡ����Ƭ���� ������Ƭ
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v("ThanksBook", "result " + resultCode);
		if (resultCode == Activity.RESULT_CANCELED)
		{
			//������ڲü���Ƭ��ʱ��ȡ������ɾ����ʱ��Ƭ
			if (this.currentAction == CROP_PHOTO)
			{
				if (null != this.tmpphoto)
				{
					File file = new File(this.tmpphoto.getPath());
					if (file.exists())
					{
						file.delete();
					}
					this.tmpphoto = null;
				}
			}
			
			this.currentAction = 0;
		}
		else if (resultCode == Activity.RESULT_OK)
		{		
			if (requestCode == TAKE_PHOTO) {
				cropImageUri(this.tmpphoto, this.tmpphoto);
			}
			else if (requestCode == PICK_PHOTO)
			{
				tmpphoto = getOutputUri();
				cropImageUri(data.getData(), this.tmpphoto);
			}
			else if (requestCode == CROP_PHOTO)
			{
				if(this.tmpphoto != null){
					//�����ǰ������Ƭ����ɾ��
					if (this.currentPhoto != null)
					{
						//this.imageBtn.setBackgroundColor(Color.rgb(230, 230, 230));
						File file = new File(this.currentPhoto.getPath());
						if (file.exists())
						{
							file.delete();
						}
						this.currentPhoto = null;
					}
					
					//�����ʱ��ƬΪ��ǰ��Ƭ
					try {
						Log.v("ThanksBook", this.tmpphoto.getPath());
						Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(this.tmpphoto));
						this.imageBtn.setImageBitmap(bitmap);
						this.imageTip.setText("");
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
					this.currentPhoto = this.tmpphoto;
					this.tmpphoto = null;
					this.currentAction = 0;
				}
			}
		}
	}

	private void cropImageUri(Uri inUri, Uri outUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(inUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", this.rateWidth);
		intent.putExtra("aspectY", this.rateHeight);
		/*intent.putExtra("outputX", this.cropWidth);
		intent.putExtra("outputY", this.cropHeight);*/
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, CROP_PHOTO);
		this.currentAction = CROP_PHOTO;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_audio_message_next_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * switch (item.getItemId()) { case android.R.id.home: // This ID
		 * represents the Home or Up button. In the case of this // activity,
		 * the Up button is shown. Use NavUtils to allow users // to navigate up
		 * one level in the application structure. For // more details, see the
		 * Navigation pattern on Android Design: // //
		 * http://developer.android.com
		 * /design/patterns/navigation.html#up-vs-back //
		 * NavUtils.navigateUpFromSameTask(this); return true; }
		 */
		return super.onOptionsItemSelected(item);
	}

	/**
	 * ��ȡ����ͼƬ�ļ���·��
	 * @return
	 */
	private Uri getOutputUri() {
		/*File fileStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"ThanksGiving");
		if (!fileStorageDir.exists()) {
			if (!fileStorageDir.mkdirs()) {
				Log.i("ThanksGiving", "failed to create directory");
				return null;
			}
		}
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File storageFile = new File(fileStorageDir.getPath() + File.separator + "Img_" + timestamp + ".jpg");*/
		
		StringBuffer fileName = new StringBuffer();
		fileName.append(TGUtil.getFilePathProperty("imagePath"));
		fileName.append("/").append(this.userName).append("/").append(new Date().getTime()).append(".jpg");
		return Uri.fromFile(new File(fileName.toString()));
	}
	
	/**
	 * �ı���Ƶ״̬
	 */
	private void toggleAudio()
	{
		//����
		if (this.audioPlayer == null)
		{
			String audioFile = this.getIntent().getExtras().getString(NewAudioMessageActivity.INTENT_AUDIO_FILE);
			if (!TGUtil.isEmpty(audioFile))
			{
				this.audioPlayer = new MediaPlayer();
				try {
					this.audioBtn.setBackgroundResource(R.drawable.mic_next_pause);
	        	
					audioPlayer.setDataSource(audioFile);
					audioPlayer.setOnCompletionListener(audioCompletionListener);
					audioPlayer.prepare();
					audioPlayer.start();
				} catch (IOException e) {
					Log.e("ThanksBook", "���� prepare() ʧ��");
					e.printStackTrace();
				}
			}
		}
		//��ͣ
		else if (this.audioPlayer!=null && this.audioPlayer.isPlaying())
		{
        	audioBtn.setBackgroundResource(R.drawable.mic_next_play);
        	
        	this.audioPlayer.pause();
		}
		//����
		else
		{
        	audioBtn.setBackgroundResource(R.drawable.mic_next_pause);
        	
        	this.audioPlayer.start();
		}
	}
	
	private OnCompletionListener audioCompletionListener = new OnCompletionListener() {
		@Override
		public void onCompletion(android.media.MediaPlayer arg0) 
		{
        	audioBtn.setBackgroundResource(R.drawable.mic_next_play);
        	
        	releasePalyer();
		}
	};
	
	private synchronized void releasePalyer()
	{
		if (audioPlayer!= null && audioPlayer.isPlaying())
		{
			audioPlayer.release();
			audioPlayer = null;
		}
	}

}
