package agent.rlapproxagent;


import java.util.ArrayList;
import java.util.List;

import agent.rlagent.QLearningAgent;
import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
/**
 * Agent qui apprend avec QLearning en utilisant approximation de la Q-valeur : 
 * approximation lineaire de fonctions caracteristiques 
 * 
 * @author laetitiamatignon
 *
 */
public class QLApproxAgent extends QLearningAgent{

	private List<Double> listPoids;
	private FeatureFunction featurefunction;

	public QLApproxAgent(double alpha, double gamma, Environnement _env,FeatureFunction _featurefunction) {
		super(alpha, gamma, _env);
		//*** VOTRE CODE
		featurefunction = _featurefunction;
		listPoids = new ArrayList<>(featurefunction.getFeatureNb());

		for (int i = 0; i < featurefunction.getFeatureNb(); ++i) {
			listPoids.add(1d);
		}
	}

	
	@Override
	public double getQValeur(Etat e, Action a) {
		//*** VOTRE CODE
		double[] listFeatures = featurefunction.getFeatures(e, a);

		int somme = 0;
		for (int i = 0; i < listFeatures.length; i++) {
			somme += listPoids.get(i) * listFeatures[i];
		}

		return somme;
	}
	
	
	
	
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		if (RLAgent.DISPRL){
			System.out.println("QL: mise a jour poids pour etat \n"+e+" action "+a+" etat' \n"+esuivant+ " r "+reward);
		}
       //inutile de verifier si e etat absorbant car dans runEpisode et threadepisode 
		//arrete episode lq etat courant absorbant	
		
		//*** VOTRE CODE
		final ArrayList<Double> anciensPoids = new ArrayList<>(listPoids);

		final double qValeurSpA = getValeur(esuivant);
		final double qValeurSA = getQValeur(e, a);

		for (int i = 0; i < anciensPoids.size(); i++) {
			double newPoids = anciensPoids.get(i)
					+ alpha * (reward + gamma * qValeurSpA - qValeurSA) * featurefunction.getFeatures(e, a)[i];

			listPoids.set(i, newPoids);
		}
		
	}
	
	@Override
	public void reset() {
		super.reset();
		this.qvaleurs.clear();
	
		//*** VOTRE CODE
		listPoids = new ArrayList<>(featurefunction.getFeatureNb());

		for (int i = 0; i < featurefunction.getFeatureNb(); ++i) {
			listPoids.add(1d);
		}

		this.episodeNb = 0;
		this.notifyObs();
	}
	
	
}
