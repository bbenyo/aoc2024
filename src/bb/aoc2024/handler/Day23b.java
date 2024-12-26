package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

public class Day23b extends Day23 {
	
	static private Logger logger = Logger.getLogger(Day23b.class.getName());
	
	@Override
	public void output() {
		// Keep finding groups of N+1 until we can't
		// Pretty bad brute force, but this will work unless we want to look up the proper clique forming algorithm
		findTriples();
		Set<Set<String>> largestClique = getTriples();
		int largestSize = 3;
		for (int i=4; i<netmap.size(); ++i) {
			Set<Set<String>> nextClique = findFullyConnectedGroups(i, largestClique);
			if (nextClique.isEmpty()) {
				// Last one was the largest, we expect it to be size 1
				logger.info("No clique of size "+i);
				break;
			}
			largestClique = nextClique;
			largestSize = i;
		}
		
		if (largestClique == null || largestClique.isEmpty()) {
			logger.error("Can't find largest clique");
			return;
		}
		
		if (largestClique.size() > 1) {
			logger.warn("Multiple options of size: "+largestSize);
		}
		
		for (Set<String> clique : largestClique) {
			List<String> clist = new ArrayList<String>(clique);
			Collections.sort(clist);
			logger.info("LAN party: "+String.join(",", clist));
		}
	}
	
	protected Set<Set<String>> getTriples() {
		Set<Set<String>> tSet = new HashSet<>();
		// We already found these
		for (Computer c : netmap.values()) {
			for (Set<String> triple : c.triples) {
				tSet.add(triple);
			}
		}
		return tSet;
	}
	
	protected Set<Set<String>> findFullyConnectedGroups(int size, Set<Set<String>> cliques) {
		logger.info("Looking for cliques of size: "+size);
		Set<Set<String>> tSet = new HashSet<>();
	
		for (Set<String> cli : cliques) {
			// Try to add another to this clique.
			// If has to be connected to the first at least, and not in the clique, and not tried yet
			Set<String> toTry = new HashSet<>();
			for (String c : cli) {
				Computer comp = netmap.get(c);
				for (String conn : comp.connections) {
					if (!cli.contains(conn)) {
						toTry.add(conn);
					}
				}
			}
			
			for (String t : toTry) {
				boolean fail = false;
				for (String c : cli) {
					Computer comp = netmap.get(c);
					if (!comp.connections.contains(t)) {
						fail = true;
						break;
					}
				}
				if (fail) {
					continue;
				}
				logger.debug("Adding "+t+" to clique: "+cli);
				Set<String> c2 = new HashSet<>();
				c2.addAll(cli);
				c2.add(t);
				tSet.add(c2);
			}
		}
		logger.info("Found "+tSet.size()+" cliques of size: "+size);
		return tSet;
	}
}
