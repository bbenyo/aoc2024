package bb.aoc2024.handler;

import java.util.Optional;

import org.apache.log4j.Logger;

import bb.aoc.utils.Direction;
import bb.aoc.utils.Location;

public class Day4b extends Day4 {
	static private Logger logger = Logger.getLogger(Day4b.class.getName());
	
	protected boolean matchCorner(Location c2, Direction d1, Direction d2) {
		Optional<Location> ul = grid.getLocAt(c2, d1);
		Optional<Location> dr = grid.getLocAt(c2, d2);
		Optional<Character> ulc = grid.get(ul);
		Optional<Character> drc = grid.get(dr);
		if (ulc.isPresent() && drc.isPresent()) {
			Character ulChar = ulc.get();
			Character drChar = drc.get();
			switch(ulChar) {
			case 'M': {
				if (drChar == 'S') {
					return true;
				}
				return false;
			}
			case 'S': {
				if (drChar == 'M') {
					return true;
				}
				return false;
			}
			default:
				break;
			}
		}
		return false;
	}
	
	@Override
	public void output() {
		int xmasCount = 0;
		for (int y=1; y<(grid.getRowCount() - 1); ++y) {
			for (int x=1; x<grid.getColumnCount(y); ++x) {
				// Get a grid centered on x,y, look for x-mas's
				// Need an A in the center for this to work
				Optional<Character> cent = grid.get(x,y);
				if (cent.isPresent() && cent.get().equals('A')) {
					// Corners need to be alternating M and S
					Location c2 = new Location(x,y);
					if (matchCorner(c2, Direction.UP_LEFT, Direction.DOWN_RIGHT) && matchCorner(c2, Direction.UP_RIGHT, Direction.DOWN_LEFT)) {
						xmasCount++;
					}					
				}
			}
		}
		logger.info("X-MAS Count: "+xmasCount);
	}
}
