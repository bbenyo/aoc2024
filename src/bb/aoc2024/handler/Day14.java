package bb.aoc2024.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import bb.aoc.utils.Grid;
import bb.aoc.utils.InputHandler;
import bb.aoc.utils.Location;
import bb.aoc.utils.Utilities;

public class Day14 implements InputHandler {
	
	static private Logger logger = Logger.getLogger(Day14.class.getName());

	Grid map = new Grid();
	protected int width = 101;
	protected int height = 103;
	protected int secs = 100;
	
	class Robot {
		Location start;
		Location cur;
		int vx;
		int vy;
		int id;
		
		public String toString() {
			return "Robot "+id+": "+cur+" vx: "+vx+" vy: "+vy;
		}
		
		public void move() {
			int nx = cur.getX() + vx;
			int ny = cur.getY() + vy;
			while (nx < 0) {
				nx = width + nx;   
			}
			while (nx >= width) {
				nx = nx - width;
			}
			while (ny < 0) {
				ny = height + ny;
			}
			while (ny >= height) {
				ny = ny - height;
			}
			cur = new Location(nx, ny);
		}
	}
	
	List<Robot> robots = new ArrayList<>();
	
	@Override
	public void initialize() {
		map.initialize(width, height, '.');
	}

	@Override
	public void handleInput(String line) {
		if (line.length() == 0) {
			return;
		}
		String p = Utilities.parseString(line, 0, "p=", " ");
		String[] psplit = p.split(",");
		if (psplit.length != 2) {
			logger.error("Unable to parse: "+line);
			return;
		}
		Integer px = Utilities.parseIntOrNull(psplit[0]);
		Integer py = Utilities.parseIntOrNull(psplit[1]);
		
		String v = Utilities.parseString(line, 0, "v=", "");
		String[] vsplit = v.split(",");
		if (vsplit.length != 2) {
			logger.error("Unable to parse: "+line);
			return;
		}
		Integer vx = Utilities.parseIntOrNull(vsplit[0]);
		Integer vy = Utilities.parseIntOrNull(vsplit[1]);
		
		Robot r1 = new Robot();
		r1.start = new Location(px, py);
		r1.cur = r1.start;
		r1.vx = vx;
		r1.vy = vy;
		r1.id = robots.size();
		robots.add(r1);
	}

	@Override
	public void output() {
		// Move robots
		for (int i=0; i<secs; ++i) {
			for (Robot r : robots) {
				r.move();
			}
		}
		
		int[] qtrs = computeSafety();
		logger.info("Robots in Top Left: "+qtrs[0]);
		logger.info("Robots in Top Right: "+qtrs[1]);
		logger.info("Robots in Bottom Left: "+qtrs[2]);
		logger.info("Robots in Bottom Right: "+qtrs[3]);
		
		long score = qtrs[0] * qtrs[1] * qtrs[2] * qtrs[3];
		logger.info("Safety: "+score);
	}
	
	protected int[] computeSafety() {
		// Count who is in each quadrant
		int qTL = 0;
		int qTR = 0;
		int qBL = 0;
		int qBR = 0;
		
		int midW = width / 2;
		int midH = height / 2;
		for (Robot r : robots) {
			if (r.cur.getX() < midW) {
				if (r.cur.getY() < midH) {
					qTL++;
				} else if (r.cur.getY() > midH) {
					qBL ++;
				}
			} else if (r.cur.getX() > midW) {
				if (r.cur.getY() < midH) {
					qTR++;
				} else if (r.cur.getY() > midH) {
					qBR ++;
				}
			}
		}
		
		int[] qtrs = new int[4];
		qtrs[0] = qTL;
		qtrs[1] = qTR;
		qtrs[2] = qBL;
		qtrs[3] = qBR;
		return qtrs;
	}
}
