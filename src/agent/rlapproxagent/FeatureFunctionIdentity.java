package agent.rlapproxagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import environnement.Action;
import environnement.Action2D;
import environnement.Etat;
import javafx.util.Pair;
/**
 * Vecteur de fonctions caracteristiques phi_i(s,a): autant de fonctions caracteristiques que de paire (s,a),
 * <li> pour chaque paire (s,a), un seul phi_i qui vaut 1  (vecteur avec un seul 1 et des 0 sinon).
 * <li> pas de biais ici 
 * 
 * @author laetitiamatignon
 *
 */
public class FeatureFunctionIdentity implements FeatureFunction {
	//*** VOTRE CODE

	private int lastPosition;
	private int nbEtat;
	private int nbAction;
	private double[] features;
	private Map<Integer, Integer> hashcodesPosition;
	private int nextPosition = 0;
	
	public FeatureFunctionIdentity(int _nbEtat, int _nbAction){
		//*** VOTRE CODE

		nbEtat = _nbEtat;
		nbAction = _nbAction;
		features = new double[nbAction * nbEtat];
		lastPosition = 0;
		hashcodesPosition = new HashMap<>();
	}
	
	@Override
	public int getFeatureNb() {
		//*** VOTRE CODE
		return nbAction*nbEtat;
	}

	@Override
	public double[] getFeatures(Etat e,Action a){
		//*** VOTRE CODE

		features[lastPosition] = 0;

		final int hashcode = e.hashCode() * a.ordinal();
		Integer newPosition;
		newPosition = hashcodesPosition.get(hashcode);

		if (newPosition == null) {
			hashcodesPosition.put(hashcode, this.nextPosition);
			newPosition = this.nextPosition;
			this.nextPosition++;
		}

		features[newPosition] = 1;
		lastPosition = newPosition;

		return features;
	}
	

}
