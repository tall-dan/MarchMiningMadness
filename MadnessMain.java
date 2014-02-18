import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author schepedw. Created Feb 11, 2014.
 */
public class MadnessMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HashMap<Integer, Team> teams = CSVReader.getTeams("C:\\Users\\schepedw\\Documents\\Courses\\CSSE 490\\MarchMiningMadness\\seed_ranks.csv");
		HashMap<String, Game> games = CSVReader.createGames("C:\\Users\\schepedw\\Documents\\Courses\\CSSE 490\\MarchMiningMadness\\seed_slots.csv",teams);// The first 64
	HashMap<String, Game> answers = CSVReader.createGames("C:\\Users\\schepedw\\Documents\\Courses\\CSSE 490\\MarchMiningMadness\\real_bracket.csv",teams);
		Bracket b = new Bracket(games, teams);
		HashMap<String, Game> prediction = b.play();
		compareResults(answers, prediction);
	}

	/**
	 * TODO Put here a description of what this method does.
	 * 
	 * @param answers
	 * @param prediction
	 */
	private static void compareResults(HashMap<String, Game> answers,
			HashMap<String, Game> prediction) {
		float score=0;
		Iterator<Entry<String, Game>> it = prediction.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Game> pairs = it.next();
			Game g1 = pairs.getValue();
			Game g2 = answers.get(pairs.getKey());
			score+=g1.getScore(g2);
		}
		score/=192;
		System.out.println("Score is "+score+", or "+score*100+"%");
	}

}
