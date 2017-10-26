
public class team implements Comparable<team>{
	private String teamName;
	static boolean printLong = false;
	public statistics stats; 
	boolean doPrint;
	
	
	public team(String tName){
		teamName = tName;
		doPrint = false;
		stats = new statistics();
	}
	
	public team(String tName, boolean b){
		this(tName);
		doPrint = b;
		
	}
	
	public boolean isEligible(){
		return stats.ab > 0;
	}
	
	public void updateStats(String play){
		stats.updateStats(play);
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
		return teamName + buff + stats;
	}

	public int compareTo(team t2) {
		return stats.compareTo(t2.stats);
	}


	
	
}