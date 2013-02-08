import java.util.HashSet;
import java.util.Stack;
import java.util.Vector;

public class eightPuzzle
{
	static Board goal;
	
	public static void main(String[] args)
	{
		goal = new Board(new int[] {1,2,3,8,0,4,7,6,5});
		
		Board b = new Board(new int[] {1,3,4,8,6,2,7,0,5});//easy
		//Board b = new Board(new int[] {2,8,1,0,4,3,7,6,5});//medium
		//Board b = new Board(new int[] {2,8,1,4,6,3,0,7,5});//hard
		//Board b = new Board(new int[] {5,6,7,4,0,8,3,2,1});//worst
		
		eightPuzzle solver = new eightPuzzle();
		//solver.dfs(b);
		solver.bfs(b);
	}
	
	
	/*
	 * This method implements blind depth-first search on the 8 puzzle,
	 *   keeping track of visited nodes to avoid infinite search. This 
	 *   method also outputs the first 15 nodes visited.
	 */
	public void dfs(Board b)
	{
		System.out.println("===DFS===");
		int count = 0;//used to output the first 15 nodes visited
		String first15states = "";
		HashSet<String> observedNodes = new HashSet<String>();//keeps track of visited states
		Stack<Board> stack = new Stack<Board>();//holds future states to explore 
		
		while(!b.equals(goal))
		{
			observedNodes.add(b.toString()); //add current state to observed nodes
			stack.addAll(b.getSuccessors()); //add all successors to stack
			b = stack.pop();				 //get first successor from stack
			while(observedNodes.contains(b.toString())) //go to the next unobserved state
			{
				b = stack.pop(); 
			}
			if(count < 15)
			{
				first15states += b + "\n";
				count++;
			}
			System.out.println(b);
		}
		System.out.println(observedNodes.size() + " nodes examined.");
		if(observedNodes.size() < 10000)
			printHistory(b);
		else
			System.out.println("Not printing history--leads to stack overflow");
		System.out.println(first15states);
	}
	public void bfs(Board b){
		System.out.println("===BFS===");

		int count = 0;
		String first15states = "";
		HashSet<String> observedNodes = new HashSet<String>();
		Stack<Board> stack = new Stack<Board>();
		if(!b.equals(goal))
		{
			observedNodes.add(b.toString()); //add first board to list of observed nodes
			stack.addAll(b.getSuccessors()); //get successors
			b = stack.pop();				 //get first successor
			while(observedNodes.contains(b.toString()))	//go to first unobserved node in stack
			{
				b = stack.pop();
			}
			//System.out.println(b);
			Stack<Board> tempStack = new Stack<Board>();
			while(!b.equals(goal)){						//checks board for goal
				while(observedNodes.contains(b.toString()))	//go to first unobserved node in stack
				{
					b = stack.pop();
				}
				while(!stack.empty() && !b.equals(goal)){ //go through stack and check for solution
					observedNodes.add(b.toString()); //adds to list of observed nodes
					tempStack.push(b);   //adds observed nodes to temporary stack
					b = stack.pop();     //gets next node
					while(observedNodes.contains(b.toString()))	//go to first unobserved node in stack
					{
						b = stack.pop();
					}
					//System.out.println(b);
				}
				if(!b.equals(goal)){    //goes through list of observed nodes and generates successors
					while(!tempStack.empty()){ //loads successors of current frontier into stack
						stack.addAll(tempStack.pop().getSuccessors());//adds successors to stack
					}
				}
			}
			if(count < 15){
				first15states += b + "\n";
				count++;
			}
		}
		if(observedNodes.size() < 10000){
			printHistory(b);
		}
		else{
			System.out.println("Not printing history--leads to stack overflow");
		}
		System.out.println(first15states);
	}
	
	/*
	 * This method prints the move history of the beginning state to the current state 
	 *   where each move is understood as moving the blank so that
	 *   1 2 3         1 2 3
	 *   4 5 6    to   4 5 6   is understood as "Left" rather than "Right"
	 *   7 8 0         7 0 8 
	 */
	public void printHistory(Board b)
	{
		Vector<Board> boards = b.getPathFromStartNode();
		System.out.println();
		for(int i = 0; i < boards.size()-1; i++)
		{
			if(boards.get(i).getSuccessor(Board.DOWN) != null)
				if(boards.get(i).getSuccessor(Board.DOWN).equals(boards.get(i+1)))
					System.out.print("Down ");
			if(boards.get(i).getSuccessor(Board.LEFT) != null)
				if(boards.get(i).getSuccessor(Board.LEFT).equals(boards.get(i+1)))
					System.out.print("Left ");
			if(boards.get(i).getSuccessor(Board.RIGHT) != null)
				if(boards.get(i).getSuccessor(Board.RIGHT).equals(boards.get(i+1)))	
					System.out.print("Right ");
			if(boards.get(i).getSuccessor(Board.UP) != null)
				if(boards.get(i).getSuccessor(Board.UP).equals(boards.get(i+1)))
					System.out.print("Up ");
		}
		System.out.println();
	}
}