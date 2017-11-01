import java.util.ArrayList;
import java.util.Collections;

public class team implements Comparable<team>{
	private String teamName;
	public ArrayList <player> players;
	static boolean printLong = false;
	public statistics stats; 
	boolean doPrint;
	int wins;
	int losses;
	
	
	public team(String tName){
		teamName = tName;
		doPrint = false;
		stats = new statistics();
		players = new ArrayList <player>();
		
	}
	
	public team(String tName, boolean b){
		this(tName);
		doPrint = b;
		
	}
	
	public boolean isEligible(){
		return stats.ab > 0;
	}
	
	public void update_stats(){
		for(player p : players)
			stats.add_player_stats(p);
	}
	public String getName(){
		return teamName;
	}
	
	public String toString(){
		if(printLong){
			return teamName + stats;
		}
		String buff = "";
		for(int x = teamName.length(); x < 20; x++)
			buff += " ";
		return teamName + buff + wins +"-" + losses;
	}

		
	public int compareTo(team t2) {
		return t2.wins - wins;
	}
	
	public String printPlayers(){
		String ret = "";
		Collections.sort(players);
		for(player x : players)
			ret += x + ", \n";
		
		return ret;
	}


	
	
}