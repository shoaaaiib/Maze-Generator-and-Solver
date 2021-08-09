import java.util.*;
import java.io.*;

class Maze {
    // number of cells described with width and height
	int height;
	int width;

	// 2d maze ArrayList
	ArrayList<ArrayList<Cell>> maze = new ArrayList<>();

	// create ArrayList of strings to store directions of solution
	ArrayList<String> solution = new ArrayList<>();

    //Initializing Maze
    Maze(int inputHeight, int inputWidth) {
        this.height = inputHeight;
        this.width = inputWidth;

        System.out.println("\nDimensions have been initialized");

        for (int i = 0; i < this.height; i++) {
            ArrayList<Cell> mazeRow = new ArrayList<>();
            for (int j = 0; j < this.width; j++) {
                Cell cell = new Cell();
                cell.row = i;
                cell.col = j;
                mazeRow.add(cell);
            }
            this.maze.add(mazeRow);
        }

        System.out.println("\nMaze has been initialized");
    }

	//Displaying the Maze dimensions
    void displayMazeDimensions() {
        System.out.println("\nThe Height of the Maze: " + this.height);
        System.out.println("The Width of the Maze: " + this.width);
        System.out.println();
    }

    

	
	void displayMaze() {
        for (ArrayList<Cell> mazeRow : maze) {

            // print top/bottom walls
            for (Cell cell : mazeRow) {
                System.out.print("+");
                // print if top wall is true
                if (cell.northWall) {
                    System.out.print("---");
                }
                else {
                    System.out.print("   ");
                }
            }
            System.out.println("+");
    
    
            // print left/right walls on next line
            System.out.print("|");
            for (Cell cell : mazeRow) {
                // print if right wall is true ( x = path, o = searched node but not part of path)
                if (cell.eastWall && cell.path && !cell.search) { // 110
                    System.out.print(" x |");
                }
                else if (cell.eastWall && !cell.path && !cell.search) { // 100
                    System.out.print("   |");
                }
                else if (!cell.eastWall && cell.path && !cell.search) { // 010
                    System.out.print(" x  ");
                }
                else if (cell.eastWall && cell.path && cell.search) { // 111
                    System.out.print(" x |");
                }
                else if (cell.eastWall && !cell.path && cell.search) { // 101
                    System.out.print(" o |");
                }
                else if (!cell.eastWall && !cell.path && cell.search) { // 001
                    System.out.print(" o  ");
                }
                else if (!cell.eastWall && cell.path && cell.search) { // 011
                    System.out.print(" x  ");
                }
                else {
                    System.out.print("    "); // 000
                }
            }
            System.out.println();
    
        }
    
        // print final bottom walls
        for (int i = 0; i < width; i++) {
            System.out.print("+---");
        }
        System.out.println("+");
    }

	// depth-first search with backtracker algorithm
    void generateMaze() {
        // set random seed using time
        Random random = new Random();
        random.setSeed(System.currentTimeMillis()/1000);

        // create an ArrayList of Cells
        Stack<Cell> mazeStack = new Stack<>();

        // create an ArrayList of neighbours of cells
        ArrayList<Cell> neighbours = new ArrayList<>();

        // choose the initial cell, mark it as visited and push it to stack
        int row = 0;
        int col = 0;
        Cell currentCell = maze.get(row).get(col);
        currentCell.visited = true;
        mazeStack.push(maze.get(row).get(col));

        // initialise neighbouring cells to something
        Cell northCell = currentCell;
        Cell eastCell = currentCell;
        Cell southCell = currentCell;
        Cell westCell = currentCell;
        Cell randomNeighbour = currentCell;

        // while stack is not empty
        while(!mazeStack.empty()) {
            
            // pop a cell from the stack and make it the current cell
            row = mazeStack.peek().row;
            col = mazeStack.peek().col;
            currentCell = maze.get(row).get(col);
            mazeStack.pop();

            // define and check if any neighbours have not been visited
            // also check for out of bound indexes
            if (row > 0 && !maze.get(row - 1).get(col).visited) {
                northCell = maze.get(row - 1).get(col);
                neighbours.add(northCell);
            }
            if (col < width - 1 && !maze.get(row).get(col + 1).visited) {
                eastCell = maze.get(row).get(col + 1);
                neighbours.add(eastCell);
            }
            if (row < height - 1 && !maze.get(row + 1).get(col).visited) {
                southCell = maze.get(row + 1).get(col);
                neighbours.add(southCell);
            }
            if (col > 0 && !maze.get(row).get(col - 1).visited) {
                westCell = maze.get(row).get(col - 1);
                neighbours.add(westCell);
            }

            // check if any neighbours have not been visited
            if (!neighbours.isEmpty()) {
                // push the current cell to the stack
                mazeStack.push(currentCell);
                // choose random unvisited neighbour
                int randomNeighbourIndex = Math.abs(random.nextInt() % neighbours.size());
                //System.out.println(randomNeighbourIndex);
                randomNeighbour = neighbours.get(randomNeighbourIndex);
                // mark chosen cell as visited and push to stack
                randomNeighbour.visited = true;
                mazeStack.push(randomNeighbour);
                // remove wall between current cell and chosen cell
                if (randomNeighbour == northCell) {
                    currentCell.northWall = false;
                }
                else if (randomNeighbour == eastCell) {
                    currentCell.eastWall = false;
                }
                else if (randomNeighbour == southCell) {
                    southCell.northWall = false;
                }
                else {
                    westCell.eastWall = false;
                }
                neighbours.clear();
            }

        }
        System.out.println("\nMaze has been Generated !\n");
    }

	//A* Search Algorithm
    void solveMaze() {

        // set of nodes to be evaluated
        ArrayList<Cell> openList = new ArrayList<>();

        // set of nodes already evaluated
        ArrayList<Cell> closedList = new ArrayList<>();

        // set target node
        int targetRow = height - 1;
        int targetCol = width - 1;
        Cell targetNode = maze.get(targetRow).get(targetCol);

        // set start and current node
        int startRow = 0;
        int startCol = 0;
        Cell startNode = maze.get(startRow).get(startCol);
        Cell currentNode = startNode;

        // print out start and end coordinates
        System.out.println("\nStart coordinates are: " + startRow + ", " + startCol);
        System.out.println("Target coordinates are: " + targetRow + ", " + targetCol);
        System.out.println();

        // add current node to open list
        openList.add(currentNode);
        currentNode.open = true;

        // initialise neighbouring cell to null
        Cell neighbourNode = null;

        // set some variables
        int currentLowestfCost = 0;
        int currentLowestfCostIndex = 0;

        while (true) {

            // set current node to node with lowest fCost in open list
            currentLowestfCost = openList.get(0).fCost;
            currentLowestfCostIndex = 0;
            for (int i = 1; i < openList.size(); i++ ) {
                if (openList.get(i).fCost < currentLowestfCost) {
                    currentLowestfCost = openList.get(i).fCost;
                    currentLowestfCostIndex = i;
                }
            }
            currentNode = openList.get(currentLowestfCostIndex);

            // remove current node from open list
            openList.remove(openList.get(currentLowestfCostIndex));
            currentNode.open = false;

            // add current node to closed list
            closedList.add(currentNode);
            currentNode.closed = true;

            // escape loop if current node is equal to target node
            if (currentNode == targetNode) {
                break;
            }

            // for each neighbour (4 adjacent) of the current [row][col]
            for (int j = -1; j < 2; j++) {
                for (int i = -1; i < 2; i++) {
                    // skip if diagonal neighbour or current node
                    if (i == j || i == -j || (i == 0 && j == 0)) {
                        continue;
                    }
                    // skip if neighbour is out of bounds
                    if (currentNode.row + i < 0 || currentNode.col + j < 0 || currentNode.row + i >= height || currentNode.col + j >= width) {
                        continue;
                    }
                    // skip if neighbour is in closed list
                    if (maze.get(currentNode.row + i).get(currentNode.col + j).closed) {
                        continue;
                    }
                    // skip if current neighbour is north and north wall exists
                    if (i == -1 && j == 0 && maze.get(currentNode.row).get(currentNode.col).northWall ) {
                        continue;
                    }
                    // skip if current neighbour is east and east wall exists
                    if (i == 0 && j == 1 && maze.get(currentNode.row).get(currentNode.col).eastWall) {
                        continue;
                    }
                    // skip if current neighbour is south and south wall exists
                    if (i == 1 && j == 0 && maze.get(currentNode.row + 1).get(currentNode.col).northWall) {
                        continue;
                    }
                    // skip if current neighbour is west and west wall exists
                    if (i == 0 && j == -1 && maze.get(currentNode.row).get(currentNode.col - 1).eastWall) {
                        continue;
                    }

                    // set gCost of neighbour
                    neighbourNode = maze.get(currentNode.row + i).get(currentNode.col + j);
                    neighbourNode.setGcost(currentNode.gCost);

                    // if new path to neighbour is shorter or neighbour is not in open list
                    if (neighbourNode.gCost < currentNode.gCost || !neighbourNode.open) {
                        // set fCost of neighbour
                        neighbourNode.setHcost(targetRow, targetCol);
                        neighbourNode.setFcost();
                        // set parent of neighbour to current
                        neighbourNode.parent = currentNode;
                        neighbourNode.search = true;
                        // if neighbour is not in open list
                        if (!neighbourNode.open) {
                            neighbourNode.open = true;
                            openList.add(neighbourNode);
                        }
                    }
                }
            }
        }

        // maze has been solved
        // now backtrack from target node to find path and store solution
        int deltaRow = 0;
        int deltaCol = 0;
        currentNode = targetNode;
        while (currentNode != startNode) {

            deltaRow = currentNode.parent.row - currentNode.row;
            deltaCol = currentNode.parent.col - currentNode.col;

            // if backtracking to north node
            if (deltaRow == -1 && deltaCol == 0) {
                solution.add("South");
            }
            // else if backtracking to east node
            else if (deltaRow == 0 && deltaCol == 1) {
                solution.add("West");
            }
            // else if backtracking to south node
            else if (deltaRow == 1 && deltaCol == 0) {
                solution.add("North");
            }
            // else must be backtracking to west node
            else {
                solution.add("East");
            }

            currentNode.path = true;
            currentNode = currentNode.parent;
        }
        startNode.path = true;
        Collections.reverse(solution);
        System.out.println("Maze has been solved!");
        System.out.println("x = path, o = search area");
        System.out.println();

    }

	void saveSolutionToTxtFile() {

        try {

            FileWriter fw = new FileWriter("Maze_Solution.txt");
            
            fw.write("The solution to the Maze is: \n");

            for(int i = 0; i < solution.size(); i++) {
                fw.write(solution.get(i) + "\n");
            }

            System.out.println("\nThe solution to this Maze has been stored in the Maze_Solution.txt File !");

            fw.close();
        } catch(IOException e) {
            System.out.println(e);
        }
        
    }

}
