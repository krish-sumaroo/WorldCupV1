package com.competition.worldcupv1.asynctasks;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;


import com.competition.worldcupv1.dto.TeamDTO;
import com.competition.worldcupv1.webServicehelper.WebServiceHelper;

import android.content.Context;
import android.os.AsyncTask;


public class GetTeamPlayerListTask extends AsyncTask<Void, Void,  List<TeamDTO>> {
	WebServiceHelper serviceHelper = new WebServiceHelper();
	private GetTeamPlayerListTaskListener mListener;	
	private Context context;

	@Override
	protected List<TeamDTO> doInBackground(Void... params) {
		try {
			return  serviceHelper.extractTeamPlayersData();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			cancel(false);
		} catch (IOException e) {
			e.printStackTrace();
			cancel(false);
		} catch (Throwable e) {
			e.printStackTrace();
			cancel(false);
		}
		return null;
	}
	
	
	
	@Override
	protected void onPostExecute(List<TeamDTO> result) {
		mListener.onComplete(result);
	}

	@Override
	protected void onPreExecute() {
		mListener.onStart();
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		mListener.onError("An error has occurred. Please try again later.");		
	}
	
	
	public interface GetTeamPlayerListTaskListener {

		public void onComplete(List<TeamDTO> result);

		public void onError(String message);

		public void onStart();

	}


	public void setmListener( GetTeamPlayerListTaskListener mListener) {
		this.mListener = mListener;
		
	}
	

	/**
	 * @param context the context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}

}

