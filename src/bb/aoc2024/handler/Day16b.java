package bb.aoc2024.handler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import bb.aoc.utils.Direction;
import bb.aoc.utils.Location;
import bb.aoc.utils.Node;

public class Day16b extends Day16 {
	
	static private Logger logger = Logger.getLogger(Day16b.class.getName());
	
	@Override
	public void output() {
		findStartEnd();
		MazeNode startNode = new MazeNode(start, Direction.RIGHT, 0);
		List<Node> foundEnds = Node.searchAll(startNode);
		
		Set<Location> bestNodes = new HashSet<>();		
		
		logger.info(System.lineSeparator());
		logger.info("Found "+foundEnds.size()+" best paths");
		
		for (Node foundEnd : foundEnds) {
			logger.info("Best Path\n"+foundEnd.printBackPath());
			for (Node n : foundEnd.getBackPath()) {
				bestNodes.add(new Location(n.getX(), n.getY()));
			}
			logger.info("Steps: "+foundEnd.getBackPathLength());
			logger.info("End cost: "+foundEnd.getG());
		}
		
		logger.info("Total Locations on a best path: "+bestNodes.size());
	}
	
}
