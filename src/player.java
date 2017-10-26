
public class player implements Comparable<player>{
	private String playerName;
	private team myTeam;
	static boolean printLong = false;
	public statistics stats; 
	boolean doPrint;
	
	
	public player(String pName, team t){
		playerName = pName.substring(1, pName.length() - 1);
		doPrint = false;
		stats = new statistics();
		myTeam = t;
	}
	
	public player(String pName, boolean b){
		this(pName);
		doPrint = b;
		
	}
	
	public boolean isEligible(){
		return stats.ab > 0;
	}
	
	public void updateStats(String play){
		stats.updateStats(play);
	}
	public String getName(){
		return playerName;
	}
	
	public String toString(){
		if(printLong){
			return playerName + stats;
		}
		String buff = "";
		for(int x = playerName.length(); x < 20; x++)
			buff += " ";
		return playerName + buff + stats;
	}

	public int compareTo(player p2) {
		return stats.compareTo(p2.stats);
	}

	
	
}
