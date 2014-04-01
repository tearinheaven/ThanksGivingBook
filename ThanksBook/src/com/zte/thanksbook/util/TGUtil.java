package com.zte.thanksbook.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * ȫ�ֹ�����
 * @author lonsy
 */
public class TGUtil {

	/**
	 * ��ʾToast��ʾ
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
}
