package com.competition.worldcupv1;

import java.util.List;

import com.competition.worldcupv1.asynctasks.CreateGameInfoTask;
import com.competition.worldcupv1.asynctasks.CreatePlayerListTask;
import com.competition.worldcupv1.asynctasks.CreateGameInfoTask.CreateGameInfoTaskListener;
import com.competition.worldcupv1.asynctasks.CreatePlayerListTask.CreatePlayerListTaskListener;
import com.competition.worldcupv1.dto.GameDTO;
import com.competition.worldcupv1.dto.GameInfoDTO;
import com.competition.worldcupv1.dto.PlayerDTO;
import com.competition.worldcupv1.fragment.TeamPlayersOneFrag;
import com.competition.worldcupv1.fragment.TeamPlayersTwoFrag;
import com.competition.worldcupv1.service.GameService;
import com.competition.worldcupv1.service.PlayerService;
import com.competition.worldcupv1.utils.ConnectionUtility;
import com.competition.worldcupv1.utils.SessionManager;
import com.competition.worldcupv1.utils.ConnectionUtility.ConnectionUtilityListener;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("NewApi")
public class PlayersFragment extends Fragment {
	
	private SlidingPaneLayout mPane;
	GameService gameService = new GameService();
	PlayerService playerService = new PlayerService();
	private static final int PLAYER_STATUS_INFO_FINAL = 3;
	private static final int PLAYER_STATUS_INFO_NOT_FINAL = 2;
	TextView txtTeam1Name;
	TextView txtTeam2Name;
	TextView txtVenue;
	TextView txtTime;
	
	
   	ProgressDialog pDialog;
   	private static SharedPreferences mSharedPreferences;
   	SessionManager session;
	final ConnectionUtility connectionUtility = new ConnectionUtility();
	View rootView ;
	
	public PlayersFragment() {
		
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
		rootView= inflater.inflate(R.layout.main_teamplayers_fragment, container, false);
         
        return rootView;
    }
		@Override 
		    public void onActivityCreated(Bundle savedInstanceState) { 
		        super.onActivityCreated(savedInstanceState); 	           
		        session = new SessionManager(getActivity());		
		 	   
				//check if gameInfo table empty			
				boolean isTableGameFill = gameService.isTableGameFill(getActivity());
				if(isTableGameFill){
					uponStatusDisplayPage2();
				}else{		
					
					try {
						if(connectionUtility.hasWifi(getActivity())){
							//if empty we populate the table
							populateTableGameInfo();
						}
						else{
							connectionUtility.showToast(getActivity());
							connectionUtility.setUtilityListener(new ConnectionUtilityListener()  {				
								@Override
								public void onInternetEnabled(boolean result) {
									//if empty we populate the table
									populateTableGameInfo();
								}
								@Override
								public void exitApplication(boolean result) {
									onDestroy();
									((MainContainerActivity)getActivity()).finish();
											
								}
							});	
						}
			    	}
					finally{
						
					}			
				}
		    } 
		
		
		

		public void  populateTablePlayers (int team1Id, int team2Id){
			CreatePlayerListTask populatePlayersTask = new CreatePlayerListTask();
			populatePlayersTask.setContext(getActivity());
			populatePlayersTask.setTeam1Id(team1Id);
			populatePlayersTask.setTeam2Id(team2Id);
			populatePlayersTask.execute();        
			populatePlayersTask.setCreatePlayerListTaskListener(new CreatePlayerListTaskListener() {
				@Override
				public void onComplete(List<PlayerDTO> result) {
					playerService.insertPlayerData(getActivity(),result);		
					
					//display page players
					 mPane = (SlidingPaneLayout) getView().findViewById(R.id.pane);		 
					 mPane.openPane();
					 
					 TeamPlayersOneFrag fragment2 = new TeamPlayersOneFrag();
					    FragmentManager fragmentManager = getFragmentManager();
					    android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					    fragmentTransaction.add(R.id.pane1, fragment2);
					    fragmentTransaction.commit();
					    
					    TeamPlayersTwoFrag fragment3 = new TeamPlayersTwoFrag();
					    FragmentManager fragmentManager2 = getFragmentManager();
					    android.app.FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
					    fragmentTransaction2.add(R.id.pane2, fragment3);
					    fragmentTransaction2.commit();
					  
				}});	
			}

		public void  populateTableGameInfo (){
			CreateGameInfoTask populateGameUnfoTask = new CreateGameInfoTask();
		    populateGameUnfoTask.setContext(getActivity());
		    populateGameUnfoTask.execute();        
		    populateGameUnfoTask.setCreateGameInfoTaskListener(new CreateGameInfoTaskListener() {	
				@Override
				public void onComplete(GameDTO result) {
					gameService.insertGameData(getActivity(),result);
					//gameService.(getApplicationContext());
					//go check the player status info
					uponStatusDisplayPage ();
					
				}});
		}
		
		public int  verifyGamePlayerStatus (){
			return playerService.getPlayerInfoStatus(getActivity());
		}
		
		public void  uponStatusDisplayPage (){
			int checkPlayerInfoStatus =  verifyGamePlayerStatus();
			if(checkPlayerInfoStatus == PLAYER_STATUS_INFO_NOT_FINAL){
				// display only game info page
				GameInfoDTO gameInfo = gameService.getGameInfoDTO(getActivity());

		        
				GameInfoFragment fragment2 = new GameInfoFragment();
				    FragmentManager fragmentManager = getFragmentManager();
				    android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				    fragmentTransaction.replace(R.id.pane, fragment2);
				   // fragmentTransaction.add(R.id.pane, fragment2);
				    fragmentTransaction.commit();
				    
//				//setContentView(R.layout.game_info);
//				txtTeam1Name = (TextView) getView().findViewById( R.id.txtTeam1Name);
//				txtTeam2Name = (TextView) getView().findViewById( R.id.txtTeam2Name);
//				txtVenue = (TextView) getView().findViewById( R.id.txtVenue);
//				txtTime = (TextView) getView().findViewById( R.id.txtTime);
//				txtTeam1Name.setText(gameInfo.getTeam1Name());
//				txtTeam2Name.setText(gameInfo.getTeam2Name());
//				txtVenue.setText(gameInfo.getVenue());
//				txtTime.setText(gameInfo.getTime());
			}
			else if (checkPlayerInfoStatus == PLAYER_STATUS_INFO_FINAL){			
				final GameDTO currentGame = gameService.getGame(getActivity());			
				boolean isTablePlayerFill = gameService.isTablePlayerFill(getActivity());
				if(!isTablePlayerFill){
					try {
						if(connectionUtility.hasWifi(getActivity())){
							//populate table game players 
							populateTablePlayers (currentGame.getTeam1Id(), currentGame.getTeam2Id());	
						}
						else{
							connectionUtility.showToast(getActivity());
							connectionUtility.setUtilityListener(new ConnectionUtilityListener()  {				
								@Override
								public void onInternetEnabled(boolean result) {
									//populate table game players 
									populateTablePlayers (currentGame.getTeam1Id(), currentGame.getTeam2Id());	
								}
								@Override
								public void exitApplication(boolean result) {
									onDestroy();
									((MainContainerActivity)getActivity()).finish();			
								}
							});	
						}
			    	}
					finally{
						
					}		
							
				}				

			}
		}
		
		
		public void  uponStatusDisplayPage2 (){
			int checkPlayerInfoStatus =  verifyGamePlayerStatus();
			if(checkPlayerInfoStatus == PLAYER_STATUS_INFO_NOT_FINAL){
				// display only game info page
				GameInfoDTO gameInfo = gameService.getGameInfoDTO(getActivity());
				//setContentView(R.layout.game_info);
				
				GameInfoFragment fragment2 = new GameInfoFragment();
			    FragmentManager fragmentManager = getFragmentManager();
			    android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			    //fragmentTransaction.add(R.id.pane, fragment2);
			    fragmentTransaction.replace(R.id.pane, fragment2);
			    fragmentTransaction.commit();
			    
//			    
//				txtTeam1Name = (TextView) getView().findViewById( R.id.txtTeam1Name);
//				txtTeam2Name = (TextView) getView().findViewById( R.id.txtTeam2Name);
//				txtVenue = (TextView) getView().findViewById( R.id.txtVenue);
//				txtTime = (TextView) getView().findViewById( R.id.txtTime);
//				txtTeam1Name.setText(gameInfo.getTeam1Name());
//				txtTeam2Name.setText(gameInfo.getTeam2Name());
//				txtVenue.setText(gameInfo.getVenue());
//				txtTime.setText(gameInfo.getTime());
			}
			else if (checkPlayerInfoStatus == PLAYER_STATUS_INFO_FINAL){			
				final GameDTO currentGame = gameService.getGame(getActivity());			
				boolean isTablePlayerFill = gameService.isTablePlayerFill(getActivity());
				if(!isTablePlayerFill){
					try {
						if(connectionUtility.hasWifi(getActivity())){
							//populate table game players 
							populateTablePlayers (currentGame.getTeam1Id(), currentGame.getTeam2Id());	
						}
						else{
							connectionUtility.showToast(getActivity());
							connectionUtility.setUtilityListener(new ConnectionUtilityListener()  {				
								@Override
								public void onInternetEnabled(boolean result) {
									//populate table game players 
									populateTablePlayers (currentGame.getTeam1Id(), currentGame.getTeam2Id());	
								}
								@Override
								public void exitApplication(boolean result) {
									onDestroy();
									((MainContainerActivity)getActivity()).finish();				
								}
							});	
						}
			    	}
					finally{
						
					}	
					
					TeamPlayersOneFrag fragment2 = new TeamPlayersOneFrag();
				    FragmentManager fragmentManager = getFragmentManager();
				    android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				    fragmentTransaction.add(R.id.pane1, fragment2);
				    fragmentTransaction.commit();
				    
				    TeamPlayersTwoFrag fragment3 = new TeamPlayersTwoFrag();
				    FragmentManager fragmentManager2 = getFragmentManager();
				    android.app.FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
				    fragmentTransaction2.add(R.id.pane2, fragment3);
				    fragmentTransaction2.commit();
//					
//					//display page players
//					 setContentView(R.layout.main_teamplayers_fragment);
//					 mPane = (SlidingPaneLayout) getView().findViewById(R.id.pane);		 
//					 mPane.openPane();
//					  
//					 getSupportFragmentManager().beginTransaction()
//					 .add(R.id.pane1, new TeamPlayersOneFrag(), "pane1").commit();
//					 getSupportFragmentManager().beginTransaction()
//					 .add(R.id.pane2, new TeamPlayersTwoFrag(), "pane2").commit();
				}	
				else{
					
					
					TeamPlayersOneFrag fragment2 = new TeamPlayersOneFrag();
				    FragmentManager fragmentManager = getFragmentManager();
				    android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				    fragmentTransaction.add(R.id.pane1, fragment2);
				    fragmentTransaction.commit();
				    
				    TeamPlayersOneFrag fragment3 = new TeamPlayersOneFrag();
				    FragmentManager fragmentManager2 = getFragmentManager();
				    android.app.FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
				    fragmentTransaction2.add(R.id.pane2, fragment3);
				    fragmentTransaction2.commit();
				    
				    
					//display page players
//					 setContentView(R.layout.main_teamplayers_fragment);
//					 mPane = (SlidingPaneLayout) getView().findViewById(R.id.pane);		 
//					 mPane.openPane();
//					  
//					 getSupportFragmentManager().beginTransaction()
//					 .add(R.id.pane1, new TeamPlayersOneFrag(), "pane1").commit();
//					 getSupportFragmentManager().beginTransaction()
//					 .add(R.id.pane2, new TeamPlayersTwoFrag(), "pane2").commit();
				}

			}
		}
}
