package bb.aoc2024.handler;

import org.apache.log4j.Logger;

public class Day19b extends Day19 {
	
	static private Logger logger = Logger.getLogger(Day19b.class.getName());
	
	@Override
	public void output() {
		for (TowelDesign d : designs) {
			logger.info("Counting options for "+d.pattern);
			countOptions(d);
			logger.info("Options for "+d.pattern+" = "+d.options);
		}

		long total = 0;
		for (TowelDesign d : designs) {
			total += d.options;
		}
		logger.info("Total Designs: "+total);
	}
}


