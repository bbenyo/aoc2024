package bb.aoc2024.handler;

import java.io.IOException;

import org.apache.log4j.Logger;

public class Day14b extends Day14 {
	
	static private Logger logger = Logger.getLogger(Day14b.class.getName());
	
	protected double stddev(int[] qtrs) {
		int sum = 0;
		for (int i=0; i<4; ++i) {
			sum += qtrs[i];
		}
		double mean = sum/4;
		double stddev = 0;
		for (int i=0; i<4; ++i) {
			stddev += Math.pow(qtrs[i] - mean, 2);
		}
		return Math.sqrt(stddev / 4);
	}
	
	@Override
	public void output() {
		// Move robots
		for (int i=1; i<20000; ++i) {
			map.reset("#", '.');
			for (Robot r : robots) {
				r.move();
				map.set(r.cur, '#');
			}
			int[] qtrs = computeSafety();
			double stddev = stddev(qtrs);
			if (stddev > 80) { // guess at a large enough value that it doesn't hit so often
				logger.info(map.print());
				try {
					logger.info("I: "+i+" Stddev of quaters: "+stddev);
					logger.info("Stddev high option, press enter to continue");
					System.in.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
