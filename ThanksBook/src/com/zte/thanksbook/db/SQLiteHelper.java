package com.zte.thanksbook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "ThanksGiving.db";
	
	private static final int DATABASE_VERSION = 1;
	
	private String tableCreateSql;

    public SQLiteHelper(Context context,String tableCreateSql) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.tableCreateSql = tableCreateSql;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tableCreateSql);
        /*String[] names = MainActivity.resources.getStringArray(R.array.activity_names);
        String[] types = MainActivity.resources.getStringArray(R.array.activity_types);
        for (int i=0; i<names.length; i++)
        {
            db.execSQL("insert into activitys(activity_id, activity_name, activity_type) " +
            		"values(" + i + ", '" + names[i] + "'," + types[i] + ")");
        }*/
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
        /*String sql = "alter table person add sex varchar(8)";  
        db.execSQL(sql); */
	}

}
