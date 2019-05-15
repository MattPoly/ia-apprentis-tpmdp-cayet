package agent.planningagent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import util.HashMapUtil;

import java.util.HashMap;

import environnement.Action;
import environnement.Etat;
import environnement.IllegalActionException;
import environnement.MDP;
import environnement.Action2D;

import static java.lang.Math.abs;


/**
 * Cet agent met a jour sa fonction de valeur avec value iteration 
 * et choisit ses actions selon la politique calculee.
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent{
	/**
	 * discount facteur
	 */
	protected double gamma;

	/**
	 * fonction de valeur des etats
	 */
	protected HashMap<Etat,Double> V;
	
	/**
	 * 
	 * @param gamma
	 * @param mdp
	 */
	public ValueIterationAgent(double gamma,  MDP mdp) {
		super(mdp);
		this.gamma = gamma;
		
		this.V = new HashMap<Etat,Double>();
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
		}
	}
	
	
	
	
	public ValueIterationAgent(MDP mdp) {
		this(0.9,mdp);

	}
	
	/**
	 * 
	 * Mise a jour de V: effectue UNE iteration de value iteration (calcule V_k(s) en fonction de V_{k-1}(s'))
	 * et notifie ses observateurs.
	 * Ce n'est pas la version inplace (qui utilise la nouvelle valeur de V pour mettre a jour ...)
	 */
	@Override
	public void updateV(){
		//delta est utilise dans la classe mere pour detecter la convergence de l'algorithme
		//Dans la classe mere, lorsque l'on planifie jusqu'a convergence, on arrete les iterations        
		//lorsque delta < epsilon  (la valeur d'epsilon est choisi dans la classe mere)
		
		//Ici dans cette classe, il suffit de mettre a jour delta 
		this.delta=0.0;

		Double deltaMax = -999999.0;
		Double deltaTemp = 0.0;
		
		//*** VOTRE CODE
		HashMap<Etat,Double> VTemp = new HashMap<Etat,Double>();

		Double vMaxTemp = -10000.0;
		Double vMinTemp = this.getMdp().getRecompenseMax();

			// Récupère la liste des actions possibles depuis cet état
		for (Etat etat : this.mdp.getEtatsAccessibles()) {
			Double maxSPrime = 0.0;
			for (Action action : this.mdp.getActionsPossibles(etat)) {

				Double sommeSPrime = 0.0;
				// Recupère la liste des cases où l'on risque d'atterire avec l'action actuelle
				try {
					for (Map.Entry<Etat, Double> sPrime : this.mdp.getEtatTransitionProba(etat, action).entrySet()) {

						sommeSPrime += sPrime.getValue() * (this.mdp.getRecompense(etat, action, sPrime.getKey()) + this.getGamma() * this.getV().get(sPrime.getKey()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				maxSPrime = (sommeSPrime > maxSPrime) ? sommeSPrime : maxSPrime;
			}

			VTemp.put(etat, maxSPrime);
			deltaTemp = abs(V.get(etat) - maxSPrime);
			deltaMax = (deltaTemp > deltaMax) ? deltaTemp : deltaMax;
			if(maxSPrime > vMaxTemp) {
				vMaxTemp = maxSPrime;
			} else if (maxSPrime < vMinTemp) {
				vMinTemp = maxSPrime;
			}
		}

		this.setDelta(deltaMax);
		this.V = VTemp;
		vmax = vMaxTemp;
		vmin = vMinTemp;
		//mise a jour de vmax et vmin (attributs de la classe mere) 
		//vmax et vmin sont utilises pour l'affichage du gradient de couleur:
		//vmax est la valeur max de V pour tout s 
		//vmin est la valeur min de V pour tout s
		
		
		//******************* laisser cette notification a la fin de la methode	
		this.notifyObs();
	}
	
	
	/**
	 * renvoi l'action executee par l'agent dans l'etat e 
	 * Si aucune action n'est possible, renvoi Action2D.NONE
	 */
	@Override
	public Action getAction(Etat e) {
		List<Action> listActions = this.getPolitique(e);
		Action action;
		if(listActions.isEmpty()) {
			action = Action2D.NONE;
		} else {
			action = listActions.get(rand.nextInt(listActions.size()));
		}
		return action;
		
	}

	/**
	 * Renvoie la valeur de l'Etat _e
	 * Cette methode est juste un getter, on ne calcule pas la valeur de l'etat _e ici
     * la valeur d'un etat est calculee dans updateV
	 */
	@Override
	public double getValeur(Etat _e) {
		
		return V.getOrDefault(_e, 0.0);
	}
	
	/**
	 * renvoi la (les) action(s) de valeur(s) max dans l'etat _e 
	 * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
	 */
	@Override
	public List<Action> getPolitique(Etat _e) {
		//*** VOTRE CODE
		List<Action> returnactions = new ArrayList<Action>();

		Double valueMax = 0.0;
		Double value = 0.0;
		for (Action action : this.mdp.getActionsPossibles(_e)) {

			// Recupère la liste des cases où l'on risque d'atterire avec l'action actuelle
			try {
				for (Map.Entry<Etat, Double> sPrime : this.mdp.getEtatTransitionProba(_e, action).entrySet()) {
					value = sPrime.getValue() * (this.mdp.getRecompense(_e, action, sPrime.getKey()) + this.getGamma() * this.getV().get(sPrime.getKey()));
					if(value > valueMax) {
						returnactions.clear();
						returnactions.add(action);
						valueMax = value;
					} else if (value.equals(valueMax)) {
						returnactions.add(action);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}


		}

	
		return returnactions;
		
	}
	
	@Override
	public void reset() {
		super.reset();
                
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
		}
		
		this.notifyObs();
	}

	

	

	public HashMap<Etat,Double> getV() {
		return V;
	}
	public double getGamma() {
		return gamma;
	}
	@Override
	public void setGamma(double _g){
		System.out.println("gamma= "+gamma);
		this.gamma = _g;
	}


	
	

	
}