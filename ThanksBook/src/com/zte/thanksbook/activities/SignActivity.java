package com.zte.thanksbook.activities;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.zte.thanksbook.R;
import com.zte.thanksbook.entity.User;
import com.zte.thanksbook.util.MD5Util;
import com.zte.thanksbook.util.WebDataProcessListener;
import com.zte.thanksbook.util.WebDataTask;

public class SignActivity extends Activity implements WebDataProcessListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setDisplayShowCustomEnabled(true);
		bar.setCustomView(R.layout.actionbar_layout_child);
		((TextView) this.findViewById(R.id.actionbar_title)).setText("注册");
		this.findViewById(R.id.actionbar_menu_back).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						SignActivity.this.finish();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.sign, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void signIn(View view) {
		// 邮件地址
		EditText mailText = (EditText) this.findViewById(R.id.sign_text_mail);
		String mail = mailText.getText().toString();

		String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(mail);
		if (!m.matches() || mail.length() > 200) {
			mailText.setError("请输入正确的邮箱地址");
			return;
		}

		// 密码
		EditText passwordText = (EditText) this
				.findViewById(R.id.sign_text_password);
		String password = passwordText.getText().toString();
		if (password.length() < 6 || password.length() > 14) {
			passwordText.setError(this.getString(R.string.user_password_hint));
		}
		String passwordEncryption = MD5Util.MD5Encode(password);
		this.addUser(mail, passwordEncryption);
	}

	public void addUser(String mail, String password) {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			StringBuffer url = new StringBuffer();
			url.append(
					"http://testasp.vicp.cc/thanksgiving/user?operation=signUp&userEmail=")
					.append(mail).append("&userPassword=").append(password);
			new WebDataTask(this).execute(url.toString());
		} else {

		}
	}

	@Override
	public void onPostExecute(String result) {
		Log.v(null, result);
		Gson gson = new Gson();
		Map<String, Object> rs = gson.fromJson(result, new TypeToken<Map<String, Object>>() {}.getType());
		Log.v(null, rs.get("result").toString());
		//Log.v(null, rs.get("user").toString());
		User user = gson.fromJson(gson.toJson(rs.get("user")), User.class);
		//Map<String, Object> user = gson.fromJson(je, new TypeToken<Map<String, Object>>() {}.getType());
		Log.v(null, user.getUserEmail());
	}

}
