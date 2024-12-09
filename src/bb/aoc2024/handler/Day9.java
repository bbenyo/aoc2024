package bb.aoc2024.handler;

import org.apache.log4j.Logger;
import bb.aoc.utils.InputHandler;

public class Day9 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day9.class.getName());

	char[] diskMap;
	long checksum = 0;
	
	class DiskPointer {
		int loc;
		int empty;
		
		DiskPointer(int l) {
			loc = l;
			empty = 0;
		}
	}
	
	DiskPointer ptr;
	int workPtr;
	int realPtr = 0;
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
	}
	
	protected int toInt(char c) {
		return (c - '0');
	}

	@Override
	public void handleInput(String line) {
		diskMap = line.toCharArray();
		ptr = new DiskPointer(0);
		
		// Initialize
		// Skip the first block, since nothing is to the left, and its checksum id is 0
		// We have disk[1] empty blocks at the ptr.
		ptr.loc = 1; 
		ptr.empty = toInt(diskMap[ptr.loc]);
		
		// If the disk map is even, the last element is empty space and can be ignored
		if ((diskMap.length % 2) == 0) {
			workPtr = diskMap.length - 2;
		} else {
			workPtr = diskMap.length - 1;
		}
		
		int moveId = workPtr / 2;
		realPtr = toInt(diskMap[0]);
		while (ptr.loc < workPtr) {
			// Move the blocks at the end (workPtr) to empty spaces starting at ptr
			int toMove = toInt(diskMap[workPtr]);
			move(moveId, toMove);
			workPtr--; // blank space
			workPtr--;
			moveId--;
		}
	}
	
	protected void move(int id, int toMove) {
		while (toMove > 0) {
			if (ptr.empty > 0) {
				logger.info("Moving "+id+" to "+realPtr);
				ptr.empty --;
				checksum += (id * realPtr);
				realPtr++;
				toMove--;
			} else {
				// No room here, move the pointer to the next empty spot
				ptr.loc ++;
				// This is a block though
				int inPlace = toInt(diskMap[ptr.loc]);
				int inPlaceId = ptr.loc / 2; // Should be even always
				if (inPlaceId == id) {
					// We're done
					ptr.empty = 10000;
					continue;
				}
				
				for (int i=0; i<inPlace; ++i) {
					checksum += (inPlaceId * realPtr);
					realPtr++;
				}
				ptr.loc ++;
				// More empty spaces here, if it's not a 0
				ptr.empty = toInt(diskMap[ptr.loc]);
			}
		}
		
	}

	@Override
	public void output() {
		logger.info("Checksum: "+checksum);
	}

}
