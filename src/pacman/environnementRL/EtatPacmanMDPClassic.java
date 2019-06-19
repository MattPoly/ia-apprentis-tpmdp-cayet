package pacman.environnementRL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import environnement.Etat;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */
public class EtatPacmanMDPClassic implements Etat , Cloneable{

	private int pacmanX;
	private int pacmanY;

	private int nbDot;

	private int closestDot;

	private ArrayList<ArrayList<Integer>> ghostPositions;
	private ArrayList<Integer> listDistance;

	private StateGamePacman stgp;

	public EtatPacmanMDPClassic(StateGamePacman _stategamepacman){

		this.stgp = _stategamepacman;
		//on récupère la position du pacman
		this.pacmanX = _stategamepacman.getPacmanState(0).getX();
		this.pacmanY = _stategamepacman.getPacmanState(0).getY();

		this.ghostPositions = new ArrayList<>(); //on récupère les positions de fantômes
		this.listDistance = new ArrayList<>(); //on récupère les distances aux fantomes
		for(int i = 0; i < _stategamepacman.getNumberOfGhosts(); i++){
			ArrayList<Integer> ghostState = new ArrayList<>();
			ghostState.add(_stategamepacman.getGhostState(i).getX());
			ghostState.add(_stategamepacman.getGhostState(i).getY());
			this.ghostPositions.add(ghostState);

			listDistance.add(Math.abs(this.pacmanX - _stategamepacman.getGhostState(i).getX()) + Math.abs(this.pacmanY - _stategamepacman.getGhostState(i).getY()));
		}

		this.nbDot = _stategamepacman.getMaze().getNbfood();

		this.closestDot = _stategamepacman.getClosestDot(_stategamepacman.getPacmanState(0));
	}

	public int getDimensions() {
		final int sizeX = stgp.getMaze().getSizeX();
		final int sizeY = stgp.getMaze().getSizeY();
		final int numberOfGhosts = stgp.getNumberOfGhosts();
		final int nbfood = stgp.getMaze().getNbfood();

		return sizeX * sizeY
				* (sizeX * sizeY)^numberOfGhosts
				* nbfood;
	}

	@Override
	public String toString() {

		return "";
	}


	public Object clone() {
		EtatPacmanMDPClassic clone = null;
		try {
			// On recupere l'instance a renvoyer par l'appel de la
			// methode super.clone()
			clone = (EtatPacmanMDPClassic)super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implementons
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}



		// on renvoie le clone
		return clone;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EtatPacmanMDPClassic that = (EtatPacmanMDPClassic) o;
		return pacmanX == that.pacmanX &&
				pacmanY == that.pacmanY &&
				nbDot == that.nbDot &&
				closestDot == that.closestDot &&
				ghostPositions.equals(that.ghostPositions);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pacmanX, pacmanY, nbDot, closestDot, ghostPositions);
	}
}
