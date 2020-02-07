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

public class BFSAgent implements Agent {
	
	@Override
	public Direction getMove(Game game) {
		
		Game g = game;
		Point goal = g.goal;
		Point start = g.p.getPosition();
		int lenx = g.map.length;
		int leny = g.map[0].length;
		
		// Only calculate shortest path one time
		if (moveList.isEmpty()) {
			HashMap <Point,Point> locPath = shortestLocPath(start,goal, lenx,leny, g);
			completePath(goal, locPath, start);
		}

		// If shortest path has been calculated, pull next move out
		if (moveList.isEmpty()) {
			System.out.println("empty moveList");
			return null;
		}else {
			Direction next = moveList.get(0);
			moveList.remove(0);
			return next;
		}
	}

	List<Direction> moveList = new ArrayList<>();
	HashMap <Point,Point> bfsMap = new HashMap<Point,Point>();	
	
	// do BFS & get map of connections
	public boolean BFS(Point start, Point goal, int lenx, int leny, Game g) {
		Queue<Point> q = new LinkedList<Point>();
		
		boolean[][] visited = new boolean[lenx][leny];
		for (int y=0; y<leny; y++) {
			for (int x=0; x<lenx; x++) {
				visited[x][y]=false;
			}
		}
		
		visited[start.x][start.y] = true;
		q.add(start);
		
		while(!q.isEmpty()) {
			Point curr = q.poll();
			for (Point i : g.getAdjacentLocations(curr)) {
				if(visited[i.x][i.y]==false) {
					visited[i.x][i.y]=true;
					bfsMap.put(i,curr);
					q.add(i);
					
					if (i.equals(goal)) {
						return true;
					}
				}
			}
		}

		return false;
	}
	
	// do shortest path & get map of moves that go to goal
	private HashMap<Point, Point> shortestLocPath(Point start, Point goal, int lenx, int leny, Game g) {
		if (!BFS(start, goal, lenx, leny, g)) {
			System.out.println("No path between source and goal");
		}
		HashMap<Point, Point> pathMap = new HashMap<Point,Point>();
		Point back = goal;
		Point pred = bfsMap.get(back);
		System.out.println(back);
		System.out.println(pred);
		while(true) {
			pathMap.put(back, pred);
			back=pred;
			if (pred.equals(start)){
				return pathMap;
			}			
			pred=bfsMap.get(back);
		}
	}
	
	// get lst of directions from map of shortest path moves
	private void completePath(Point dest, HashMap<Point, Point> preds, Point p) {
		Point curr = new Point(dest);
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

