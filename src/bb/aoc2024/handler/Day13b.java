package bb.aoc2024.handler;

import org.apache.log4j.Logger;

public class Day13b extends Day13 {
	
	static private Logger logger = Logger.getLogger(Day13b.class.getName());
	
	// lol, of course we can't brute force
		
	public long optimizeLinearAlgebra(ClawMachine cm) {
		logger.info("Linear algebra optimization for "+cm);
		/**
		 * c = A.X
		 * d = A.Y
		 * e = B.X
		 * f = B.Y
		 * 
		 * Ac + Be = X
		 * Ad + Bf = Y
		 * 
		 * Be = X - Ac
		 * B = (X - Ac)/e
		 * 
		 * Ad + f(X - Ac)(1/e) = Y
		 * eAd + f(X - Ac) = Ye
		 * eAd + fX - fAc = Ye
		 * eAd - fAc = Ye - fX
		 * A(ed - fc) = Ye - fX
		 * A = (Ye - fX)/(ed - fc)
		 * 
		 */
		
		long c = cm.a.x;
		long d = cm.a.y;
		long e = cm.b.x;
		long f = cm.b.y;
		long X = cm.prizeX;
		long Y = cm.prizeY;
		long A = ((Y * e) - (f * X)) / ((e*d) - (f*c));
		long B = (X - (A * c)) / e;
		
		// Check if we got there exactly
		if (A < 0 || B < 0) {
			logger.info("\tNegative value for presses, we can't win");
			return Long.MAX_VALUE;
		}
		
		long x1 = (A * c) + (B * e);
		long y1 = (A * d) + (B * f);
		if (x1 == X && y1 == Y) {
			logger.info("\tWin with A="+A+" B="+B);
			return (A * 3) + B;			
		}
		logger.info("\tWe can't win this one");
		return Long.MAX_VALUE;
	}
	
	@Override
	public Long optimize(ClawMachine cm) {
		return this.optimizeLinearAlgebra(cm);
	}

}
