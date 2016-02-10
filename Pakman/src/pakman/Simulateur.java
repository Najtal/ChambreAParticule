package pakman;
import agents.ACheep;
import agents.ASuperBrick;
import agents.AWolf;
import be.jvdurieu.Apcc;
import be.jvdurieu.WatchListener;
import be.jvdurieu.connexion.Watcher;
import gui.World;

import javax.swing.*;

import util.Dijkstra;
import util.PosUtil;
import util.Util;

import agents.Agent;


import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jvdur on 13/01/2016.
 */
public class Simulateur extends java.util.Observable implements Runnable {

    public static final Dimension FRAME_DIMENSIONS = new Dimension(1200,800);
    public static boolean IS_TORIQUE;

    private final int gridSizeY;
    private final int gridSizeX;
    public final int powerToken;
    private ArrayList<Agent> agents;
    private Agent[][] plateau;
    public int[][] dijkstra;

	private boolean isfini;
    private JFrame ihm;
    private StartPositionHandler startPositionHandler;

    public ASuperBrick superBrickAgent;

    public int kVertical;
	public int kHorizontal;
    public ACheep cheep;

    /**
     * Constructeur
     * @param nbAgents tous les agents, minimu 2, 1 player and wolves
     * @param gridSizeX
     * @param gridSizeY
     * @param istorique
     */
    public Simulateur(int nbAgents, int gridSizeX, int gridSizeY, boolean istorique, int propWall, int powerToken) {

        this.gridSizeX = gridSizeX;
        this.gridSizeY = gridSizeY;

        Simulateur.IS_TORIQUE = istorique;

        this.powerToken = powerToken;

        this.startPositionHandler = new StartPositionHandler(gridSizeX, gridSizeY);

        initUtil();

        
        // On crée les agents
        agents = startPositionHandler.getStartPositions(nbAgents, this, istorique, propWall);
        plateau = startPositionHandler.getPlateau();

    }

    private void initUtil() {
    	PosUtil.init(this);
    	Dijkstra.init(this);
	}

	/**
     * The run method makes the game turn
     */
    public void run() {
        //un tour de boucle demande a tous de doit()
        //attention, les agents ne doit jamais dans le meme ordre

    	// On dessine une première fois le jeu
        setChanged();
        notifyObservers();
        notifyObservers(this.agents);
    	
        // Pour chaque tour
        int cpt = 0;
        
        while(!isfini) {

            // waiting for user input
            int kh, kv;
        	/*do {
                kh = kHorizontal;
                kv = kVertical;
            } while (kh == 0 && kv == 0);*/

            while (kHorizontal == 0 && kVertical == 0) {
                try {
                    Thread.currentThread().sleep(20);
                } catch (InterruptedException e) {
                }
            }
            kh = kHorizontal;
            kv = kVertical;

            kHorizontal = 0;
            kVertical = 0;

            // moving our agent
            if ((kh == 0 && kv == 0)
                    || (kh==1 && kv == 1)) {
                break;
            }
            ((ACheep)agents.get(0)).setDirections(kh, kv);
            try {
                agents.get(0).doit();
            } catch (Exception e) {
                System.out.println("error in cheep move");
                //e.printStackTrace();
                //continue;
            }

            // Compute dijkstra !
        	dijkstra = Dijkstra.computeDijkstra();
            Dijkstra.print(dijkstra);


            // Give action to each agent
            for (Agent a:agents) {
                if (a instanceof AWolf) {
                    try {
                        a.doit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            setChanged();
            notifyObservers();
            notifyObservers(this.agents);
        }

        System.out.println("Bien essayé ! :)");

    }




    /*
     * GETTERS
     */
    public ArrayList<Agent> getAgents() {
        return agents;
    }

    public Agent[][] getPlateau() {
        return plateau;
    }

    public int getGridSizeY() {
        return gridSizeY;
    }

    public int getGridSizeX() { 
    	return gridSizeX; 
    }
    
    public int[][] getDijkstra() {
		return dijkstra;
	}

    
    /*
     * MAIN
     */
    /**
     * Methode MAIN, initialise et lance l'application
     * @param args tableau d'arguments
     *    args[0] : nbWolf		-> le nombre d'agents à mettre en jeu
     *    args[1] : gridSizeX   -> largeur du tableau de jeu (en épaisseur bille)
     *    args[2] : gridSizeY   -> hauteur du tableau de jeu (en épaisseur bille)
     *    args[3] : ballSize    -> taille des billes
     *    args[4] : propsWall   -> indice de proportion des murs en jeu
     *    args[5] : grille      -> affiche la grille (1) ou pas (!1)
     *    args[6] : torique     -> détermine si le monde est torique (1) ou non (!1)
     *    args[7] : powerToken  -> Durée pendant laquelle la superbrick repousse les rouges.
     */
    public static void main(String args[]) {

        if (args.length != 8) {
            System.out.println("ERROR : mauvais nombre d'arguments");
            System.exit(0);
        }

        int nbWolf = 0;
        int gridSizeX = 0;
        int gridSizeY = 0;
        int ballSize = 0;
        int propWall = 0;
        boolean grille = false;
        boolean isTorique = false;
        int powerToken = 0;

        try {
            nbWolf = Integer.parseInt(args[0]);
            gridSizeX = Integer.parseInt(args[1]);
            gridSizeY = Integer.parseInt(args[2]);
            ballSize = Integer.parseInt(args[3]);
            propWall = Integer.parseInt(args[4]);
            grille = (Integer.parseInt(args[5]) == 1) ? true : false;
            isTorique = (Integer.parseInt(args[6]) == 1) ? true : false;
            powerToken = Integer.parseInt(args[7]);
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("ERROR: Mauvais respect des usages !");
            System.exit(0);
        }

        
        if (Util.validationDesArguments(nbWolf, gridSizeX, gridSizeY, ballSize, grille, isTorique)) {
            System.exit(0);
        }


        // Cree le simulateur
        Simulateur sim = new Simulateur(nbWolf, gridSizeX, gridSizeY, isTorique, propWall, powerToken);

        // Cree l'IHM
        World world = new World(sim.FRAME_DIMENSIONS, gridSizeX, gridSizeY, ballSize, sim, grille);

        // Lance le jeu
        sim.run();
    }

	public void isFini(boolean b) {
		this.isfini = b;
	}

}
