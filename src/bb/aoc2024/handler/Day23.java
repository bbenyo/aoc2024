package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import bb.aoc.utils.InputHandler;

public class Day23 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day23.class.getName());

	class Computer {
		String hostname;
		Set<String> connections;
		List<Set<String>> triples;
		
		public Computer(String n) {
			this.hostname = n;
			connections = new HashSet<>();
			triples = new ArrayList<>();
		}
		
		public void addConnection(String c) {
			// Handle bad inputs
			if (c.equals(hostname)) {
				logger.warn("Loopback connection, ignoring");
				return;
			}
			connections.add(c);
		}
		
		@Override
		public String toString() {
			return hostname;
		}
	}
	
	@Override
	public void initialize() {
		// no-op
	}

	Map<String, Computer> netmap = new HashMap<>();
	
	@Override
	public void handleInput(String line) {
		if (line.isEmpty()) {
			return;
		}
		String[] comps = line.split("-");
		if (comps.length != 2) {
			logger.error("Unable to parse: "+line);
			return;
		}
		
		String c1 = comps[0];
		String c2 = comps[1];
		Computer comp1 = netmap.computeIfAbsent(c1, __ -> new Computer(c1));
		Computer comp2 = netmap.computeIfAbsent(c2, __ -> new Computer(c2));
		comp1.addConnection(c2);
		comp2.addConnection(c1);
	}
	
	public void findTriples() {
		for (Computer c : netmap.values()) {
			List<String> connList = new ArrayList<>(c.connections);
			for (int i=0; i<connList.size(); ++i) {
				Computer c2 = netmap.get(connList.get(i));					
				for (int j=i+1; j<connList.size(); ++j) {
					// Double (c, c2)
					// Find a c3 where c-c3 and c2-c3
					Computer c3 = netmap.get(connList.get(j));
					if (c2.connections.contains(c3.hostname)) {
						c.triples.add(new HashSet<>(List.of(c.hostname, c2.hostname, c3.hostname)));
					}					
				}
			}
		}
	}

	@Override
	public void output() {
		findTriples();
		
		Set<Set<String>> tSet = new HashSet<>();
		for (Computer c : netmap.values()) {
			for (Set<String> triple : c.triples) {
				for (String t : triple) {
					if (t.charAt(0) == 't') {
						tSet.add(triple);
					}
				}
			}
		}
		
		for (Set<String> triple : tSet) {
			logger.info("Triple: "+triple);
		}
		logger.info("Triple Set size: "+tSet.size());
	}

}
