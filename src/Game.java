import java.util.ArrayList;
import java.util.Arrays;

public class Game {
	team home;
	team away;
	String[] printGame; // Away, Home: Print the games of these teams.
	int [] score; // score[0] = away team score
	ArrayList<String[]> code;
	ArrayList<String> scoringPlays;
	int total_outs;
	String lastPlay[];
	
	public Game(String [] lines){
		score = new int[2];
		printGame = new String[]{"",""};
		code = new ArrayList<String[]>();
		scoringPlays = new ArrayList<String>();
		total_outs = 0;
		
		for(String x : lines)
			code.add(x.split(","));
		
		for(String[] data : code)
			parse_line(data);
 
		if(stats.setSits)
			stats.mySit.endGame(score[0] < score[1]);
		
		if(score[0] == score[1] ){//&& Integer.parseInt(lastPlay[1])<9){
			//Inconclusive game
			//System.out.print(toString());
		}
		else if(score[0] < score[1]){
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

			player p = stats.allPlayers.get(data[3] + t_name);
			if(isGame())
				p.stats.doPrint(false);
			if(stats.setSits)
				stats.mySit.addSit(total_outs , score[1], score[0]);
			
			int runs_scored = p.update_stats(data[6]);
			total_outs += p.stats.out_holder;
			
			
			if(runs_scored != 0)
				scoringPlays.add(runs_scored + ":" + Arrays.toString(data));
			
			if(home != null && away != null && isGame())
				System.out.println(total_outs + " " + p.stats.out_holder + " " + Arrays.toString(data));
			score[data[2].equals("0") ? 0: 1] += runs_scored;
			lastPlay = data;
		}
	}

	private boolean isGame(){
		return away.getName().equals(printGame[0]) && home.getName().equals(printGame[1]) 
			   || away.getName().equals(printGame[0]) && "".equals(printGame[1]) ||
			   "".equals(printGame[0]) && home.getName().equals(printGame[1]);
	}

	public String toString(){
		String ret = "";

		ret += home.getName() + ": " + score[1] + "\n";
		ret += away.getName() + ": " + score[0] + "\n";
		
		if(score[1] == score[0])
			for(String x: scoringPlays)
				ret += x + "\n";
		for(String[] x : code)
			ret += Arrays.toString(x) + "\n";
		return ret;
	}
	
	static double getInning(int tot_outs){
		return (tot_outs / 6) + tot_outs % 6 / 10. + 1;
	} 
	
}
