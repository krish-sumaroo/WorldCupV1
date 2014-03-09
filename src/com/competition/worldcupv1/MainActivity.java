package com.competition.worldcupv1;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;


import com.competition.worldcupv1.asynctasks.GetTeamPlayerListTask;
import com.competition.worldcupv1.asynctasks.GetTeamPlayerListTask.GetTeamPlayerListTaskListener;
import com.competition.worldcupv1.dto.TeamDTO;
import com.competition.worldcupv1.service.TeamService;
import com.competition.worldcupv1.webServicehelper.WebServiceHelper;


public class MainActivity extends Activity {
	
	private ProgressDialog progressDialog;
	WebServiceHelper serviceHelper = new WebServiceHelper();

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		progressDialog = new ProgressDialog(getBaseContext());
		progressDialog.setMessage("Loading ...");
		//initTeamPlayerList();
		try {
			try {
				serviceHelper.saveUser("roma", "B123", 1, getBaseContext());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	
	private void initTeamPlayerList(){
		GetTeamPlayerListTask teamPlayersTask = new GetTeamPlayerListTask();
		teamPlayersTask.setmListener(new GetTeamPlayerListTaskListener() {
			
			@Override
			public void onError(String message) {
				progressDialog.dismiss();
				Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
				toast.show();
			}		

			@Override
			public void onStart() {
				
				try {
					serviceHelper.extractTeamPlayersData();
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
				progressDialog.setMessage("Loading  list...");
				progressDialog.show();
			}

			@Override
			public void onComplete(List<TeamDTO> result) {
				progressDialog.dismiss();
				
			}
		});
		teamPlayersTask.setContext(getApplicationContext());
		teamPlayersTask.execute();	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//test
		return true;
	}

}
