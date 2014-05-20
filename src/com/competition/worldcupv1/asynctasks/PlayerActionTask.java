package com.competition.worldcupv1.asynctasks;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;

import com.competition.worldcupv1.dto.PlayerActionDTO;
import com.competition.worldcupv1.webServicehelper.WebServiceHelper;

public class PlayerActionTask extends AsyncTask<Void, Void, String>{
	
	WebServiceHelper serviceHelper = new WebServiceHelper();
	private PlayerActionDTO playerActionDTO;
	private Context context;
	PlayerActionTaskListener playerActionTaskListener;

	@Override
	protected String doInBackground(Void... params) {
		try {
			return  serviceHelper.addPlayerAction(context, playerActionDTO);
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
	public interface PlayerActionTaskListener {
		public void onComplete(String result);
	}
	public PlayerActionDTO getPlayerActionDTO() {
		return playerActionDTO;
	}
	public void setPlayerActionDTO(PlayerActionDTO playerActionDTO) {
		this.playerActionDTO = playerActionDTO;
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public PlayerActionTaskListener getPlayerActionTaskListener() {
		return playerActionTaskListener;
	}
	public void setPlayerActionTaskListener(
			PlayerActionTaskListener playerActionTaskListener) {
		this.playerActionTaskListener = playerActionTaskListener;
	}

}
