package com.zte.thanksbook.db;

import android.database.sqlite.SQLiteDatabase;

public class ThanksImgDAO {

	private SQLiteHelper helper;
	private SQLiteDatabase db;
	
	private static final String projection[] = {"_id","message_text","message_img","create_by","thank_to","create_date"};
	
	private static final String USER_TABLE_NAME = "ts_thanks_img";
	private static final String CREATE_THANKS_IMG = "create table "+USER_TABLE_NAME+"(" +
			"id INTEGER PRIMARY KEY AUTOINCREMENT,"+"belong_to INTEGER,"+"original_img blob," +"thumbnail blob," +
			"enable_flag char(1),create_date DATETIME,last_update_date DATETIME);";
	
	public ThanksImgDAO()
	{
		helper = new SQLiteHelper(null,CREATE_THANKS_IMG);
		db = helper.getWritableDatabase();
	}
	
	
	
}
