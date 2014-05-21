package com.competition.worldcupv1.asynctasks;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;

import com.competition.worldcupv1.dto.UserDTO;
import com.competition.worldcupv1.webServicehelper.WebServiceHelper;

public class LoginTask extends AsyncTask<Void, Void, String>{
	WebServiceHelper serviceHelper = new WebServiceHelper();
	private UserDTO user;
	public UserDTO getUser() {
		return user;
	}
	public void setUser(UserDTO user) {
		this.user = user;
	}
	private Context context;
	LoginTaskListener loginTaskListener;


	public LoginTaskListener getLoginTaskListener() {
		return loginTaskListener;
	}
	public void setLoginTaskListener(LoginTaskListener loginTaskListener) {
		this.loginTaskListener = loginTaskListener;
	}
	@Override
	protected String doInBackground(Void... params) {
		try {
			return  serviceHelper.login(context, user);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			System.out.println(">>>>>>>>>>>>>>>> e " + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "done";
	}
	@Override
	protected void onPostExecute(String result) {
		loginTaskListener.onComplete(result);
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	public interface LoginTaskListener {
		public void onComplete(String result);
	}

}
