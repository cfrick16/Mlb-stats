import java.util.ArrayList;
import java.util.Arrays;

public class Game {
	team home;
	team away;
	int [] score; // score[0] = away team score
	ArrayList<String[]> code;
	
	public Game(String [] lines){
		score = new int[2];
		code = new ArrayList<String[]>();
		for(String x : lines)
			code.add(x.split(","));
		
		for(String[] data : code)
			parse_line(data);

		//System.out.println(home.getName() + " " + score[1]);
		//System.out.println(away.getName() + " " + score[0] + "\n");

		if(score[0] < score[1]){
			home.wins++;
			away.losses++;
		} else{
			home.losses++;
			away.wins++;
		}
	}
	
	private void parse_line(String [] data){
		if(data[0].equals("info") && data[1].equals("visteam") || data[1].equals("hometeam")){
			team temp = null;
				if(stats.allTeams.containsKey(data[2]))
					temp = stats.allTeams.get(data[2]);
				else{
					temp = new team(data[2]);
					stats.allTeams.put(data[2], temp);
				}
				if(data[1].equals("visteam"))
					away = temp;
				else
					home = temp;
				
				
		} else if(data[0].equals("start") || data[0].equals("sub")){
			
			String playerCode = data[3].equals("0") ? data[1] + away.getName(): data[1] + home.getName();
			//If this is the first appearence of player for his team
			if(!stats.allPlayers.containsKey(playerCode)){
				stats.allPlayers.put(playerCode, new player(data[2]));
							
				if(data[3].equals("1") && !home.players.contains(stats.allPlayers.get(playerCode)))
					home.players.add(stats.allPlayers.get(playerCode));
				else if(data[3].equals("0") && !away.players.contains(stats.allPlayers.get(playerCode)))
					away.players.add(stats.allPlayers.get(playerCode));			
			}
			
		} else if(data[0].equals("play")){
			String t_name = data[2].equals("0") ? away.getName(): home.getName();

			
				int runs_scored = stats.allPlayers.get(data[3]+t_name).update_stats(data[6]);
				
				score[data[2].equals("0") ? 0: 1] += runs_scored;
		}
	}

	
}
