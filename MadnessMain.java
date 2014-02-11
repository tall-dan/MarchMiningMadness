import java.util.ArrayList;


/**
 * TODO Put here a description of what this class does.
 *
 * @author schepedw.
 *         Created Feb 11, 2014.
 */
public class MadnessMain {

	/**
	 * TODO Put here a description of what this method does.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<Team> north=Team.readTeams("");
		ArrayList<Team> south=Team.readTeams("");
		ArrayList<Team> east=Team.readTeams("");
		ArrayList<Team> west=Team.readTeams("");
		Bracket b= new Bracket(north,south,east, west);
		b.play();
	}

}
