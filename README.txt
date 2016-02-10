

#	
#	Réalisé par DURIEU JEAN-VITAL
#
#	Système multi agents, trois projets :
#		1. Chambre a particule
#		2. Wator
#		3. Pakman
#
#		> Tous les détails dans le rapport
#






------------------------------------
	CHAMBRE A PARTICULE
------------------------------------

Le projet de chambre à particule est une application JAVA graphique. Elle propose de faire interagir un nombre variable de particules entre elles. Chaque particule est un agent. Tous les agents agissent selon les même règles.

Une bille est un agent. Elle évolue dans un monde torique ou non torique. Toutes les billes sont placées sur une position entière du monde. Lors de l'initialisation toutes les billes sont assignées à des positions différentes. Deux billes ne peuvent pas avoir la même position. La parole est donné à une bille une seul fois par tour. L'ordre d'action des billes est répétitif ou aléatoire.

USAGE:
	args[0]  : nbAgents     -> le nombre d'agents à mettre en jeu
	args[1]  : nbTours      -> nombre de tours. Si zéro, tournera à l'infini
	args[2]  : gridSizeX    -> largeur du tableau de jeu (en épaisseur bille)
	args[3]  : gridSizeY    -> hauteur du tableau de jeu (en épaisseur bille)
	args[4]  : ballSize     -> taille des billes
	args[5]  : vitesse      -> latence entre chaque tour de jeu (en millisecondes)
	args[6]  : paroleRandom -> donne la parole aléatoirement aux agents (1) ou non (!1)
	args[7]  : grille       -> affiche la grille (1) ou pas (!1)
	args[8]  : colorMode    -> [ 0:unicolor ; [2-6]: # une_bille_rouge ; -1:multicolor
	args[9]  : torique      -> détermine si le monde est torique (1) ou non (!1)
	args[10] : game         -> lance le jeu des couleurs (1) ou non (!1)
	args[11] : apccWatcher  -> Enregistre en tat que client APCC (hors sujet, ne pas prendre en compte -> 0)
	args[12] : apcc priority-> indique la priorité du client (hors sujet, ne pas prendre en compte -> 0)

	Exemples,

		Lancer une simulation infinie : 
		> java cap.jar 600 0 100 50 30 50 1 0 -1 1 0 0 0


		Lancer le jeu et voyez quel couleur l'emporte avec les arguments : 
		> java cap.jar 600 0 100 50 15 10 1 0 -1 1 1 0 0







------------------------------------
	WATOR
------------------------------------

Wator est un système multi-agent qui se distingue de la chambre a particule en intégrant plusieur types d'agents, les fish et les sharks. Les fish se reproduisent, les sharks mangent les fish et se reproduisent. Le jeu consiste à trouver un équilibre entre la production et la consommation de ressources.

Les Fish ont un temps de gestation puis peuvent se reproduire. Le temps de gestation dure un certain nombre de tours. Lorsqu'un fish peut se reproduire, et qu'il y a de la place autour de lui, il le fait.
Les sharks eux ont la capacité de se reproduire, mais ont surtout besoin de manger pour ne pas mourir de faim. Pour celà ils mangent des poissons autour d'eux. Les requins se déplacent droit devant ou en diagonale pour trouver du poisson. Lorsqu'il y a du poisson autour d'eux, ils le mangent. Les shark peuvent faire trois actions au même tour, se déplacer (si besoin), manger (si possible), et se reproduire si leur période de gestation est arrivée à terme.

USAGE:
	args[0]  : nbAgents     	-> le nombre d'agents à mettre en jeu
	args[1]  : nbTours      	-> nombre de tours. Si zéro, tournera à l'infini
	args[2]  :  gridSizeX   	-> largeur du tableau de jeu (en épaisseur bille)
	args[3]  : gridSizeY    	-> hauteur du tableau de jeu (en épaisseur bille)
	args[4]  : ballSize     	-> taille des billes
	args[5]  : vitesse      	-> latence entre chaque tour de jeu (en millisecondes)
	args[6]  : paroleRandom 	-> donne la parole aléatoirement aux agents (1) ou non (!1)
	args[7]  : grille       	-> affiche la grille (1) ou pas (!1)
	args[8]  : colorMode    	-> [ 0:unicolor ; [2-6]: # une_bille_rouge ; -1:multicolor
	args[9]  : torique      	-> détermine si le monde est torique (1) ou non (!1)
	args[10] : game         	-> lance le jeu des couleurs (1) ou non (!1)
	args[11] : fish breed time	-> temps de gestation des poissons
	args[12] : shark breed time	-> temps de gestation des requins
	args[13] : shark starve time-> temps de survie sans manger des requins
	args[14] : # fish to shark	-> Nombre de poisson par requin


	Exemple,

		Lancer une simulation : 
		> java wator.jar 2000 0 250 200 6 50 0 0 -2 1 0 3 5 4 10







------------------------------------
	PAKMAN
------------------------------------

Pakman est un simulateur d'agents dont l'un est contrôlé par l'utilisateur tandis que les autres sont animés par le système. L'agent contrôlé par l'interraction du joueur est appelé mouton tandis que les autres agents animés sont des loups. Comme dans Chambre a particule Pakman possède des agents silencieux (inanimés). Des murs et un superBlock. La chèvre se déplace dans le monde avec les flèches, tandis que les loups la pourchassent. Sur le plateau de jeu se trouve un superBlock. Lorsque la chèvre l'attrappe, les loups la fuie pendant un certain nombre de tours. Le jeu s'arrête lorsque les loups ont attrapé la chèvre.\\

Le mouton joue en premier. Il peut se déplacer dans quatre directions. Les murs et les bords empêchent de passer. Si il se déplace sur le superBlock, il devient à son tour super. Le superBlock disparait et apparait autrepart.
C'est alors au tour des agents loups de jouer. Un algorithme de dijkstra calcule le nombre des cases qui sépare chacune case jusqu'au mouton. Si le mouton n'est pas super, ils se déplacent vers l'option indiquant la plus petite ditance. Sinon, ils se déplacent vers celle indiquant le plus grande distance afin de s'en éloigner.

USAGE:
	args[0] : nbWolf      -> le nombre d'agents à mettre en jeu
	args[1] : gridSizeX   -> largeur du tableau de jeu (en épaisseur bille)
	args[2] : gridSizeY   -> hauteur du tableau de jeu (en épaisseur bille)
	args[3] : ballSize    -> taille des billes
	args[4] : propsWall   -> indice de proportion des murs en jeu
	args[5] : grille      -> affiche la grille (1) ou pas (!1)
	args[6] : torique     -> détermine si le monde est torique (1) ou non (!1)
	args[7] : powerToken  -> Durée pendant laquelle la superbrick repousse les rouges.


	Exemple,

		Lancer une simulation :
		> java pakman.jar 5 50 20 50 3 0 0 10



