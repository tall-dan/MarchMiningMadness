import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author schepedw. Created Feb 10, 2014.
 */
public class Team {
	protected float score;
	protected int id;

	public Team(int id, float score) {
		this.score = score;
		this.id = id;
	}

	@Override
	public String toString() {
		return String.valueOf(this.id);
	}


}
