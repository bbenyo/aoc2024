package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import bb.aoc.utils.Grid;
import bb.aoc.utils.InputHandler;
import bb.aoc.utils.Location;
import bb.aoc.utils.LocationDirection;

public class Day12 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day12.class.getName());
	
	Grid map = new Grid();
	Set<Location> rLocs = new HashSet<>();

	class Region {
		char label;
		List<Location> locs;
		
		public Region(char lbl) {
			this.label = lbl;
			this.locs = new ArrayList<>();
		}
		
		@Override
		public String toString() {
			return ""+label;
		}
		
		public int area() {
			return locs.size();
		}
		
		public void addLoc(Location l) {
			locs.add(l);
			rLocs.add(l);
		}
		
		public int perimeter() {
			int count = 0;
			for (Location l : locs) {
				List<LocationDirection> inRegionNeighbors = map.findNeighbors(label, l, false);
				// For any missing neighbor, there's either another region or out of grid, so add to perimeter
				count += (4 - inRegionNeighbors.size());
			}
			return count;
		}
		
		public long price() {
			return area() * perimeter();
		}
		
	}
	
	@Override
	public void initialize() {
		// No-op
	}

	@Override
	public void handleInput(String line) {
		map.addRow(line.toCharArray());
	}
	
	List<Region> regions = new ArrayList<>();
	
	protected boolean inRegion(Location l) {
		if (rLocs.contains(l)) {
			return true;
		}
		return false;
	}
	
	protected void findRegions() {
		for (int i=0; i<map.getRowCount(); ++i) {
			for (int j=0; j<map.getColumnCount(i); ++j) {
				Location l = new Location(j,i);
				if (inRegion(l)) {
					continue;
				}
				logger.info("Creating new region at "+l);
				// Find a new region starting here
				createRegion(l);
			}
		}
	}
	
	// Create a region starting at loc l
	// Put L on a work queue, add all neighbors, etc.
	protected void createRegion(Location l) {
		List<Location> workQueue = new ArrayList<>();
		workQueue.add(l);
		char rc = map.get(l).get();
		Region r = new Region(rc);
		regions.add(r);
		
		while (!workQueue.isEmpty()) {
			Location loc = workQueue.remove(0);
			if (inRegion(loc)) {
				continue;
			}
			r.addLoc(loc);
			List<LocationDirection> nbrs = map.findNeighbors(rc, loc, false);
			for (LocationDirection ld : nbrs) {
				Location nl = new Location(ld.getX(), ld.getY());
				if (this.inRegion(nl)) {
					continue;
				}
				workQueue.add(nl);
			}
		}
		logger.info("Added a region of size "+r.area()+" starting at "+l+" of type "+rc);
	}

	@Override
	public void output() {
		findRegions();
		long price = 0;
		for (Region r : regions) {
			long rprice = r.price();
			logger.info("Region "+r.label+":"+r.locs.get(0)+" has a price of "+rprice);
			logger.info("\tArea: "+r.area()+" * Perimeter: "+r.perimeter());
			price += rprice;
		}
		logger.info("Total Fencing price: "+price);
	}

}
