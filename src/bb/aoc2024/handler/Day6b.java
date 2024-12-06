package bb.aoc2024.handler;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.log4j.Logger;

import bb.aoc.utils.Direction;
import bb.aoc.utils.Location;
import bb.aoc.utils.LocationFacing;

public class Day6b extends Day6 {
	static private Logger logger = Logger.getLogger(Day6.class.getName());
	
	Set<LocationFacing> visited = new HashSet<>();
	
	// Brute force, try an obstacle at each point and test for a loop
	// Should be small enough for brute force
	@Override
	public void output() {
		int count = 0;
		for (int y=0; y<room.getRowCount(); ++y) {
			for (int x=0; x<room.getColumnCount(y); ++x) {
				Optional<Character> p = room.get(x,y);
				if (p.isPresent() && p.get().equals('.')) {
					// Try an obstacle here
					logger.info("Trying obstacle at "+x+","+y);
					room.set(x,y,'#');
					if (findLoop()) {
						count++;
					}
					room.reset("<>^v", '.');
					room.set(x,y,'.');
					visited.clear();
				}
			}
		}
		logger.info("Found "+count+" obstacles that lead to a loop");
	}
	

	protected boolean findLoop() {
		Location g = new Location(gStart);
		Optional<Character> rOpt = room.get(g);
		LocationFacing facing = new LocationFacing(g.getX(), g.getY());
		Location tl = new Location(0,0);
		Location br = new Location(room.getColumnCount(row - 1), row - 1);
		facing.setFacing(Direction.UP);
		while (rOpt.isPresent()) {
			room.set(facing, facing.getFacingChar());
			visited.add(facing);
			LocationFacing nxt = new LocationFacing(facing);
			if (!nxt.forward(tl, br, false)) {
				break;
			}
			rOpt = room.get(nxt);
			if (rOpt.isEmpty()) {
				break;
			}
			if (rOpt.get().equals('#')) {
				// Obstacle
				facing.turnRight();
				rOpt = room.get(facing);
			} else {
				// commit the move
				facing = nxt;
				Optional<Character> moveTo = room.get(facing);
				if (moveTo.isPresent()) {
					if (visited.contains(facing)) {
						// We've been here and going this way before, this will be a loop
						logger.info("Found a infinite loop, we've been at "+facing+" before");
						return true;
					}
				}
			}
		}
		logger.info("No loop, guard off grid");
		return false;
	}
}
