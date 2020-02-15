package pacman.entries.ghosts;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getActions() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.ghosts.mypackage).
 */
public class OrrGhosts extends Controller<EnumMap<GHOST, MOVE>> {
	private EnumMap<GHOST, MOVE> myMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
	private Random rnd = new Random();
	private MOVE[] moves = MOVE.values();

	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		myMoves.clear();

		// Place your game logic here to play the game as the ghosts
		
		// will use:
		// for(GHOST ghost : GHOST.values())
		// myMoves.put(ghost,game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
		// game.getPacmanCurrentNodeIndex(),game.getGhostLastMoveMade(ghost),DM.PATH));

		if (game.wasPillEaten()) // Frightened; change to account for time
		// if (game.timeSincPillWasEaten() < ___ sec)
		{
			return myMoves;
		} else // (if timeSinceLastReversal > ___ sec)
		{
			// switch modes to chase or scatter
			// make new variable maybe
			return myMoves;
		}

	}
}