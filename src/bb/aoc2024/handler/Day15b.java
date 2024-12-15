package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.log4j.Logger;

import bb.aoc.utils.Direction;
import bb.aoc.utils.Location;

public class Day15b extends Day15 {
	
	static private Logger logger = Logger.getLogger(Day15b.class.getName());

	@Override
	public void handleInput(String line) {
		if (line.length() == 0) {
			logger.info("Done parsing grid, switching to commands");
			parseCommands = true;
			return;			
		}
		if (!parseCommands) {
			StringBuffer newRow = new StringBuffer();
			for (char c : line.toCharArray()) {
				switch(c) {
				case '#' : newRow.append("##"); break;
				case '.' : newRow.append(".."); break;
				case 'O' : newRow.append("[]"); break;
				case '@' : newRow.append("@."); break;
				default:
					logger.error("Invalid map char: "+c);
					return;
				}
			}
			map.addRow(newRow.toString().toCharArray());
		} else {
			cmdBuffer.append(line.trim());
		}
	}
	
	@Override
	protected Location pushBoxes(Location orig, Location next, Direction d) {
		// We could be pushing a [ or ]
		// There's a box at next. See if we can push it direction d
		// If we're pushing up or down, we have to push 2 columns, which is trickier
		// If we're pushing left or right, it's simple
		if (Direction.UP.equals(d) || Direction.DOWN.equals(d)) {
			return pushBoxesVertical(orig, next, d);
		}
		logger.info("\tTrying to push boxes at "+next);
		Optional<Character> nextPush = map.get(next);
		if (nextPush.isEmpty()) {
			logger.error("Logic error");
			return orig;
		}
		Character nChar = nextPush.get();
		Location nLoc = next.moveTo(d, 1);
		
		List<Location> pushTo = new ArrayList<>();
		List<Character> pushee = new ArrayList<>();
		
		Optional<Character> valO = map.get(nLoc);
		
		while (valO.isPresent()) {
			char val = valO.get();
			if (val == '.' || val == '@') {
				// Empty space, can move everything
				pushTo.add(nLoc);
				pushee.add(nChar);
				map.set(orig, '.');
				map.set(next, '@'); // Player moves here, it's empty now
				for (Location pt : pushTo) {
					map.set(pt, pushee.remove(pushee.size() - 1));
				}
				logger.debug("\tPushed "+pushTo.size()+" boxes "+d);
				return next;
			} else if (val == '[' || val == ']') {
				// Another box, keep going
				pushTo.add(nLoc);
				pushee.add(val);
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
	
	protected Location pushBoxesVertical(Location orig, Location next, Direction d) {
		logger.info("\tTrying to push boxes at "+next);
		Optional<Character> nextPush = map.get(next);
		if (nextPush.isEmpty()) {
			logger.error("Logic error");
			return orig;
		}
		Character nChar = nextPush.get();
		Location nLoc = next.moveTo(d, 1);
		
		// Map of locations we've pushed and which char we pushed
		Map<Location, Character> pushed = new HashMap<>();
		List<Location> toClear = new ArrayList<>();
		
		// List of boxes to push
		List<Location> toPush = new ArrayList<>();
		
		toPush.add(next);
		if (nChar == '[') {
			// Also push the ] to the right
			Location rBox = next.moveTo(Direction.RIGHT, 1);
			toPush.add(rBox);
		} else if (nChar == ']') {
			Location lBox = next.moveTo(Direction.LEFT, 1);
			toPush.add(lBox);
		}
		
		while (!toPush.isEmpty()) {
			Location p = toPush.remove(0);
			Optional<Character> toPushOC = map.get(p);
			if (toPushOC.isEmpty()) {
				logger.error("Invalid location to be pushing: "+p);
				return orig;
			}
			Character toPushC = toPushOC.get();
			Location pNext = p.moveTo(d, 1);
			Optional<Character> pcOpt = map.get(pNext);
			if (pcOpt.isEmpty()) {
				logger.error("Trying to push off the map at "+pNext);
				return orig;
			}
			Character val = pcOpt.get();
			if (val == '.' || val == '@') {
				// Empty space, can move
				pushed.put(pNext, toPushC);
				toClear.add(p);
			} else if (val == '[') {
				// Another box, keep going
				pushed.put(pNext, toPushC);
				toClear.add(p);
				toPush.add(pNext);
				Location pRight = pNext.moveTo(Direction.RIGHT, 1);
				toPush.add(pRight);
			} else if (val == ']') {
				// Another box, keep going
				pushed.put(pNext, toPushC);
				toClear.add(p);
				toPush.add(pNext);
				Location pLeft = pNext.moveTo(Direction.LEFT, 1);
				toPush.add(pLeft);
			} else if (val == '#') {
				// Stuck
				logger.debug("\tStuck, can't push "+p+" "+d+", hit a "+val);
				return orig;
			} else {
				logger.error("Corrupted map, unexpected char: "+val+" at "+nLoc);
				return orig;
			}
		}
		
		// We can push everything, now commit
		for (Location toc : toClear) {
			map.set(toc, '.');
		}
		for (Entry<Location, Character> p : pushed.entrySet()) {
			Location l = p.getKey();
			map.set(l, p.getValue());
		}
		map.set(orig, '.');
		map.set(next, '@');
		return next;
	}
	
	protected long computeGPS() {
		long gps = 0;

		map.setCursor(new Location(0,0));
		Optional<Location> boxLoc = map.scan('[');
		while (boxLoc.isPresent()) {
			Location b = boxLoc.get();
			long bGPS = (b.getY() * 100) + b.getX();
			logger.info("Box at "+b+" GPS: "+bGPS);
			gps += bGPS;
			boxLoc = map.scan('[');
		}
		return gps;
	}
}
