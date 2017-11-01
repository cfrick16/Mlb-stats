import java.io.*;
import java.util.*;
import java.util.*;
import java.util.Map.Entry;
public class stats {
	
	public static Map<String, player> allPlayers = new HashMap<String, player>();
	public static Map<String, team> allTeams = new HashMap<String, team>();
	
	public static void main(String [] args) throws FileNotFoundException{
		boolean oneOnly = false;
		String year = "2016";
		File [] files ={new File(year + "CIN.EVN"), new File(year + "ARI.EVN"),
				new File(year + "BAL.EVA"), new File(year + "ATL.EVN"),
				new File(year + "BOS.EVA"), new File(year + "HOU.EVA"),
				new File(year + "CHA.EVA"), new File(year + "CHN.EVN"),
				new File(year + "CLE.EVA"), new File(year + "COL.EVN"),
				new File(year + "DET.EVA"), new File(year + "LAN.EVN"),
				new File(year + "ANA.EVA"), new File(year + "MIA.EVN"),
				new File(year + "KCA.EVA"), new File(year + "MIL.EVN"),
				new File(year + "MIN.EVA"), new File(year + "NYN.EVN"),
				new File(year + "NYA.EVA"), new File(year + "PHI.EVN"),
				new File(year + "OAK.EVA"), new File(year + "PIT.EVN"),
				new File(year + "SEA.EVA"), new File(year + "SDN.EVN"),
				new File(year + "TBA.EVA"), new File(year + "SFN.EVN"),
				new File(year + "TEX.EVA"), new File(year + "SLN.EVN"),
				new File(year + "TOR.EVA"), new File(year + "WAS.EVN"),
				

		
				
		
		};

		String home = null;
		String away = null;
		ArrayList<Game> games = new ArrayList<Game>();
		for(File file : files){
			Scanner input = new Scanner(file);

			String ln = input.nextLine();
			String gmCode = "";
			while(input.hasNextLine()){
				ln = input.nextLine();
				while(input.hasNextLine() && !ln.substring(0,2).equals("id")){
					ln = input.nextLine() + "\n";
					gmCode += ln;
				}
				games.add(new Game(gmCode.split("\n")));
				gmCode = "";
			}
			
			input.close();
			if (oneOnly)
				return ;
		}
		statTeams();
		printPlayers(allPlayers, 10);
		System.out.println();
		printTeams(allTeams);
		

		
	}
	public static void statTeams(){
		for(team t : allTeams.values())
			t.update_stats();
	}
	public static void printPlayers(Map <String, player> m, int limit){
		Collection <player> c = m.values();
		List <player> l = new ArrayList<player>(c);
		Collections.sort(l);
		for(player x : l){
			if(x.isEligible())
				System.out.println(x);
			if(--limit == 0)
				break;
		}
	}
	
	public static void printPlayers(Map <String, player> m){
		printPlayers(m, -1);
	}
	
	public static void printTeams(Map <String, team> m){
		Collection <team> c = m.values();
		List <team> l = new ArrayList<team>(c);
		Collections.sort(l);
		for(team x : l)
			if(x.isEligible())
			System.out.println(x);
	}
	
}