package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.List;

public class Day2b extends Day2 {
	
	@Override
	protected boolean isSafe(List<Integer> row, int rowCount) {
		if (super.isSafe(row, rowCount)) {
			return true;
		}
		// Brute force it, rows are small enough.  We can be cleverer later
		for (int i=0; i<row.size(); ++i) {
			List<Integer> newRow = new ArrayList<>(row);
			newRow.remove(i);
			if (super.isSafe(newRow, rowCount)) {
				return true;
			}
		}
		return false;
	}

}
