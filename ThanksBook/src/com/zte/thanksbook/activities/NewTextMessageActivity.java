package com.zte.thanksbook.activities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zte.thanksbook.R;

public class NewTextMessageActivity extends Activity implements OnClickListener {

	private static final int TAKEPHOTO = 1;
	private static final int SELECTPICTURE = 2;
	private TextView dateText ;
	private EditText thanksMsg;
	private ImageButton takePhoto;
	private ImageButton selectPicture;
	private Uri currentphoto;
	private List<Uri> photos = new ArrayList<Uri>();
	private static final int PHOTO_NUMBER_ALLOWED = 9;
	
    Calendar calendar = Calendar.getInstance(Locale. CHINA);
    
	private OnDateSetListener dateListener = new OnDateSetListener()
	{
		@Override
		public void onDateSet(DatePicker arg0, int year, int month, int day) 
		{
		calendar.set(Calendar.YEAR,year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		SimpleDateFormat simFormat = new SimpleDateFormat("yyyy-MM-dd" );
		dateText.setText(simFormat.format(calendar .getTime()));
		}
	};

    @Override
	protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_text_message);
		dateText = (TextView)this.findViewById(R.id.dateText);
		int year = calendar .get(Calendar.YEAR);
		int month = calendar .get(Calendar.MONTH+1);
		int day = calendar .get(Calendar.DAY_OF_MONTH);
		dateText.setText(year+"-" +month+"-"+day);
		dateText.setOnClickListener(this);
		thanksMsg = (EditText)this.findViewById(R.id.msg_text);
		thanksMsg.setFocusable(true);
		takePhoto = (ImageButton)this.findViewById(R.id.takePhoto);
		takePhoto.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(photos.size()>=PHOTO_NUMBER_ALLOWED)
				{
					Toast.makeText(NewTextMessageActivity.this, R.string.max_photo_allow_toast, 2).show();
				}
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				currentphoto = getOutputUri();
				intent.putExtra(MediaStore.EXTRA_OUTPUT, currentphoto);
				startActivityForResult(intent,TAKEPHOTO);
			}
		});
		selectPicture = (ImageButton)this.findViewById(R.id.selectPicture);
		selectPicture.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
			}
		});
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
    
    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.publish_message, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
		        return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		String dateStr = dateText.getText().toString();
        String[] dateStrArr = dateStr.split( "-");
        new DatePickerDialog(NewTextMessageActivity.this, dateListener, Integer.parseInt(dateStrArr[0]),
        Integer.parseInt (dateStrArr[1])-1, Integer.parseInt(dateStrArr[2])).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==TAKEPHOTO)
		{
			if(resultCode==Activity.RESULT_OK)
			{
				Uri newImage = currentphoto;
				//Bitmap newBitmap = null;
				/*try
				{
					newBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), newImage);
					
				}catch(Exception e)
				{
					e.printStackTrace();
				}*/
				photos.add(newImage);
				PhotoShowFragment photoFragment = new PhotoShowFragment(photos);
				FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
				transaction.replace(R.id.photoShow, photoFragment);
				transaction.commit();
				
			}else if(resultCode==Activity.RESULT_CANCELED)
			{
				
			}else
			{
				
			}
		}
		
	}
	
	private Uri getOutputUri()
	{
		Uri outputUri = null;
		File fileStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "ThanksGiving");
		if(!fileStorageDir.exists())
		{
			if(!fileStorageDir.mkdirs())
			{
				 Log.i("ThanksGiving", "failed to create directory");
				 return null;
			};
		}
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File storageFile = new File(fileStorageDir.getPath()+File.separator+"Img_"+timestamp+".jpg");
		return Uri.fromFile(storageFile);
	}
	
}
