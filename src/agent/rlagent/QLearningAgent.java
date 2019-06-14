package agent.rlagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.util.Pair;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
/**
 *
 * @author laetitiamatignon
 *
 */
public class QLearningAgent extends RLAgent {
	/**
	 *  format de memorisation des Q valeurs: utiliser partout setQValeur car cette methode notifie la vue
	 */
	protected HashMap<Etat,HashMap<Action,Double>> qvaleurs;
	
	//AU CHOIX: vous pouvez utiliser une Map avec des Pair pour cles 
	//protected HashMap<Pair<Etat,Action>,Double> qvaleurs;

	
	/**
	 * 
	 * @param alpha
	 * @param gamma
	 * @param Environnement
	 */
	public QLearningAgent(double alpha, double gamma, Environnement _env) {
		super(alpha, gamma,_env);
		qvaleurs = new HashMap<Etat,HashMap<Action,Double>>();
	}


	
	
	/**
	 * renvoi action(s) de plus forte(s) valeur(s) dans l'etat e
	 *  (plusieurs actions sont renvoyees si valeurs identiques)
	 *  renvoi liste vide si aucunes actions possibles dans l'etat (par ex. etat absorbant)

	 */
	@Override
	public List<Action> getPolitique(Etat e) {
		List<Action> returnactions = new ArrayList<Action>();
		if (this.getActionsLegales(e).size() == 0){//etat  absorbant; impossible de le verifier via environnement
			System.out.println("aucune action legale");
			return new ArrayList<Action>();
			
		}
		//*** VOTRE CODE
		double maxQ = -1.0;
		for(Action a: this.getActionsLegales(e)){
			double qvaleur = this.getQValeur(e,a);
			if(this.getQValeur(e,a) > maxQ){
				returnactions.clear();
				returnactions.add(a);
 				maxQ = qvaleur;
			} else if(qvaleur == maxQ){
				returnactions.add(a);
				maxQ = qvaleur;
			}
		}
		
		return returnactions;
		
		
	}
	
	/**
	 * renvoi V(e) = max_a Q(e,a)
	 */
	@Override
	public double getValeur(Etat e) {
		//*** VOTRE CODE
		Double maxEchanQValeur = 0.0;
		try {
			for (Map.Entry<Action , Double> tempAction : this.qvaleurs.get(e).entrySet()) {
				maxEchanQValeur = (tempAction.getValue() > maxEchanQValeur) ? tempAction.getValue() : maxEchanQValeur;
			}
		} catch (Exception E){

		}
		return maxEchanQValeur;
		
	}

	/**
	 * renvoi Q(e,a), et 0 si les cles n'existent pas
	 */
	@Override
	public double getQValeur(Etat e, Action a) {
		//*** VOTRE CODE
		double qValeur = 0.0;

		if(this.qvaleurs.containsKey(e)) {
			if(this.qvaleurs.get(e).containsKey(a)){
				qValeur = this.qvaleurs.get(e).get(a);
			}
		}
		return qValeur;
	}
	
	
	/**
	 * juste un setter: met la Q valeur du couple (e,a) a d
	 * la valeur d du couple est calcule dans endStep
	 */
	@Override
	public void setQValeur(Etat e, Action a, double d) {
		//*** VOTRE CODE
		if(!this.qvaleurs.containsKey(e)) {
			this.qvaleurs.put(e, new HashMap<>());
		}
		this.qvaleurs.get(e).put(a,d);
		//mise a jour de vmax et vmin (attributs de la classe mere) 
		//vmax et vmin sont utilises pour l'affichage du gradient de couleur:
		//vmax est la valeur max de V pour tout s 
		//vmin est la valeur min de V pour tout s
		
		
		
		this.notifyObs();
		
	}
	
	
	/**
	 * mise a jour du couple etat-valeur (e,a) apres chaque interaction <etat e,action a, etatsuivant esuivant, recompense reward>
	 * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement apres avoir realise une action.
	 * @param e
	 * @param a
	 * @param esuivant
	 * @param reward
	 */
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		if (RLAgent.DISPRL)
			System.out.println("QL mise a jour etat "+e+" action "+a+" etat' "+esuivant+ " r "+reward);

		//*** VOTRE CODE

		Double oldQvaleur = this.getQValeur(e,a);
		Double newEchan = reward + getGamma() * this.getValeur(esuivant);
		Double newQvaleur = (1 - this.getAlpha()) * oldQvaleur + this.getAlpha() * newEchan;

		this.setQValeur(e, a, newQvaleur);
	}

	@Override
	public Action getAction(Etat e) {
		this.actionChoisie = this.stratExplorationCourante.getAction(e);
		return this.actionChoisie;
	}

	@Override
	public void reset() {
		super.reset();
		this.qvaleurs.clear();
		
		this.episodeNb =0;
		this.notifyObs();
	}









	


}
