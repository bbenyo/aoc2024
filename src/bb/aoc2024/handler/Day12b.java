package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import bb.aoc.utils.Direction;
import bb.aoc.utils.Location;

public class Day12b extends Day12 {
	
	static private Logger logger = Logger.getLogger(Day12b.class.getName());
	
	class Fence {
		Location p1;
		Location p2;
		boolean horizontal;
		
		public String toString() {
			return p1+" - "+p2+" horiz: "+horizontal;
		}
	}
	
	protected Fence createFence(Location l, Direction d) {
		Fence f1 = new Fence();
		switch(d) {
		case DOWN_LEFT:
		case DOWN_RIGHT:
		case UP_LEFT:
		case UP_RIGHT:
			logger.error("Unexpected direction: "+d);
			break;
		case DOWN:
			f1.p1 = new Location(l.getX(), l.getY() + 1);
			f1.p2 = new Location(l.getX() + 1, l.getY() + 1);
			f1.horizontal = true;
			break;
		case LEFT:
			f1.p1 = new Location(l.getX(), l.getY());
			f1.p2 = new Location(l.getX(), l.getY() + 1);
			f1.horizontal = false;
			break;
		case RIGHT:
			f1.p1 = new Location(l.getX() + 1, l.getY());
			f1.p2 = new Location(l.getX() + 1, l.getY() + 1);
			f1.horizontal = false;
			break;
		case UP:
			f1.p1 = new Location(l.getX(), l.getY());
			f1.p2 = new Location(l.getX() + 1, l.getY());
			f1.horizontal = true;
			break;
		default:
			break;
		}
		return f1;
	}
	
	public int countSides(Region r) {
		logger.info("Creating fences for "+r);
		// For each location in the region, determine how many fences it has around it
		// Then count corners, #sides = #corners
		List<Fence> fences = new ArrayList<>();
		for (Location l : r.locs) {
			for (Direction d : Direction.values()) {
				if (Direction.isDiagonal(d)) {
					continue;
				}
				Location ld = l.moveTo(d, 1);
				if (!r.inRegion(ld)) {
					// fence here between this loc and the neighbor
					Fence f = createFence(l, d);
					fences.add(f);
				}
			}
		}
		logger.info("\t"+fences.size()+" fences created");
		
		Map<Location, Integer> corners = new HashMap<>(); // Avoid duplicates
		// Now find corners.  A corner is between any 2 fences that share the same point (p1 or p2), and are different directions
		for (int i=0; i<fences.size(); ++i) {
			Fence f1 = fences.get(i);
			for (int j=i+1; j<fences.size(); ++j) {
				Fence f2 = fences.get(j);
				if (f1.horizontal == f2.horizontal) {
					continue;
				}
				Location corner = null;
				if (f1.p1.equals(f2.p1) || f1.p1.equals(f2.p2)) {
					corner = f1.p1;
				} else if (f1.p2.equals(f2.p1) || f1.p2.equals(f2.p2)) {
					corner = f1.p2;
				}
				if (corner != null) {
					Integer clocs = corners.get(corner);
					if (clocs == null) {
						clocs = 0;
						corners.put(corner, 0);
					}
					clocs++;
					corners.put(corner, clocs);
					logger.info("\tFound a corner ("+clocs+") "+corner+" between "+f1+" and "+f2);
				}					
			}
		}
		
		int cornerCount = 0;
		for (Location c : corners.keySet()) {
			Integer count = corners.get(c);
			if (count == 4) {
				/** 4 corners all in one place means this X shape:
				*  XB
				*  BX  We count 4 corners here, but there should be 2
				*  
				*  This should be the only case where we count 4 corners from one location
				**/
				corners.put(c, 2);
				cornerCount+=2;
			} else {
				cornerCount += count;
			}
		}
		
		logger.info("\tTotal corners found: "+cornerCount);
		
		// Number of sides = number of corners
		// To create a new side, you have to turn a corner
		logger.info("\t"+r+" has "+cornerCount+" sides");
		return cornerCount;
	}
	
	@Override
	public void output() {
		findRegions();
		long price = 0;
		
		Map<Region, Integer> sideMap = new HashMap<>();
		for (Region r : regions) {
			int sides = countSides(r);
			sideMap.put(r, sides);
		}
			
		for (Region r : regions) {
			int sides = sideMap.get(r);
			logger.info("Region "+r.label+" outer sides: "+sides);
			long rprice = r.area() * (long)sides;
			logger.info("Region "+r.label+":"+r.locs.get(0)+" has a price of "+rprice);
			logger.info("\tArea: "+r.area()+" * Sides: "+sides);
			price += rprice;
		}
		logger.info("Total Fencing price: "+price);
	}
}
