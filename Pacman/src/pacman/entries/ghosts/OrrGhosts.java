package pacman.entries.ghosts;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;

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

	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		myMoves.clear();

		// Place your game logic here to play the game as the ghosts
		
		mode = Mode(game, prevTimeDue);
		for (GHOST ghost : GHOST.values()) // for each ghost
		{
			if (ghost.equals(GHOST.PINKY))
				if (game.doesGhostRequireAction(ghost)) // if ghost requires an action
				{
					if (mode.equals(MODE.FRIGHTENED))
						Frighten(ghost);
					else if (mode.equals(MODE.SCATTER))
						Scatter(ghost);
					else
						Chase(ghost);

				}
		}
		prevTimeDue = timeDue;
		return myMoves;
	}

	// determines current mode
	public MODE Mode(Game game, long prevTimeDue) {
		if (game.isGhostEdible(GHOST.PINKY))
			return MODE.FRIGHTENED;
		else if (game.getTimeOfLastGlobalReversal() == -1)
			return MODE.SCATTER;
		else if (mode.equals(MODE.FRIGHTENED))
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

	public void Scatter(GHOST ghost) {
		System.out.println("scatter mode");
	}

	public void Chase(GHOST ghost) {
		System.out.println("chase mode");
	}

}
