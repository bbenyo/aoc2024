package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import bb.aoc.utils.Grid;
import bb.aoc.utils.InputHandler;
import bb.aoc.utils.Location;

public class Day8 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day8.class.getName());
	
	Map<Character, List<Location>> antennae = new HashMap<>();
	Set<Location> antinodes = new HashSet<>();
	int rowCount = 0;
	Grid map = new Grid();
	
	@Override
	public void initialize() {
	}

	@Override
	public void handleInput(String line) {
		char[] row = line.toCharArray();
		for (int i=0; i<row.length; ++i) {
			char c = row[i];
			if (c == '.') { continue; }
			List<Location> locs = antennae.computeIfAbsent(c, __ -> new ArrayList<>());
			Location aLoc = new Location(i, rowCount);
			locs.add(aLoc);			
		}
		map.addRow(row);
		rowCount++;
	}

	@Override
	public void output() {
		findAntinodes();
		logger.info("Antinode count: "+antinodes.size());
	}
	
	protected void findAntinodes() {
		for (Entry<Character, List<Location>> ants : antennae.entrySet()) {
			char ant = ants.getKey();
			List<Location> alocs = ants.getValue();
			for (int i=0; i<alocs.size(); ++i) {
				for (int j=i+1; j<alocs.size(); ++j) {
					Location l1 = alocs.get(i);
					Location l2 = alocs.get(j);
					addAntinodes(ant, l1, l2);
				}
			}
		}
	}
	
	protected void addAntinodes(char ant, Location l1, Location l2) {
		// Get the slope in x/y diff between the points
		int xDiff = l1.getX() - l2.getX();
		int yDiff = l1.getY() - l2.getY();
		// Propagate in each direction to get a point twice as far away from the further loc
		Location a1 = new Location(l1.getX() + xDiff, l1.getY() + yDiff);
		Location a2 = new Location(l2.getX() - xDiff, l2.getY() - yDiff); // opposite direction
		if (map.get(a1).isPresent()) {
			// Valid loc
			antinodes.add(a1);
		}
		if (map.get(a2).isPresent()) {
			antinodes.add(a2);
		}		
	}

}
