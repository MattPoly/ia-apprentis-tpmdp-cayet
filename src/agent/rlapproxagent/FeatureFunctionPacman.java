package agent.rlapproxagent;

import pacman.elements.ActionPacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import pacman.environnementRL.EnvironnementPacmanMDPClassic;
import environnement.Action;
import environnement.Etat;
/**
 * Vecteur de fonctions caracteristiques pour jeu de pacman: 4 fonctions phi_i(s,a)
 *  
 * @author laetitiamatignon
 *
 */
public class FeatureFunctionPacman implements FeatureFunction{
	private double[] vfeatures ;

	private static int NBACTIONS = 4;//5 avec NONE possible pour pacman, 4 sinon 
	//--> doit etre coherent avec EnvironnementPacmanRL::getActionsPossibles


	public FeatureFunctionPacman() {

	}

	@Override
	public int getFeatureNb() {
		return 9;
	}

	@Override
	public double[] getFeatures(Etat e, Action a) {
		vfeatures = new double[9];
		StateGamePacman stategamepacman ;
		//EnvironnementPacmanMDPClassic envipacmanmdp = (EnvironnementPacmanMDPClassic) e;

		//calcule pacman resulting position a partir de Etat e
		if (e instanceof StateGamePacman){
			stategamepacman = (StateGamePacman)e;
		}
		else{
			System.out.println("erreur dans FeatureFunctionPacman::getFeatures n'est pas un StateGamePacman");
			return vfeatures;
		}

		StateAgentPacman pacmanstate_next= stategamepacman.movePacmanSimu(0, new ActionPacman(a.ordinal()));

		//*** VOTRE CODE
		vfeatures[0] = 1;

		final int pacmanX = pacmanstate_next.getX();
		final int pacmanY = pacmanstate_next.getY();

		final int numberOfGhosts = stategamepacman.getNumberOfGhosts();
		int numberOfGhostsCloseToPacman = 0;

		for (int g = 0; g < numberOfGhosts; g++) {
			final StateAgentPacman ghost = stategamepacman.getGhostState(g);
			final int ghostX = ghost.getX();
			final int ghostY = ghost.getY();

			final int distanceX = Math.abs(ghostX - pacmanX);
			final int distanceY = Math.abs(ghostY - pacmanY);

			if (distanceX == 1 || distanceY == 1) {
				numberOfGhostsCloseToPacman++;
			}
		}

		vfeatures[1] = numberOfGhostsCloseToPacman;

		final boolean[][] dots = stategamepacman.getMaze().getFood();

		vfeatures[2] = dots[pacmanX][pacmanY] ? 5 : 0;

		final int distanceToClosestDot = stategamepacman.getClosestDot(pacmanstate_next);
		final int mapSize = stategamepacman.getMaze().getSizeX() * stategamepacman.getMaze().getSizeY();

		vfeatures[3] = distanceToClosestDot / mapSize;

		vfeatures[4] = - stategamepacman.getMaze().getNbfood() * 2;

		vfeatures[5] = stategamepacman.getMaze().isWall(pacmanX, pacmanY) ? -10 : 0;

		vfeatures[6] = stategamepacman.isWin() ? 10 : 0;
		vfeatures[8] = stategamepacman.isLose() ? -10 : 0;

		return vfeatures;
	}

	public void reset() {
		vfeatures = new double[8];

	}
}