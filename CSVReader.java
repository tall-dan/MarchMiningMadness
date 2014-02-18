import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


/**
 * TODO Put here a description of what this class does.
 *
 * @author schepedw.
 *         Created Feb 16, 2014.
 */
public class CSVReader {
	public static HashMap<String, Game> createGames(String filename, HashMap<Integer, Team> teams) {
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		HashMap<String, Game> games= new HashMap<String, Game>();
		try {
			br = new BufferedReader(new FileReader(filename));
			while ((line = br.readLine()) != null) {
				// use comma as separator
				//Expects team id, then score
				// team name would be neat too
				String[] stuff = line.split(csvSplitBy);
				games.put(stuff[0],new Game(stuff[0],teams.get((Integer.valueOf(stuff[1]))),teams.get(Integer.valueOf(stuff[2]))));
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

		return games;
	}
	

	/**
	 * TODO Refactor 
	 */
	public static HashMap<Integer, Team> getTeams(String filename) {
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		HashMap<Integer, Team> teams= new HashMap<Integer, Team>();
		try {
			br = new BufferedReader(new FileReader(filename));
			while ((line = br.readLine()) != null) {
				// use comma as separator
				//Expects team id, then score
				// team name would be neat too
				String[] stuff = line.split(csvSplitBy);
				teams.put(Integer.valueOf(stuff[0]),new Team(Integer.valueOf(stuff[0]),Float.valueOf(stuff[1])));
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

		return teams;
	}
}
