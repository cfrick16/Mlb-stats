import java.util.*;
//INNING IS A THIRD OF AN INNING
public class Situation {
	boolean do_diff = true;
	final int LOADED_INNING = 10000;
	final int LOADED_HOME = 100;
	final int LOADED_AWAY = 1;
	// [inning][home score][away score]
	//HOME TEAM WINS
	int wSit[][][];
	int wSitDiff[][];
	//AWAY TEAM WINS
	int lSit[][][];
	int lSitDiff[][];
	int[] loadedSits;
	int lsIndex;
	int prevSit;
	
	public Situation() {
		wSit = new int[9*6][40][40];
		lSit = new int[9*6][40][40];
		wSitDiff = new int[9*6][21]; // 10 is tie game
		lSitDiff = new int[9*6][21];
		loadedSits = new int[1000];
		lsIndex = 0;
		//prevSit = 0;
	}
	
	void addSit(int inning, int homeScore, int awayScore){
		if(inning >= 9 * 6)
			inning = 8 * 6 + inning % 6;
		int temp = inning * LOADED_INNING + homeScore * LOADED_HOME
				+ awayScore * LOADED_AWAY; 
		if(temp == 0)
			temp = -12;
		if(temp != prevSit)
			loadedSits[lsIndex++] = temp;
		prevSit = temp;
	}
	
	
	void endGame(boolean homeTeamWon){
		lsIndex = 0;
		while(loadedSits[lsIndex] != 0){
			int sit = loadedSits[lsIndex];
			int in = sit/LOADED_INNING;
			int home = (sit-in*LOADED_INNING)/LOADED_HOME;
			int away = sit % LOADED_HOME;
			
			if(sit == -12)
			{
				in = 0;
				home = 0;
				away = 0;
			}
			
			if(in >= 9 * 6){
				in = 8 * 6 + in % 6;
			}
			if(do_diff){
				int diff = home-away;
				diff = Math.max(-10, diff);
				diff = Math.min(10, diff);
				wSitDiff[in][diff+10] += homeTeamWon ? 1:0;
				lSitDiff[in][diff+10] += homeTeamWon ? 0:1;	
			} else {
				wSit[in][home][away] += homeTeamWon ? 1:0;
				lSit[in][home][away] += homeTeamWon ? 0:1;
			}
			loadedSits[lsIndex++] = 0;
		}
		lsIndex = 0;
	}
	
	double getSit(int inning, int myScore, int oppScore){
		if(do_diff){
			return getSit(inning, myScore - oppScore);
		} else {
			int home_wins = wSit[inning][myScore][oppScore];
			int away_wins = lSit[inning][myScore][oppScore];
			if(home_wins + away_wins < 10)
				return -1;
			return (home_wins * 1.) / (away_wins + home_wins);
		}
	}
	
	double getSit(int inning, int diff){
		diff = Math.min(10, diff); diff = Math.max(-10, diff);
		int home_wins = wSitDiff[inning][diff+10];
		int away_wins = lSitDiff[inning][diff+10];
		if(home_wins + away_wins < 10)
			return -1;
		return (home_wins * 1.) / (away_wins + home_wins);
	}
	
	int getTrials(int inning, int myScore, int oppScore){
		if(do_diff)
			return getTrials(inning, myScore - oppScore);
		return wSit[inning][myScore][oppScore] + lSit[inning][myScore][oppScore];
	}
	int getTrials(int inning, int diff){
		diff = Math.min(10, diff); diff = Math.max(-10, diff);
		return wSitDiff[inning][diff+10] + lSitDiff[inning][diff+10];
	}
	
	void setPreviousSit(Scanner sits){
		for(int inning = 0; inning < 9 * 6; ++inning){
			for(int diff = -10; diff < 11; diff++){
				wSitDiff[inning][diff+10] = sits.nextInt();
				lSitDiff[inning][diff+10] = sits.nextInt();
			}
		}
		
	}
	
	String writeSit(int inning, int diff){
		return wSitDiff[inning][diff + 10]  + " " + lSitDiff[inning][diff + 10];		
	}
	
	
	double getMarginOfError(int inning, int myScore, int oppScore){
		int wins = wSit[inning][myScore][oppScore];
		int total = 0;
		if(do_diff)
			total = getTrials(inning, myScore - oppScore);
		else
			total = getTrials(inning, myScore, oppScore);
		
		double ave = wins*1./total;
		double adj_ave = (wins + 2.) / (total + 2.);
		double x = (adj_ave * (1 - adj_ave)) / (total + 2.);
		return 2 * Math.sqrt(x);
		
	}
}
