import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author schepedw. Created Feb 16, 2014.
 */
public class Game {
	private int round;
	private String region;
	private int gameNumber;
	private Team team1;
	private Team team2;
	private String slot;

	// Data comes in like R1W1, which means the first game of the first round in
	// the west region
	public Game(String slot, Team team1, Team team2) {
		this.slot=slot;
		this.team1 = team1;
		this.team2 = team2;
		this.round = Character.getNumericValue(slot.charAt(1));
		this.region = String.valueOf(slot.charAt(2));
		if (slot.length()>3)
			this.gameNumber = Character.getNumericValue((slot.charAt(3)));
		else
			this.gameNumber=55;
	}
	
	@Override
	public String toString(){
		return this.slot+":  "+this.team1+" vs. "+ this.team2;
	}
	
	public Team getWinner(){
		return this.team1.score>this.team2.score? this.team1:this.team2;
	}
	
	public Team getLoser(){
		return this.team1.score>this.team2.score? this.team2: this.team1;
	}

	/**
	 * Returns the value of the field called 'round'.
	 * 
	 * @return Returns the round.
	 */
	public int getRoundNumber() {
		return this.round;
	}

	/**
	 * Returns the value of the field called 'region'.
	 * 
	 * @return Returns the region.
	 */
	public String getRegion() {
		return this.region;
	}

	/**
	 * Returns the value of the field called 'gameNumber'.
	 * 
	 * @return Returns the gameNumber.
	 */
	public int getGameNumber() {
		return this.gameNumber;
	}

	/**
	 * Returns the value of the field called 'team1'.
	 * 
	 * @return Returns the team1.
	 */
	public Team getTeam1() {
		return this.team1;
	}

	/**
	 * Returns the value of the field called 'team2'.
	 * 
	 * @return Returns the team2.
	 */
	public Team getTeam2() {
		return this.team2;
	}
	
	public int getScore(Game other){
		int score=0;//TODO: put a catch for the winner here
		this.round=this.round==18? 6:this.round;
		if (this.round==1){
			return score;
		}
		if (this.team1==other.team1||this.team1==other.team2)//got one team right
			score+=Math.pow(2,(this.round-2));
		if (this.team2==other.team1||this.team2==other.team2)//got other team right
			score+=Math.pow(2,(this.round-2));
		return score;
	}
	
	@Override
	public boolean equals(Object g){
		Game other =(Game) g;
		return this.region==other.region&&this.round==other.round;
	}


}
