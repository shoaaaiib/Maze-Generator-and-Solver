class Cell{

    boolean visited = false;
    int row = 0;
    int col = 0;

    //only need to manipulate 2 walls for each cell since cells share walls
    boolean northWall = true;
    boolean eastWall = true;

    //for A* algorithm
    boolean open = false;
    boolean closed = false;
    int hCost = 0; // heuristic (estimated) cost of moving from current cell to final cell
    int gCost = 0; // cost of moving from initial cell to current cell
    int fCost = 0; // gCost + hCost
    Cell parent = null;
    boolean path = false;
    boolean search = false;

    void setGcost(int currentgCost) {
		gCost = currentgCost + 1;
	}

    void setHcost(int targetRow, int targetCol) {
		hCost = Math.abs(targetRow - this.row) + Math.abs(targetCol - this.col);
	}

    void setFcost() {
		fCost = gCost + hCost;
	}

}