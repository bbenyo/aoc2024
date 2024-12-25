package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import bb.aoc.utils.InputHandler;
import bb.aoc.utils.Utilities;

public class Day22 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day22.class.getName());
	
	List<Long> seeds = new ArrayList<>();

	@Override
	public void initialize() {
		// No-op
	}

	@Override
	public void handleInput(String line) {
		if (line.trim().isEmpty()) {
			return;
		}
		seeds.add(Utilities.parseLongOrNull(line.trim()));
	}

	protected long evolve(long secret) {
		long res = secret * 64;
		long s2 = mix(res, secret);
		s2 = prune(s2);
		
		res = (s2 / 32l);
		s2 = mix(res, s2);
		s2 = prune(s2);
		
		res = s2 * 2048;
		s2 = mix(res, s2);
		return prune(s2);		
	}
	
	protected long mix(long v1, long v2) {
		return v1 ^ v2;
	}
	
	protected long prune(long s1) {
		return s1 % 16777216;
	}
	
	protected long evolve(long s, int times) {
		long s2 = s;
		for (int i=0; i<times; ++i) {
			s2 = evolve(s2);
		}
		return s2;
	}

	@Override
	public void output() {
		logger.info("Test: mix(15, 42) should be 37 = "+mix(15, 42));
		logger.info("Test: prune(100000000) should be 16113920 = "+prune(100000000));
		
		logger.info("Test 123: ");
		long s = 123;
		for (int i=0; i<10; ++i) {
			s = evolve(s);
			logger.info(i+": "+s);
		}
		
		long total = 0;
		for (Long seed : seeds) {
			long ev = evolve(seed, 2000);
			logger.info("Seed: "+seed+": "+ev);
			total += ev;
		}
		
		logger.info("Total: "+total);
	}

}
