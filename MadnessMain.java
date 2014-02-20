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
		String[] trainingSeasons = { "A", "B", "C", "D", "E", "F", "G", "H",
				"I", "J", "K", "L" };
		String[] testingSeasons = { "M", "N", "O", "P", "Q", "R" };
		float trainingSetCorrect = getAccuracyRate(trainingSeasons);
		float testSetCorrect = getAccuracyRate(testingSeasons);
		System.out.println("Training accuracy is " + trainingSetCorrect * 100
				+ "%");
		System.out.println("Testing accuracy is " + testSetCorrect * 100 + "%");

	}

	private static float getAccuracyRate(String[] seasons) {
		float average = 0;
		for (int i = 0; i < seasons.length; i++) {
			HashMap<Integer, Team> teams = CSVReader
					.getTeams("C:\\Users\\schepedw\\Documents\\Courses\\CSSE 490\\MarchMiningMadness\\computedTables\\seed_ranks"
							+ seasons[i] + ".csv");
			HashMap<String, Game> games = CSVReader
					.createGames(
							"C:\\Users\\schepedw\\Documents\\Courses\\CSSE 490\\MarchMiningMadness\\computedTables\\seed_slots"
									+ seasons[i] + ".csv", teams);
			HashMap<String, Game> answers = CSVReader
					.createGames(
							"C:\\Users\\schepedw\\Documents\\Courses\\CSSE 490\\MarchMiningMadness\\computedTables\\real_bracket"
									+ seasons[i] + ".csv", teams);
			Bracket b = new Bracket(games, teams);
			HashMap<String, Game> prediction = b.play();
			average += compareResults(answers, prediction);
		}
		return average / seasons.length;
	}

	/**
	 * TODO Put here a description of what this method does.
	 * 
	 * @param answers
	 * @param prediction
	 */
	private static float compareResults(HashMap<String, Game> answers,
			HashMap<String, Game> prediction) {
		float score = 0;
		Iterator<Entry<String, Game>> it = prediction.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Game> pairs = it.next();
			Game g1 = pairs.getValue();
			Game g2 = answers.get(pairs.getKey());
			System.out.println("Answer: " + g2.toString());
			System.out.println("Guess:  " + g1.toString());
			float points = g1.getScore(g2);
			score += points;
			System.out.println("Points: " + points + "\n");
		}
		score /= 192;
		return score;
	}

}
