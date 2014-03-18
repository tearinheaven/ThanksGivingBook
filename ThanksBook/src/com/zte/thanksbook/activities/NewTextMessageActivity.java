package com.zte.thanksbook.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;

import com.zte.thanksbook.R;

public class NewTextMessageActivity extends Activity implements OnClickListener {

	private TextView dateText ;
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
	}

	@Override
	public void onClick(View v) {
		String dateStr = dateText.getText().toString();
        String[] dateStrArr = dateStr.split( "-");
        new DatePickerDialog(NewTextMessageActivity.this, dateListener, Integer.parseInt(dateStrArr[0]),
        Integer.parseInt (dateStrArr[1])-1, Integer.parseInt(dateStrArr[2])).show();
	}
	
}
