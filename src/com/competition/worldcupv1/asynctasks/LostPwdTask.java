package com.competition.worldcupv1.asynctasks;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.competition.worldcupv1.asynctasks.CreateUserTask.CreateUserTaskListener;
import com.competition.worldcupv1.dto.UserDTO;
import com.competition.worldcupv1.webServicehelper.WebServiceHelper;

import android.content.Context;
import android.os.AsyncTask;

public class LostPwdTask extends AsyncTask<Void, Void, String>{
	WebServiceHelper serviceHelper = new WebServiceHelper();
	private String email;
	private Context context;
	CreateLostPwdTaskListener createLostPwdTaskListener;
	
	
	@Override
	protected String doInBackground(Void... params) {
		try {
			return  serviceHelper.lostPassword(email, context);
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
		return null;
	}
	

	@Override
	protected void onPostExecute(String result) {
		createLostPwdTaskListener.onComplete(result);
	}

	@Override
	protected void onPreExecute() {
		//mListener.onStart();
	}
	
	
	public interface CreateLostPwdTaskListener {
		public void onComplete(String result);
	}
	public WebServiceHelper getServiceHelper() {
		return serviceHelper;
	}
	public void setServiceHelper(WebServiceHelper serviceHelper) {
		this.serviceHelper = serviceHelper;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public CreateLostPwdTaskListener getCreateLostPwdTaskListener() {
		return createLostPwdTaskListener;
	}
	public void setCreateLostPwdTaskListener(
			CreateLostPwdTaskListener createLostPwdTaskListener) {
		this.createLostPwdTaskListener = createLostPwdTaskListener;
	}
	
	
}
