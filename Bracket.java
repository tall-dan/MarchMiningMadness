import java.util.ArrayList;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author schepedw. Created Feb 10, 2014.
 */
public class Bracket {
	private ArrayList<Team> east;
	private ArrayList<Team> north;
	private ArrayList<Team> south;
	private ArrayList<Team> west;
	public Team champion;

	public Bracket(ArrayList<Team> north, ArrayList<Team> south,
			ArrayList<Team> east, ArrayList<Team> west) {
		this.east = east;
		this.west = west;
		this.north = north;
		this.south = south;
	}

	public void play() {
		this.east = playRound(this.east);
		this.west = playRound(this.west);
		this.north = playRound(this.north);
		this.south = playRound(this.south);

		printFinalFour();
		playFinalFour();

	}

	/**
	 * Figure out how to whittle down the list to the people who won that round
	 * Have a check for when region.size==1
	 * 
	 * @param east2
	 * @return
	 */
	private ArrayList<Team> playRound(ArrayList<Team> region) {
		if (region.size() == 1) {
			return region;
		}
		ArrayList<Team> winners = new ArrayList<Team>();
		for (int i = 0; i < region.size(); i += 2) {
			Team team1 = region.get(i);
			Team team2 = region.get(i + 1);
			Team winner = (team1.score >= team2.score) ? team1 : team2;
			System.out.println(team1.toString() + " vs " + team2.toString()
					+ ": " + winner.toString() + " wins");
			winners.add(winner);
		}
		return playRound(winners);

		
	}

	/**
	 * TODO Put here a description of what this method does.
	 * 
	 */
	private void playFinalFour() {
		Team finalist1;
		Team finalist2;
		finalist1 = (east.get(0).score > west.get(0).score) ? east.get(0)
				: west.get(0);
		finalist2 = (north.get(0).score > south.get(0).score) ? north.get(0)
				: south.get(0);
		this.champion = finalist1.score > finalist2.score ? finalist1
				: finalist2;
		System.out.println("Champion: " + this.champion);

	}

	/**
	 * TODO Put here a description of what this method does.
	 * 
	 */
	private void printFinalFour() {
		System.out.println("Final four:--------");
		System.out.println("East: " + this.east.get(0));
		System.out.println("West: " + this.west.get(0));
		System.out.println("South: " + this.south.get(0));
		System.out.println("North: " + this.north.get(0));
	}

	/**
	 * TODO Put here a description of what this method does.
	 * 
	 * @return
	 */
	private ArrayList<ArrayList<Team>> getAllTeams() {
		ArrayList<ArrayList<Team>> allRegions = new ArrayList<ArrayList<Team>>();
		allRegions.add(this.east);
		allRegions.add(this.west);
		allRegions.add(this.north);
		allRegions.add(this.south);
		return allRegions;
	}

}
