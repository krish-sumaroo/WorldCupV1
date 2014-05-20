package com.competition.worldcupv1;

import com.competition.worldcupv1.dto.GameInfoDTO;
import com.competition.worldcupv1.service.GameService;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("NewApi")
public class GameInfoFragment extends Fragment {
	
	TextView txtTeam1Name;
	TextView txtTeam2Name;
	TextView txtVenue;
	TextView txtTime;
	GameService gameService = new GameService();

	public GameInfoFragment() {
		
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.game_info, container, false);
         
        return rootView;
    }

		@Override 
		    public void onActivityCreated(Bundle savedInstanceState) { 
		        super.onActivityCreated(savedInstanceState); 	
		    	GameService gameService = new GameService();

		     // display only game info page
				GameInfoDTO gameInfo = gameService.getGameInfoDTO(getActivity());
				//setContentView(R.layout.game_info);
				txtTeam1Name = (TextView) getActivity().findViewById( R.id.txtTeam1Name);
				txtTeam2Name = (TextView) getActivity().findViewById( R.id.txtTeam2Name);
				txtVenue = (TextView) getActivity().findViewById( R.id.txtVenue);
				txtTime = (TextView) getActivity().findViewById( R.id.txtTime);
				txtTeam1Name.setText(gameInfo.getTeam1Name());
				txtTeam2Name.setText(gameInfo.getTeam2Name());
				txtVenue.setText(gameInfo.getVenue());
				txtTime.setText(gameInfo.getTime());
	 }
}
