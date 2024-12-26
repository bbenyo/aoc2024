package bb.aoc2024.handler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import bb.aoc.utils.Gate;

public class Day24b extends Day24 {
	
	static private Logger logger = Logger.getLogger(Day24b.class.getName());
	
	@Override
	public void output() {
		// First, try to look at it graphically, maybe it's obvious
		// If not, find nodes that are different in terms of number of connections
		writeDotFile();
		// View the graph with graphviz, fdp layout
        // fdp -Tsvg day25.dot -o d25_fdp.svg
		
		// Looking at the svg, each input pair (x11,y11) should have an AND and XOR gate to the same output
		// e.g. X12 AND Y12 = xxx, X12 XOR Y12 = yyy
		// X11/y11 don't follow this pattern
		makeGateMap();
		// Assume x00/y00 are abnormal since they start
		for (int i=0; i<64; ++i) {
			String xgate = "x"+i;
			if (i < 10) {
				xgate = "x0"+i;
			}
			String ygate = "y"+i;
			if (i < 10) {
				ygate = "y0"+i;
			}
			checkInputGate(xgate, ygate);
			checkInputGate(ygate, xgate);
		}
		
		// z05 doesn't connect to anything
		// x05 AND y05 = z05 is wrong, z05 should be something else

		// x05 AND y05 = tst
		// pvb XOR vtn = z05
		
		// sps, z11 are next
		// tff OR rrr -> z11
		// hkc XOR tdd -> sps
		
		// frt and z23
		// pjg AND kmg -> frt
		
		// cgh and pmd
		
		List<String> swaps = new ArrayList<>(List.of("tst", "z05", "sps", "z11", "frt", "z23", "cgh", "pmd"));
		Collections.sort(swaps);
		logger.info("Swaps: "+String.join(",", swaps));		
	}
	
	protected void checkInputGate(String g1, String g2) {
		List<Gate> gs = gateMap.get(g1);
		if (gs == null) {
			return;
		}
		// Expect 2 gates, one AND, one XOR
		if (gs.size() != 2) {
			logger.warn(g1+" has "+gs.size()+" gates");
		}

		// Find a gate xgate AND ygate = xxx
		// Output of the AND should go into one OR only
		// Output of the XOR should go into one AND only
		String andOut = null;
		String xorOut = null;
		for (Gate g : gs) {
			if ((g.getWire1().equals(g1) && g.getWire2().equals(g2)) ||
					(g.getWire1().equals(g2) && g.getWire2().equals(g1))) {
				if (g.getType().equals(Gate.GateType.AND)) {
					andOut = g.getWireOut();
				} else if (g.getType().equals(Gate.GateType.XOR)) {
					xorOut = g.getWireOut();
				} else {
					logger.warn("Suspicious gate: "+g);
				}
			} else {
				logger.warn("Suspicious gate: "+g+" unexpected wires: "+g1+","+g2);
			}				
		}
		if (andOut == null) {
			logger.info("No AND gate for "+g1+" "+g2);
		} else {
			// And out should go into one OR
			List<Gate> andOutGates = gateMap.get(andOut);
			if (andOutGates == null) {
				logger.warn(andOut+" doesn't connect to anything");
			} else {
				if (andOutGates.size() == 1 && andOutGates.get(0).getType().equals(Gate.GateType.OR)) {
					// expected
				} else {
					for (Gate g3 : andOutGates) {
						logger.warn("Unexpected AND gates for "+andOut+" from "+g1+","+g2+": "+g3);
					}
				}
			}
		}
		if (xorOut == null) {
			logger.info("No XOR gate for "+g1+" "+g2);
		} else {
			// XOR out should go into one AND and one XOR to the Z
			List<Gate> xOutGates = gateMap.get(xorOut);
			if (xOutGates == null) {
				logger.warn(xorOut+" doesn't connect to anything");
			} else {
				String foundAND = null;
				String foundXOR = null;
				boolean sus = false;
				for (Gate g3 : xOutGates) {
					if (foundAND == null && g3.getType().equals(Gate.GateType.AND)) {
						foundAND = g3.getWireOut();
						continue;
					}
					if (foundXOR == null && g3.getType().equals(Gate.GateType.XOR)) {
						foundXOR = g3.getWireOut();
						continue;
					}
					sus = true;
				}
				String zgate = "z"+g1.substring(1);
				if (!zgate.equals(foundXOR)) {
					logger.warn("XOR gate should connect to "+zgate+" but it's "+foundXOR);
					sus = true;
				}
				if (sus) {
					for (Gate g3 : xOutGates) {
						logger.warn("Unexpected XOR gates for "+xorOut+" from "+g1+","+g2+": "+g3);
					}
				}
			}
		}
	}
	
	Map<String, List<Gate>> gateMap = new HashMap<>();
	
	protected void makeGateMap() {
		for (Gate g : gates) {
			List<Gate> w1g = gateMap.computeIfAbsent(g.getWire1(), __ -> new ArrayList<>());
			w1g.add(g);
			List<Gate> w2g = gateMap.computeIfAbsent(g.getWire2(), __ -> new ArrayList<>());
			w2g.add(g);			
		}		
	}
	
	protected void writeDotFile() {
		try (BufferedWriter bwrite = new BufferedWriter(new FileWriter("day24.dot"))) {
			bwrite.write("graph d24 {"+System.lineSeparator());
			for (Gate g : gates) {
				bwrite.write(g.toDot());
			}
			bwrite.write("}");
			logger.info("Wrote day24.dot");
		} catch (IOException ex) {
			logger.error(ex.toString(), ex);
		}
	}
}
