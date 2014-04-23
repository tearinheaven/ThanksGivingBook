package com.zte.thanksbook.db;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.zte.thanksbook.entity.ThanksMessageEntity;

/**
 * �ж���Ϣ���ݿ���
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
	 * ����ı���ж���Ϣ
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
	 * ���������ж���Ϣ
	 * @param msg
	 * @return
	 */
	public boolean addThanksVideo(Long msgId,SQLiteStatement stat)
	{
		boolean result = false;
		
		
		return result;
	}
	
	/**
	 * ����ı�
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
	 * ���ͼƬ
	 * @param stat
	 * @param msgId
	 * @param img
	 * @return
	 * @throws IOException
	 */
	private SQLiteStatement addImg (SQLiteStatement stat,Long msgId,Uri img)  throws IOException
	{
		stat.clearBindings();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Bitmap map = MediaStore.Images.Media.getBitmap(context.getContentResolver(), img);
		map.compress(Bitmap.CompressFormat.PNG, 80, os);
		byte[] imgByte = os.toByteArray();
		stat.bindLong(1, msgId);
		stat.bindBlob(2, imgByte);
		stat.bindBlob(3, imgByte);//����ͼ������
		return stat;
	}
	
	/**
	 * �������
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
	 * �ж����ݲ�ѯ
	 * @return Cursor
	 */
	public Cursor queryThanksMessages()
	{
		return this.db.query(TABLE_NAME_MSG, projection, null, null, null, null, "create_date desc");
	}

}
