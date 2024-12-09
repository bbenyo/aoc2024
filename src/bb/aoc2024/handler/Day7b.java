package bb.aoc2024.handler;

import java.util.List;

import org.apache.log4j.Logger;

public class Day7b extends Day7 {
	
	static private Logger logger = Logger.getLogger(Day7.class.getName());
	
	@Override
	protected List<Long> computeNextPossible(List<Long> possibles, Integer v, Long val) {
		List<Long> next = super.computeNextPossible(possibles, v, val);
		for (Long p : possibles) {
			String concat = String.valueOf(p) + String.valueOf(v);
			try {
				Long np = Long.parseLong(concat);
				next.add(np);
			} catch (NumberFormatException ex) {
				logger.error(ex.toString(), ex);
			}
		}
		return next;
	}
}
