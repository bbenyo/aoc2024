package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class Day9b extends Day9 {
	
	static private Logger logger = Logger.getLogger(Day9b.class.getName());
	
	class File {
		int start;
		int size;
		int id;
		
		public String toString() {
			return "File "+id+" size: "+size+" starting at "+start;
		}
	}
	
	class Space {
		int start;
		int size;
		
		public String toString() {
			return size+" empty spaces starting at "+start;
		}
	}

	List<File> files = new ArrayList<>();
	List<Space> spaces = new ArrayList<>();

	@Override
	public void handleInput(String line) {
		diskMap = line.toCharArray();
		realPtr = 0;
				
		// Can't really be clever with pointers, we just need to store files and their location
		for (int i=0; i<diskMap.length; ++i) {
			if ((i % 2) == 0) {
				// file
				File f = new File();
				f.id = i / 2;
				f.start = realPtr;
				f.size = toInt(diskMap[i]);
				realPtr += f.size;
				files.add(f);
			} else {
				// empty space
				Space s = new Space();
				s.start = realPtr;
				s.size = toInt(diskMap[i]);
				realPtr += s.size;
				spaces.add(s);
			}			
		}
		
		for (File f : files) {
			logger.info(f);
		}
		
		for (Space s : spaces) {
			logger.info(s);
		}
		
		// For each file, starting at the last, try to move it 
		for (int i=0; i<files.size(); ++i) {
			File f = files.get(files.size() - 1 - i);
			logger.info("Trying to move "+f.id);
			
			Space moveTo = null;
			for (Space s : spaces) {
				if (s.start > f.start) {
					// We're past the file already, can't move it
					break;
				}
				if (s.size >= f.size) {
					moveTo = s;
					break;
				}
			}
			if (moveTo != null) {
				logger.info("\t to space: "+moveTo);
				move(f, moveTo);
				logger.info("\t space is now: "+moveTo);
			} else {
				logger.info("\t No space found, keeping file at "+f.start);
			}
		}		
	}
	
	protected void move(File f, Space s) {
		f.start = s.start;
		s.start += f.size;
		s.size -= f.size;
		if (s.size == 0) {
			spaces.remove(s);
		}
	}

	@Override
	public void output() {
		// Recompute checksum
		checksum = 0;
		for (File f : files) {
			int loc = f.start;
			for (int i=0; i<f.size; ++i) {
				checksum += (f.id * loc);
				loc++;
			}
		}
		
		logger.info("Checksum: "+checksum);
	}
}
