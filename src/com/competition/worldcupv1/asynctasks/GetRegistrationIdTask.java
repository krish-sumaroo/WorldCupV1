package com.competition.worldcupv1.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.competition.worldcupv1.service.GameService;

public class GetRegistrationIdTask extends AsyncTask<Void, Void, Void>{
	private Context context;
	GetRegistrationIdTaskListener getRegistrationIdTaskListener;
	GameService gameService = new GameService();
	
	@Override
	protected Void doInBackground(Void... params) {
		gameService.registerId(context);
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

}
