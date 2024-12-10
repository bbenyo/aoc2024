package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import bb.aoc.utils.Location;
import bb.aoc.utils.LocationDirection;

public class Day10b extends Day10 {
	
	static private Logger logger = Logger.getLogger(Day10b.class.getName());
	
	@Override
	protected int trailHeadScore(Location n) {
		List<WorkItem> workList = new ArrayList<>();
		WorkItem w = new WorkItem(n, 1);
		workList.add(w);
				
		int rating = 0;
		while (!workList.isEmpty()) {
			WorkItem next = workList.remove(0);
			// Look for the next value starting here
			List<LocationDirection> nbrs = map.findNeighbors(next.getChar(), next.loc, false);
			int newNext = next.next + 1;
			for (LocationDirection nbr : nbrs) {
				if (next.next == 9) {
					logger.info("\tFound summit 9 at "+nbr);
					rating++;					
				} else {
					WorkItem w2 = new WorkItem(nbr, newNext);
					workList.add(w2);
				}
			}
		}
		
		logger.info("\tFinal rating: "+rating);
		return rating;
	}

}
