package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import bb.aoc.utils.Direction;
import bb.aoc.utils.Grid;
import bb.aoc.utils.InputHandler;
import bb.aoc.utils.Location;
import bb.aoc.utils.Node;

public class Day18 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day18.class.getName());

	Grid map = new Grid();
	int rocks = 1024;
	List<Location> bytes = new ArrayList<>();
	
	@Override
	public void initialize() {
		map.initialize(71, 71, '.');
	}

	@Override
	public void handleInput(String line) {
		if (line.trim().isEmpty()) {
			return;
		}
		Location l = new Location(line);
		bytes.add(l);
		if (rocks > 0) {
			rocks--;
			map.set(l, '#');
		}
	}

	class MNode extends Node {

		public MNode(Location l1) {
			super(l1);
		}
		
		@Override
		public Node createNode(Location l, Direction d) {
			return new MNode(l);
		}
		
		@Override
		public boolean isValidNode(Node n) {
			Optional<Character> m = map.get(n);
			if (m.isEmpty() || m.get() == '#') {
				return false;
			}
			Node uNode = nodes.get(n.getLabel());
			if (uNode != null && uNode.getG() <= n.getG()) {
				return false;
			}
			return true;
		}
			
		@Override
		public int getGridSizeX() {
			return map.getColumnCount(0);
		}
		
		@Override
		public int getGridSizeY() {
			return map.getRowCount();
		}
		
		@Override
		protected void computeHScore() {
			hScore = computeHeuristic();
		}
	}
	
	@Override
	public void output() {		
		MNode start = new MNode(new Location(0,0));
		Node end = Node.search(start);
		logger.info("Best path: \n"+end.printBackPath());
		logger.info(map.print());
		logger.info("Best path: "+end.toString());
	}

}
