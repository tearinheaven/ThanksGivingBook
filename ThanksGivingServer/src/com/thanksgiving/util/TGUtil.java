package com.thanksgiving.util;

public class TGUtil {

	public static boolean isEmpty(String str)
	{
		if (str==null || "".equals(str))
		{
			return true;
		}
		return false;
	}
}
