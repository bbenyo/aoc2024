package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import bb.aoc.utils.InputHandler;
import bb.aoc.utils.Utilities;

public class Day17 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day17.class.getName());

	// Registers
	Long A;
	Long B;
	Long C;
	
	List<Character> program = new ArrayList<>();
	List<Character> output = new ArrayList<>();
		
	@Override
	public void initialize() {
		// no-op
	}

	@Override
	public void handleInput(String line) {
		if (line.trim().length() == 0) {
			return;
		}
		if (line.startsWith("Register")) {
			Long val = Utilities.parseLongOrNull(line.substring(line.indexOf(":") + 1).trim());
			if (val == null) {
				logger.error("Unable to parse: "+line);
			}
			
			if (line.startsWith("Register A")) {
				A = val;				
			} else if (line.startsWith("Register B")) {
				B = val;
			} else if (line.startsWith("Register C")) {
				C = val;
			}
		} else if (line.startsWith("Program: ")) {
			String prg = line.substring(line.indexOf(":") + 1);
			String[] pArr = prg.split(",");
			for (String pStrp : pArr) {
				String pTrim = pStrp.trim();
				if (pTrim.length() != 1) {
					logger.error("Unable to parse program step: "+pStrp);
					continue;
				}
				program.add(pTrim.charAt(0));
			}
		}
	}
	
	protected void runProgram() {
		int ip = 0;
		while (ip < (program.size() -1)) {
			Character opcode = program.get(ip);
			Character operand = program.get(ip+1);
			ip = execute(opcode, operand, ip);
		}

		logger.info("A = "+A+" B = "+B+" C = "+C);
		logger.info("Final output: "+Utilities.listToString(output, ","));	
	}

	@Override
	public void output() {
		runProgram();
	}

	protected int execute(Character opcode, Character operand, int ip) {
		long arg = decodeOperand(opcode, operand);
		switch (opcode) {
		case '0' : 
			A = A >> arg;
			logger.debug("adv("+arg+"): A = "+A);
			return ip+2;			
		case '1' : 
			logger.debug("bxl: B: "+B+" arg: "+arg);
			B = B ^ arg;
			logger.debug("bxl("+arg+"): B = "+B);
			return ip+2;
		case '2' : 
			B = (arg % 8);
			logger.debug("bst("+arg+"): B = "+B);
			return ip+2;		
		case '3' :
			if (A == 0) {
				logger.debug("jnz(A = 0) = nop");
				return ip+2;
			}
			logger.debug("jnz(A = "+A+"): ip = "+arg); 
			return Math.toIntExact(arg);
		case '4' :
			long v = B ^ C;
			logger.debug("bxc(B = "+B+" C="+C+"): B = "+v);
			B = v;
			return ip+2;
		case '5' : 
			long v5 = arg % 8;
			output.add(Character.valueOf((char)('0' + v5)));
			logger.debug("out("+arg+") = "+output.get(output.size() - 1));
			return ip+2;
			
		case '6' : 
			B = A >> arg;
			logger.debug("bdv("+arg+"): B = "+B);
			return ip+2;	
		case '7' :
			C = A >> arg;
			logger.debug("cdv("+arg+"): C = "+C);
			return ip+2;	
		default:
			logger.error("Unexpected opcode: "+opcode);
		}
		return ip;
	}
	
	protected long decodeOperand(Character opcode, Character operand) {
		boolean isLiteral = true;
		if (opcode == '0' || opcode == '2' || opcode == '5' || opcode == '6' || opcode == '7') {
			isLiteral = false;
		}
		if (isLiteral) {
			return (operand - '0');
		} else {
			switch (operand) {
			case '0': return 0l;
			case '1': return 1l;
			case '2': return 2l;
			case '3': return 3l;
			case '4': return A;
			case '5': return B;
			case '6': return C;
			default: 
				logger.error("Parsed combo operand "+operand+", invalid program");
				return 0;
			}
		}
	}

}
