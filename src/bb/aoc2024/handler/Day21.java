package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import bb.aoc.utils.Direction;
import bb.aoc.utils.InputHandler;
import bb.aoc.utils.Location;
import bb.aoc.utils.Node;
import bb.aoc.utils.Utilities;

public class Day21 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day21.class.getName());
	
	class KeypadNode extends Node {
		Character c; 
		Direction facing;
		Map<Character, List<String>> paths;

		public KeypadNode(int x, int y, Character c) {
			super(new Location(x,y));
			this.c = c;
			paths = new HashMap<>();
		}
		
		public KeypadNode(Location l1, Direction d) {
			super(l1);
			this.facing = d;
			this.c = '?';
			paths = new HashMap<>();
		}
		
		protected boolean isBlank(int x, int y) {
			return x == 0 && y == 3;
		}
		
		// Get the maximum X value for a Location
		@Override
		public int getGridSizeX() {
			return 3;
		}
		
		// Get the maximum Y value for a Location
		@Override
		public int getGridSizeY() {
			return 4;
		}
		
		 public Node createNode(Location l, Direction d) {
			return new KeypadNode(l, d);
		}
		
		@Override
		public void computeHScore() {
			hScore = gScore;
		}
		
		@Override
		public String toString() {
			if (c == '?') {
				return super.toString();
			}
			return ""+c;
		}
		
		@Override
		public boolean isValidNode(Node n) {
			if (!super.isValidNode(n)) {
				return false;
			}
			if (isBlank(n.getX(), n.getY())) {
				return false;
			}
			return true;
		}
		
		protected String pathToString() {
			String path = "";
			for (Node n : this.getBackPath()) {
				KeypadNode kn = (KeypadNode)n;
				if (kn.facing != null) {
					path = Direction.toChar(((KeypadNode)n).facing) + path;
				}
			}
			return path;
		}
		
		protected int getDirectionChanges() {
			int changes = 0;
			Direction last = null;
			for (Node n : this.getBackPath()) {
				KeypadNode kn = (KeypadNode)n;
				if (last == null || (kn.facing != null && !last.equals(kn.facing))) {
					changes++;
				}
				last = kn.facing;
			}
			return changes;
		}
		
		protected void addAllPaths(Collection<KeypadNode> oNodes, boolean doMinChanges) {
			for (KeypadNode to : oNodes) {
				if (this.c.equals(to.c)) {
					continue;
				}
				if (isBlank(x,y)) {
					continue;
				}
				this.gScore = 0;
				this.neighbors.clear();
				this.backPath = null;
				// Find all paths from here -> to
				logger.info("Finding paths from "+this+" to "+to);
				List<Node> allPaths = Node.searchAll(this, to);
				if (allPaths.isEmpty()) {
					logger.error("Unable to find path from "+this+" to "+to);
					return;
				}
				List<String> pathStrs = new ArrayList<>();
				paths.put(to.c, pathStrs);

				int minChange = Integer.MAX_VALUE;
				if (doMinChanges) {
					// Count direction changes, return the paths with the least direction changes only
					for (Node p : allPaths) {
						KeypadNode kp = (KeypadNode)p;
						int dc = kp.getDirectionChanges();
						if (dc < minChange) {
							minChange = dc;
						}
					}
				}
				
				for (Node p : allPaths) {
					KeypadNode kp = (KeypadNode)p;
					int dc = kp.getDirectionChanges();
					if (dc > minChange) {
						// This path can't be the shortest
						continue;
					}
					String ps = ((KeypadNode)p).pathToString();
					logger.info("Path from "+this+" -> "+to+": "+ps);
					pathStrs.add(ps);
				}
			}
		}
	}
	
	class KeypadNode2 extends KeypadNode {
		
		public KeypadNode2(int x, int y, Character c) {
			super(x,y,c);
		}
		
		public KeypadNode2(Location l1, Direction d) {
			super(l1, d);
		}
		
		@Override
		protected boolean isBlank(int x, int y) {
			return x == 0 && y == 0;
		}
		
		@Override
		public int getGridSizeY() {
			return 2;
		}
		
		@Override
		public Node createNode(Location l, Direction d) {
			return new KeypadNode2(l, d);
		}
	}
	
	Map<Character, KeypadNode> keypad1 = new HashMap<>();
	Map<Character, KeypadNode> keypad2 = new HashMap<>();
		
	@Override
	public void initialize() {
		keypad1.put('7', new KeypadNode(0,0,'7'));
		keypad1.put('8', new KeypadNode(1,0,'8'));
		keypad1.put('9', new KeypadNode(2,0,'9'));
		keypad1.put('4', new KeypadNode(0,1,'4'));
		keypad1.put('5', new KeypadNode(1,1,'5'));
		keypad1.put('6', new KeypadNode(2,1,'6'));
		keypad1.put('1', new KeypadNode(0,2,'1'));
		keypad1.put('2', new KeypadNode(1,2,'2'));
		keypad1.put('3', new KeypadNode(2,2,'3'));
		keypad1.put('0', new KeypadNode(1,3,'0'));
		keypad1.put('A', new KeypadNode(2,3,'A'));
		
		for (KeypadNode from : keypad1.values()) {
			from.addAllPaths(keypad1.values(), true);
		}

		keypad2.put('^', new KeypadNode2(1,0,'^'));
		keypad2.put('A', new KeypadNode2(2,0,'A'));
		keypad2.put('<', new KeypadNode2(0,1,'<'));
		keypad2.put('v', new KeypadNode2(1,1,'v'));
		keypad2.put('>', new KeypadNode2(2,1,'>'));
		
		for (KeypadNode from : keypad2.values()) {
			from.addAllPaths(keypad2.values(), true);			
		}
	}
	
	List<String> codes = new ArrayList<>();
	
	@Override
	public void handleInput(String line) {
		codes.add(line);
	}
	
	public Map<Character, KeypadNode> getPad(int padNum) {
		if (padNum == 0) {
			return keypad1;
		} else if (padNum > 1) {
			return keypad2;
		}
		return null;
	}
	
	// Keypad index -> Map of path to shortest length
	// We split paths by A, since every sequence must be moving from key1 to key2 then pressing A to press key2
	// So each split is independent, since you're always ending on A
	// Thus the cache should end up ~25 entries per level (5 key1 * 5 key2)
	Map<Integer, Map<String, Long>> cache = new HashMap<>();

	public long getShortestSubPathLength(String s, int padNum) {
		if (padNum == (this.getRobotCount() + 1)) {
			return s.length();
		}
		Map<String, Long> sCache = cache.computeIfAbsent(padNum, __ -> new HashMap<>());
		Long cacheHit = sCache.get(s);
		if (cacheHit != null) {
			return cacheHit;
		}
		
		String[] split = s.split("A");
		long total = 0;
		if (split.length == 0) {
			// Just an "A"
			split = new String[1];
			split[0] = "";
		}
		for (String s2 : split) {
			List<String> paths = getPaths(s2+"A", padNum+1);
			if (paths.isEmpty()) {
				throw new RuntimeException("Unable to find path for "+s2+" at level "+(padNum+1));
			}
			long minLen = Long.MAX_VALUE;
			for (String p : paths) {
				long shortest2 = getShortestSubPathLength(p, padNum+1);
				if (shortest2 < minLen) {
					minLen = shortest2;
				}
			}
			total += minLen;
		}
		logger.info("Adding "+total+" to cache for "+s+" for level "+padNum);
		sCache.put(s, total);
		return total;		
	}
	
	
	public List<String> getPaths(String codeStr, int padNum) {
		Map<Character, KeypadNode> pad = getPad(padNum);
		if (pad == null) {
			return null;
		}
		
		char[] code = codeStr.toCharArray();
		List<String> paths = new ArrayList<>();
		paths.add("");
		for (int i=0; i<code.length; ++i) {
			// Allow an npe here if we have a bad character, throw the exception
			KeypadNode n1 = null;
			if (i == 0) {
				n1 = pad.get('A');
			} else {
				n1 = pad.get(code[i-1]);
			}
			KeypadNode n2 = pad.get(code[i]);
			List<String> pathAdd = n1.paths.get(n2.c);
			if (pathAdd == null && n1 == n2) {
				pathAdd = new ArrayList<>();
				pathAdd.add("");
			} else if (pathAdd == null) {
				logger.error("Can't find a path from "+n1+" to "+n2);
			}
			List<String> nextPaths = new ArrayList<>();
			for (String p : paths) {
				for (String add : pathAdd) {
					nextPaths.add(p+add+"A");
				}
			}
			paths.clear();
			paths.addAll(nextPaths);
		}
		return paths;		
	}

	@Override
	public void output() {
		long complexityTotal = 0;
		for (String code : codes) {
			long shortest = findShortestHumanPress(code);
			long comp = complexity(shortest, code);
			logger.info("Complexity for "+code+" is "+comp+" with shortest length: "+shortest);
			complexityTotal += comp;
		}
		logger.info("Total: "+complexityTotal);
	}
		
	protected int getRobotCount() {
		return 2;
	}
	
	protected long findShortestHumanPress(String code) {
		List<String> best = getPaths(code, 0);
		
		int minLen = Integer.MAX_VALUE;
		for (String b : best) {
			if (b.length() < minLen) {
				minLen = b.length();
			}
		}
		
		List<String> shortest = new ArrayList<>();
		for (String b : best) {
			if (b.length() == minLen) {
				shortest.add(b);
				logger.info(code+": "+b);
			}
		}
		
		// Now find the N robot pushes to generate the best code
		Long minLenFinal = Long.MAX_VALUE;
		for (String s2 : shortest) {
			long sLen = getShortestSubPathLength(s2, 1);
			if (sLen < minLenFinal) {
				minLenFinal = sLen;
			}
		}
		
		return minLenFinal;
	}
	
	public long complexity(long shortest, String code) {
		String codeNum = "";
		for (int i=0; i<code.length(); ++i) {
			if (code.charAt(i) == 'A') {
				continue;
			}
			if (code.charAt(i) == '0' && codeNum.length() == 0) {
				continue;
			}
			codeNum = codeNum + code.charAt(i);
		}
		
		Integer codeNumInt = Utilities.parseIntOrNull(codeNum);
		return shortest * codeNumInt;
	}

}
