package bb.aoc2024.handler;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

public class Day5b extends Day5 {
	static private Logger logger = Logger.getLogger(Day5b.class.getName());
	
	@Override
	protected boolean isValid(List<Integer> pageList) {
		boolean correctOrder = super.isValid(pageList);
		// If this is in the correct order, we don't want to add it, so ignore
		if (correctOrder) {
			return false;
		}
	
		// Fix the ordering
		logger.info("Fixing ordering for "+pageList);
		Collections.sort(pageList, new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				OrderingRule r1 = rules.get(o1);
				if (r1 != null) {
					if (r1.after.contains(o2)) {
						// o1 has to be before o2
						return -1;
					}
				}
				OrderingRule r2 = rules.get(o2);
				if (r2 != null) {
					if (r2.after.contains(o1)) {
						// o1 has to be after o1
						return 1;
					}
				}
				// No rule for these, any ordering is fine
				return 0;
			}			
		});
		logger.info("\tFixed ordering: "+pageList);
				
		return true;
	}
}
