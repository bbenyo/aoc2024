package bb.aoc2024.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import bb.aoc.utils.InputHandler;
import bb.aoc.utils.Utilities;

public class Day11 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day11.class.getName());
	
	class Stone {
		long num;
		Long leftStone = null;
		Long rightStone = null;
		
		public Stone(long n) {
			this.num = n;
			String nStr = String.valueOf(n);
			if ((nStr.length() % 2) == 0) {
				String left = nStr.substring(0, nStr.length() / 2);
				String right = nStr.substring(nStr.length() / 2);
				leftStone = Utilities.parseLongOrNull(left);
				rightStone = Utilities.parseLongOrNull(right);
			}
		}
		
		public String toString() {
			if (leftStone == null) {
				return ""+num;
			} else {
				return num+" ("+leftStone+"), ("+rightStone+")";
			}
		}
	}
	
	// Assuming order is irrelevant, store the number of stones of each type(number) we have
	Map<Stone, Long> stoneMap = new HashMap<>();
	// Map of stone id -> stone
	Map<Long, Stone> stoneStore = new HashMap<>();

	protected int blinkCount = 25;

	@Override
	public void initialize() {
		// no-op
	}
	
	@Override
	public void handleInput(String line) {
		int[] stones = Utilities.stringListToInts(line, " ");
		for (int i : stones) {
			long num = (long)i;
			Stone s = stoneStore.computeIfAbsent(num, __ -> new Stone(num));
			Long stoneCount = stoneMap.computeIfAbsent(s, __ -> Long.valueOf(0));
			stoneMap.put(s, stoneCount + 1);
		}
		
		for (int i=0; i<blinkCount; ++i) {
			blink(i);
		}
	}
	
	protected void addStone(Map<Stone, Long> next, Stone nStone, long stonesToAdd) {
		Long c = next.computeIfAbsent(nStone, __ -> Long.valueOf(0));
		next.put(nStone, c + stonesToAdd);
	}
	
	protected long countStones() {
		long count = 0;
		for (Entry<Stone, Long> sEntry : stoneMap.entrySet()) {
			Long sCount = sEntry.getValue();
			count += sCount;
		}
		return count;
	}
	
	protected void blink(int i) {
		Map<Stone, Long> next = new HashMap<>();
		logger.info("Blink "+i+"...");
		for (Entry<Stone, Long> sEntry : stoneMap.entrySet()) {
			Stone s = sEntry.getKey();
			Long sCount = sEntry.getValue();
			
			// Rule 1
			if (s.num == 0) {
				Stone nStone = stoneStore.computeIfAbsent((long)1, __ -> new Stone(1));
				addStone(next, nStone, sCount);
			} else if (s.leftStone != null && s.rightStone != null) { // Rule 2
				Stone ls = stoneStore.computeIfAbsent(s.leftStone, __ -> new Stone(s.leftStone));
				Stone rs = stoneStore.computeIfAbsent(s.rightStone, __ -> new Stone(s.rightStone));
				// Even, create 2 stones
				addStone(next, ls, sCount);
				addStone(next, rs, sCount);
			} else { // Rule 3
				long nVal = s.num * 2024;
				Stone nStone = stoneStore.computeIfAbsent(nVal, __ -> new Stone(nVal));
				addStone(next, nStone, sCount);
			}
		}
		stoneMap = next;
		logger.info("\t "+countStones()+" stones");
	}

	@Override
	public void output() {
		logger.info("Final stone count after "+blinkCount+" blinks: "+countStones());
	}

}
