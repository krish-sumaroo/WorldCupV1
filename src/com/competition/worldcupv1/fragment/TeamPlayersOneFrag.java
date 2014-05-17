package com.competition.worldcupv1.fragment;

import java.util.ArrayList;
import java.util.List;

import com.competition.worldcupv1.R;
import com.competition.worldcupv1.dto.GameInfoDTO;
import com.competition.worldcupv1.dto.PlayerDTO;
import com.competition.worldcupv1.dto.Player;
import com.competition.worldcupv1.service.GameService;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TeamPlayersOneFrag extends Fragment {
	
	GameService gameService = new GameService();
	 View v ;
	
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	 Bundle savedInstanceState) {
	  
	v = inflater.inflate(R.layout.team_player_one_fragment, container, false);	  
	 v.setBackgroundColor(Color.parseColor("#D0F5A9"));
	  
	 return v;
	 }
	 
	 @SuppressWarnings("unchecked")
		@Override 
		    public void onActivityCreated(Bundle savedInstanceState) { 
		        super.onActivityCreated(savedInstanceState); 		           
		        final ListView listView = (ListView) getActivity().findViewById(R.id.listViewTeam1);
		        
		        GameInfoDTO currentGame = gameService.getGameInfoDTO(getActivity());
		        
		        TextView tv = (TextView)v.findViewById(R.id.txtTeamName1);
		   	 	tv.setText(currentGame.getTeam1Name());	
		   	 
		        List<PlayerDTO> listPlayers = gameService.getPlayers(getActivity(), currentGame.getTeam1Id());
		        
		        ArrayList<Player> playersList = new ArrayList<Player>();
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
 		                   
		            	// Create custom dialog object
		                final Dialog dialog = new Dialog(getActivity());
		                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		                dialog.setCanceledOnTouchOutside(true);
		                // Include dialog.xml file
		                dialog.setContentView(R.layout.player_action_popup);
		                
		                Player player = (Player) listView.getItemAtPosition(position);
		                
		                System.out.println(">>>>>>>>>>>>>>>>>>>>> player id = " + player.playerId);
		            
		                // Set dialog title
		              //  dialog.setTitle("Player Action");
		                
		    	        //substring the name to get the img name
		               // String playerName = child.get(childPosition);
		                //String playerImgName = playerName.substring(playerName.lastIndexOf(' ')+1, playerName.length());
		               // System.out.println(">>>>>>>>>>>>>>>>> playerImgName =" +playerImgName);
		                
		               // ImageView imageView = (ImageView) dialog.findViewById(R.id.imageView1);
		               // imageView.setImageResource(PlayerImgHelper.getPlayerName(playerImgName));
		                
		                dialog.show();
		            } 
		        }); 
		           
		    } 

}
