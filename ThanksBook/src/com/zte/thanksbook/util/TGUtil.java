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
 * 全局工具类
 * @author lonsy
 */
public class TGUtil {
	private static Properties properties = new Properties();

	/**
	 * 获取配置文件 config.properties 的内容
	 * @return
	 */
	static {
		try
		{
			InputStream in = TGUtil.class.getClassLoader().getResourceAsStream("config.properties");
			properties.load(in);
		} catch (IOException ex)
		{
			Log.e("ThanksBook", "读取配置文件config.properties错误");
			ex.printStackTrace();
		}
	}

	/**
	 * 显示Toast提示 中间
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
	 * 显示Toast提示 下面
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
	 * 判断字符串是否为空
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
	 * 获取文件路径类型的键值
	 * 设置默认为sd卡
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
					Log.e("ThanksBook", "文件路径创建失败!");
					ex.printStackTrace();
				}
			}
			
			return path.toString();
		}
		
		return null;
	}
	
	/**
	 * 根据键名获取配置文件中对应的键值
	 * @param name
	 * @return
	 */
	public static String getProperty(String name) {
		return properties.getProperty(name);
	}
}
