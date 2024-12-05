package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import bb.aoc.utils.InputHandler;
import bb.aoc.utils.Utilities;

public class Day5 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day5.class.getName());
	
	class OrderingRule {
		int before;
		List<Integer> after;
		
		public OrderingRule(int b, int a) {
			before = b;
			after = new ArrayList<>();
			after.add(a);
		}
		
		public void add(int a) {
			if (!after.contains(a)) {
				after.add(a);
			}
		}
	}
	
	// Key is the first int
	Map<Integer, OrderingRule> rules = new HashMap<>();
	List<Integer> pageList;
	boolean doneRules = false;
	long sum = 0;
	
	@Override
	public void initialize() {
	}

	@Override
	public void handleInput(String line) {
		if (line.trim().length() == 0) {
			return;
		}
		if (line.indexOf(",") > -1) {
			logger.info("Switching to parsing page lists");
			doneRules = true;
		}
		if (!doneRules) {
			String[] nums = line.split("\\|");
			if (nums.length != 2) {
				logger.error("Unable to parse rule: "+line);
				return;
			}
			Integer i1 = Utilities.parseIntOrNull(nums[0]);
			Integer i2 = Utilities.parseIntOrNull(nums[1]);
			if (i1 != null && i2 != null) {
				OrderingRule rule = rules.get(i1);
				if (rule != null) {
					rule.after.add(i2);
				} else {
					rule = new OrderingRule(i1, i2);
					rules.put(i1, rule);
				}
			}			
		} else {
			String[] pages = line.split(",");
			pageList = new ArrayList<>();
			for (int i=0; i<pages.length; ++i) {
				Integer page = Utilities.parseIntOrNull(pages[i]);
				if (page != null) {
					pageList.add(page);
				}
			}
			if (isValid(pageList)) {
				int mid = pageList.get(pageList.size() / 2);
				logger.info("Valid list: "+pageList+" Mid: "+mid);
				sum += mid;
			}
		}		
	}
	
	protected boolean isValid(List<Integer> pageList) {
		logger.info("Checking validitiy for "+pageList);
		for (int i=0; i<pageList.size(); ++i) {
			Integer page = pageList.get(i);
			// Get the ordering rules that apply to this page first
			OrderingRule rule = rules.get(page);
			if (rule == null) {
				continue;
			}
			// All of these pages have to come after if they're in the list
			// If they're not in the list, this rule doesn't apply
			for (Integer after : rule.after) {
				for (int j=0; j<i; ++j) {
					if (pageList.get(j) == after) {
						// Violation
						logger.info("\tViolation of rule: "+page+"|"+after);
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public void output() {
		logger.info("Sum: "+sum);
	}
	
}
