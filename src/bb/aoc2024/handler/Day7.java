package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import bb.aoc.utils.InputHandler;
import bb.aoc.utils.Utilities;

public class Day7 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day7.class.getName());
	
	long calibration = 0;

	@Override
	public void initialize() {
	}

	@Override
	public void handleInput(String line) {
		int cPos = line.indexOf(":");
		if (cPos < 0) {
			return;
		}
		Long val = Utilities.parseLongOrNull(line.substring(0, cPos));
		String argStr = line.substring(cPos+1);
		String[] argStrings = argStr.split(" ");
		List<Integer> args = new ArrayList<>();
		for (String s : argStrings) {
			Integer v = Utilities.parseIntOrNull(s);
			if (v != null) {
				args.add(v);
			}
		}
		
		// Brute force
		List<Long> possibles = new ArrayList<>();
		for (Integer v : args) {
			if (possibles.isEmpty()) {
				possibles.add((long)v);
				continue;
			}
			// For each possible value so far, we can either add this or multiply it
			List<Long> next = computeNextPossible(possibles, v, val);
			possibles = next;
		}
		
		// Is val in the list of possibles?
		if (possibles.contains((long)val)) {
			logger.info("Possibly true: "+val+" = "+args);
			calibration += val;
		} else {
			logger.info("Not possible: "+val+" = "+args);
		}

	}
	
	protected List<Long> computeNextPossible(List<Long> possibles, Integer v, Long val) {
		List<Long> next = new ArrayList<>();
		for (Long p : possibles) {
			long sum = p + v;
			long prod = p * v;
			if (sum <= val) {
				next.add(sum);			
			}
			if (prod <= val) {
				next.add(prod);
			}
		}
		return next;
	}

	@Override
	public void output() {
		logger.info("Calibration result: "+calibration);
	}

}
