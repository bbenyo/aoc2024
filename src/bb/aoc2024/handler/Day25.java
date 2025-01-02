package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import bb.aoc.utils.Grid;
import bb.aoc.utils.InputHandler;
import bb.aoc.utils.Utilities;

public class Day25 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day25.class.getName());

	class KeyLock extends Grid {
		
		List<Integer> cols;
		boolean lock = false;
		boolean key = false;
		
		public KeyLock() {
			super();
			cols = new ArrayList<>();
		}
		
		protected boolean isLock() {
			for (int i=0; i<getColumnCount(0); ++i) {
				if (get(i,0).get() != '#') {
					return false;
				}
			}
			return true;
		}
		
		protected boolean isKey() {
			for (int i=0; i<getColumnCount(getRowCount() - 1); ++i) {
				if (get(i,getRowCount() - 1).get() != '#') {
					return false;
				}
			}
			return true;
		}
		
		public void initialize() {
			lock = isLock();
			key = isKey();
			if (lock && key) {
				logger.warn("Grid is both a lock and a key!");
				logger.warn(print());
			} else if (!lock && !key) {
				logger.warn("Grid is neither a lock nor a key!");
				logger.warn(print());
			}
			
			if (key) {
				initKeyCounts();
			} else if (lock) {
				initLockCounts();
			}
		}
		
		public void initKeyCounts() {
			for (int x=0; x<getColumnCount(0); ++x) {
				int count = 0;
				for (int y=getRowCount() - 2; y > -1; --y) {
					if (get(x,y).get() == '#') {
						count++;
					} else {
						break;
					}
				}
				cols.add(count);
			}
			logger.debug("Columns: "+Utilities.listToString(cols, ","));
		}
		
		public void initLockCounts() {
			for (int x=0; x<getColumnCount(0); ++x) {
				int count = 0;
				for (int y=1; y < getRowCount(); ++y) {
					if (get(x,y).get() == '#') {
						count++;
					} else {
						break;
					}
				}
				cols.add(count);
			}
			logger.debug("Columns: "+toString());
		}
		
		public String toString() {
			return Utilities.listToString(cols, ",");
		}
		
	}
	
	List<KeyLock> keys;
	List<KeyLock> locks;
		
	@Override
	public void initialize() {
		keys = new ArrayList<>();
		locks = new ArrayList<>();
	}

	KeyLock cur = null;
	
	protected void finalizeKeyLock() {
		if (cur != null) {
			cur.initialize();
			if (cur.isKey()) {
				keys.add(cur);
			} else if (cur.isLock()) {
				locks.add(cur);
			}
			cur = null;
		}
	}
	
	@Override
	public void handleInput(String line) {
		if (line.isEmpty()) {
			finalizeKeyLock();			
		} else {
			if (cur == null) {
				cur = new KeyLock();
			}
			cur.addRow(line.toCharArray());
		}
	}
	
	public boolean match(KeyLock l, KeyLock k) {
		int rows = l.getRowCount() - 2; // ignore the top and bottom
		for (int i=0; i<l.getColumnCount(0); ++i) {
			if ((l.cols.get(i) + k.cols.get(i)) > rows) {
				logger.info("Lock "+l+" and key "+k+": overlap in column "+i);
				return false;
			}
		}
		logger.info("Lock "+l+" and key "+k+": all columns fit");
		return true;
	}

	@Override
	public void output() {
		finalizeKeyLock();
		int matches = 0;
		for (KeyLock l : locks) {
			for (KeyLock k : keys) {
				if (match(l,k)) {
					matches++;
				}
			}
		}
		logger.info("Total matches: "+matches);
	}

}
