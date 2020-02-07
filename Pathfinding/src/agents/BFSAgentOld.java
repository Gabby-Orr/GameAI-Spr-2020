package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import game.Direction;
import game.Game;
import game.Point;

public class BFSAgentOld implements Agent {
	
	@Override
	public Direction getMove(Game game) {
		
		Game g = game;
		Point goal = g.goal;
		Point pos = g.p.getPosition();
		
		// allows path to not be recalculated each time
		if (moveList.isEmpty()) {
			List<Point> shortPath = path(pos, g);
			completePath(goal, predSucc, pos);
		}

		if (moveList.isEmpty()) {
			System.out.println("empty moveList");
			return null;
		}else {
			Direction next = moveList.get(0);
			moveList.remove(0);
			return next;
//			return moveList.pop();
		}
		//return Direction.RIGHT;
	}
			
	HashMap <Point,Point> predSucc = new HashMap<Point,Point>();
	List<Point> path = new ArrayList<>();	
	List<Direction> moveList = new ArrayList<>();
				
	
	// BFS Search, makes Map of Point=pairs in predSucc
	public List<Point> path(Point curr, Game g) {
		Queue<Point> q = new LinkedList<Point>();
		q.add(curr);
		Point prev = curr;
		while(!q.isEmpty()) {
			curr=q.poll();
//			Point curr=nextCurr;
			List<Point> next = g.getAdjacentLocations(curr);
			for (Point adj: next) {
				if (!predSucc.containsValue(adj)) {
					q.add(adj);
				}
			}
			if (!prev.equals(curr) && !predSucc.containsKey(curr)) {
				predSucc.put(curr, prev);
				prev = curr;
			}
			if (curr.equals(g.goal)) {
				break;
			}
		}

		System.out.println(predSucc);
		return path;
	}
	
	// turn path of locations into directions
	private void completePath(Point dest, HashMap<Point, Point> preds, Point p) {
		Point curr = new Point(dest);
		//System.out.println("doin completePath");
		while (true) {
			if (curr.equals(p)) return;
			Point prev = preds.get(curr);
			if (prev.x > curr.x) { // left
				if (prev.y > curr.y) { // up
					moveList.add(0,Direction.UP_LEFT);
				}
				else if (prev.y < curr.y) { // down
					moveList.add(0,Direction.DOWN_LEFT);
				}
				else {
					moveList.add(0,Direction.LEFT);
				}
			}
			else if (prev.x < curr.x) { // right
				if (prev.y > curr.y) { // up
					moveList.add(0,Direction.UP_RIGHT);
				}
				else if (prev.y < curr.y) { // down
					moveList.add(0,Direction.DOWN_RIGHT);
				}
				else {
					moveList.add(0,Direction.RIGHT);
				}
			}
			else { // equal
				if (prev.y > curr.y) { // up
					moveList.add(0,Direction.UP);
				}
				else if (prev.y < curr.y) { // down
					moveList.add(0,Direction.DOWN);
				}
				else { //should never happen
					System.out.println("Shouldn't be here!");
				}
			}
			curr = prev;
			}
		}
}