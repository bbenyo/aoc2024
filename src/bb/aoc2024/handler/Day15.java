package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import bb.aoc.utils.Direction;
import bb.aoc.utils.Grid;
import bb.aoc.utils.InputHandler;
import bb.aoc.utils.Location;

public class Day15 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day15.class.getName());

	Grid map = new Grid();
	StringBuffer cmdBuffer = new StringBuffer();
	boolean parseCommands = false;

	@Override
	public void handleInput(String line) {
		if (line.length() == 0) {
			logger.info("Done parsing grid, switching to commands");
			parseCommands = true;
			return;			
		}
		if (!parseCommands) {
			map.addRow(line.toCharArray());
		} else {
			cmdBuffer.append(line.trim());
		}
	}
	
	protected void simulate() {
		// logger.debug("Step 0: "+map.print());
		Optional<Location> curOpt = map.scan('@');
		if (curOpt.isEmpty()) {
			logger.error("Can't find player @");
			return;
		}
		Location player = curOpt.get();
		map.set(player, '.');
		String commands = cmdBuffer.toString();
		for (int i=0; i<commands.length(); ++i) {
			char cmd = commands.charAt(i);
			Optional<Direction> d = Direction.fromChar(cmd);
			if (d.isEmpty()) {
				logger.error("Can't parse direction: "+cmd);
				return;
			}
			Direction dir = d.get();
			logger.info("Moving from "+player+" "+d);
			player = simulateStep(player, dir);
			// logger.debug("Step: "+(i+1)+"\n"+map.print());
		}
	}
	
	protected Location simulateStep(Location p, Direction d) {
		Location c = p.moveTo(d, 1);
		Optional<Character> cVal = map.get(c);
		if (cVal.isEmpty()) {
			logger.error("Shouldn't be able to move off map, there are walls surrounding: "+c);
			return c;
		}
		Character val = cVal.get();
		switch(val) {
		case '.' :
		case '@' :
			logger.info("\tEmpty, player at "+c);
			map.set(p, '.');
			map.set(c, '@');
			return c; // Can just move, nothing there
		case '#' :
			logger.info("\tBlocked, player stays at "+p);
			return p; // Can't move
		case 'O' : 
		case '[' :
		case ']' :
			return pushBoxes(p, c, d);
		}
		logger.error("Corrupted grid, unexpected char "+val+" at "+c);
		return p;
	}
	
	protected Location pushBoxes(Location orig, Location next, Direction d) {
		// There's a box at next. See if we can push it direction d
		logger.info("\tTrying to push boxes at "+next);
		Location nLoc = next.moveTo(d, 1);
		List<Location> pushTo = new ArrayList<>();
		Optional<Character> valO = map.get(nLoc);
		while (valO.isPresent()) {
			char val = valO.get();
			if (val == '.' || val == '@') {
				// Empty space, can move everything north
				pushTo.add(nLoc);
				map.set(orig, '.');
				map.set(next, '@'); // Player moves here, it's empty now
				for (Location pt : pushTo) {
					map.set(pt, 'O');
				}
				logger.debug("\tPushed "+pushTo.size()+" boxes "+d);
				return next;
			} else if (val == 'O') {
				// Another box, keep going
				pushTo.add(nLoc);
				nLoc = nLoc.moveTo(d, 1);
				valO = map.get(nLoc);
			} else if (val == '#') {
				// Stuck
				logger.debug("\tStuck, not moving");
				return orig;
			} else {
				logger.error("Corrupted map, unexpected char: "+val+" at "+nLoc);
				return orig;
			}
		}
		return orig;
	}
	
	protected long computeGPS() {
		long gps = 0;

		map.setCursor(new Location(0,0));
		Optional<Location> boxLoc = map.scan('O');
		while (boxLoc.isPresent()) {
			Location b = boxLoc.get();
			long bGPS = (b.getY() * 100) + b.getX();
			logger.info("Box at "+b+" GPS: "+bGPS);
			gps += bGPS;
			boxLoc = map.scan('O');
		}
		return gps;
	}

	@Override
	public void output() {
		simulate();
		
		long gps = computeGPS();
		logger.info(map.print());
		logger.info("Total GPS: "+gps);		
	}

	@Override
	public void initialize() {		
	}

}
