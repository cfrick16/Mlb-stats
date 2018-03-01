import java.io.*;
import java.util.*;
import java.util.*;
import java.util.Map.Entry;
public class stats {
	
	public static Situation mySit; 
	public static Map<String, player> allPlayers = new HashMap<String, player>();
	public static Map<String, team> allTeams = new HashMap<String, team>();
	public static BufferedWriter writer;
	public static boolean setSits = false;
	
	public static void main(String [] args) throws IOException{
		Scanner kb = new Scanner(System.in);
		
		WrapInt start = new WrapInt();
		WrapInt end = new WrapInt();
		File[] files = selectFiles(start, end, kb);
		
		long start_time = System.nanoTime();
		mySit = new Situation();
		
		File prevSits = new File("./save_sits/" + start.x + "-" + end.x + ".sits");
		boolean viewSitsOnly = false;
		if(prevSits.exists()){
			mySit.setPreviousSit(new Scanner(prevSits)); 
			System.out.println("Previous sit data detected\nWould you like to view only situations?"
					+ "\n1. View Situations\n2. Continue to parse data");
			viewSitsOnly = kb.nextInt() == 1;
			if(viewSitsOnly)
				printSits();
		} else {
			setSits = true;
		}
		if(!viewSitsOnly){
			parseFiles(files);
			if(setSits){
				storeSits(start.x, end.x);
			}
			statTeams();
		
		
			System.out.println("TOTAL TIME: " + (System.nanoTime()  - start_time));
			int printout = 1;
			while(printout > 0 && printout < 5){
				System.out.println("What would you like to View?\n\t1. Print Player Stats\n\t2. Print Standings\n\t"
						+ "3. Print Odds of Winning in a Situation\n\tAny Other Number: End");
				
				printout = kb.nextInt();
	
				switch(printout){
					case 1:	System.out.println("How many players would you like to see?");
							printPlayers(allPlayers, kb.nextInt());
							break;
					case 2: printTeams(allTeams);
							break;
					case 3:	printSits();
							break;
					//case 4: storeSits(start.x, end.x);
				}
			}
		} else{
			System.out.println("Type 1 to try another situation");
			while(kb.nextInt() == 1){
				printSits();
				System.out.println("Type 1 to try another situation or any other number to quit");		
			}
		}
		
		writer.close();
	}
	
	public static void storeSits(int startYear, int endYear) throws IOException{ 
		BufferedWriter storeSits = 
				new BufferedWriter(new FileWriter("./save_sits/" + startYear +"-" + endYear + ".sits"));
		for(int inning = 0; inning < 9 * 6; ++inning){
			for(int diff = -10; diff < 11; diff++){
				storeSits.write(mySit.writeSit(inning,diff) + " ");
			}
		}
		
		storeSits.close();

	}
	
	public static void printSits() throws IOException{
		Scanner kb = new Scanner(System.in);
		System.out.println("Please enter inning range: (min then max)");
		int low_innings = (kb.nextInt()-1) * 6;
		int high_innings = (kb.nextInt()) * 6;
		System.out.println("Please enter score Range: ");
		System.out.println("Home Score: (min then max)");
		int home_score_min = kb.nextInt();
		int home_score_max = kb.nextInt();
		System.out.println("Away Score: (min then max");
		int away_score_min = kb.nextInt();
		int away_score_max = kb.nextInt();
		
		System.out.println("Write results to File? \t 1 for yes 2 for no");
		boolean writeToFile = kb.nextInt() == 1;

		for(int j = home_score_min; j <= home_score_max; j++){
			for(int k = away_score_min; k <= away_score_max; k++){

				quickPrint(String.format("Home: %d, Away: %d\n", j, k), writeToFile);

				for(int i = low_innings; i < high_innings; i++){					
					quickPrint(String.format("\tInning: %.1f\n", Game.getInning(i)), writeToFile);
					if(mySit.getSit(i, j, k) != -1){
						quickPrint(String.format("\t\tOdds of home team winning: %.3f +- %.4f\n",
								mySit.getSit(i,j,k),mySit.getMarginOfError(i, j, k)), writeToFile);
						quickPrint(String.format("\t\tTrials: " + mySit.getTrials(i,j,k)+ "\n"), writeToFile);
					}
				}
			}
		}
	}
	
	public static void quickPrint(String str, boolean writeToFile) throws IOException{
		System.out.print(str);
		if(writeToFile)
			writer.write(str);
	}
	
	public static void printGames(ArrayList <Game> games, String home, String away){
		for(Game g : games){
			if((home == null || g.home.equals(allTeams.get(home))) && (away == null || g.away.equals(allTeams.get(away))))
				System.out.println(g);
		}
	}

	public static void printGames(ArrayList <Game> games, String team){
		printGames(games, team, null);
		printGames(games, null, team);
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
	
	public static File[] selectFiles(WrapInt start, WrapInt end, Scanner kb) throws IOException{
		ArrayList<String> possible_years = new ArrayList<String>();
		System.out.println("Possible Years: ");
		File years = new File("./teams/");
		boolean first = true;
		for(File f : years.listFiles()){
			if(first)
				first = false;
			else
				System.out.print(", ");
			
			possible_years.add(f.getName());
			System.out.print(f.getName());
		}
		System.out.println();
		System.out.println("Enter start year:");
		start.x = kb.nextInt();
		System.out.println("Enter end year (exclusive):");
		end.x = kb.nextInt();
		File [] files = getFiles("./teams/", getYearRange(start, end, possible_years));
		System.out.println(start.x + " - " + end.x);
		writer = new BufferedWriter(new FileWriter("./results/" + start.x +"-" + end.x + ".out"));
		return files;
	}
	
	public static File[] getFiles(String location, int [] years){
		ArrayList <File> files = new ArrayList<>();
		// If years is empty, Add all
		if(years.length == 0){
			File []dir = new File(location).listFiles();
			for(File k : dir){
				File [] yearlyFiles = new File(location + k.getName() + "/").listFiles();
				for(File f : yearlyFiles)
					files.add(f);
			}
			return files.toArray(new File[files.size()]);
		}
		
		for(int year : years){
			assert((new File(location + year + "/").exists()));
			File [] yearlyFiles = new File(location + year + "/").listFiles();
			for(File f : yearlyFiles)
				files.add(f);
			
		}
		return files.toArray(new File[files.size()]);
	}
	
	// Lo inclusive, Hi exclusive
	public static int[] getYearRange(WrapInt lo, WrapInt hi, ArrayList<String> possible_years) 
			throws FileNotFoundException{
		int [] nums = new int[hi.x-lo.x];
		int count = 0;
		while(lo.x < hi.x){
			if(possible_years.contains(lo.x+"")){
				nums[count++] = lo.x;
			}
			lo.x ++;
		}
		int [] ret = new int[count ];
		count = 0;
		while(count < ret.length){
			ret[count ] = nums[count];
			++count;
		}
		if(ret.length == 0){
			throw new FileNotFoundException("No Years in range found: " + lo.x + " " + hi.x);
		}
			
		lo.x = ret[0];
		hi.x = ret[ret.length-1];
		return ret;
	}

	public static void parseFiles(File[] files)throws IOException{
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
					//Switch these two statements to store game
					//games.add(new Game(gmCode.split("\n")));
					new Game(gmCode.split("\n"));
				gmCode = "";
			}

			System.out.println(file.getName());
			input.close();
		}
	}
}


class WrapInt{
	int x;
	
	WrapInt(){
		x = 0;
	}
	WrapInt(int _x){
		x = _x;
	}
}