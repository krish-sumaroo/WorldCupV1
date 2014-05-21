package com.competition.worldcupv1.asynctasks;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;

import com.competition.worldcupv1.dto.PlayerDTO;
import com.competition.worldcupv1.dto.TeamDTO;
import com.competition.worldcupv1.webServicehelper.WebServiceHelper;

public class CreateTeamListTask extends AsyncTask<Void, Void, List<TeamDTO>>{
	
	WebServiceHelper serviceHelper = new WebServiceHelper();
	private Context context;	
	CreateTeamListTaskListener createTeamListTaskListener;
	
	
	@Override
	protected List<TeamDTO> doInBackground(Void... params) {
		try {
			return  serviceHelper.getAllTeam();
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
	public interface CreateTeamListTaskListener {
		public void onComplete(List<TeamDTO> result);
	}

	public WebServiceHelper getServiceHelper() {
		return serviceHelper;
	}
	public void setServiceHelper(WebServiceHelper serviceHelper) {
		this.serviceHelper = serviceHelper;
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public CreateTeamListTaskListener getCreateTeamListTaskListener() {
		return createTeamListTaskListener;
	}
	public void setCreateTeamListTaskListener(
			CreateTeamListTaskListener createTeamListTaskListener) {
		this.createTeamListTaskListener = createTeamListTaskListener;
	}
		
	
	@Override
	protected void onPostExecute(List<TeamDTO> result) {
		createTeamListTaskListener.onComplete(result);
	}
	@Override
	protected void onPreExecute() {
		//mListener.onStart();
	}
}
