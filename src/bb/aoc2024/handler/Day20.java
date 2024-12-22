package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.log4j.Logger;

import bb.aoc.utils.Grid;
import bb.aoc.utils.GridSearch;
import bb.aoc.utils.GridSearch.GridNode;
import bb.aoc.utils.InputHandler;
import bb.aoc.utils.Location;
import bb.aoc.utils.Node;

public class Day20 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day20.class.getName());

	protected int cheatMax = 2;
	Grid map = new Grid();
	
	@Override
	public void initialize() {
		// no-op
	}

	@Override
	public void handleInput(String line) {
		if (line.trim().isEmpty()) {
			return;
		}
		map.addRow(line.toCharArray());
	}
	
	public GridNode findBasePath() {
		GridSearch gs = new GridSearch(map, 'S', 'E');
		GridNode path = gs.search();
		return path;
	}
		
	public Map<Integer, Integer> findCheats(GridNode path) {
		int len = path.getBackPathLength();
		logger.info("Base path: "+len);

		Map<Location, Integer> distFromStart = new HashMap<>();
		for (Node n : path.getBackPath()) {
			distFromStart.put(n.toLocation(), len);
			logger.info(n+" = "+len);
			len--;
		}
		
		// Number of steps saved -> number of cheats found 
		Map<Integer, Integer> cheatCounts = new HashMap<>();
		
		for (Node n : path.getBackPath()) {
			// Try a cheat here.  Find every point cheatMax from here that is a valid path point
			List<Location> cheats = findValidWithinDistance(n.toLocation(), cheatMax);
			for (Location c : cheats) {
				// We can cheat here and go from n to c via a cheat
				// How much would this save?
				Integer orig = distFromStart.get(n.toLocation());
				Integer hax = distFromStart.get(c);
				int dist = n.toLocation().manhattanDistance(c);
				int saved = (hax - orig) - dist;
				if (saved > 0) {
					Integer count = cheatCounts.computeIfAbsent(saved, __ -> Integer.valueOf(0));
					cheatCounts.put(saved, count+1);
				}
			}
		}
		return cheatCounts;
	}

	public List<Location> findValidWithinDistance(Location loc, int dist) {
		List<Location> valid = new ArrayList<>();
		// It's a diamond pattern, start dist straight up, etc.
		for (int y=dist; y>=0; --y) {
			for (int x=0; x<=(dist -y); ++x) {
				Location pot = new Location(loc.getX() - x, loc.getY() - y);
				maybeAddLocation(pot, valid);
				if (x > 0) {
					pot = new Location(loc.getX() + x, loc.getY() - y);
					maybeAddLocation(pot, valid);
				}
				if (y > 0) {
					pot = new Location(loc.getX() - x, loc.getY() + y);
					maybeAddLocation(pot, valid);
					if (x > 0) {
						pot = new Location(loc.getX() + x, loc.getY() + y);
						maybeAddLocation(pot, valid);
					}
				}
			}
		}
		return valid;
	}
	
	public void maybeAddLocation(Location pot, List<Location> valid) {
		Optional<Character> pChar = map.get(pot);
		if (pChar.isPresent() && pChar.get() != '#') {
			valid.add(pot);
		}
	}

	@Override
	public void output() {
		GridNode path = findBasePath();
		Map<Integer, Integer> cheatCounts = findCheats(path);

		int saved100 = 0;
		Set<Integer> cKeys = cheatCounts.keySet();
		List<Integer> cKeyList = new ArrayList<>(cKeys);
		Collections.sort(cKeyList);
		for (Integer k : cKeyList) {
			Integer v = cheatCounts.get(k);
			logger.info("Found "+v+" cheats that save "+k);
			if (k >= 100) {
				saved100 += v;
			}
		}

		logger.info("Cheats that saved at least 100: "+saved100);
	}

}
