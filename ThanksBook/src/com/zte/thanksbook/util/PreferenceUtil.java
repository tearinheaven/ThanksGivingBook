package com.zte.thanksbook.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Preference工具类
 * @author Administrator
 *
 */
public class PreferenceUtil {
	private static final String FILE_KEY = "ThanksGiving";
	
	public static final String IS_FIRST_USE = "is_first_use";
	
	public static final String USER_NAME = "user_name";
	
	/**
	 * 获取bool类型的配置值
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean getBooleanPre(Context context, String key, boolean defaultValue)
	{
		SharedPreferences  sharePre = context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE);
		return sharePre.getBoolean(key, defaultValue);
	}
	
	/**
	 * 设置bool类型的配置值
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setBooleanPre(Context context, String key, boolean value)
	{
		SharedPreferences sharedPref = context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	/**
	 * 获取String类型的配置值
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getStringPre(Context context, String key, String defaultValue)
	{
		SharedPreferences  sharePre = context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE);
		return sharePre.getString(key, defaultValue);
	}
	
	/**
	 * 设置String类型的配置值
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setStringPre(Context context, String key, String value)
	{
		SharedPreferences sharedPref = context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(key, value);
		editor.commit();
	}
}
