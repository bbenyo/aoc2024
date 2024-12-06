package bb.aoc2024.handler;

import java.util.Optional;

import org.apache.log4j.Logger;

import bb.aoc.utils.Direction;
import bb.aoc.utils.Grid;
import bb.aoc.utils.InputHandler;
import bb.aoc.utils.Location;
import bb.aoc.utils.LocationFacing;

public class Day6 implements InputHandler {
	static private Logger logger = Logger.getLogger(Day6.class.getName());
	Grid room = new Grid();
	int row = 0;
	Location gStart = new Location(0,0);
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleInput(String line) {
		if (line.trim().length() > 0) {
			int gPos = line.indexOf("^");
			if (gPos > -1) {
				gStart.setX(gPos);
				gStart.setY(row);
			}
			room.addRow(line.toCharArray());
			row++;
		}
	}

	@Override
	public void output() {
		logger.info("Guard starts at "+gStart);
		Location g = new Location(gStart);
		Optional<Character> rOpt = room.get(g);
		LocationFacing facing = new LocationFacing(g.getX(), g.getY());
		Location tl = new Location(0,0);
		Location br = new Location(room.getColumnCount(row - 1), row - 1);
		facing.setFacing(Direction.UP);
		while (rOpt.isPresent()) {
			room.set(facing, facing.getFacingChar());
			LocationFacing nxt = new LocationFacing(facing);
			if (!nxt.forward(tl, br, false)) {
				logger.info("Off grid at "+nxt);
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
				logger.info("Guard moving to "+facing);
			}
		}
		int count = room.count("><^v");
		logger.info(room.print());
		logger.info("Visited: "+count);
	}

}
