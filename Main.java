class Main {
    public static void main(String[] args) {
        // define dimensions
        int height = 20;
        int width = 20;

        // create maze object and initialize maze
        Maze maze = new Maze(height, width);

        // Display maze dimensions
	    maze.displayMazeDimensions();
        
        // Generate maze (using depth-first search with backtracker algorithm) and display it
        maze.generateMaze();
        maze.displayMaze();

        // Solve maze (using A* algorithm) and display it
        maze.solveMaze();
        maze.displayMaze(); 

        // Save solution to a txt file
        maze.saveSolutionToTxtFile();

    }
}
