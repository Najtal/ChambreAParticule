import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jvdur on 13/01/2016.
 */
public class Simulateur extends java.util.Observable implements Runnable {

    public static final Dimension FRAME_DIMENSIONS = new Dimension(1200,800);
    public static boolean IS_TORIQUE;
    public static boolean JEU;

    private final int gridSizeY;
    private final int gridSizeX;
    private final boolean paroleRandom;
    private ArrayList<Agent> agents;
    private Agent[][] plateau;
    private int nbTours;
    private int vitesse;
    private boolean isfini;
    private JFrame ihm;
    private StartPositionHandler startPositionHandler;

    public Simulateur(int nbAgents, int nbTours, int gridSizeX, int gridSizeY, int vitesse, boolean paroleRandom, int colorMode, boolean istorique, boolean jeu) {

        this.nbTours = nbTours;
        this.vitesse = vitesse;

        this.gridSizeX = gridSizeX;
        this.gridSizeY = gridSizeY;

        Simulateur.IS_TORIQUE = istorique;
        this.paroleRandom = paroleRandom;
        Simulateur.JEU = jeu;

        this.startPositionHandler = new StartPositionHandler(gridSizeX, gridSizeY, colorMode);

        // On crée les agents
        agents = startPositionHandler.getStartPositions(nbAgents, this, istorique);
        plateau = startPositionHandler.getPlateau();

    }

    public void run() {
        //un tour de boucle demande a tous de doit()
        //attention, les agents ne doit jamais dans le meme ordre

        int[] orderTable;
        isfini = false;

        // Pour chaque tour
        int cpt = 0;
        while(!isfini && (cpt < nbTours || nbTours == 0)) {

            // On genere un tablea d'ordre aleatoire ou pas
            if (paroleRandom) {
                orderTable = Util.getRandomTable(agents.size());
            } else {
                orderTable = Util.getTable(agents.size());
            }

            // pour chaque agent
            for (int j = 0; j < agents.size(); j++) {
                // On le fais jouer
                agents.get(orderTable[j]).doit();
            }

            // On check la fin du jeu si jeu
            if (JEU) {
                isfini = true;
                Color lastColor = agents.get(0).getColor();
                for (Agent a: agents) {
                    if (lastColor != a.getColor()) {
                        isfini = false;
                        break;
                    }
                }

                if (isfini) {
                    System.out.println("FINIIIII :)");
                    System.out.println("Nombre de tours : " + cpt);
                }
            }

            setChanged();
            notifyObservers();
            notifyObservers(this.agents);

            try {
                Thread.sleep(vitesse);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            cpt++;
        }

    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }

    public Agent[][] getPlateau() {
        return plateau;
    }

    public int getGridSizeY() {
        return gridSizeY;
    }

    public int getGridSizeX() { return gridSizeX; }

    /**
     * Methode MAIN, initialise et lance l'application
     * @param args tableau d'arguments
     *    args[0] : nbAgents    -> le nombre d'agents à mettre en jeu
     *    args[1] : nbTours     -> nombre de tours. Si zéro, tournera à l'infini
     *    args[2] : gridSizeX   -> largeur du tableau de jeu (en épaisseur bille)
     *    args[3] : gridSizeY   -> hauteur du tableau de jeu (en épaisseur bille)
     *    args[4] : ballSize    -> taille des billes
     *    args[5] : vitesse     -> latence entre chaque tour de jeu (en millisecondes)
     *    args[6] : paroleRandom-> donne la parole aléatoirement aux agents (1) ou non (!1)
     *    args[7] : grille      -> affiche la grille (1) ou pas (!1)
     *    args[8] : colorMode   -> [ 0:unicolor ; [2-6]: # une_bille_rouge ; -1:multicolor
     *    args[9] : torique     -> détermine si le monde est torique (1) ou non (!1)
     *    args[10] : game        -> lance le jeu des couleurs (1) ou non (!1)
     */
    public static void main(String args[]) {

        if (args.length != 11) {
            System.out.println("ERROR : mauvais nombre d'arguments");
            System.exit(0);
        }

        int nbAgents = 0;
        int nbTours = 0;
        int gridSizeX = 0;
        int gridSizeY = 0;
        int ballSize = 0;
        int vitesse = 0;
        boolean paroleRandom = false;
        boolean grille = false;
        int colorMode = 0;
        boolean isTorique = false;
        boolean jeu = false;

        try {

            nbAgents = Integer.parseInt(args[0]);
            nbTours = Integer.parseInt(args[1]);
            gridSizeX = Integer.parseInt(args[2]);
            gridSizeY = Integer.parseInt(args[3]);
            ballSize = Integer.parseInt(args[4]);
            vitesse = Integer.parseInt(args[5]);
            paroleRandom = (Integer.parseInt(args[6]) == 1) ? true : false;
            grille = (Integer.parseInt(args[7]) == 1) ? true : false;
            colorMode = Integer.parseInt(args[8]);
            isTorique = (Integer.parseInt(args[9]) == 1) ? true : false;
            jeu = (Integer.parseInt(args[10]) == 1) ? true : false;

            if (jeu) {
                colorMode = -1;
            }

        } catch (Exception e) {
            System.out.println("ERROR: Mauvais respect des usages !");
            System.exit(0);
        }

        if (Util.validationDesArguments(nbAgents, nbTours, gridSizeX, gridSizeY, ballSize, vitesse, paroleRandom, grille, colorMode, isTorique, jeu)) {
            System.exit(0);
        }

        // Cree le simulateur
        Simulateur sim = new Simulateur(nbAgents, nbTours, gridSizeX, gridSizeY, vitesse, paroleRandom, colorMode, isTorique, jeu);

        // Cree l'IHM
        World world = new World(sim.FRAME_DIMENSIONS, gridSizeX, gridSizeY, ballSize, sim, grille);

        // Lance le jeu
        sim.run();
    }

}
