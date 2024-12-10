package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.log4j.Logger;

import bb.aoc.utils.Grid;
import bb.aoc.utils.InputHandler;
import bb.aoc.utils.Location;
import bb.aoc.utils.LocationDirection;

public class Day10 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day10.class.getName());

	Grid map = new Grid();
	@Override
	public void initialize() {
	}

	@Override
	public void handleInput(String line) {
		map.addRow(line.toCharArray());
	}

	@Override
	public void output() {
		Optional<Location> nextHead = Optional.empty();
		if (map.get(0,0).get() == '0') {
			nextHead = Optional.of(new Location(0,0));
		} else {
			nextHead = map.scan('0');
		}
		long score = 0;
		while (nextHead.isPresent()) {
			Location next = nextHead.get();
			logger.info("Evaluating train head: "+next);
			score += trailHeadScore(next);
			nextHead = map.scan('0');
		}
		
		logger.info("Sum: "+score);
	}
	
	class WorkItem {
		Location loc;
		int next;
		
		public WorkItem(Location l, int n) {
			this.loc = l;
			this.next = n;
		}
		
		public char getChar() {
			return Character.forDigit(next, 10);
		}
	}
	
	protected int trailHeadScore(Location n) {
		List<WorkItem> workList = new ArrayList<>();
		WorkItem w = new WorkItem(n, 1);
		workList.add(w);
		
		Set<Location> found9s = new HashSet<>();
		
		while (!workList.isEmpty()) {
			WorkItem next = workList.remove(0);
			// Look for the next value starting here
			List<LocationDirection> nbrs = map.findNeighbors(next.getChar(), next.loc, false);
			int newNext = next.next + 1;
			for (LocationDirection nbr : nbrs) {
				if (next.next == 9) {
					logger.info("\tFound summit 9 at "+nbr);
					Location loc9 = new Location(nbr.getX(), nbr.getY());
					found9s.add(loc9);
				} else {
					WorkItem w2 = new WorkItem(nbr, newNext);
					workList.add(w2);
				}
			}
		}
		
		return found9s.size();		
	}

}
