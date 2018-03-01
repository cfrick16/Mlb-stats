import java.util.Arrays;

public class statistics implements Comparable<statistics>{

	static boolean printLong = false;
	int hits;
	int singles;
	int doubles;
	int triples;
	int homeruns;
	int pa;
	int walks;
	int sac;
	int ab;
	
	int out_holder;
	
	boolean doPrint;
	
	
	public statistics(){
		hits = 0;
		singles = 0;
		doubles = 0;
		triples = 0;
		homeruns = 0;
		pa = 0;
		walks = 0;
		ab = 0;
		sac = 0;
		doPrint = false;
	}
	
	public statistics(boolean b){
		this();
		doPrint = b;
		
	}
	
	
	//Returns true if the first a.length chars of play is 
	//the same as the string a. Can send multiple strings 
	//split by '|'
	private boolean stringEquals(String a, String play){
		String [] strs = a.split(" ");
		
		for(String s : strs){
			if(s.length() <= play.length())
				if(s.equals(play.substring(0, s.length())))
					return true;
		}
		return false;
	}
	
	private void print(String x){
		if(doPrint)
			System.out.println(x);
	}
	
	public void doPrint(boolean bool){
		doPrint = bool;
	}
	
	//Parses fullPlay and calls the corresponding methods to update stats
	//Returns runs scored on the play
	public int update_stats(String fullPlay){
		out_holder = 0;
		//Splitting up fullPlay
		int slash = fullPlay.indexOf("/");
		int period = fullPlay.indexOf(".");
		String play = "", mod = "", advance = "";
		if(slash < 0 && period < 0){
			play = fullPlay;
		} else if(period < 0 ){
					play = fullPlay.substring(0, slash);
					mod = fullPlay.substring(slash + 1);
				
		} else if(slash < 0 || period < slash){
			play = fullPlay.substring(0, period);
			advance = fullPlay.substring(period + 1);
		} else{
			play =fullPlay.substring(0, slash);
			mod = fullPlay.substring(slash + 1, period);
			advance = fullPlay.substring(period+1);
		}
		String [] advances = advance.split(";");
		String [] mods = mod.split("/");
		
		
		//Calls parse_play(), if no runners advance, sac bunt or fly do not count
		//So mods is not needed
		int homer = 0;
		if(advance.equals(""))
			homer = parse_play(play, new String[0],advances);
		else
			homer = parse_play(play, mods, advances);
		
		for(String x : mods)
			if(x.contains("DP")){
				out_holder = 2;
			}
		int rs =  runs_scored(advances) + homer;
		//THIS JUST WORKS
		if(rs>=100){
			rs -= 100;
			rs -= homer;
		}
		print(fullPlay + "  " + out_holder);
		return rs;
	}
	
	//Parses the play and decides if it was hit single...
	private int parse_play(String play, String [] mods, String [] advances){
		int homer = 0; //Homer or stolen home

		if(stringEquals("SB DI", play)){
			//Stolen base: attempted to throw out or no attempt
			if(stringEquals("SBH DIH", play))
				homer++;
		} else if(stringEquals("POCS PO", play)){
			//Picked off at base POCS%, Caught stealing or putout
			out_holder++;
		
		} else if(stringEquals("OA", play)){
			//Out at base for some unknown reason
			//out_holder++;
		} else if(stringEquals("WP PB", play)){
			//Wild pitch or passed ball
		} else if(stringEquals("CS", play)){
			//Caught stealing
			out_holder++;
		} else if(stringEquals("BK", play)){
			//Balk
		}
		
		//Batter plays
		else if(stringEquals("C", play)){
			//Interference, No Ab, go to 1st
		} else if(stringEquals("E", play)){
			//Error
			ab++; pa++;
		} else if(stringEquals("S", play)){
			//Single
			ab++; pa++; hits++; singles++;
		} else if(stringEquals("D DGR", play)){
			//Double
			ab++; pa++; hits++; doubles++;
		} else if(stringEquals("T", play)){
			//Triple
			ab++; pa++; hits++; triples++;
		} else if(stringEquals("HP I W IW", play)){
			//Walk
			pa++; walks++;
		} else if(stringEquals("H HR", play)){
			//Homer
			ab++; pa++; hits++; homeruns++;  homer++;
		} else if(stringEquals("FC", play)){
			//Fielders choice
			ab++; pa++;
		} else if(stringEquals("FLE", play)){
			//Error on foul ball
		} else if(stringEquals("NP", play)){
			//No play
		} else if(stringEquals("K", play)){
			//Strikeout
			pa++; ab++; out_holder++;
		} else if( stringEquals("1 2 3 4 5 6 7 8 9", play)){
			//Out in field of play
			pa++; ab++; out_holder++;


			for(String x : mods)
				if(x.equals("SH") || x.equals("SF")){
					sac++;
					ab--;
					break;
				}
		} else{
			System.out.println("Unknown play "+ play);
		}

		//out_holder += Arrays.toString(advances).length() -
			//	Arrays.toString(advances).replaceAll("X", "").length();
		for(String x : advances)
		{
			out_holder += x.contains("X") && !x.contains("E") ? 1:0;
		}
		//Additional play, Recursive call
		if(play.indexOf('+') < 3 && play.indexOf('+') > 0){
			homer += parse_play(play.substring(play.indexOf('+') + 1), mods, new String[0]);

		}
		return homer;
		
	}
	
	private int runs_scored(String [] advances){


		int count = 0;
		for(String x : advances){
			if(x.contains("-H"))
				count++;
			if(x.matches(".*XH\\([0-9]*E.*") || x.matches(".*XH\\(.R.*"))
				count++;
			if(x.contains("B-H"))
				count+=100;
			//out_holder += x.contains("X") ? 1:0;
		}

		return count;
	}
	
	public String getBA(){
		if(ab == 0)
			return ".000";
		
		double ba = ((int)Math.round(((1000. * hits) / ab))) /1000.;

		if(ba == 1)
			return "1.000";
		
		String ret = ba + "00";

		return ret.substring(1, 5);
	}
	
	public void add_player_stats(player p){
		hits += p.stats.hits;
		singles += p.stats.singles;
		doubles += p.stats.doubles;
		triples += p.stats.triples;
		homeruns += p.stats.homeruns;
		pa += p.stats.pa;
		walks += p.stats.walks;
		sac += p.stats.sac;
		ab += p.stats.ab;
	}
	
	public String toString(){
		if(printLong){
			return ":\nHits: " + hits +"\nSingles: " +
					singles + "\nDoubles: " + doubles + "\nTriples: " +
					triples + "\nHomeruns: " + homeruns + 
					"\nWalks: " + walks +"\nAt-Bats: " + ab 
					+"\nBA: " + getBA();
		}
		return " has " + hits +" hits " + pa +" plate Apperances "
				 + getBA();
	}

	public int compareTo(statistics p2) {
		return p2.hits - hits;
	}

	
	
}
