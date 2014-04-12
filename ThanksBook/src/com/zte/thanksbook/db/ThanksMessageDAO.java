package com.zte.thanksbook.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zte.thanksbook.entity.ThanksMessageEntity;

/**
 * 感恩信息数据库类
 * @author huangjianxin
 * @since 20140310
 */
public class ThanksMessageDAO {
	
	private SQLiteHelper helper;
	private SQLiteDatabase db;
	
	private static final String projection[] = {"_id","message_text","message_img","create_by","thank_to","create_date"};
	
	private static final String USER_TABLE_NAME = "ts_thanks_message";
	private static final String CREATE_THANKS_MESSAGE = "create table "+USER_TABLE_NAME+"(" +
			"id INTEGER PRIMARY KEY AUTOINCREMENT,"+"message_text TEXT,"+"enable_flag char(1)," +"status varchar(10)," +
			"create_by INTEGER,thank_to TEXT,create_date DATETIME,last_update_date DATETIME);";
	
	public ThanksMessageDAO()
	{
		helper = new SQLiteHelper(null,CREATE_THANKS_MESSAGE);
		db = helper.getWritableDatabase();
	}
	
	public void addThanksMessage(ThanksMessageEntity message)
	{
		
	}
	/**
	 * 感恩数据查询
	 * @return Cursor
	 */
	public Cursor queryThanksMessages()
	{
		return this.db.query("thanks_message", projection, null, null, null, null, "create_date desc");
	}

}
