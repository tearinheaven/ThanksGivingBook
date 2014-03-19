package com.zte.thanksbook.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zte.thanksbook.R;

public class NewTextMessageActivity extends Activity implements OnClickListener {

	private static final int TAKEPHOTO = 1;
	private static final int SELECTPICTURE = 2;
	private TextView dateText ;
	private EditText thanksMsg;
	private ImageButton takePhoto;
	private ImageButton selectPicture;
	
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
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent,TAKEPHOTO);
			}
		});
		selectPicture = (ImageButton)this.findViewById(R.id.selectPicture);
		selectPicture.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
			}
		});
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
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	
}
