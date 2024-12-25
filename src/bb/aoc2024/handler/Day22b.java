package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class Day22b extends Day22 {
	
	static private Logger logger = Logger.getLogger(Day22b.class.getName());
	
	Set<String> keys = new HashSet<>();
	
	class Monkey {
		long seed;
		int[] ones;
		Map<String, Integer> sellMap;
		
		public Monkey(long s) {
			this.seed = s;
			ones = new int[2001];
			sellMap = new HashMap<>();
		}
		
		public void initialize() {
			long s1 = seed;
			for (int i=0; i<2001; ++i) {
				ones[i] = (int)(s1 % 10);
				s1 = evolve(s1);
			}
			
			for (int i=4; i<2001; ++i) {
				int d1 = ones[i-3] - ones[i-4];
				int d2 = ones[i-2] - ones[i-3];
				int d3 = ones[i-1] - ones[i-2];
				int d4 = ones[i] - ones[i-1];
				String delta = ""+d1+","+d2+","+d3+","+d4;
				if (sellMap.containsKey(delta)) {
					continue;
				}
				sellMap.put(delta, ones[i]);
				keys.add(delta);
			}			
		}		
	}
	
	@Override
	public void output() {
		List<Monkey> monkies = new ArrayList<>();
		for (Long seed : seeds) {
			Monkey m1 = new Monkey(seed);
			m1.initialize();
			monkies.add(m1);
		}
		
		long maxProfit = 0;
		String bestKey = null;
		for (String k : keys) {
			long val = 0;
			for (Monkey m1 : monkies) {
				Integer profit = m1.sellMap.get(k);
				if (profit != null) {
					val += profit;
				}
			}
			if (maxProfit < val) {
				bestKey = k;
				maxProfit = val;
			}
		}
		
		logger.info("Best Signal: "+bestKey);
		logger.info("Profit: "+maxProfit);		
	}
}
