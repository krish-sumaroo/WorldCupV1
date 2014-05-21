package com.competition.worldcupv1.asynctasks;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import com.competition.worldcupv1.dto.UserDTO;
import com.competition.worldcupv1.webServicehelper.WebServiceHelper;
import android.content.Context;
import android.os.AsyncTask;

public class CreateUserTask extends AsyncTask<Void, Void, String>{
	WebServiceHelper serviceHelper = new WebServiceHelper();
	private UserDTO user;
	private Context context;
	CreateUserTaskListener createUserTaskListener;
	
	public void setcreateUserTaskListener(CreateUserTaskListener createUserTaskListener) {
		this.createUserTaskListener = createUserTaskListener;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		try {
			return  serviceHelper.createUser(context, user);
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
	
	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	@Override
	protected void onPostExecute(String result) {
		createUserTaskListener.onComplete(result);
	}

	@Override
	protected void onPreExecute() {
		//mListener.onStart();
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	public interface CreateUserTaskListener {
		public void onComplete(String result);
	}
}
