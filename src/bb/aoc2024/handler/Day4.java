package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import bb.aoc.utils.Direction;
import bb.aoc.utils.Grid;
import bb.aoc.utils.InputHandler;
import bb.aoc.utils.Location;
import bb.aoc.utils.LocationDirection;

public class Day4 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day4.class.getName());
	
	protected Grid grid;
	@Override
	public void initialize() {
		grid = new Grid();
	}

	@Override
	public void handleInput(String line) {
		grid.parseRow(line);
	}

	@Override
	public void output() {
		int xmasCount = search("XMAS");
		logger.info("XMAS Count: "+xmasCount);
	}
	
	// Location in the grid and the index of the letter of the word we're looking for
	class LocLetter {
		Location loc;
		Direction dir;
		int letter;
		
		public LocLetter(Location l, int letter) {
			this.loc = l;
			this.letter = letter;
			dir = null;
		}
		
		public LocLetter(Location l, int letter, Direction dir) {
			this.loc = l;
			this.letter = letter;
			this.dir = dir;
		}
	
		public String toString() {
			return loc.toString() + " "+dir+" "+letter;
		}
	}
	
	protected int search(String word) {
		// Scan grid for the first letter, then search in each cardinal direction
		char firstLetter = word.charAt(0);
		Optional<Location> findNext = grid.scan(firstLetter);
		int count = 0;
		while (findNext.isPresent()) {
			List<LocLetter> workList = new ArrayList<>();
			LocLetter first = new LocLetter(findNext.get(), 1);
			workList.add(first);
			while (!workList.isEmpty()) {
				LocLetter nxt = workList.remove(0);
				if (nxt.letter >= word.length()) {
					logger.info("Found "+word+" from "+first.loc+" to "+nxt.loc);
					count++;
				} else {
					char letterAt = word.charAt(nxt.letter);
					addNextLocations(workList, nxt, letterAt);
				}
			}
			findNext = grid.scan(firstLetter);
		}
		return count;
	}
	
	protected void addNextLocations(List<LocLetter> workList, LocLetter ll, char lookFor) {
		if (ll.dir == null) {
			// Look in all directions
			List<LocationDirection> nbrs = grid.findNeighbors(lookFor, ll.loc);
			for (LocationDirection nbr : nbrs) {
				workList.add(new LocLetter(nbr, ll.letter + 1, nbr.getDir()));
			}
		} else {
			// Look only in the direction we're going
			Optional<Location> nLoc = grid.getLocAt(ll.loc, ll.dir);
			if (nLoc.isPresent()) {
				Location next = nLoc.get();
				Optional<Character> nChar = grid.get(next.getX(), next.getY());
				if (nChar.isPresent() && nChar.get().equals(lookFor)) {
					workList.add(new LocLetter(next, ll.letter + 1, ll.dir));
				}				
			}
		}
	}

}
