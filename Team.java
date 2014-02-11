import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author schepedw. Created Feb 10, 2014.
 */
public class Team {
	protected float score;
	protected String name;

	public Team(String name, float score) {
		this.score = score;
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public static ArrayList<Team> readTeams(String teamFile) {
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		ArrayList<Team> teams= new ArrayList<Team>();
		try {

			br = new BufferedReader(new FileReader(teamFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				//Expects team name first, then score
				String[] stuff = line.split(csvSplitBy);
				teams.add(new Team(stuff[0],Float.valueOf(stuff[1])));

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}
}
