package bb.aoc2024.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import bb.aoc.utils.InputHandler;

public class Day3 implements InputHandler {

	static private Logger logger = Logger.getLogger(Day3.class.getName());

	static protected String MUL_REGEX = "mul\\(\\d+,\\d+\\)";
	protected Pattern mulPattern;
	long sum = 0;
	
	@Override
	public void handleInput(String line) {
		line = line.trim();
		if (line.length() == 0) {
			return;
		}
		
		Matcher m = mulPattern.matcher(line);
		while(m.find()) {
			String mul = m.group();
			logger.info("Mul found: "+mul);
			sum += computeMul(mul);
		}
		
	}
	
	protected long computeMul(String mul) {
		String[] mSplit = mul.split("\\(|\\)|,");  // Split on ( or , or )
		if (mSplit.length != 3) {
			logger.error("Unable to parse mul: "+mul);
			return 0;
		}
		String s1 = mSplit[1];
		String s2 = mSplit[2];
		try {
			Long l1 = Long.valueOf(s1);
			Long l2 = Long.valueOf(s2);
			return l1*l2;
		} catch (NumberFormatException ex) {
			logger.error(ex.toString(), ex);
		}
		return 0;
	}
	
	@Override
	public void output() {
		logger.info("Final Sum: "+sum);
	}

	@Override
	public void initialize() {
		mulPattern = Pattern.compile(MUL_REGEX);
	}

}
