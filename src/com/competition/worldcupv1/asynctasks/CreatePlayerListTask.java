package com.competition.worldcupv1.asynctasks;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;

import com.competition.worldcupv1.dto.PlayerDTO;
import com.competition.worldcupv1.webServicehelper.WebServiceHelper;

public class CreatePlayerListTask  extends AsyncTask<Void, Void, List<PlayerDTO>>{
	WebServiceHelper serviceHelper = new WebServiceHelper();
	private Context context;	
	CreatePlayerListTaskListener createPlayerListTaskListener;
	private int team1Id;
	private int team2Id;
	
	@Override
	protected List<PlayerDTO> doInBackground(Void... params) {
		try {
			return  serviceHelper.getAllPlayers(team1Id, team2Id);
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
	
	public interface CreatePlayerListTaskListener {
		public void onComplete(List<PlayerDTO> result);
	}
	
	
	@Override
	protected void onPostExecute(List<PlayerDTO> result) {
		createPlayerListTaskListener.onComplete(result);
	}
	@Override
	protected void onPreExecute() {
		//mListener.onStart();
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
	public CreatePlayerListTaskListener getCreatePlayerListTaskListener() {
		return createPlayerListTaskListener;
	}
	public void setCreatePlayerListTaskListener(
			CreatePlayerListTaskListener createPlayerListTaskListener) {
		this.createPlayerListTaskListener = createPlayerListTaskListener;
	}
	public int getTeam1Id() {
		return team1Id;
	}
	public void setTeam1Id(int team1Id) {
		this.team1Id = team1Id;
	}
	public int getTeam2Id() {
		return team2Id;
	}
	public void setTeam2Id(int team2Id) {
		this.team2Id = team2Id;
	}

}
