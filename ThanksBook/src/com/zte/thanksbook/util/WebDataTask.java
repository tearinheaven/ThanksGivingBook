package com.zte.thanksbook.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Web数据访问工具类
 * @author lonsy
 */
public class WebDataTask extends AsyncTask<String, Void, String> {
	private WebDataProcessListener caller;

	public WebDataTask(WebDataProcessListener caller) {
		super();
		this.caller = caller;
	}

	@Override
	protected String doInBackground(String... urls) {
		try {
			return downloadUrl(urls[0]);
		} catch (IOException e) {
			e.printStackTrace();
			return "0";
		}
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result) {
		this.caller.onPostExecute(result);
	}

	private String downloadUrl(String myurl) throws IOException {
		InputStream is = null;

		try {
			URL url = new URL(myurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			if (conn.getResponseCode() == 200) {
				is = conn.getInputStream();

				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					bout.write(buffer, 0, len);
				}
				bout.close();
				return bout.toString();
			}
			else
			{
				throw new IOException();
			}

			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} 
		finally {
			if (is != null) {
				is.close();
			}
		}
	}
}
