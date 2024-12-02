package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import bb.aoc.utils.InputHandler;

public class Day2 implements InputHandler {

	static private Logger logger = Logger.getLogger(Day2.class.getName());
		
	protected int totalSafe = 0;
	protected int rowCount = 0;
	
	@Override
	public void handleInput(String line) {
		line = line.trim();
		if (line.length() == 0) {
			return;
		}
		String[] vals = line.split("\\s+");
		List<Integer> row = new ArrayList<>();
		for (String v : vals) {
			try {
				Integer i1 = Integer.parseInt(v.trim());
				row.add(i1);
			} catch (NumberFormatException ex) {
				logger.error(ex.toString(), ex);
			}
		}
		if (isSafe(row, rowCount)) {
			totalSafe++;
		}		
		rowCount++;
	}
	
	protected boolean isSafe(List<Integer> row, int rowCount) {
		Integer last = null;
		boolean increasing = false;
		boolean first = true;
		logger.info("Row: "+rowCount+": "+row);
		for (Integer i : row) {
			if (last == null) {
				last = i;
				continue;
			}
			int diff = Math.abs(i - last);
			if (diff < 1 || diff > 3) {
				logger.info("Row "+rowCount+" difference of "+diff+" between "+last+" and "+i);
				return false;
			}
			if (first) {
				// Are we increasing or decreasing
				if (last < i) {
					increasing = true;
				}
				first = false;
				last = i;
				continue;				
			}
			
			if (increasing) {
				if (i < last) {
					logger.info("Row "+rowCount+" was increasing, but decreased "+last+" to "+i);
					return false;
				}
			} else {
				if (i > last) {
					logger.info("Row "+rowCount+" was decreasing, but increased "+last+" to "+i);
					return false;
				}
			}
			last = i;			
		}
		logger.info("Row "+rowCount+" is safe");
		return true;
	}

	@Override
	public void output() {
		logger.info("Total Safe rows: "+totalSafe);
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
	}

}
