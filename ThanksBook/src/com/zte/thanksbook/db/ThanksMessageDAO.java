package com.zte.thanksbook.db;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.zte.thanksbook.entity.ThanksMessageEntity;

/**
 * 感恩信息数据库类
 * @author huangjianxin
 * @since 20140310
 */
public class ThanksMessageDAO {
	
	private SQLiteHelper helper;
	private SQLiteDatabase db;
	private Context context;
	private String[] projection = {"id","msg_type","message_text","create_date"};
	private static final String TABLE_NAME_MSG = "ts_thanks_message";
	private static final String TABLE_NAME_IMG = "ts_thanks_img";
	
	private static final String INSERT_MESSAGE = "insert into "+TABLE_NAME_MSG+" (message_text,enable_flag,status,create_by,thank_to,msg_type)"
			 									+" values (?,'Y',?,?,?,?)";
	private static final String INSERT_IMG = "insert into "+TABLE_NAME_IMG+"(belong_to,original_img,thumbnail,enable_flag)"
			 								+" values (?,?,?,'Y')";
	
	public ThanksMessageDAO(Context context)
	{
		this.context = context;
		helper = new SQLiteHelper(context);
		db = helper.getWritableDatabase();
	}
	
	/**
	 * 添加文本类感恩信息
	 * @param message
	 * @return
	 */
	public boolean addThanksMessage(ThanksMessageEntity msg)
	{
		boolean result = false;
		SQLiteStatement stat = null;
		db.beginTransaction();
		stat = addText(msg);
		Long msgId = stat.executeInsert();
		
		List<Uri> imgs =msg.getImgs();
		if(imgs!=null&&imgs.size()>0)
		{
			stat = db.compileStatement(INSERT_IMG);
			for(Uri uri:imgs)
			{
				try
				{
					stat = addImg(stat,msgId,uri);
				}catch(IOException e)
				{
					e.printStackTrace();
					return result;
				}
				stat.executeInsert();
			}
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		result = true;
		return result;
	}
	
	/**
	 * 添加语音类感恩信息
	 * @param msg
	 * @return
	 */
	public boolean addThanksVideo(Long msgId,SQLiteStatement stat)
	{
		boolean result = false;
		
		
		return result;
	}
	
	/**
	 * 添加文本
	 * @param text
	 * @return
	 */
	private SQLiteStatement addText(ThanksMessageEntity text)
	{
		SQLiteStatement stat = db.compileStatement(INSERT_MESSAGE);
		stat.bindString(1, text.getMessageText());
		stat.bindString(2, "unSYN");
		stat.bindLong(3, text.getCreateBy());
		stat.bindString(4, text.getThanksTo());
		stat.bindString(5, text.getMessageType());
		return stat;
	}
	
	/**
	 * 添加图片
	 * @param stat
	 * @param msgId
	 * @param img
	 * @return
	 * @throws IOException
	 */
	private SQLiteStatement addImg (SQLiteStatement stat,Long msgId,Uri img)  throws IOException
	{
		stat.clearBindings();
		String imgPath = img.getSchemeSpecificPart();
		stat.bindLong(1, msgId);
		stat.bindString(2, imgPath);
		stat.bindString(3, imgPath);//缩略图待处理
		return stat;
	}
	
	/**
	 * 添加语音
	 * @param stat
	 * @param msgId
	 * @param vedio
	 * @return
	 * @throws IOException
	 */
	private SQLiteStatement addVedio(SQLiteStatement stat,Long msgId,Uri vedio) throws IOException
	{
		
		return stat;
	}
	
	/**
	 * 感恩数据查询
	 * @return Cursor
	 */
	public Cursor queryThanksMessages()
	{
		return this.db.query(TABLE_NAME_MSG, projection, null, null, null, null, "create_date desc");
	}
	
	/**
	 * 获取特定感恩信息的图片
	 * @param msgId
	 * @return
	 */
	public List<Long> queryImagesByMsgId(long msgId)
	{
		List<Long> images = new ArrayList<Long>();
		Cursor imgIds = this.db.rawQuery("select id from "+ TABLE_NAME_IMG +" img where img.belong_to = ?", new String[]{msgId+""});
		while(imgIds.moveToNext())
		{
			Long imgId = imgIds.getLong(imgIds.getColumnIndex("id"));
			images.add(imgId);
		}
		return images;
	}
	
	public String queryThumbnailById(long imgId)
	{
		String imgPath = null;
		Cursor img = this.db.rawQuery("select thumbnail from "+TABLE_NAME_IMG+" img where img.id = ?", new String[]{imgId+""});
		if(img.moveToFirst())
		{
			imgPath = img.getString(img.getColumnIndex("thumbnail"));
		}
		return imgPath;
	}
	
	

}
