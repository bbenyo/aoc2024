package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import bb.aoc.utils.InputHandler;
import bb.aoc.utils.Utilities;

public class Day13 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day13.class.getName());

	class Button {
		long x;
		long y;
		char label;
		
		public Button(long x, long y) {
			this.x = x;
			this.y = y;
		}
		
		public String toString() {
			return label+" X+"+x+" Y+"+y;
		}
	}
	
	class ClawMachine {
		Button a;
		Button b;
		long prizeX;
		long prizeY;
		int index;
		
		public String toString() {
			return "ClawMachine: "+index+" Prize at X="+prizeX+" Y="+prizeY;
		}
		
		// Surely this won't work for part b, but for now...
		public long optimizeBruteForce() {
			// max 100 presses = 101*101 total combinations possible, we can brute that
			long min = Long.MAX_VALUE;
			logger.info("Brute forcing claw machine "+toString());
			for (int pressA = 0; pressA <= 100; ++pressA) {
				long vX = (pressA * a.x);
				if (vX > prizeX) {
					// Past it already
					continue;
				}
				long vY = (pressA * a.y);
				if (vY > prizeY) {
					continue;
				}
				if (vX == prizeX && vY == prizeY) {
					int p = (pressA * 3);
					if (p < min) {
						min = p;
						logger.info("\tWin in "+min+": A="+pressA+" B=0");
						continue;
					}
				}
				for (int pressB = 1; pressB <= 100; ++pressB) {
					vX += b.x;
					if (vX > prizeX) {
						break;
					}
					vY += b.y;
					if (vY > prizeY) {
						break;
					}
					if (vX == prizeX && vY == prizeY) {
						int p = (pressA * 3) + pressB;
						if (p < min) {
							min = p;
							logger.info("\tWin in "+min+": A="+pressA+" B="+pressB);
							break;
						}
					}
				}
			}
			logger.info("\tMin presses: "+min);
			return min;
		}
	}
	
	List<ClawMachine> machines = new ArrayList<>();
	
	@Override
	public void initialize() {
		// no-op
	}

	Button curA;
	Button curB;
	
	@Override
	public void handleInput(String line) {
		if (line.startsWith("Button A")) {
			Integer xVal = Utilities.parseInt(line, "X+", ",");
			Integer yVal = Utilities.parseInt(line, "Y+", "");
			curA = new Button(xVal, yVal);						
		} else if (line.startsWith("Button B")) {
			Integer xVal = Utilities.parseInt(line, "X+", ",");
			Integer yVal = Utilities.parseInt(line, "Y+", "");
			curB = new Button(xVal, yVal);						
		} else if (line.startsWith("Prize:")) {
			Integer xVal = Utilities.parseInt(line, "X=", ",");
			Integer yVal = Utilities.parseInt(line, "Y=", "");
			ClawMachine cm = new ClawMachine();
			cm.index = machines.size();
			cm.a = curA;
			curA = null;
			cm.b = curB;
			curB = null;
			cm.prizeX = xVal;
			cm.prizeY = yVal;
			machines.add(cm);
		}
	}
	
	protected Long optimize(ClawMachine cm) {
		return cm.optimizeBruteForce();
	}

	@Override
	public void output() {
		long tokens = 0;
		for (ClawMachine cm : machines) {
			cm.prizeX = cm.prizeX + 10000000000000l; // lol
			cm.prizeY = cm.prizeY + 10000000000000l; // lol
		}
		
		for (ClawMachine cm : machines) {
			Long min = optimize(cm);
			if (min == Long.MAX_VALUE) {
				// Can't win
				logger.info("\tCan't win this machine!");
			} else {
				tokens += min;
			}
		}
		logger.info("Tokens to spend: "+tokens);

	}

}
