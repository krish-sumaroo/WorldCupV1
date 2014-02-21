package com.competition.worldcupv1.dto;

import java.io.Serializable;
import java.util.Date;

import com.competition.worldcupv1.enums.MatchStatus;


/**
 * @author roma
 *
 */
@SuppressWarnings("serial")
public class MatchDTO implements Serializable{
	
	private Integer matchId;
	private TeamDTO homeTeam;
	private TeamDTO awayTeam;
	private ScoreDTO score;
	private MatchStatus status;
	private Date kickOff;
	private String venue;
	
	public MatchDTO(){}
	
	public MatchDTO(TeamDTO homeTeam, TeamDTO awayTeam){
		super();
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
	}
	
	public Integer getMatchId() {
		return matchId;
	}
	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}
	public TeamDTO getHomeTeam() {
		return homeTeam;
	}
	public void setHomeTeam(TeamDTO homeTeam) {
		this.homeTeam = homeTeam;
	}
	public TeamDTO getAwayTeam() {
		return awayTeam;
	}
	public void setAwayTeam(TeamDTO awayTeam) {
		this.awayTeam = awayTeam;
	}

	public ScoreDTO getScore() {
		return score;
	}

	public void setScore(ScoreDTO score) {
		this.score = score;
	}

	public MatchStatus getStatus() {
		return status;
	}

	public void setStatus(MatchStatus status) {
		this.status = status;
	}

	public Date getKickOff() {
		return kickOff;
	}

	public void setKickOff(Date kickOff) {
		this.kickOff = kickOff;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}
		
}
