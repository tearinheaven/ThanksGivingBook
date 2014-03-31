package com.zte.thanksbook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zte.thanksbook.entity.User;

public class UserDao {
    public static final String USER_TABLE_NAME = "ts_user";
    public static final String USER_TABLE_CREATE =
                "CREATE TABLE " + USER_TABLE_NAME + " (" +
                " user_id integer primary key, " +
                " user_name text, user_email text, user_signature text, last_update_date TimeStamp NOT NULL );";
	
	public static void addUser(Context context, User user)
	{
		SQLiteHelper dbHelper = new SQLiteHelper(context);
		
		SQLiteDatabase database = null;  
        try {  
            database = dbHelper.getWritableDatabase();
            ContentValues cv=new ContentValues();              
            cv.put("user_id", user.getUserId());             
            cv.put("user_email", user.getUserEmail());             
            cv.put("user_name", user.getUserName());             
            cv.put("user_signature", user.getUserSignature());             
            cv.put("last_update_date", user.getLastUpdateDateString());  
            database.insert(USER_TABLE_NAME, null, cv);
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if(database != null){  
                database.close();  
            }            
        }
	}
}
