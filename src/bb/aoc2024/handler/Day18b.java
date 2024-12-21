package bb.aoc2024.handler;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import bb.aoc.utils.Location;
import bb.aoc.utils.Node;

public class Day18b extends Day18 {
	
	static private Logger logger = Logger.getLogger(Day18b.class.getName());
	
	@Override
	public void output() {
		// There's still a path to the exit after 1024 (from part 1)
		int index = 1024;
		
		// Partial brute force
		// If the next byte isn't on the best path, then it can't block and we skip
		// If it is on the best path, then we search for a new best path and continue
		Set<Location> currentBestPath = new HashSet<>();
		while (true) {
			// Get the best path to the end
			MNode start = new MNode(new Location(0,0));
			Node end = Node.search(start);
			if (end == null) {
				logger.info("No path found!");
				break;
			}
			logger.info("Best path length: "+end.getBackPathLength());
			currentBestPath.clear();
			for (Node n : end.getBackPath()) {
				Location l = new Location(n.getX(), n.getY());
				currentBestPath.add(l);
			}
			
			for (int i=index; i<bytes.size(); ++i) {
				Location nextByte = bytes.get(i);
				map.set(nextByte, '#');
				if (currentBestPath.contains(nextByte)) {
					break;
				}
				index++;
				logger.info("Byte "+i+" isn't blocking the best path");
			}			
		}
		
		logger.info("Last byte added at index: "+index+" = "+bytes.get(index));
	}
}
