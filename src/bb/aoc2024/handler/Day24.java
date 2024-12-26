package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import bb.aoc.utils.Gate;
import bb.aoc.utils.Gate.GateType;
import bb.aoc.utils.InputHandler;
import bb.aoc.utils.Utilities;

public class Day24 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day24.class.getName());
	
	Map<String, Boolean> wires = new HashMap<>();
	List<Gate> gates = new ArrayList<>();
	
	@Override
	public void initialize() {
		// no-op
	}

	@Override
	public void handleInput(String line) {
		if (line.isEmpty()) {
			return;
		}
		int cPos = line.indexOf(":");
		if (cPos > -1) {
			String wire = line.substring(0, cPos).trim();
			String val = line.substring(cPos+1).trim();
			Integer valI = Utilities.parseIntOrNull(val);
			if (valI == 0) {
				wires.put(wire, Boolean.FALSE);
			} else if (valI == 1) {
				wires.put(wire, Boolean.TRUE);
			} else {
				logger.error("Unexpected wire value: "+valI);
			}
			logger.info(wire+" = "+wires.get(wire));
		} else {
			String[] split = line.split(" ");
			if (split.length != 5) {
				logger.error("Unable to parse gate: "+line);
				return;
			}
			String w1 = split[0];
			String w2 = split[2];
			String gateType = split[1];
			String wout = split[4];
			
			Gate g = new Gate(GateType.valueOf(gateType), w1, w2, wout);
			logger.info("Parsed gate: "+g);
			gates.add(g);
			if (!wires.containsKey(w1)) {
				wires.put(w1, null);
			}
			if (!wires.containsKey(w2)) {
				wires.put(w2, null);
			}
			if (!wires.containsKey(wout)) {
				wires.put(wout, null);
			}
		}
	}

	@Override
	public void output() {
		execute();
		
		long val = 0;
		for (int i=0; i<64; ++i) {
			String wire = "z"+i;
			if (i < 10) {
				wire = "z0"+i;
			}
			Boolean out = wires.get(wire);
			logger.info("wire "+wire+" = "+out);
			if (out == null) {
				break;
			}
			if (out) {
				val += Math.pow(2, i);
			}
		}
		logger.info("Final value from z wires: "+val);
	}
	
	public void execute() {
		boolean changed = true;
		
		int i=0;
		while (changed) {
			logger.info("Cycle: "+i);
			changed = false;
			for (Gate g : gates) {
				Boolean out = g.getOut();
				Boolean o2 = g.execute(wires);
				if (o2 != null && !o2.equals(out)) {
					logger.info("\t"+g+" changed value to "+o2);
					changed = true;
					wires.put(g.getWireOut(), o2);
				}
			}
			i++;
		}
	}

}
