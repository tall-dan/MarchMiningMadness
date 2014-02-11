
/**
 * TODO Put here a description of what this class does.
 *
 * @author schepedw.
 *         Created Feb 10, 2014.
 */
public class Team {
	protected float score;
	protected String name; 
	public Team(String name, float score){
		this.score=score;
		this.name=name;
	}
	@Override
	public String toString(){
		return this.name;
	}
}
