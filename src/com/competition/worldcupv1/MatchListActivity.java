package com.competition.worldcupv1;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MatchListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.match_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.match_list, menu);
		return true;
	}

}
