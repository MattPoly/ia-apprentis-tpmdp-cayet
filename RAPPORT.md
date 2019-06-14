**Nom/Prénom Etudiant 1 :** CAYET Matthieu

**Nom/Prénom Etudiant 2 :** PERRIN Antoine

# Rapport TP1

## Question 5.1 Brigde Grid
*Donnez les valeurs des paramètres et la justification de ces choix*

Le bruit doit être mis à 0. L'agent évite ainsi de tomber dans les -100.


## Question 5.2 Discount Grid
*Donnez les valeurs des paramètres dans chaque cas et la justification de ces choix*
1. Gamma: 0.1; Bruit: 0.2; other: 0;
En faisant cela on minimise l'importance de la valeur des cases adjacentes.

2. Gamma: 0.9; Bruit: 0; other: 0;
On empêche notre agent d'aller sur des cases qui minimisent le chemin dangereux.

3. Gamma: 0.9; Bruit: 0; other: 0;
// TODO
# Rapport TP2

## Question 1:
*Précisez et justifiez les éléments que vous avez utilisés pour la définition d’un état du MDP pour le jeu du Pacman (partie 2.2)*

Afin de créer des états intéressant du jeu pour faire jouer l'IA Qlearning agent, nous avons tenté l'utilisation de plusieurs paramètres mis à notre disposition.
Tout d'abord nous avons pris la position du pacman afin que chaque état ou il bouge soit considéré comme différent dans le hashcode.
Ensuite nous avons pris la position des fantômes afin qu'il sache quelle action effectuer quand les fantomes sont dans de positions connues.
Dans ce même objectif nous avons également pris le nombre de points à manger restant.

A partir de ces paramètres, nous avons lancé des test et le pacman gagnait dans environ 60% des cas ce qui n'était pas assez à notre goût.
De plus dans la grille "openMaze.lay", le pacman ne fait que des aller retour sur 2 cases.

Nous avons donc ajouté un indicateur de la nourriture la plus proche pour essayer d'augmenter la valeur des mouvement l'amenant proche de l'unique nourriture de la carte. 
Cependant, cela n'a fait que ralentir l'exécution des  épisodes sur cette carte. 
Mais la bonne nouvelle est que cela a augmenté les performances de 30% sur la carte de base avec 90% de réussite en moyenne.

Nous avons par la suite tenté de mettre en place d'autres indicateurs pour augmenter ce score à 100%.
Nous pouvons citer la distance au fantômes au lieu de leur position. Ou encore la prise en compte du score.
Cependant la première à fait baisser la réussite à 60% et la deuxième à un catastophique 10%. Nous avons donc choisi de ne pas les intégrer.


## Question 2:
*Précisez et justifiez les fonctions caractéristiques que vous avez choisies pour la classe FeatureFunctionPacman (partie 2.3).*
