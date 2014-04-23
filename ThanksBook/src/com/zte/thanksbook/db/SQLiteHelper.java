package com.zte.thanksbook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "ThanksGiving.db";
	
	private static final int DATABASE_VERSION = 1;
	
	//table names
	private static final String TABLE_NAME_MSG = "ts_thanks_message";
	private static final String TABLE_NAME_IMG = "ts_thanks_img";
	private static final String TABLE_NAME_USER = "ts_user";
	
	//create tables
	private static final String CREATE_THANKS_MESSAGE = "create table "+TABLE_NAME_MSG+"(" +
			"id INTEGER PRIMARY KEY AUTOINCREMENT,message_text TEXT,enable_flag char(1),status varchar(10)," +
			"create_by INTEGER,thank_to TEXT,create_date TimeStamp NOT NULL DEFAULT (datetime('now','localtime')),"+
			"last_update_date TimeStamp NOT NULL DEFAULT (datetime('now','localtime')),msg_type varchar(10));";
	private static final String CREATE_THANKS_IMG = "create table "+TABLE_NAME_IMG+"(" +
			"id INTEGER PRIMARY KEY AUTOINCREMENT,belong_to INTEGER,original_img blob,thumbnail blob," +
			"enable_flag char(1),create_date TimeStamp NOT NULL DEFAULT (datetime('now','localtime')),"+
			"last_update_date TimeStamp NOT NULL DEFAULT (datetime('now','localtime')));";
	private static final String USER_TABLE_CREATE =
             "CREATE TABLE " + TABLE_NAME_USER + " (" +
             " user_id integer primary key, " +
             " user_name text, user_email text, user_signature text, last_update_date TimeStamp NOT NULL );";
	
	//drop tables
	private static final String DROP_THANKS_MESSAGE = "DROP TABLE IF EXISTS "+TABLE_NAME_MSG;
	private static final String DROP_THANKS_IMG = "DROP TABLE IF EXISTS "+TABLE_NAME_IMG;
	private static final String DROP_USER = "DROP TABLE IF EXISTS "+TABLE_NAME_USER;
	
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	Log.i("start", "crate tables");
    	db.execSQL(CREATE_THANKS_MESSAGE);
    	db.execSQL(CREATE_THANKS_IMG);
    	db.execSQL(USER_TABLE_CREATE);
    	Log.i("end", "crate tables");
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//这样每次都会把所有表删除，重新创建，要判断新老版本的差别
		/*db.execSQL(DROP_THANKS_MESSAGE);
		db.execSQL(DROP_THANKS_IMG);
		db.execSQL(DROP_USER);
		this.onCreate(db);*/
	}

}
