package com.competition.worldcupv1.asynctasks;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.competition.worldcupv1.dto.GameDTO;
import com.competition.worldcupv1.webServicehelper.WebServiceHelper;

import android.content.Context;
import android.os.AsyncTask;

public class CreateGameInfoTask  extends AsyncTask<Void, Void, GameDTO>{
	WebServiceHelper serviceHelper = new WebServiceHelper();
	private Context context;
	CreateGameInfoTaskListener createGameInfoTaskListener;
	
	@Override
	protected GameDTO doInBackground(Void... params) {
		try {
			return  serviceHelper.insertGameInfo(context);
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
	
	public interface CreateGameInfoTaskListener {
		public void onComplete(GameDTO result);
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

	public CreateGameInfoTaskListener getCreateGameInfoTaskListener() {
		return createGameInfoTaskListener;
	}

	public void setCreateGameInfoTaskListener(CreateGameInfoTaskListener createGameInfoTaskListener) {
		this.createGameInfoTaskListener = createGameInfoTaskListener;
	}
	
	@Override
	protected void onPostExecute(GameDTO result) {
		createGameInfoTaskListener.onComplete(result);
	}
	@Override
	protected void onPreExecute() {
		//mListener.onStart();
	}

}
