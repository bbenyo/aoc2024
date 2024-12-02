package bb.aoc2024.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class Day1b extends Day1 {

	static private Logger logger = Logger.getLogger(Day1b.class.getName());
	
	@Override
	public void output() {
		// Could have created this list while reading in the inputs
		// Map of id to count
		Map<Integer, Integer> rightCounts = new HashMap<>();
		for (Integer i1 : rightList) {
			int oCount = rightCounts.computeIfAbsent(i1, __ -> Integer.valueOf(0));
			rightCounts.put(i1, ++oCount);
		}
		
		// Similarity = sum(id * # times that id appears in the right list)
		long similarity = 0;
		for (Integer i1 : leftList) {
			Integer rightCount = rightCounts.get(i1);
			if (rightCount != null) {
				similarity += (i1 * rightCount);
			}
		}
		
		logger.info("Total Similarity score: "+similarity);
	}

}
