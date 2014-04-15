package com.zte.thanksbook.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * ȫ�ֹ�����
 * @author lonsy
 */
public class TGUtil {
	private static Properties properties = new Properties();

	/**
	 * ��ȡ�����ļ� config.properties ������
	 * @return
	 */
	static {
		try
		{
			InputStream in = TGUtil.class.getClassLoader().getResourceAsStream("config.properties");
			properties.load(in);
		} catch (IOException ex)
		{
			Log.e("ThanksBook", "��ȡ�����ļ�config.properties����");
			ex.printStackTrace();
		}
	}

	/**
	 * ��ʾToast��ʾ �м�
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void showToast(Context context, String text, int duration) 
	{		
		Toast toast = Toast.makeText(context, text, duration);
		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}

	/**
	 * ��ʾToast��ʾ ����
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void showToastBottom(Context context, String text, int duration) 
	{		
		Toast toast = Toast.makeText(context, text, duration);
		toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 100);
		toast.show();
	}
	
	/**
	 * �ж��ַ����Ƿ�Ϊ��
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str)
	{
		if (str==null || "".equals(str))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * ��ȡ�ļ�·�����͵ļ�ֵ
	 * ����Ĭ��Ϊsd��
	 * @param name
	 * @return
	 * @throws IOException 
	 */
	public static String getFilePathProperty(String name) {		
		String fileBasePath = getProperty("fileBasePath");
		if (!isEmpty(fileBasePath))
		{
			StringBuffer path = new StringBuffer();
			String xpath = getProperty(name);
			String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
			path.append(sdPath).append(fileBasePath).append(xpath);
			File file = new File(path.toString());
			if (!file.exists())
			{
				try
				{
					file.mkdirs();
				} catch (Exception ex)
				{
					Log.e("ThanksBook", path.toString());
					Log.e("ThanksBook", "�ļ�·������ʧ��!");
					ex.printStackTrace();
				}
			}
			
			return path.toString();
		}
		
		return null;
	}
	
	/**
	 * ���ݼ�����ȡ�����ļ��ж�Ӧ�ļ�ֵ
	 * @param name
	 * @return
	 */
	public static String getProperty(String name) {
		return properties.getProperty(name);
	}
}
