package com.competition.worldcupv1.asynctasks;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.competition.worldcupv1.asynctasks.CreateUserTask.CreateUserTaskListener;
import com.competition.worldcupv1.dto.UserDTO;
import com.competition.worldcupv1.webServicehelper.WebServiceHelper;

import android.content.Context;
import android.os.AsyncTask;

public class CheckUserNameTask extends AsyncTask<Void, Void, String>{
	WebServiceHelper serviceHelper = new WebServiceHelper();
	private UserDTO user;
	private Context context;
	CheckUserNameTaskListener checkUserNameTaskListener;
	
	public void setCheckUserNameTaskListener(CheckUserNameTaskListener checkUserNameTaskListener) {
		this.checkUserNameTaskListener = checkUserNameTaskListener;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		try {
			return  serviceHelper.checkUserName(context, user);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "done";
	}
	
	public interface CheckUserNameTaskListener {
		public void onComplete(String result);
	}
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}	

	@Override
	protected void onPostExecute(String result) {
		checkUserNameTaskListener.onComplete(result);
	}
	@Override
	protected void onPreExecute() {
		//mListener.onStart();
	}
}
