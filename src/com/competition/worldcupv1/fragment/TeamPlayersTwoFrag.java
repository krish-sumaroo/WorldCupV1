package com.competition.worldcupv1.fragment;

import java.util.ArrayList;
import java.util.List;

import com.competition.worldcupv1.R;
import com.competition.worldcupv1.dto.GameInfoDTO;
import com.competition.worldcupv1.dto.PlayerDTO;
import com.competition.worldcupv1.dto.Player;
import com.competition.worldcupv1.service.GameService;
import com.competition.worldcupv1.service.PlayerService;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TeamPlayersTwoFrag extends Fragment {
	
	GameService gameService = new GameService();
	PlayerService playerService = new PlayerService();
	
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	 Bundle savedInstanceState) {
	  
	 View v = inflater.inflate(R.layout.team_player_two_fragment, container, false);	  
	 v.setBackgroundColor(Color.parseColor("#5ca028"));
	 TextView tv = (TextView)v.findViewById(R.id.txtTeamName2);
	 tv.setText("Spain");	  
	 return v;
	 }
	 
	 @SuppressWarnings("unchecked")
		@Override 
		    public void onActivityCreated(Bundle savedInstanceState) { 
		        super.onActivityCreated(savedInstanceState); 		           
		        ListView listView = (ListView) getActivity().findViewById(R.id.listViewTeam2);
		        GameInfoDTO currentGame = gameService.getGameInfoDTO(getActivity());
		        List<PlayerDTO> listPlayers = playerService.getPlayers(getActivity(), currentGame.getTeam2Id());
		        
		        ArrayList<Player> playersList = new ArrayList<Player>();
		        if(listPlayers.size() >0){
			        for ( PlayerDTO player: listPlayers) {
			        	Player gamePlayer = new Player(R.drawable.player, player.getName(), player.getPosition(), String.valueOf(player.getNumber()),
			        	player.getPlayerId(), player.getTeamId());
			        	playersList.add(gamePlayer);
					}	
			        
			        PlayerListAdapter adapter = new PlayerListAdapter(getActivity(), 
			                R.layout.listview_item_row, playersList);

	             listView.setBackgroundResource(R.drawable.background);                 
	             listView.setAdapter(adapter);		           
			        listView.setOnItemClickListener(new OnItemClickListener() { 
			   
			            @Override 
			            public void onItemClick(AdapterView<?> parent, View view, 
			                    int position, long id) { 
	 		                   
			                   
			            } 
			        }); 
		        }
		        

		           
		    } 
}
