package bb.aoc2024.handler;

import java.util.Objects;
import java.util.Optional;

import org.apache.log4j.Logger;

import bb.aoc.utils.Direction;
import bb.aoc.utils.Grid;
import bb.aoc.utils.InputHandler;
import bb.aoc.utils.Location;
import bb.aoc.utils.Node;

public class Day16 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day16.class.getName());

	Location end = new Location(0,0);
	Location start = new Location(0,0);
	Grid maze = new Grid();
	
	class MazeNode extends Node {
		Direction facing;
		
		public MazeNode(Location l1, Direction d, int cost) {
			super(l1);
			this.facing = d;
			this.localCost = cost;
			this.label = l1.toString()+":"+d;
		}
		
		@Override
		public String toString() {
			return super.toString()+" Dir: "+facing+" Cost: "+localCost;
		}
		
		@Override
		public void gatherNeighbors() {
			// Go straight 1
			Location l2 = this.moveTo(facing, 1);
			Optional<Character> straightC = maze.get(l2);
			if (straightC.isPresent() && straightC.get() != '#') {
				// We can move straight
				addNeighbor(new MazeNode(l2, facing, 1));
			}
			
			// We can turn left if there's an empty space there, if there's a wall then there's no point
			Direction left = Direction.turnLeft90(facing);
			Location l3 = this.moveTo(left, 1);
			Optional<Character> lC = maze.get(l3);
			if (lC.isPresent() && lC.get() != '#') {
				// This is a turn and a move 1
				addNeighbor(new MazeNode(l3, left, 1001));
			}
			
			// We can turn left if there's an empty space there, if there's a wall then there's no point
			Direction right = Direction.turnRight90(facing);
			Location l4 = this.moveTo(right, 1);
			Optional<Character> rC = maze.get(l4);
			if (rC.isPresent() && rC.get() != '#') {
				// This is a turn and a move 1
				addNeighbor(new MazeNode(l4, right, 1001));
			}
		}
		
		@Override
		public int getWorstScore(Location l1) {
			// Snake like grid could have you turn every other square
			int area = getGridSizeX() * getGridSizeY();
			return (area / 2) + ((area / 2) * 1000);
		}
		
		// Bottom right
		@Override
		public boolean isEnd() {
			if ((getY() == end.getY()) &&
				(getX() == end.getX())) {
				return true;
			}
			return false;
		}
		
		// Guess we can move to the end with 1 turn per 5 steps?
		@Override
		public int computeHeuristic() {
			int straightToEnd = Math.abs(end.getX() - getX()) + Math.abs(end.getY() - getY());
			int turns = straightToEnd / 5;
			return (turns * 1000) + straightToEnd;
		}
		
		@Override
		protected void computeHScore() {
			hScore = computeHeuristic();
		}
		
		@Override
		public int getGridSizeX() {
			// Assume an even width
			return maze.getColumnCount(0);
		}
		
		// Get the maximum Y value for a Location
		@Override
		public int getGridSizeY() {
			return maze.getRowCount();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + Objects.hash(facing);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			MazeNode other = (MazeNode) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return facing == other.facing;
		}

		private Day16 getEnclosingInstance() {
			return Day16.this;
		}
		
	}

	@Override
	public void initialize() {
		// no-op
	}

	@Override
	public void handleInput(String line) {
		maze.addRow(line.toCharArray());
	}

	public void findStartEnd() {	
		Optional<Location> startO = maze.scan('S');
		if (startO.isEmpty()) {
			logger.error("Invalid maze, can't find start position S");
			return;
		}
		start = startO.get();
				
		maze.setCursor(new Location(0,0));
		Optional<Location> endO = maze.scan('E');
		if (endO.isEmpty()) {
			logger.error("Invalid maze, can't find start position E");
			return;
		}
		end = endO.get();
	}
		
	@Override
	public void output() {
		findStartEnd();
		MazeNode startNode = new MazeNode(start, Direction.RIGHT, 0);
		Node foundEnd = Node.search(startNode);
		logger.info(System.lineSeparator());
		logger.info("Found end: "+foundEnd);

		logger.info("Path: "+foundEnd.printBackPath());
		logger.info("Steps: "+foundEnd.getBackPathLength());
		logger.info("End cost: "+foundEnd.getG());
	}

}
