package com.zte.thanksbook.activities;

import java.io.File;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zte.thanksbook.R;
import com.zte.thanksbook.util.PreferenceUtil;
import com.zte.thanksbook.util.TGUtil;

public class NewAudioMessageNextActivity extends Activity {
	private static final int TAKE_PHOTO = 1;

	private static final int PICK_PHOTO = 2;
	
	private static final int CROP_PHOTO = 3;
	
	/**
	 * 当前操作 照相、选择照片、裁剪照片
	 */
	private int currentAction;

	/**
	 * 当前照片URI
	 */
	private Uri currentphoto = null;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_audio_message_next);

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
						tmpphoto = getOutputUri();
						currentAction = TAKE_PHOTO;
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, tmpphoto);

						startActivityForResult(intent, TAKE_PHOTO);
					}
				});
	}

	// 拍照或者选择照片结束 处理照片
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v("ThanksBook", "result " + resultCode);
		if (resultCode == Activity.RESULT_CANCELED)
		{
			//如果是在裁剪照片的时候取消，则删除临时照片
			if (this.currentAction == CROP_PHOTO)
			{
				File file = new File(this.tmpphoto.getPath());
				if (file.exists())
				{
					file.delete();
				}
				this.tmpphoto = null;
			}
			
			this.currentAction = 0;
		}
		else if (resultCode == Activity.RESULT_OK)
		{		
			if (requestCode == TAKE_PHOTO) {
				cropImageUri(this.tmpphoto);
			}
			else if (requestCode == PICK_PHOTO)
			{
				
			}
			else if (requestCode == CROP_PHOTO)
			{
				if(this.tmpphoto != null){
					//如果当前存在照片，则删除
					if (this.currentphoto != null)
					{
						//this.imageBtn.setBackgroundColor(Color.rgb(230, 230, 230));
						File file = new File(this.currentphoto.getPath());
						if (file.exists())
						{
							file.delete();
						}
						this.currentphoto = null;
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
					this.currentphoto = this.tmpphoto;
					this.tmpphoto = null;
					this.currentAction = 0;
				}
			}
		}

	}

	private void cropImageUri(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", this.rateWidth);
		intent.putExtra("aspectY", this.rateHeight);
		/*intent.putExtra("outputX", this.cropWidth);
		intent.putExtra("outputY", this.cropHeight);*/
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
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

}
