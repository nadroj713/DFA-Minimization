

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class dfamin {
	
	int numDFAs;
	int numStates;
	int alphaSize;
	int numAcceptStates;
	int[] acceptStates;
	int[][][] arrayDFA;
	int[][] minDFA;
	int[][] resultDFA;
	
	
	public class Pair
	{
		int i;
		int j;
		
		
		public Pair(int x, int y)
		{
			this.i = x;
			this.j = y;
		}
	}
	
	public static int[][] setUp(int[][][] arrayDFA, int[][] minDFA, int[] acceptStates, int numStates, int numAcceptStates)
	{
		//For each state i < j if one of the two is an accept state and the other isn't, mark the two as unique to each other
		for(int i = 0; i < numStates; i++)
		{
			for(int j = 0; j < numStates; j++)
			{
				
				if(i >= j);
				
				else if(arraySearch(acceptStates, i, numAcceptStates) == true && 
						arraySearch(acceptStates, j, numAcceptStates) == true == false)
				minDFA[i][j] = 1;
				
				else if(arraySearch(acceptStates, j, numAcceptStates) == true && 
						arraySearch(acceptStates, i, numAcceptStates) == false)
					minDFA[i][j] = 1;
				
			}
		}
		
		return minDFA;
	}
	
	public static int min(int x, int y)
	{
		
		if(x >= y)
			return y;
		
		return x;
	}
	
	public static int max(int x, int y)
	{
		
		if(x >= y)
			return x;
		
		return y;
	}
	
	public static int[][] Dist(int i, int j, int[][]minDFA, List<dfamin.Pair>[][] arrayPairs)
	{
		//Mark that the states split
		minDFA[i][j] = 1;
		
		int numPairs = arrayPairs[i][j].size();
		//follow the saved pairs and mark them as split as well
		Pair testPair = null;
		for (int x = 0; x < numPairs; x++)
		{
			testPair = arrayPairs[i][j].get(x);
			if(testPair.i != i || testPair.j != j)
				minDFA = Dist(testPair.i, testPair.j, minDFA, arrayPairs);
		}
		
		return minDFA;
	}
	
	public static boolean arraySearch(int[] array, int goal, int length)
	{
		for(int i = 0; i < length; i++)
		{
			if(array[i] == goal)
				return true;
		}
		
		return false;
		
	}
	
	public static boolean listSearch(List<dfamin.Pair> state, Pair target)
	{
		int size = state.size();
		for(int x = 0; x < size; x++)
		{
			if(target.i == state.get(x).i && target.j == state.get(x).j)
				return true;
		}
		
		return false;
	}
	
	public static int listPairSearch(List<dfamin.Pair> state, int target)
	{
		int size = state.size();
		for(int x = 0; x < size; x++)
		{
			if(target == state.get(x).j)
				return state.get(x).i;
		}
		
		return -1;
	}
	
	public static int listPairComparison(List<dfamin.Pair> state, int target)
	{
		int size = state.size();
		int total = 0;
		for(int x = 0; x < size; x++)
		{
			if(target > state.get(x).j)
				total++;
		}
		
		return total;
	}
	
	public static int listPairCount(List<dfamin.Pair> state, int[] oldAcceptStates, int oldNumAcceptStates, int target)
	{
		int total = 0;
		for(int x = 0; x < oldNumAcceptStates; x++)
		{
			if(target > oldAcceptStates[x])
			{
				if(listPairSearch(state, oldAcceptStates[x]) > -1)
					total++;
			}
		}
		
		return total;
	}
	

	
	public static int[][] minimize(dfamin DFAMin, int[][][] arrayDFA, int[][] minDFA, List<dfamin.Pair>[][] arrayPairs, 
			int[] acceptStates, int numStates, int numAcceptStates, int alphaSize)
	{
		boolean foundTrans = false;
		Pair testPair = DFAMin.new Pair(0,0);
		minDFA = setUp(arrayDFA, minDFA, acceptStates, numStates, numAcceptStates);

		print2DArray(minDFA, numStates, numStates);
		for(int i = 0; i < numStates; i++)
		{
			for(int j = 0; j < numStates; j++)
			{
				if(i < j && minDFA[i][j] == 0)
				{
					print2DArray(minDFA, numStates, numStates);

					//Check for Possibility 1
					//If there exists a transition in the language where
					//i and j are connected to separate states by the
					//Same transition and those new states have a 1 in minDFA, run Dist(i, j)
					for(int x = 0; x < alphaSize; x++)
					{
						for(int y = 0; y < numStates; y++)
						{
							if(arrayDFA[i][y][x] >= 0)
							{
								for(int z = 0; z < numStates; z++)
								{										
									if(arrayDFA[j][z][x] == arrayDFA[i][y][x] && (i != min(z,y) || j != max(z,y)) )
									{
										if(minDFA[min(z,y)][max(z,y)] == 1 )
										{
										minDFA = Dist(i, j, minDFA, arrayPairs);

										foundTrans = true;
										break;
										}
										
									}
								}
							}
							
							if(foundTrans == true)
								break;
						}
						
						if(foundTrans == true)
							break;
						
							
					}
					
					if(foundTrans == true)
						foundTrans = false;
					
					//Possibility 2
					else if(i < j)
					{
						//For each transition in the alphabet, if i and j can reach 
						//separate states with the same transition type, add the current
						//i j pair to those lists in S
						for(int x = 0; x < alphaSize; x++)
						{
							for(int y = 0; y < numStates; y++)
							{
								if(arrayDFA[i][y][x] >= 0)
								{
									for(int z = 0; z < numStates; z++)
									{
										if( (y < z || z < y) && arrayDFA[i][y][x] == arrayDFA[j][z][x] && (i != min(z, y) || j != max(z, y)) )
										{
											testPair.i = i;
											testPair.j = j;

												arrayPairs[min(y,z)][max(y,z)].add( DFAMin.new Pair(i, j));

											
											
											
												
										}
											
									}
								}
							}
						}
					}
					
					
				}
				
			}
		}
		

		return minDFA;
		
	}
	
	public static void eliminate(dfamin DFAMin, int numDFAs, int[][][] arrayDFA, int[][] minDFA, 
			int[] acceptStates, int numStates, int numAcceptStates, int alphaSize)
	{
		List<dfamin.Pair> eliminations = new ArrayList();
		for(int i = 0; i < numStates; i++)
		{
			for(int j = 0; j < numStates; j++)
			{
				if(i < j && minDFA[i][j] == 0 && listPairSearch(eliminations, j) == -1)
				{
					eliminations.add(DFAMin.new Pair(i, j));
				}
			}
		}
		
		int newNumStates = numStates - eliminations.size();
		int currentState = 0;
		int[][] newDFA = new int[newNumStates][alphaSize];
		
		//Initialize result DFA
		for(int i = 0; i < numStates; i++)
		{
			//Check to see if i is an eliminated state
			if(listPairSearch(eliminations, i) > -1);
				//If so, skip it and don't touch currentState
				
			
			else
			{
				//If not, search through the ith row in arrayDFA and copy the values greater than -1 to their respective places in the newDFA
					for(int l = 0; l < numStates; l++)
					{
						for(int m = 0; m < alphaSize; m++)
						{
							if(arrayDFA[i][l][m] > -1 )
							{
								newDFA[currentState][m] = l;
							}
								
						
						}
						
					}
		
				currentState++;
				
			}
		}
		
		//Replace eliminated transitions in newDFA with their replacements specified in eliminations list
		int deletedTrans = -1;
		for(int i = 0; i < newNumStates; i++)
		{
			for(int j = 0; j < alphaSize; j++)
			{
				deletedTrans = listPairSearch(eliminations, newDFA[i][j]);
				if(deletedTrans > -1)
				{
					newDFA[i][j] = deletedTrans;
				}
			}
		}
		
		//Rename eliments greater than elements removed
		for(int i =0; i < newNumStates; i++)
		{
			for(int j = 0; j < alphaSize; j++)
			{
				currentState = listPairComparison(eliminations, newDFA[i][j]);
				newDFA[i][j] = newDFA[i][j] - currentState;
			}
		}
		
		//Find new number of accept states
		int newNumAcceptStates = 0;
		for(int i = 0; i < numAcceptStates; i++)
		{
			deletedTrans = listPairSearch(eliminations, acceptStates[i]);
			if(deletedTrans > 0)
			{
				newNumAcceptStates++;
				acceptStates[i] = -1;
			}
				
		}
		
		newNumAcceptStates = numAcceptStates - newNumAcceptStates;
		int[] newAcceptStates = new int[newNumAcceptStates];
		currentState = 0;
		//Create new array for accept states that havent been eliminated
		for(int i = 0; i < numAcceptStates; i++)
		{
			if(acceptStates[i] > -1)
			{
				newAcceptStates[currentState] = acceptStates[i];
				currentState++;
			}
				
		}
		
		//Rename accept states greater than those that have been deleted
		for(int i = 0; i < newNumAcceptStates; i++)
		{
			newAcceptStates[i] = newAcceptStates[i] - listPairCount(eliminations, acceptStates, numAcceptStates, newAcceptStates[i]);
		}
		
		
		printAnswer(numDFAs, newDFA, newNumStates, alphaSize, newNumStates, alphaSize, newNumAcceptStates, newAcceptStates);
		
	}
	
	public static void printArray(int[] array, int length)
	{
		for(int i = 0; i < length; i++)
			System.out.print(array[i] + " ");
		
		System.out.print("\n");
	}
	
	public static void print2DArray(int[][] array, int length, int width)
	{
		for(int i = 0; i < length; i++)
		{
			for(int j = 0; j < width; j++)
			{
				System.out.print( array[i][j] + " ");
			}
			
			System.out.print("\n");
		}
		
		System.out.print("\n");
				
	}
	
	public static void printAnswer(int dfaNum, int[][] array, int length, int width, int numStates, int alphaSize, int numAcceptStates, int[] acceptStates)
	{
		System.out.println("DFA #" + dfaNum + ":");
		System.out.println(numStates + " " + alphaSize);
		System.out.print(numAcceptStates + " ");
		for(int i = 0; i < numAcceptStates; i++)
		{
			System.out.print(acceptStates[i] + " ");
		}
		
		System.out.println("");
		
		for(int i = 0; i < length; i++)
		{
			for(int j = 0; j < width; j++)
			{
				System.out.print( array[i][j] + " ");
			}
			
			System.out.print("\n");
		}
		
		System.out.print("\n");
				
	}
	
	
	public static int[] DFS(int[][][] arrayDFA, int[][] transitions, int[] visited, int state, int numStates, int alphaSize)
	{
		//Mark state as visited
		visited[state] = 1;
		
		//Find the states this state is connected to and follow it
		for(int newState = 0; newState < numStates; newState++)
		{
			for(int alpha = 0; alpha < alphaSize; alpha++)
			{
				if(arrayDFA[state][newState][alpha] >= 0 && transitions[state][newState] != 1)
				{
					transitions[state][newState] = 1;
					visited = DFS(arrayDFA, transitions, visited, newState, numStates, alphaSize);
				}
					
			}
		}
		
		//move back to the previous state
		return visited;
	}
	
	public static dfamin dfaDFS( dfamin DFAMin, int[][][] arrayDFA, int[] acceptStates, int numStates, int numAcceptStates, int alphaSize)
			{
		
				//Initialize necessary structures
				int[][] transitions = new int[numStates][numStates];
				int[] visited = new int[numStates];
				for(int i = 0; i < numStates; i++)
				{
					visited[i] = 0;
					for(int j = 0; j < numStates; j++)
					transitions[i][j] = 0;
				}
				
				//Start at start state and follow paths until all valid paths are taken and record the states you bisit
				visited[0] = 1;
				for(int state = 0; state < numStates; state++)
				{
						for(int alpha = 0; alpha < alphaSize; alpha++)
						{
							if(arrayDFA[0][state][alpha] >= 0)
							{
								transitions[0][state] = 1;
								visited = DFS(arrayDFA, transitions, visited, state, numStates, alphaSize);
							}
						}	
				}
				
				int newNumStates = numStates;
				int currentStateX = 0;
				int currentStateY = 0;
				List<Integer>removedStates = new ArrayList<Integer>();
				//Count the number of remaining states, add the removed ones to a list
				for(int x = 0; x < numStates; x++)
					if(visited[x] == 0)
					{
						newNumStates--;
						removedStates.add(x);
					}
						
				//If there is no difference in states, save some time and return now
				if(newNumStates == numStates)
					return DFAMin;
				
				int [][][] newDFA = new int[newNumStates][newNumStates][alphaSize];
				
				//Copy relevant old transitions to the new array by using two sets of indexes
				for(int i = 0; i < numStates; i++)
				{
					if(visited[i] == 1)
					{
						for(int j = 0; j < numStates; j++)
						{
							if(visited[j] == 1)
							{
								for(int alpha = 0; alpha < alphaSize; alpha++)
								{
									newDFA[currentStateX][currentStateY][alpha] = arrayDFA[i][j][alpha];
									
								}
								
								currentStateY++;
								if(currentStateY == newNumStates)
								{
									currentStateX++;
									currentStateY = 0;
								}
							}
						}
					}
				}
				
				
				//Update accept states, first by counting how many you have, then by updating the ones greater than eliminated states
				boolean foundState = false;
				int newNumAcceptStates = numAcceptStates;
				int acceptDifference = 0;
				for(int i = 0; i < numAcceptStates; i++)
				{
					if(removedStates.contains(acceptStates[i]))
					{
						acceptStates[i] = -1;
						newNumAcceptStates--;
						acceptDifference++;
					}
					
					else
					{
						acceptStates[i] = acceptStates[i] - acceptDifference;
						acceptDifference = 0;
					}
						
				}
				
				int[] newAcceptStates = new int[newNumAcceptStates];
				currentStateX = 0;
				for(int i = 0; i < numAcceptStates; i++)
				{
					if(acceptStates[i] != -1)
					{
						newAcceptStates[currentStateX] = acceptStates[i];
						currentStateX++;
					}
				}
				
				//Save the new information and return it
				DFAMin.numStates = newNumStates;
				DFAMin.numAcceptStates = newNumAcceptStates;
				DFAMin.acceptStates = newAcceptStates;
				DFAMin.arrayDFA = newDFA;
				
				return DFAMin;
			}
	
	public static void main (String [] args) throws java.io.IOException
	{
		//Create an instance of dfamin for function returning
		dfamin DFAMin = new dfamin();
		int numDFAs;
		int numStates;
		int alphaSize;
		int numAcceptStates;
		int[] acceptStates;
		int[][][] arrayDFA;
		int[][] minDFA;
		int[][] resultDFA;
		List<dfamin.Pair>[][] arrayPairs ;
		@SuppressWarnings("resource")
		Scanner input = new Scanner(new FileReader("input.txt"));
		numDFAs = input.nextInt();
		
		//Fill out initial information
		for(int i = 0; i < numDFAs; i++)
		{
			numStates = input.nextInt();	
			arrayPairs = (List<dfamin.Pair>[][])new ArrayList[numStates][numStates];
			for(int j = 0; j < numStates; j++)
				for(int k = 0; k < numStates; k++)
					arrayPairs[j][k] = new ArrayList();
						
			minDFA = new int[numStates][numStates];
			for(int j = 0; j < numStates; j++)
				for(int k = 0; k < numStates; k++)
					minDFA[j][k] = 0;
			
			alphaSize = input.nextInt();
			
			arrayDFA = new int[numStates][numStates][alphaSize];
			for(int j = 0; j < numStates; j++)
				for(int k = 0; k < numStates; k++)
					for(int l = 0; l < alphaSize; l++)
					arrayDFA[j][k][l] = -1;
			
			numAcceptStates = input.nextInt();
			acceptStates = new int[numAcceptStates];
			
			for(int j = 0; j < numAcceptStates; j++)
			{
				acceptStates[j] = input.nextInt();
			}
			
			for(int j = 0; j < numStates; j++)
			{
				for(int k = 0; k < alphaSize; k++)
				{
					arrayDFA[j][input.nextInt()][k] = k;
				}
			}
			
			//Save initial information
			DFAMin.arrayDFA = arrayDFA;
			DFAMin.numStates = numStates;
			DFAMin.numAcceptStates = numAcceptStates;
			DFAMin.acceptStates = acceptStates;
			
			//Call the DFS to search for unreachable states and eliminate them from arrayDFA
			DFAMin = dfaDFS(DFAMin, arrayDFA, acceptStates, numStates, numAcceptStates, alphaSize);
			
			//Save carried data to working regs
			arrayDFA = DFAMin.arrayDFA;
			numStates = DFAMin.numStates;
			numAcceptStates = DFAMin.numAcceptStates;
			acceptStates = DFAMin.acceptStates;			
			
			//Find the states that split
			minDFA = minimize(DFAMin, arrayDFA, minDFA, arrayPairs, acceptStates, numStates, numAcceptStates, alphaSize);
			//Eliminate the ones that don't and fuse them into corresponding states, then print the answer
			eliminate( DFAMin, i + 1, arrayDFA, minDFA, acceptStates, numStates, numAcceptStates, alphaSize);
			
			
		}
		
		
	}

}
