package model;

import agent.AFish;
import agent.AShark;
import agent.Agent;
import gui.World;
import util.Util;

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

    public static int sharkStarveTime;
    public static int fishBreedTime;
    public static int sharkBreedTime;
    private final int nbFishPerShark;

    private final int gridSizeY;
    private final int gridSizeX;
    private final boolean paroleRandom;
    private final int colorMode;
    private ArrayList<Agent> agents;
    public Agent[][] plateau;
    private int nbTours;
    private int vitesse;
    private boolean isfini;
    private JFrame ihm;
    private StartPositionHandler startPositionHandler;

    public Simulateur(int nbAgents, int nbTours, int gridSizeX,
                      int gridSizeY, int vitesse, boolean paroleRandom,
                      int colorMode, boolean istorique, boolean jeu,
                      int fishBreedTime, int sharkBreedTime, int sharkStarveTime,
                      int nbFishPerShark) {

        this.nbTours = nbTours;
        this.vitesse = vitesse;

        this.gridSizeX = gridSizeX;
        this.gridSizeY = gridSizeY;

        this.colorMode = colorMode;

        Simulateur.IS_TORIQUE = istorique;
        this.paroleRandom = paroleRandom;
        Simulateur.JEU = jeu;

        this.fishBreedTime = fishBreedTime;
        this.sharkBreedTime = sharkBreedTime;
        this.sharkStarveTime = sharkStarveTime;
        this.nbFishPerShark = nbFishPerShark;

        this.startPositionHandler = new StartPositionHandler(this, gridSizeX, gridSizeY, colorMode);

        // On crée les agents
        agents = startPositionHandler.getStartPositions(nbAgents, this, istorique, nbFishPerShark);
        plateau = startPositionHandler.getPlateau();

    }

    public void run() {
        //un tour de boucle demande a tous de doit()
        //attention, les agents ne doit jamais dans le meme ordre

        int[] orderTable;
        isfini = false;

        // Header de la sortie
        System.out.println("SHARKS,FISH,BABY SHARK,BABY FISH");

        // Pour chaque tour
        int cpt = 0;
        while(!isfini && (cpt < nbTours || nbTours == 0)) {

            agents.trimToSize();

            // On genere un tablea d'ordre aleatoire ou pas
            if (paroleRandom) {
                orderTable = Util.getRandomTable(agents. size());
            } else {
                orderTable = Util.getTable(agents.size());
            }

            // pour chaque agent
            /*for (int j = 0; j < agents.size(); j++) {
                // On le fais jouer
                try {
                    if (agents.size() > j && orderTable.length > j) {
                        if (agents.get(orderTable[j]) != null)
                            agents.get(orderTable[j]).doit();
                    }
                } catch (Exception e) {

                }
            }*/

            ArrayList<Agent> agentClone = (ArrayList<Agent>) agents.clone();
            for (int j=0; j<agentClone.size();j++) {
                try {
                    agentClone.get(j).doit();
                } catch (Exception e) {
                }
            }


            if (colorMode == -2) {

                int sharks = 0;
                int sharksNewBorn = 0;
                int fish = 0;
                int fishNewBorn = 0;

                for (Agent a : agents) {
                    if (a instanceof AShark) {
                        sharks++;
                        if (a.age <= 1) {
                            sharksNewBorn++;
                        }
                    } else if (a instanceof AFish) {
                        fish++;
                        if (a.age <= 1) {
                            fishNewBorn++;
                        }
                    }
                }

                System.out.println(sharks+","+fish+","+sharksNewBorn+","+fishNewBorn);

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
            //notifyObservers();
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

    public StartPositionHandler getStartPositionHandler() {
        return startPositionHandler;
    }


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
     *    args[11] : fish breed time
     *    args[12] : shark breed time
     *    args[13] : shark starve time
     *    args[14] : Nombre de poisson par requin
     */
    public static void main(String args[]) {

        if (args.length != 15) {
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

        int fishBreedTime = 0;
        int sharkBreedTime = 0;
        int sharkStarveTime = 0;
        int nbFishPerShark = 0;

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

            fishBreedTime = Integer.parseInt(args[11]);
            sharkBreedTime = Integer.parseInt(args[12]);
            sharkStarveTime = Integer.parseInt(args[13]);
            nbFishPerShark = Integer.parseInt(args[14]);

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
        Simulateur sim = new Simulateur(nbAgents, nbTours, gridSizeX,
                gridSizeY, vitesse, paroleRandom,
                colorMode, isTorique, jeu,
                fishBreedTime, sharkBreedTime, sharkStarveTime, nbFishPerShark);

        // Cree l'IHM
        World world = new World(sim.FRAME_DIMENSIONS, gridSizeX, gridSizeY, ballSize, sim, grille);

        // Lance le jeu
        sim.run();
    }

}
