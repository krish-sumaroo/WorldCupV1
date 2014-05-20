package com.competition.worldcupv1.asynctasks;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;

import com.competition.worldcupv1.dto.ScorePredictionDTO;
import com.competition.worldcupv1.webServicehelper.WebServiceHelper;

public class AddPredictionTask extends AsyncTask<Void, Void, String>{

	WebServiceHelper serviceHelper = new WebServiceHelper();
	private ScorePredictionDTO scorePredictionDTO;
	private Context context;
	ScorePredicitonListener scorePredicitonListener;
	
	@Override
	protected String doInBackground(Void... params) {
		try {
			return  serviceHelper.addPrediction(context, scorePredictionDTO);
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

	public interface ScorePredicitonListener {
		public void onComplete(String result);
	}
	
	public ScorePredictionDTO getScorePredictionDTO() {
		return scorePredictionDTO;
	}

	public void setScorePredictionDTO(ScorePredictionDTO scorePredictionDTO) {
		this.scorePredictionDTO = scorePredictionDTO;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public ScorePredicitonListener getScorePredicitonListener() {
		return scorePredicitonListener;
	}

	public void setScorePredicitonListener(
			ScorePredicitonListener scorePredicitonListener) {
		this.scorePredicitonListener = scorePredicitonListener;
	}	
}
