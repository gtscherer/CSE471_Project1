import java.util.HashSet;
import java.util.Stack;
import java.util.Vector;

public class eightPuzzle
{
	static Board goal;
	
	public static void main(String[] args)
	{
		goal = new Board(new int[] {1,2,3,8,0,4,7,6,5});
		
		//Board b = new Board(new int[] {1,3,4,8,6,2,7,0,5});//easy
		//Board b = new Board(new int[] {2,8,1,0,4,3,7,6,5});//medium
		//Board b = new Board(new int[] {2,8,1,4,6,3,0,7,5});//hard
		Board b = new Board(new int[] {5,6,7,4,0,8,3,2,1});//worst
		
		eightPuzzle solver = new eightPuzzle();
		//solver.dfs(b);
		//solver.bfs(b); //1
		//solver.bestFirst(b); //2
		//solver.AStar(b); //3
		//solver.AStarManhattan(b); //4
		//solver.AStarManhattan2(b); //5
		//solver.iterativeDeepening(b); //6
		solver.iterativeDeepeningAStar(b); //7
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
			//System.out.println(b);
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
			if(count < 15){
				first15states += b + "\n";
				count++;
			}
			while(!b.equals(goal)){						//checks board for goal
				while(observedNodes.contains(b.toString()))	//go to first unobserved node in stack
				{
					b = stack.pop();
				}
				while(!stack.empty() && !b.equals(goal)){ //go through stack and check for solution
					observedNodes.add(b.toString()); //adds to list of observed nodes
					tempStack.push(b);   //adds observed nodes to temporary stack
					b = stack.pop();     //gets next node
					while(observedNodes.contains(b.toString()) && !stack.empty())	//go to first unobserved node in stack
					{
						b = stack.pop();
					}
					if(count < 15){
						first15states += b + "\n";
						count++;
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
		System.out.println(observedNodes.size() + " nodes examined.");
		if(observedNodes.size() < 10000){
			printHistory(b);
		}
		else{
			System.out.println("Not printing history--leads to stack overflow");
		}
		System.out.println(first15states);
	}

	
	public void bestFirst(Board b){

		System.out.println("===Best-First===");
		int count = 0;//used to output the first 15 nodes visited
		String first15states = "";
		HashSet<String> observedNodes = new HashSet<String>();//keeps track of visited states
		Stack<Board> stack = new Stack<Board>();//holds future states to explore 
		Vector<Board> open = new Vector<Board>();
		while(!b.equals(goal)){
			observedNodes.add(b.toString()); //add current state to observed nodes
			stack.addAll(b.getSuccessors()); //add all successors to stack
			if(count < 15)
			{
				first15states += b + "\n";
				count++;
			}
			while(!stack.empty()){
				b=stack.pop();
				if(!observedNodes.contains(b.toString())){
					b.setCostEstimate(heuristic(b));
					open.add(b);
				}
			}
			open = sortByCost(open, open.size());
			b = open.elementAt(0);
			open.removeElementAt(0);
			//System.out.println(b);
		}
		System.out.println(observedNodes.size() + " nodes examined.");
		if(observedNodes.size() < 10000)
			printHistory(b);
		else
			System.out.println("Not printing history--leads to stack overflow");
		System.out.println(first15states);
	}

	
	public void AStar(Board b){

		System.out.println("===A*===");
		int count = 0;//used to output the first 15 nodes visited
		String first15states = "";
		HashSet<String> observedNodes = new HashSet<String>();//keeps track of visited states
		Stack<Board> stack = new Stack<Board>();//holds future states to explore 
		Vector<Board> successors = new Vector<Board>();
		while(!b.equals(goal)){
			observedNodes.add(b.toString()); //add current state to observed nodes
			stack.addAll(b.getSuccessors()); //add all successors to stack
			if(count < 15)
			{
				first15states += b + "\n";
				count++;
			}
			while(!stack.empty()){
				b=stack.pop();
				if(!observedNodes.contains(b.toString())){
					b.setCostEstimate(heuristic(b) + b.getPathFromStartNode().size());
					successors.add(b);
				}
			}
			successors = sortByCost(successors, successors.size());
			b = successors.elementAt(0);
			successors.removeElementAt(0);
			//System.out.println(b);
		}
		System.out.println(observedNodes.size() + " nodes examined.");
		if(observedNodes.size() < 10000)
			printHistory(b);
		else
			System.out.println("Not printing history--leads to stack overflow");
		System.out.println(first15states);
	}
	
	public void AStarManhattan(Board b){

		System.out.println("===A* Manhattan===");
		int count = 0;//used to output the first 15 nodes visited
		String first15states = "";
		HashSet<String> observedNodes = new HashSet<String>();//keeps track of visited states
		Stack<Board> stack = new Stack<Board>();//holds future states to explore 
		Vector<Board> successors = new Vector<Board>();
		while(!b.equals(goal)){
			observedNodes.add(b.toString()); //add current state to observed nodes
			stack.addAll(b.getSuccessors()); //add all successors to stack
			if(count < 15)
			{
				first15states += b + "\n";
				count++;
			}
			while(!stack.empty()){
				b=stack.pop();
				if(!observedNodes.contains(b.toString())){
					b.setCostEstimate(heuristicManhattan(b) + b.getPathFromStartNode().size());
					successors.add(b);
				}
			}
			successors = sortByCost(successors, successors.size());
			b = successors.elementAt(successors.size() - 1);
			successors.removeElementAt(successors.size() - 1);
			//System.out.println(b);
		}
		System.out.println(observedNodes.size() + " nodes examined.");
		if(observedNodes.size() < 10000)
			printHistory(b);
		else
			System.out.println("Not printing history--leads to stack overflow");
		System.out.println(first15states);
	}
	
	public void AStarManhattan2(Board b){

		System.out.println("===A* Manhattan x2===");
		int count = 0;//used to output the first 15 nodes visited
		String first15states = "";
		HashSet<String> observedNodes = new HashSet<String>();//keeps track of visited states
		Stack<Board> stack = new Stack<Board>();//holds future states to explore 
		Vector<Board> successors = new Vector<Board>();
		while(!b.equals(goal)){
			observedNodes.add(b.toString()); //add current state to observed nodes
			stack.addAll(b.getSuccessors()); //add all successors to stack
			if(count < 15)
			{
				first15states += b + "\n";
				count++;
			}
			while(!stack.empty()){
				b=stack.pop();
				if(!observedNodes.contains(b.toString())){
					b.setCostEstimate((heuristicManhattan(b)*2) + b.getPathFromStartNode().size());
					successors.add(b);
				}
			}
			successors = sortByCost(successors, successors.size());
			b = successors.elementAt(successors.size() - 1);
			successors.removeElementAt(successors.size() - 1);
			//System.out.println(b);
		}
		System.out.println(observedNodes.size() + " nodes examined.");
		if(observedNodes.size() < 10000)
			printHistory(b);
		else
			System.out.println("Not printing history--leads to stack overflow");
		System.out.println(first15states);
	}
	
	//maybe write a function that will search at certain depths and then use a loop...
	public void iterativeDeepening(Board b)
	{
		{
			System.out.println("===Iterative Deepening===");
			int count = 0;//used to output the first 15 nodes visited
			String first15states = "";
			HashSet<String> observedNodes = new HashSet<String>();//keeps track of visited states
			Vector<Board> vectorStk = new Vector<Board>();//holds future states to explore 
			Vector<Board> successors = new Vector<Board>();
			int depth = 0;
			successors.addElement(b);
			while(!b.equals(goal))
			{
				++depth;
				int i = 0;
				HashSet<String> observedNodesTemp = new HashSet<String>();
				while(!b.equals(goal) && successors.size() > i){
					observedNodes.add(b.toString()); //add current state to observed nodes
					observedNodesTemp.add(b.toString());
					vectorStk.addAll(b.getSuccessors()); //add all successors to stack
					if(count < 15)
					{
						first15states += b + "\n";
						count++;
					}
					while((vectorStk.size() > 0)){
						b = vectorStk.elementAt(0);
						b.setCostEstimate(i + 1);
						if(!(b.getCostEstimate() > depth)){
							successors.add(b);
						}
						vectorStk.removeElementAt(0);
					}
					b = successors.elementAt(0);
									 //get first successor from stack
					boolean notEmpty = true;
					while(observedNodesTemp.contains(b.toString()) && notEmpty) //go to the next unobserved state
					{
						int index = successors.indexOf(b);
						if((index + 1 < successors.size())){
							b = successors.elementAt(index + 1);
						}
						else{
							notEmpty = false;
						}
						successors.removeElementAt(index);
						//System.out.println(successors.size());
					}
					++i;
					//System.out.println(b);
	
				}
			}
			System.out.println(observedNodes.size() + " nodes examined.");
			if(observedNodes.size() < 10000)
				printHistory(b);
			else
				System.out.println("Not printing history--leads to stack overflow");
			System.out.println(first15states);
		}
	}
	public void iterativeDeepeningAStar(Board b)
	{
		{
			System.out.println("===Iterative Deepening A*===");
			int count = 0;//used to output the first 15 nodes visited
			String first15states = "";
			HashSet<String> observedNodes = new HashSet<String>();//keeps track of visited states
			Vector<Board> vectorStk = new Vector<Board>();//holds future states to explore 
			Vector<Board> successors = new Vector<Board>();
			int depth = heuristicManhattan(b) + b.getPathFromStartNode().size();
			int FirstDepth = depth;
			vectorStk = b.getSuccessors();
			successors.addElement(b);
			for(int i = 0; i < vectorStk.size(); ++i){
				vectorStk.elementAt(i).setCostEstimate(heuristicManhattan(vectorStk.elementAt(i)) + vectorStk.elementAt(i).getPathFromStartNode().size());
			}
			Board c = b;
			vectorStk = sortByCost(vectorStk, vectorStk.size());
			int nextDepth = vectorStk.elementAt(vectorStk.size() - 1).getCostEstimate();
			while(!b.equals(goal))
			{
				depth = nextDepth;
				int i = 0;
				HashSet<String> observedNodesTemp = new HashSet<String>();
				if(successors.size() < 0){
					successors.addElement(c);
					b = c;
				}
				while(!b.equals(goal) && successors.size() > i){
					observedNodes.add(b.toString()); //add current state to observed nodes
					observedNodesTemp.add(b.toString());
					vectorStk.addAll(b.getSuccessors()); //add all successors to stack
					if(count < 15)
					{
						first15states += b + "\n";
						count++;
					}
					int asdf = 0;
					while((vectorStk.size() > 0)){
						b = vectorStk.elementAt(0);
						b.setCostEstimate(i + 1);
						if(!(b.getCostEstimate() > depth)){
							successors.add(b);
						}
						else if(asdf == 0){
							nextDepth = b.getCostEstimate();
							++asdf;

						}
						else if(b.getCostEstimate() < nextDepth){
							nextDepth = b.getCostEstimate();
						}
						vectorStk.removeElementAt(0);
					}
					b = successors.elementAt(0);
									 //get first successor from stack
					boolean notEmpty = true;
					while(observedNodesTemp.contains(b.toString()) && notEmpty) //go to the next unobserved state
					{
						int index = successors.indexOf(b);
						if((index + 1 < successors.size())){
							b = successors.elementAt(index + 1);
						}
						else{
							notEmpty = false;
						}
						successors.removeElementAt(index);
						//System.out.println(successors.size());
					}
					++i;
					//System.out.println(b);
	
				}
				if(depth == nextDepth){
					nextDepth = depth + 1;
				}
			}
			System.out.println(observedNodes.size() + " nodes examined.");
			if(observedNodes.size() < 10000)
				printHistory(b);
			else
				System.out.println("Not printing history--leads to stack overflow");
			System.out.println(first15states);
		}
	}


/*
		public void iterativeDeepeningAStar(Board b)
	{
		{
			System.out.println("===Iterative Deepening A*===");
			int count = 0;//used to output the first 15 nodes visited
			String first15states = "";
			HashSet<String> observedNodes = new HashSet<String>();//keeps track of visited states
			Vector<Board> vectorStk = new Vector<Board>();//holds future states to explore 
			Vector<Board> successors = new Vector<Board>();
			int depth = heuristicManhattan(b) + b.getPathFromStartNode().size();
			int FirstDepth = depth;
			vectorStk = b.getSuccessors();
			successors.addElement(b);
			for(int i = 0; i < vectorStk.size(); ++i){
				vectorStk.elementAt(i).setCostEstimate(heuristicManhattan(vectorStk.elementAt(i)) + vectorStk.elementAt(i).getPathFromStartNode().size());
			}
			vectorStk = sortByCost(vectorStk, vectorStk.size());
			int nextDepth = vectorStk.elementAt(0).getCostEstimate();
			while(!b.equals(goal))
			{
				depth = nextDepth;
				HashSet<String> observedNodesTemp = new HashSet<String>();
				vectorStk.addAll(b.getSuccessors())
				if(!successors.empty()){
					while(!b.equals(goal) && successors.size() > 0){
					observedNodes.add(b.toString()); //add current state to observed nodes
					observedNodesTemp.add(b.toString());
					vectorStk.addAll(b.getSuccessors()); //add all successors to stack
					if(count < 15)
					{
						first15states += b + "\n";
						count++;
					}
					while((vectorStk.size() > 0)){
						b = vectorStk.elementAt(0);
						b.setCostEstimate(i + 1);
						if(!(b.getCostEstimate() > depth)){
							successors.add(b);
						}
						vectorStk.removeElementAt(0);
					}
					b = successors.elementAt(0);
									 //get first successor from stack
					boolean notEmpty = true;
					while(observedNodesTemp.contains(b.toString()) && notEmpty) //go to the next unobserved state
					{
						int index = successors.indexOf(b);
						if((index + 1 < successors.size())){
							b = successors.elementAt(index + 1);
						}
						else{
							notEmpty = false;
						}
						successors.removeElementAt(index);
						//System.out.println(successors.size());
					}
					++i;
					//System.out.println(b);
	
					}
				}
				
			}
			System.out.println(observedNodes.size() + " nodes examined.");
			if(observedNodes.size() < 10000)
				printHistory(b);
			else
				System.out.println("Not printing history--leads to stack overflow");
			System.out.println(first15states);
		}
	}
	
	/*
	public void iterativeDeepeningAStar(Board b)
	{
		{
			System.out.println("===Iterative Deepening A*===");
			int count = 0;//used to output the first 15 nodes visited
			String first15states = "";
			HashSet<String> observedNodes = new HashSet<String>();//keeps track of visited states
			Vector<Board> vectorStk = new Vector<Board>();//holds future states to explore 
			Vector<Board> successors = new Vector<Board>();
			int depth = heuristicManhattan(b) + b.getPathFromStartNode().size();
			int FirstDepth = depth;
			vectorStk = b.getSuccessors();
			successors.addElement(b);
			Board c = b;
			for(int i = 0; i < vectorStk.size(); ++i){
				vectorStk.elementAt(i).setCostEstimate(heuristicManhattan(vectorStk.elementAt(i)) + vectorStk.elementAt(i).getPathFromStartNode().size());
			}
			vectorStk = sortByCost(vectorStk, vectorStk.size());
			int nextDepth = vectorStk.elementAt(0).getCostEstimate();
			System.out.println("Depth " + depth + " nextDepth " + nextDepth);
			while(!b.equals(goal))
			{
				depth = nextDepth;
				nextDepth = FirstDepth;
				System.out.println("Depth = " + depth);
				HashSet<String> observedNodesTemp = new HashSet<String>();
				if(!(successors.size() > 0)){
					successors.addElement(c);
					b = c;
				}
				while(!b.equals(goal) && successors.size() > 0){
					observedNodes.add(b.toString()); //add current state to observed nodes
					observedNodesTemp.add(b.toString());
					vectorStk.removeAllElements();
					vectorStk.addAll(b.getSuccessors()); //add all successors to stack
					if(count < 15)
					{
						first15states += b + "\n";
						count++;
					}
					successors.removeElementAt(0);
					int i = 0;
					while((vectorStk.size() > 0)){
						b = vectorStk.elementAt(0);
						b.setCostEstimate(heuristicManhattan(b) + b.getPathFromStartNode().size());
						if((b.getCostEstimate() <= depth)){
							successors.add(b);
						}
						else if(i == 0){
							nextDepth = b.getCostEstimate();
							++i;

						}
						else if(b.getCostEstimate() < nextDepth && b.getCostEstimate() > depth){
							nextDepth = b.getCostEstimate();
						}
						vectorStk.removeElementAt(0);
					}
					successors = sortByCost(successors, successors.size());
					if(successors.size() > 0){
						b = successors.elementAt(0);
					}
					else{
						b = null;
					}
									 //get first successor from stack
					if(b != null){
						boolean notEmpty = true;
						while(observedNodesTemp.contains(b.toString()) && notEmpty) //go to the next unobserved state
						{
							int index = successors.indexOf(b);
							successors.removeElementAt(index);
							if((index + 1 < successors.size())){
								b = successors.elementAt(index + 1);
							}
							else{
								notEmpty = false;
							}
							System.out.println(successors.size());
						}
						System.out.println(b);
					}
				}
			}
			System.out.println(observedNodes.size() + " nodes examined.");
			if(observedNodes.size() < 10000)
				printHistory(b);
			else
				System.out.println("Not printing history--leads to stack overflow");
			System.out.println(first15states);
		}
	}

	*/
	/*
	public void iterativeDeepeningAStar(Board b)
	{
		{
			System.out.println("===Iterative Deepening A*===");
			int count = 0;//used to output the first 15 nodes visited
			String first15states = "";
			HashSet<String> observedNodes = new HashSet<String>();//keeps track of visited states
			Vector<Board> vectorStk = new Vector<Board>();//holds future states to explore 
			Vector<Board> successors = new Vector<Board>();
			int depth = heuristicManhattan(b) + b.getPathFromStartNode().size();
			successors.addElement(b);
			while(!b.equals(goal))
			{
				int i = heuristicManhattan(b) + b.getPathFromStartNode().size();
				int successorLevel = i;
				HashSet<String> observedNodesTemp = new HashSet<String>();
				while(!b.equals(goal) && successors.size() > 0){
					int nextI = i;
					System.out.println(i);
					observedNodes.add(b.toString()); //add current state to observed nodes
					observedNodesTemp.add(b.toString());
					vectorStk = (b.getSuccessors()); //add all successors to stack
					if(count < 15)
					{
						first15states += b + "\n";
						count++;
					}
					int tempcount = 1;
					successors.removeElementAt(successors.indexOf(b));
					while(vectorStk.size() > 0){
						b = vectorStk.elementAt(0);
						System.out.println(b.toString());
						b.setCostEstimate(heuristicManhattan(b) + b.getPathFromStartNode().size());
						System.out.print("Before " + successorLevel);
						System.out.println("\nb.getCostEstimate: " + b.getCostEstimate() + " > depth: " + depth);
						if(!(b.getCostEstimate() > depth)){
							successors.add(b);
						}
						else if(tempcount == 1){
							++tempcount;
							successorLevel = b.getCostEstimate();
						}
						else if(tempcount > 1){
							int tempLevel =  b.getCostEstimate();
							if(successorLevel > tempLevel){
								successorLevel = tempLevel;
							}
						}
						System.out.println("After: " + successorLevel);
						vectorStk.removeElementAt(0);
					}
					System.out.println(successors.size());
					successors = sortByCost(successors, successors.size());
					b = successors.elementAt(successors.size() - 1);
									 //get first successor from stack
					boolean notEmpty = true;
					while(observedNodesTemp.contains(b.toString()) && notEmpty) //go to the next unobserved state
					{
						int index = successors.indexOf(b);
						if(index - 1 > 0){
							b = successors.elementAt(index - 1);
							nextI = b.getCostEstimate();
						}
						else{
							nextI = depth;
							notEmpty = false;
						}
						System.out.println(b);
						successors.removeElementAt(index);
					}
					System.out.println("Size: " + successors.size());
					i = nextI;
					System.out.println(b);
					System.out.println(successorLevel);
				}
				depth = successorLevel;
			}
			System.out.println(observedNodes.size() + " nodes examined.");
			if(observedNodes.size() < 10000)
				printHistory(b);
			else
				System.out.println("Not printing history--leads to stack overflow");
			System.out.println(first15states);
		}
	}
	
	*/
	
	
	
	public Vector<Board> reverseVector(Vector<Board> successors){
		Vector<Board> reversed = new Vector<Board>();
		for(int i = successors.size()-1; !(i < 0); --i){
			reversed.addElement(successors.elementAt(i));
			successors.removeElementAt(i);
		}
		return reversed;
	}
	public Vector<Board> sortByCost(Vector<Board> successors, int size){
		Vector<Board> sortedBoard = new Vector<Board>();
		while(successors.size() > 0){
			int index = 0;
			int temp2 = successors.elementAt(0).getCostEstimate();
			if(successors.size() > 1){
				for(int i = 1; i < successors.size(); ++i){
					int temp = successors.elementAt(i).getCostEstimate();
					if(temp > temp2){
						temp = temp2;
						index = i;
					}
				}
			}
			sortedBoard.add(successors.elementAt(index));
			successors.removeElementAt(index);
		}
		/*for(int i = 0; i < sortedBoard.size(); ++i){
			System.out.println(sortedBoard.elementAt(i));
		}*/
		return sortedBoard;
	}
	
	/*
	public Vector<Board> sortByCost(Vector<Board> successors, int size){
		if(size > 1){
			Vector<Board> B1 = new Vector<Board>();
			Vector<Board> B2 = new Vector<Board>();
			if(size % 2 == 0){
				for(int i = 0; i < size; ++i){
					if(i < size / 2){
						B1.add(successors.elementAt(i));	
					}
					else{
						B2.add(successors.elementAt(i));
					}
				}
			}
			else{
				for(int i = 0; i < size; ++i){
					if(i > (size / 2) + 1){
						B2.add(successors.elementAt(i));
					}
					else{
						B1.add(successors.elementAt(i));
					}
				}
			}
			return merge(sortByCost(B1, B1.size()), sortByCost(B2, B2.size()));
		}
		else{
			return successors;
		}
		
	}
	public Vector<Board> merge(Vector<Board> B1, Vector<Board> B2)
	{
		Vector<Board> B3 = new Vector<Board>();
		int maxSize = B1.size() + B2.size();
		int index2 = 0;
		for(int i = 0; i+index2 < maxSize; ++i){
			if(B1.elementAt(i).getCostEstimate() < B2.elementAt(index2).getCostEstimate()){
				B3.add(B1.elementAt(i));
			}
			else{
				B3.add(B2.elementAt(index2));
				++index2;
			}
		}
		return B3;
	}
	*/

	public int heuristic(Board b){
		int h = 0;
		for(int i = 0; i < b.board.length; ++i){
			if(b.board[i] == goal.board[i]){
				++h;
			}
		}
		return h;
	}
	
	public int heuristicManhattan(Board b){
		int g = 0;
		for(int i = 0; i < 3; ++i){
			for(int j = 0; j < 3; ++j){
				int tile = b.getTileAt(i, j);
				g += getDistance(tile, i, j);
			}
		}
		return g;
	}
	public int getDistance(int tile, int x, int y){
		int distance = 0;
		switch(tile){
		case 0:
			distance = abs(1 - x) + abs(1-y);
			break;
		case 1:
			distance = abs(0 - x) + abs(0 - y);
			break;
		case 2: 
			distance = abs(1 - x) + abs(0 - y);
			break;
		case 3:
			distance = abs(2-x) + abs(0-y);
			break;
		case 4:
			distance = abs(2-x) + abs(1-y);
			break;
		case 5:
			distance = abs(2-x) + abs(2-y);
			break;
		case 6:
			distance = abs(1-x) + abs(2-y);
			break;
		case 7:
			distance = abs(0-x) + abs(2-y);
			break;
		case 8:
			distance = abs(0-x) + abs(1-y);
			break;
		}
		return distance;
	}
	public int abs(int x){
		if(x < 0){
			return x*-1;
		}
		else{
			return x;
		}
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