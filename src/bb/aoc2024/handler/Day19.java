package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import bb.aoc.utils.InputHandler;

public class Day19 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day19.class.getName());

	List<String> towels = new ArrayList<>();
	
	class TowelDesign {
		String pattern;
		long options;
		
		public TowelDesign(String p) {
			pattern = p;
		}
	}

	List<TowelDesign> designs = new ArrayList<>();
	
	@Override
	public void initialize() {
		// no-op
	}

	boolean readingTowels = true;
	
	@Override
	public void handleInput(String line) {
		if (line.isBlank()) {
			return;
		}
		if (readingTowels) {
			String[] tSplit = line.split(",");
			for (String t : tSplit) {
				towels.add(t.trim());
			}
			readingTowels = false;
			return;
		}
		
		TowelDesign d = new TowelDesign(line.trim());
		d.options = 0;
		designs.add(d);
	}
	
	// Recursion with a cache
	protected long countOptions(TowelDesign d) {
		Map<String, Long> cache = new HashMap<>();
		d.options = countOptions(d.pattern, cache);
		return d.options;
	}
	
	protected long countOptions(String pattern, Map<String, Long> cache) {
		if (pattern.length() == 0) {
			return 1;
		}
		Long cached = cache.get(pattern);
		if (cached != null) {
			return cached;			
		}
		long count = 0;
		for (int i=0; i<towels.size(); ++i) {
			String towel = towels.get(i);
			if (pattern.startsWith(towel)) {
				String pLeft = pattern.substring(towel.length());
				count += countOptions(pLeft, cache);
			}
		}
		cache.put(pattern, count);
		logger.info("Options for "+pattern+" = "+count);
		return count;
	}
	
	@Override
	public void output() {
		for (TowelDesign d : designs) {
			logger.info("Counting options for "+d.pattern);
			countOptions(d);
		}
		
		long possible = 0;
		for (TowelDesign d : designs) {
			if (d.options > 0) {
				possible++;
			}
		}
		
		logger.info("Total Designs possible: "+possible);
	}

}
