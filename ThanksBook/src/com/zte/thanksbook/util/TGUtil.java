package com.zte.thanksbook.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 全局工具类
 * @author lonsy
 */
public class TGUtil {

	/**
	 * 显示Toast提示
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
}
