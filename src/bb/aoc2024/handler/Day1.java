package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import bb.aoc.utils.InputHandler;

public class Day1 implements InputHandler {

	static private Logger logger = Logger.getLogger(Day1.class.getName());

	protected List<Integer> leftList = new ArrayList<>();
	protected List<Integer> rightList = new ArrayList<>();
		
	@Override
	public void handleInput(String line) {
		line = line.trim();
		if (line.length() == 0) {
			return;
		}
		String[] vals = line.split("\\s+");
		if (vals.length != 2) {
			logger.error("Unable to parse line (expecting 2 ints): "+line);
			return;
		}
		
		try {
			Integer i1 = Integer.parseInt(vals[0].trim());
			Integer i2 = Integer.parseInt(vals[1].trim());
			leftList.add(i1);
			rightList.add(i2);
		} catch (NumberFormatException ex) {
			logger.error(ex.toString(), ex);
		}		
	}

	@Override
	public void output() {
		// Sort lists by value
		Collections.sort(leftList);
		Collections.sort(rightList);
		
		double total = 0;
		for (int i=0; i<leftList.size(); ++i) {
			Integer i1 = leftList.get(i);
			Integer i2 = rightList.get(i);
			int dist = Math.abs(i1 - i2);
			total += dist;
		}
		
		logger.info("Total distances is: "+total);
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
	}

}
