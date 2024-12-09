package bb.aoc2024.handler;

import java.util.Optional;

import bb.aoc.utils.Location;

public class Day8b extends Day8 {
		
	@Override
	protected void addAntinodes(char ant, Location l1, Location l2) {
		// antennae are antinodes too
		antinodes.add(l1);
		antinodes.add(l2);
		
		// Get the slope in x/y diff between the points
		int xDiff = l1.getX() - l2.getX();
		int yDiff = l1.getY() - l2.getY();
		
		// Keep going until we're off the map
		// Propagate in each direction to get a point twice as far away from the further loc
		Optional<Character> aloc = Optional.of('.');
		Location a1 = new Location(l1);
		while (aloc.isPresent()) {
			a1 = new Location(a1.getX() + xDiff, a1.getY() + yDiff);
			aloc = map.get(a1);
			if (aloc.isPresent()) {
				antinodes.add(a1);
			}
		}
		
		aloc = Optional.of('.');
		Location a2 = new Location(l2);
		while (aloc.isPresent()) {
			a2 = new Location(a2.getX() - xDiff, a2.getY() - yDiff);
			aloc = map.get(a2);
			if (aloc.isPresent()) {
				antinodes.add(a2);
			}
		}
	}
}
