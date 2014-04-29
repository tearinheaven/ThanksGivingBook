package com.zte.thanksbook.activities;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zte.thanksbook.R;
import com.zte.thanksbook.db.UserDao;
import com.zte.thanksbook.entity.User;
import com.zte.thanksbook.service.UserBO;
import com.zte.thanksbook.util.MD5Util;
import com.zte.thanksbook.util.PreferenceUtil;
import com.zte.thanksbook.util.TGUtil;
import com.zte.thanksbook.util.WebDataProcessListener;
import com.zte.thanksbook.util.WebDataTask;

public class SignActivity extends Activity implements WebDataProcessListener, View.OnClickListener {
	public static final int TYPE_SIGN_UP = 1;
	public static final int TYPE_LOGIN_IN = 2;
	
	private int signType = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);
		// Show the Up button in the action bar.

		

		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setDisplayShowCustomEnabled(true);
		bar.setCustomView(R.layout.actionbar_layout_child);
		this.findViewById(R.id.actionbar_menu_back).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						SignActivity.this.finish();
					}
				});
		
		//判断是注册还是登录，并更改对应显示和响应
		String signType = this.getIntent().getExtras().get("signType").toString();
		Log.v(null, signType);
		TextView mailLabel = (TextView)this.findViewById(R.id.sign_mail_label);
		EditText mailText = (EditText)this.findViewById(R.id.sign_mail_text);
		Button confirmBtn = (Button)this.findViewById(R.id.sign_confirm_button);
		TextView title = (TextView) this.findViewById(R.id.actionbar_title);
		if (TGUtil.isEmpty(signType) || TYPE_SIGN_UP==Integer.valueOf(signType))
		{
			//注册
			this.signType = TYPE_SIGN_UP;
			
			mailLabel.setText(this.getResources().getString(R.string.email_address));
			mailText.setHint(this.getResources().getString(R.string.email_address_hint));
			confirmBtn.setText(this.getResources().getString(R.string.title_activity_sign));
			title.setText(this.getResources().getString(R.string.title_activity_sign));
		}
		else
		{
			//登录
			this.signType = TYPE_LOGIN_IN;
			
			mailLabel.setText(this.getResources().getString(R.string.user_account));
			mailText.setHint(this.getResources().getString(R.string.user_account_hint));
			confirmBtn.setText(this.getResources().getString(R.string.title_activity_login));
			title.setText(this.getResources().getString(R.string.title_activity_login));
		}
		
		//注册 登录/注册 的点击事件
		confirmBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if (this.signType == TYPE_SIGN_UP)
		{
			this.signIn(view);
		}
		else if (this.signType == TYPE_LOGIN_IN)
		{
			this.loginIn(view);
		}
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

	/**
	 * 用户注册
	 * @param view
	 */
	public void signIn(View view) {
		// 邮件地址
		EditText mailText = (EditText) this.findViewById(R.id.sign_mail_text);
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
			return;
		}
		String passwordEncryption = MD5Util.MD5Encode(password);

		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			StringBuffer url = new StringBuffer();
			url.append(
					"http://testasp.vicp.cc/thanksgiving/user?operation=signUp&userEmail=")
					.append(mail).append("&userPassword=").append(passwordEncryption);
			new WebDataTask(this).execute(url.toString());
		} else {
			TGUtil.showToast(this, "网络连接不可用", 3000);
		}
	}
	
	/**
	 * 注册 服务器响应处理
	 * @param result
	 */
	private void signInDone(String result)
	{
		Log.v("signIn", result);
		Gson gson = new Gson();
		Map<String, Object> rs = gson.fromJson(result, new TypeToken<Map<String, Object>>() {}.getType());
		Log.v(null, rs.get("result").toString());
		double resultCode = Double.valueOf(rs.get("result").toString());
		if (UserBO.OPERACTION_SUCCESS == resultCode)
		{
			//Log.v(null, rs.get("user").toString());
			User user = gson.fromJson(gson.toJson(rs.get("user")), User.class);
			//Map<String, Object> user = gson.fromJson(je, new TypeToken<Map<String, Object>>() {}.getType());
			Log.v(null, user.getUserEmail());
			Log.v(null, user.getUserId());
			Log.v(null, user.getUserSignature());
			Log.v(null, user.getUserName());
			Log.v(null, user.getLastUpdateDateString());
			UserDao.addUser(this, user);
			
			PreferenceUtil.setBooleanPre(this, PreferenceUtil.IS_FIRST_USE, false);
			PreferenceUtil.setStringPre(this, PreferenceUtil.USER_NAME, user.getUserName());
			
			Intent toMain = new Intent(SignActivity.this ,MainActivity.class);
			startActivity(toMain);
			finish();
		}
		else if (UserBO.USEREMAIL_DUPLICATE == resultCode)
		{
			//((EditText)this.findViewById(R.id.sign_text_mail)).setError("该邮箱已被使用");
			TGUtil.showToast(this, "该邮箱已被使用，请换个邮箱~", 3000);
		}
	}

	/**
	 * 用户登录
	 * @param view
	 */
	public void loginIn(View view) {
		// 帐号
		EditText accountText = (EditText) this.findViewById(R.id.sign_mail_text);
		String account = accountText.getText().toString();
		if (TGUtil.isEmpty(account) || account.length() < 5)
		{
			accountText.setError("帐号至少为5个字符");
			return;
		}

		// 密码
		EditText passwordText = (EditText) this
				.findViewById(R.id.sign_text_password);
		String password = passwordText.getText().toString();
		if (password.length() < 6 || password.length() > 14) {
			passwordText.setError(this.getString(R.string.user_password_hint));
			return;
		}
		String passwordEncryption = MD5Util.MD5Encode(password);
		
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			StringBuffer url = new StringBuffer();
			url.append(
					"http://testasp.vicp.cc/thanksgiving/user?operation=loginIn&userAccount=")
					.append(account).append("&userPassword=").append(passwordEncryption);
			
			Log.v(null, url.toString());
			
			new WebDataTask(this).execute(url.toString());
		} else {
			TGUtil.showToast(this, "网络连接不可用", 3000);
		}
	}
	
	/**
	 * 登录 服务器响应处理
	 * @param result
	 */
	private void loginInDone(String result)
	{
		Log.v(null, result);
		Gson gson = new Gson();
		Map<String, Object> rs = gson.fromJson(result, new TypeToken<Map<String, Object>>() {}.getType());
		//Log.v(null, rs.get("result").toString());
		double resultCode = Double.valueOf(rs.get("result").toString());
		if (UserBO.OPERACTION_SUCCESS == resultCode)
		{
			//Log.v(null, rs.get("user").toString());
			User user = gson.fromJson(gson.toJson(rs.get("user")), User.class);
			//Map<String, Object> user = gson.fromJson(je, new TypeToken<Map<String, Object>>() {}.getType());
			Log.v(null, user.getUserEmail());
			Log.v(null, user.getUserId());
			Log.v(null, user.getUserSignature());
			Log.v(null, user.getUserName());
			Log.v(null, user.getLastUpdateDateString());
			UserDao.addUser(this, user);
			
			PreferenceUtil.setBooleanPre(this, PreferenceUtil.IS_FIRST_USE, false);
			PreferenceUtil.setStringPre(this, PreferenceUtil.USER_NAME, user.getUserName());
			
			Intent toMain = new Intent(SignActivity.this ,MainActivity.class);
			startActivity(toMain);
			finish();
		}
		else if (UserBO.OPERACTION_FAILURE == resultCode)
		{
			//((EditText)this.findViewById(R.id.sign_text_mail)).setError("该邮箱已被使用");
			TGUtil.showToast(this, "帐号或密码错误，请重新输入", 3000);
		}
	}

	/**
	 * WebDataTask响应结束回调函数
	 */
	@Override
	public void onPostExecute(String result) {

		if (this.signType == TYPE_SIGN_UP)
		{
			this.signInDone(result);
		}
		else if (this.signType == TYPE_LOGIN_IN)
		{
			this.loginInDone(result);
		}
	}

}
