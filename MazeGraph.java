/**
 * Ryan Slyter
 * CS 223
 * Final (required) programming assignment:
 * modified DFS maze
 */


import java.util.ArrayList;
import java.util.Random;

import java.util.Stack;

public class MazeGraph implements Graph {
	private int vertices;
	private int width, height;
	private int[][] cells;        
	final int EAST = 1; //0001
	final int NORTH = 2; // 0010
	final int WEST = 4; // 0100
	final int SOUTH = 8; // 1000
	
	
	public MazeGraph(int W, int H){
		width = W; height = H;
		vertices = width*height;
		cells = new int[H][W];
		for (int r = 0; r < H; r++){
			for (int c = 0; c < W; c++){
				cells[r][c] = r*W + c; //encodes each index with its value v (properly; tested)
				
			}
		}
	}
	
	public int numVerts() {return width*height;} //this is required for interface 
												//even though I just cached width*height 
	
	/** My adjacents method uses the row and column integers
	 * obtained from the vertice position and checks those integers
	 * with the width and height boundaries to determine if there are
	 * N,S,E, or W vertices to add to an arraylist, which is returned
	 */
	public ArrayList<Integer> adjacents(int v) {
		
	ArrayList<Integer> adjacents = new ArrayList<Integer>();
		
	int r = v / width;
	int c = v % width;
	
	if (c - 1 >= 0){adjacents.add(cells[r][c-1]);} //case WEST wall check
	if (c + 1 <= (width - 1)){adjacents.add(cells[r][c + 1]);} //case EAST wall check
	if (r - 1 >= 0){adjacents.add(cells[r-1][c]);} //case NORTH wall check
	if (r + 1 <= (height - 1)){adjacents.add(cells[r+1][c]);}//case SOUTH wall check
	
	/**
	 * So the arraylist returned will always have either 2,3, or 4 elements...
	 */
	
		return adjacents;
	}

	/** 
	 The only difference between my DFS and the pseudocode int the assignment prompt is that
	 I just cached a height*width array and initialized the vertice numbers, so there's
	 no graph to pass any into mazeDFS other than the starting vertice. The adjacents method
	 uses the 2d graph member variable to find the adjacent vertices.
	 */
	private int[] mazeDFS(int s){      
		Random randgen = new Random();
		Stack<Integer> temp = new Stack<Integer>();
		boolean[] visited = new boolean[vertices];
		for (int y = 0; y < vertices; y++){visited[y] = false;}
		int[] parent = new int[vertices];
		
		
		
		visited[s] = true;
		parent[s] = -1;
		int visitCount = 1;
		ArrayList<Integer> neighbors = new ArrayList<Integer>();
		int w = 0;
		
		
		while (visitCount < vertices){
			
			ArrayList<Integer> unvisited = new ArrayList<Integer>();
			
			
				
			neighbors = adjacents(s);
			       	
			
			for (int i = 0; i < neighbors.size(); i++){    
				
				w = neighbors.get(i);	
				
				if (visited[w] == false){unvisited.add(w);}
				
			}
						
			if (!unvisited.isEmpty()){
				
				int z = randgen.nextInt(unvisited.size());
				
				w = unvisited.get(z);  
				
				visited[w] = true;
				visitCount++;
				parent[w] = s;
				temp.push(w);
				
				s = w;
			}			
			
			else {s = temp.pop();} 
			}
	
		return parent;
	}
/**
 * Final method in the maze implementation. It's gonna be called on the MazeGen instance
 * and passed in the int[] parent DFS path that was generated by the DFS method above.
 * an int[][] variable needs to be created in main, assigned the output of this method,
 * and that is what will be used to be written to wherever to create the graphic of
 * the maze.
 * 
 */
	private int[][] Mazecarver(int[] path){
		
		int[][] finalMaze = new int[height][width];
		for (int r = 0; r < height; r++){
			for (int c = 0; c < width; c++){
				finalMaze[r][c] = 0xF;
			}
		}
		
		
		for (int r = 0; r < height; r++){
			for (int c = 0; c < width; c++){
				int v = r*width + c;
				int w = path[v];
				
				if (w >= 0){		// parent test
					int r0 = w / width;
					int c0 = w % width;
				
				if (c0 == c + 1){				//EAST - WEST case ([r][c] is left of '-')
					finalMaze[r][c] &= ~EAST;
					finalMaze[r0][c0] &= ~WEST;}
					
				if (c0 == c - 1){				//WEST - EAST case
					finalMaze[r][c] &= ~WEST;
					finalMaze[r0][c0] &= ~EAST;}
				
				if (r0 == r + 1){				//NORTH - SOUTH case
					finalMaze[r][c] &= ~NORTH;
					finalMaze[r0][c0] &= ~SOUTH;}
				
				if (r0 == r - 1){				//SOUTH - NORTH case
					finalMaze[r][c] &= ~SOUTH;
					finalMaze[r0][c0] &= ~NORTH;}
				} 
			}
		}
	return finalMaze; 
	}
    public static void main(String[] args) {
        int W, H;
        if (args.length != 2) {
            W = 16;
            H = 16;
        } else {
            W = Integer.parseInt(args[0]);
            H = Integer.parseInt(args[1]);
            if (W < 5 || H < 5) {
                System.err.println("bogus size!");
                return;
            }
        }
        
        int WEST = 4;
        int EAST = 1;
        MazeGraph randMaze = new MazeGraph(W,H);
        
        int start = 0; //choose easy starting point for now
        
        
        int[] path = randMaze.mazeDFS(start);
       int[][] finalproduct = randMaze.Mazecarver(path);
       finalproduct[0][0] &= ~WEST;
       finalproduct[4][W - 1] &= ~EAST;
        
        
        System.out.println(W + " " + H);
        for (int r = 0; r < H; r++) {
            for (int c = 0; c < W; c++) {
                System.out.print(finalproduct[r][c] + " ");
            }
            System.out.println();
        }
    
    
    
    
    }        
}