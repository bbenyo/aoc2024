package bb.aoc2024.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class Day3b extends Day3 {
	static private Logger logger = Logger.getLogger(Day3b.class.getName());
	static protected String MUL_DO_DONT_REGEX = "mul\\(\\d+,\\d+\\)|do\\(\\)|don't\\(\\)";
	
	boolean doing = true;
	@Override
	public void handleInput(String line) {
		line = line.trim();
		if (line.length() == 0) {
			return;
		}
		
		Matcher m = mulPattern.matcher(line);
		while(m.find()) {
			String mul = m.group();
			if (mul.startsWith("mul")) {
				logger.info("Mul found: "+mul);
				if (doing) {
					logger.info("\tin Do mode, adding to sum");
					sum += computeMul(mul);
				} else {
					logger.info("\tin Don't mode, ignoring");
				}
			} else if (mul.startsWith("don")) {
				logger.info("Entering don't mode: "+mul);
				doing = false;
			} else if (mul.startsWith("do(")) {
				logger.info("Entering do mode: "+mul);
				doing = true;
			} else {
				logger.error("Regex error, unexpected mul: "+mul);
			}
		}
	}
	
	@Override
	public void initialize() {
		mulPattern = Pattern.compile(MUL_DO_DONT_REGEX);
		doing = true;
	}

}
