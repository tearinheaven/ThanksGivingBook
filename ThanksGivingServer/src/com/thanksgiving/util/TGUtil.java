package com.thanksgiving.util;

public class TGUtil {

	public static boolean isEmpty(String str)
	{
		if (str==null || str.length()<1)
		{
			return true;
		}
		return false;
	}
}
