package com.competition.worldcupv1.asynctasks;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;

import com.competition.worldcupv1.service.GameService;
import com.competition.worldcupv1.service.UserService;
import com.competition.worldcupv1.webServicehelper.WebServiceHelper;

public class GetRegistrationIdTask extends AsyncTask<Void, Void, Void>{
	private Context context;
	GetRegistrationIdTaskListener getRegistrationIdTaskListener;
	private String uid;
	private String regisId;
	WebServiceHelper serviceHelper = new WebServiceHelper();
	
	@Override
	protected Void doInBackground(Void... params) {
		try {
			serviceHelper.regisisterGCM(uid, regisId, context);
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
	
	public interface GetRegistrationIdTaskListener {
		public void onComplete(String result);
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public GetRegistrationIdTaskListener getGetRegistrationIdTaskListener() {
		return getRegistrationIdTaskListener;
	}

	public void setGetRegistrationIdTaskListener(
			GetRegistrationIdTaskListener getRegistrationIdTaskListener) {
		this.getRegistrationIdTaskListener = getRegistrationIdTaskListener;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getRegisId() {
		return regisId;
	}

	public void setRegisId(String regisId) {
		this.regisId = regisId;
	}

}
