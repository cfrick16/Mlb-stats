import java.io.*;
import java.util.*;
import java.util.*;
import java.util.Map.Entry;
public class stats {
	public static void main(String [] args) throws FileNotFoundException{
		String year = "2014";
		File [] files ={new File(year + "ANA.EVA"), new File(year + "ARI.EVN"),
				new File(year + "BAL.EVA"), new File(year + "ATL.EVN"),
				new File(year + "BOS.EVA"), new File(year + "CHN.EVN"),
				new File(year + "CHA.EVA"), new File(year + "CIN.EVN"),
				new File(year + "CLE.EVA"), new File(year + "COL.EVN"),
				new File(year + "DET.EVA"), new File(year + "LAN.EVN"),
				new File(year + "HOU.EVA"), new File(year + "MIA.EVN"),
				new File(year + "KCA.EVA"), new File(year + "MIL.EVN"),
				new File(year + "MIN.EVA"), new File(year + "NYN.EVN"),
				new File(year + "NYA.EVA"), new File(year + "PHI.EVN"),
				new File(year + "OAK.EVA"), new File(year + "PIT.EVN"),
				new File(year + "SEA.EVA"), new File(year + "SDN.EVN"),
				new File(year + "TBA.EVA"), new File(year + "SFN.EVN"),
				new File(year + "TEX.EVA"), new File(year + "SLN.EVN"),
				new File(year + "TOR.EVA"), new File(year + "WAS.EVN"),
				

		
				
		
		};
		Map<String, player> players = new HashMap<String, player>();
		Map<String, team> teams = new HashMap<String, team>();
		String home = null;
		String away = null;
		for(File file : files){
			Scanner input = new Scanner(file);
			
			while(input.hasNextLine()){
				String ln = input.nextLine();
				//String lnType = ln.substring(0, ln.indexOf(','));
				String [] data = ln.split(",");
				
				if(data[0].equals("id")){
					
				} else if(data[0].equals("version")){
					
				} else if(data[0].equals("info")){
					if(data[1].equals("visteam")){
						away = data[2];
						if(teams.containsKey(data[2]))
							teams.get(away);
						else{
							teams.put(away, new team(away));
						}
					}
					if(data[1].equals("hometeam")){
						home = data[2];
						if(teams.containsKey(data[2]))
							teams.get(home);
						else{
							teams.put(home, new team(home));
						}
					}
						
				} else if(data[0].equals("start") || data[0].equals("sub")){
					if(!players.containsKey(data[1])){
						team myTeam = teams.get(away);
						if(data[3].equals("1"))
							myTeam = teams.get(home);
						players.put(data[1], new player(data[2],myTeam));
					}
					
				} else if(data[0].equals("play")){
					//if(data[2].equals("1"))
						players.get(data[3]).updateStats(data[6]);
						
						if(data[2].equals("1"))
							teams.get(home).updateStats(data[6]);
						else
							teams.get(away).updateStats(data[6]);
				}
				
			}
			input.close();
		}
		//System.out.println(players.get("gomec002"));
		printPlayers(players);
		//printTeams(teams);
		

		
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