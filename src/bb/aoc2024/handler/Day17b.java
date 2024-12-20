package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import bb.aoc.utils.Utilities;

public class Day17b extends Day17 {
	
	static private Logger logger = Logger.getLogger(Day17b.class.getName());
	
	/**
	 * 2,4,1,1,7,5,1,5,4,0,5,5,0,3,3,0
	 * 2,4= bst (B = A % 8)
	 * 1,1= bxl(1) = B XOR 1 (flip the last bit)
	 * 7,5= cdv(B) = C = A >> B
	 * 1,5= bxl(5) = B XOR 5 (101), flip the third and last bit
	 * 4,0= bxc = B XOR C
	 * 5,5= out(B % 8)
	 * 0,3= adv(3) = A = A >> 3
	 * 3,0= jnz 0
	 * 
	 * A must have 16x3 bits = 48 bits
	 * 
	 * We continually take the least 3 bits from A
	 */
	
	public void reset(long a, long b, long c) {
		A = a;
		B = b;
		C = c;
		output.clear();		
	}
	
	class WorkItem {
		long A;
		int digit;
		
		public WorkItem(Long a, int d) {
			this.A = a;
			this.digit = d;
		}
	}
	
	@Override
	public void output() {
		long foundA = Long.MAX_VALUE;
		List<WorkItem> workQueue = new ArrayList<>();
		workQueue.add(new WorkItem(0l, 0));
		while(!workQueue.isEmpty()) {
			WorkItem wi = workQueue.remove(0);
			if (wi.digit == 16) {
				if (foundA > wi.A) {
					logger.info("Found a better answer: "+wi.A);
					foundA = wi.A;
				}
				continue;
			}
			Character next = program.get(program.size() - 1 - wi.digit);
			List<WorkItem> nxt = findDigit(next, wi.A, wi.digit);
			workQueue.addAll(0, nxt);
		}
				
		if (foundA < Long.MAX_VALUE) {
			reset(foundA, 0, 0);
			runProgram();
		
			logger.info("Final A: "+foundA);
			logger.info("Original Program: "+Utilities.listToString(program, ","));	
		} else {
			logger.error("Unable to find a value for A");
		}
	}
	
	protected List<WorkItem> findDigit(char c, long baseA, int digit) {
		List<WorkItem> next = new ArrayList<>();
		// Which last 3 bits will get an output of c
		for (int i=0; i<8; ++i) {
			long A2 = (baseA << 3) + i;
			logger.info("Trying "+A2);
			reset(A2,0,0);
			runProgram();
			if (output.isEmpty()) {
				logger.error("No output for A="+A);
			}
			if (output.get(0) == c) {
				logger.info("A = "+A2+" produces a "+c);
				next.add(new WorkItem(A2, digit+1));
			}
		}
		return next;
	}
}
