package bb.aoc2024.handler;

import java.util.Optional;

import org.apache.log4j.Logger;

import bb.aoc.utils.Grid;
import bb.aoc.utils.InputHandler;
import bb.aoc.utils.Location;
import bb.aoc.utils.Node;

public class Day18 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day18.class.getName());

	Grid map = new Grid();
	int rocks = 12;
	
	@Override
	public void initialize() {
		map.initialize(7, 7, '.');
	}

	@Override
	public void handleInput(String line) {
		if (line.trim().isEmpty()) {
			return;
		}
		if (rocks > 0) {
			rocks--;
			Location l = new Location(line);
			map.set(l, '#');
		}
	}

	class MNode extends Node {

		public MNode(Location l1) {
			super(l1);
		}
		
		@Override
		protected void addNeighbor(Location lup) {
			Optional<Character> m = map.get(lup);
			if (m.isEmpty() || (m.isPresent() && m.get() == '#')) {
				return;
			}
			if (nodes.get(lup.toString()) == null) {
				MNode m2 = new MNode(lup);
				neighbors.add(m2);
				nodes.put(lup.toString(), m2);
			}
		}
		
		@Override
		public int getGridSizeX() {
			return map.getColumnCount(0);
		}
		
		@Override
		public int getGridSizeY() {
			return map.getRowCount();
		}
		
		protected void computeHScore() {
			hScore = (70*70) - gScore;
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
