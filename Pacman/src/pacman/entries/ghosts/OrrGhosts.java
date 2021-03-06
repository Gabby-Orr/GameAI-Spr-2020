package pacman.entries.ghosts;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.GameView;
import java.awt.Color;

@SuppressWarnings("unused")
/*
 * This is the class you need to modify for your entry. In particular, you need
 * to fill in the getActions() method. Any additional classes you write should
 * either be placed in this package or sub-packages (e.g.,
 * game.entries.ghosts.mypackage).
 */
public class OrrGhosts extends Controller<EnumMap<GHOST, MOVE>> {
	private EnumMap<GHOST, MOVE> myMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
	private Random rnd = new Random();
	private MOVE[] moves = MOVE.values();
	private MODE mode;
	private long prevTimeDue = 0;
	private final EnumMap<GHOST, Integer> targetIndex = new EnumMap<GHOST, Integer>(GHOST.class);

	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		myMoves.clear();

		if (targetIndex.isEmpty()) {
			targetIndex.put(GHOST.BLINKY, game.getPowerPillIndices()[1]); // TOP-RIGHT
			targetIndex.put(GHOST.INKY, game.getPowerPillIndices()[3]); // BOTTOM-RIGHT
			targetIndex.put(GHOST.PINKY, game.getPowerPillIndices()[0]); // TOP-LEFT
			targetIndex.put(GHOST.SUE, game.getPowerPillIndices()[2]); // BOTTOM-LEFT
		}

//		GameView.addPoints(game, Color.CYAN, game.getPowerPillIndices()[330]);
		GameView.addPoints(game,Color.CYAN,305,  335);

		for (GHOST ghost : GHOST.values()) // for each ghost
		{
			mode = Mode(game, prevTimeDue, ghost);
			if (game.doesGhostRequireAction(ghost)) // if ghost requires an action
			{
				if (mode.equals(MODE.FRIGHTENED))
					Frighten(ghost);
				else if (mode.equals(MODE.SCATTER))
					myMoves.put(ghost, getScatterActions(game, ghost));
				else
					myMoves.put(ghost, getChaseActions(game, ghost));
			}
			prevTimeDue = timeDue;
		}
		return myMoves;
	}

	// determines current mode
	public MODE Mode(Game game, long prevTimeDue, GHOST ghost) {
		if (game.isGhostEdible(ghost))
			return MODE.FRIGHTENED;
		else if (game.getTimeOfLastGlobalReversal() == -1 || mode.equals(MODE.FRIGHTENED)) // initial gamestate or
																							// ghosts aren't edible
																							// anymore, switch to
																							// scatter
			return MODE.SCATTER;
		else if (game.getTimeOfLastGlobalReversal() <= prevTimeDue)
			if (mode.equals(MODE.SCATTER))
				return MODE.CHASE;
			else
				return MODE.SCATTER;
		else
			return mode; // return last mode if not switching yet
	}

	public enum MODE {
		FRIGHTENED, SCATTER, CHASE
	};

	public void Frighten(GHOST ghost) {
		System.out.println("ahh, frightened mode");
	}

	private MOVE getScatterActions(Game game, GHOST ghost) {
		int currentIndex = game.getGhostCurrentNodeIndex(ghost);
		int pacManIndex = game.getPacmanCurrentNodeIndex();

		if (!(ghost.equals(GHOST.BLINKY) && isGhostHouseEmpty(game)))
			return game.getApproximateNextMoveTowardsTarget(currentIndex, targetIndex.get(ghost),
					game.getGhostLastMoveMade(ghost), DM.PATH);
		else
			return getChaseActions(game, ghost);
	}

	// TODO: see if you need break; or not
	private MOVE getChaseActions(Game game, GHOST ghost) {
		int currentIndex = game.getGhostCurrentNodeIndex(ghost);
		int pacManIndex = game.getPacmanCurrentNodeIndex();
		int target = currentIndex;
		int pacMv1, pacMv2, pacMv3, pacMv4;

		switch (ghost) {
		case BLINKY:
			System.out.println("Blinky chase");
			// target PM current tile
			target = pacManIndex;
			break;
		case INKY:
			System.out.println("Inky chase");
			// target 2 spots ahead of PM
			pacMv1 = game.getNeighbour(pacManIndex, game.getPacmanLastMoveMade());
			if (pacMv1 == -1)
				target = pacManIndex;
			else {
				pacMv2 = game.getNeighbour(pacMv1, game.getPacmanLastMoveMade());
				if (pacMv2 == -1)
					target = pacMv1;
				else
					target = pacMv2;
			}
			break;
		// TODO: Inky's complicated chase rule
		case PINKY:
			System.out.println("pinky chase");
			// target 4 ahead (if left, down, or up)
			target = tilesAheadOfPacman(game, 4, pacManIndex);
			System.out.println(target + "  " + pacManIndex);
			break;

		case SUE:
			System.out.println("sue chase");
			// if (PM > 8 tiles away)
			// Target PM current tile
			// else
			// scatter target (bottom-left)
			if (game.getShortestPathDistance(currentIndex, pacManIndex, game.getGhostLastMoveMade(ghost)) > 8)
				target = pacManIndex;
			else
				target = targetIndex.get(ghost);
			break;
		default:
			System.out.println("uh oh in chase");
			break;
		}
		return game.getApproximateNextMoveTowardsTarget(currentIndex, target, game.getGhostLastMoveMade(ghost),
				DM.PATH);
	}

	private Boolean isGhostHouseEmpty(Game game) {
		int t = 0;
		for (GHOST ghost : GHOST.values())
			t += game.getGhostLairTime(ghost);
		return t == 0;
	}

	private int tilesAheadOfPacman(Game game, int tiles, int currentIndex) {
		int nxtSpace = game.getNeighbour(currentIndex, game.getPacmanLastMoveMade());
		if (nxtSpace == -1 || tiles==-1)
			return currentIndex;
		else
			return tilesAheadOfPacman(game, tiles - 1, nxtSpace);
	}
}
