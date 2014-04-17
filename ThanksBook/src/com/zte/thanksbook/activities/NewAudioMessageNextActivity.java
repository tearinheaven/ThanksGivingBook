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
	 * 当前操作 照相、选择照片、裁剪照片
	 */
	private int currentAction;

	/**
	 * 当前照片URI
	 */
	private Uri currentPhoto = null;

	/**
	 * 上一张URI
	 */
	private Uri tmpphoto = null;

	/**
	 * 照片裁剪参数
	 */
	private final int rateWidth = 1;
	private final int rateHeight = 1;
	/*private final int cropWidth = 250;
	private final int cropHeight = 250;*/
	
	/**
	 * 用户名
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
	 * 录音播放工具实例
	 */
	private MediaPlayer audioPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_audio_message_next);
		this.intent = this.getIntent();

		// 标题栏设置
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setDisplayShowCustomEnabled(true);
		bar.setCustomView(R.layout.actionbar_layout_child);
		// 返回按钮
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
		((TextView) this.findViewById(R.id.actionbar_title)).setText("新建感恩");

		// 用户名初始化
		this.userName = PreferenceUtil.getStringPre(this,
				PreferenceUtil.USER_NAME, null);

		this.imageBtn = (ImageButton)this.findViewById(R.id.audio_message_btn);
		this.imageTip = (TextView)this.findViewById(R.id.audio_message_tip);
		// 添加照片		
		this.imageBtn.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String[] strs = new String[] {"拍摄", "相册", "取消"};
						if (currentPhoto != null)
						{
							strs = new String[] {"拍摄", "相册", "删除照片"};
						}
						ThreeButtonShadowFragment shadow = new ThreeButtonShadowFragment(strs, chooseLinstener);
						FragmentManager fragManager = getFragmentManager();
				        FragmentTransaction tran = fragManager.beginTransaction();
				        tran.replace(R.id.audio_shadow, shadow);
				        tran.addToBackStack(null);
				        tran.commit();
					}
				});
		
		//播放与暂停
		this.audioBtn = (Button)this.findViewById(R.id.audio_message_play_btn);
		this.audioBtn.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						toggleAudio();
					}
				});
		
		//初始化数据
		Bundle bundle = this.intent.getExtras();
		if (!TGUtil.isEmpty(bundle.getString(INTENT_PHOTO_URI)))
		{
			Log.v("ThanksBook", "初始化图片");
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
	
	//添加照片事件
	private ThanksShadowLinstener chooseLinstener = new ThanksShadowLinstener() {
		//拍摄
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

		//相册
		@Override
		public void subAction() {
			toggleAudio();
			
			currentAction = PICK_PHOTO;
			Intent intent = new Intent();  
	        intent.setAction(Intent.ACTION_PICK);  
	        intent.setType("image/*");  
	        startActivityForResult(intent, PICK_PHOTO);
		}

		//删除照片
		@Override
		public void cancelAction() {
			//如果当前存在照片，则删除
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
				imageTip.setText("点击添加照片");
			}
		}
	};

	// 拍照或者选择照片结束 处理照片
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v("ThanksBook", "result " + resultCode);
		if (resultCode == Activity.RESULT_CANCELED)
		{
			//如果是在裁剪照片的时候取消，则删除临时照片
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
					//如果当前存在照片，则删除
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
					
					//填充临时照片为当前照片
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
	 * 获取保存图片文件的路径
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
	 * 改变音频状态
	 */
	private void toggleAudio()
	{
		//播放
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
					Log.e("ThanksBook", "播放 prepare() 失败");
					e.printStackTrace();
				}
			}
		}
		//暂停
		else if (this.audioPlayer!=null && this.audioPlayer.isPlaying())
		{
        	audioBtn.setBackgroundResource(R.drawable.mic_next_play);
        	
        	this.audioPlayer.pause();
		}
		//继续
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
