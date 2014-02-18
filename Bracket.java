import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author schepedw. Created Feb 10, 2014.
 */
public class Bracket {
	private static HashMap<String, Game> games;
	private static HashMap<Integer, Team> teams;
	public Team champion;

	/**
	 * TODO Put here a description of what this constructor does.
	 * 
	 * @param games
	 * @param teams2
	 */
	public Bracket(HashMap<String, Game> games, HashMap<Integer, Team> teams) {
		Bracket.teams = teams;
		Bracket.games = games;
	}

	public static Team getTeamFromId(int id) {
		return teams.get(id);
	}

	public HashMap<String, Game> play() {
		HashMap<String, Game> round2=playRound(Bracket.games);//32 teams left
		HashMap<String, Game> round3=playRound(round2);//16
		HashMap<String, Game> round4=playRound(round3);//8
		HashMap<String, Game> round5=playRound(round4);//Round5 just has the final four. The 4 games are teams vs. themselves
		HashMap<String, Game> theRest=playFinalFour(round5);
		Bracket.games.putAll(round2);
		Bracket.games.putAll(round3);
		Bracket.games.putAll(round4);
		Bracket.games.putAll(theRest);
		return Bracket.games;
		
		
		

	}

	/**
	 * @param round2
	 */
	private void printRound(HashMap<String, Game> round2) {
		Iterator<Entry<String, Game>> it = round2.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<String, Game> pairs = it.next();
	        System.out.println(pairs.getValue());
	    }
	}

	public HashMap<String, Game> playRound(HashMap<String, Game> round) {
		HashMap<String, Game> nextRound = new HashMap<String, Game>();
		int roundNumber = (int)( (Math.log(32 / round.size())/Math.log(2)) + 1);//64,32,16
		int numGames = (int) Math.pow(2, 4 - roundNumber);//8,4,2,1 - games per region TODO: fix this
		String[] regions = { "W", "X", "Y", "Z" };
		for (int j = 0; j < 4; j++) {
			String region = regions[j];
			for (int i = 1; i <= (Math.round(numGames*1.0 / 2)); i++) {
				String key1 = "R" + roundNumber + region + i;
				String key2 = "R" + roundNumber + region + (numGames - i + 1);
				Team team1 = round.get(key1).getWinner();
				Team team2 = round.get(key2).getWinner();
				String slot = "R" + (roundNumber + 1) + region + i;
				Game nextGame = new Game(slot, team1, team2);
				nextRound.put(slot, nextGame);
			}
		}

		return nextRound;
	}

	private HashMap<String, Game> playFinalFour(HashMap<String, Game> round5) {
		HashMap<String, Game> finals=new HashMap<String, Game>();
		Game r5wx= new Game("R5WX",round5.get("R5W1").getTeam1(),round5.get("R5X1").getTeam1());
		Game r5yz= new Game("R5YZ",round5.get("R5Y1").getTeam1(),round5.get("R5Z1").getTeam1());
		Game r6ch = new Game("R6CH",r5yz.getWinner(),r5wx.getWinner());
		finals.put("R5WX",r5wx);
		finals.put("R5YZ",r5yz);
		finals.put("R6CH",r6ch);
		determineAndAddWinner(finals);
		return finals;
		
	}

	private void determineAndAddWinner(HashMap<String, Game> finals) {
		Game championship=finals.get("R6CH");
		Team champ=championship.getWinner();
		Team loser=championship.getLoser();
		finals.put("WIN",new Game("WIN",champ,loser));
	}



}
