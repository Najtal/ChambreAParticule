package model;

import agent.AFish;
import agent.AShark;
import agent.Agent;
import model.Simulateur;
import util.Util;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jvdur on 15/01/2016.
 */
public class StartPositionHandler {

    private final int colorMode;
    private final Simulateur sim;
    private int[] taken;
    private int positionsTaken;
    private int gridSizeX;
    private int gridSizeY;
    private static Agent[][] plateau;

    private ArrayList<Color> colorArray;

    public StartPositionHandler(Simulateur sim, int gridSizeX, int gridSizeY, int colorMode) {

        this.sim = sim;
        this.taken = Util.getRandomTable(gridSizeX * gridSizeY);
        this.gridSizeX = gridSizeX;
        this.gridSizeY = gridSizeY;

        this.plateau = new Agent[gridSizeX][gridSizeY];
        this.colorMode = colorMode;

        colorArray = new ArrayList<>();
        colorArray.add(new Color(255, 51, 51));
        colorArray.add(new Color(255, 255, 51));
        colorArray.add(new Color(255, 51, 255));
        colorArray.add(new Color(51, 255, 51));
        colorArray.add(new Color(51, 255, 255));
        colorArray.add(new Color(51, 51, 255));
    }

    public Agent[][] getPlateau() {
        return plateau;
    }

    public ArrayList<Agent> getStartPositions(int nbAgents, Simulateur sim, boolean torique, int nbFishPerShark) {

        System.out.println("nb Fish per Shark = " + nbFishPerShark);

        if (!torique) {
            for (int i=0;i<gridSizeX;i++) {
                StartPosition sp = new StartPosition();
                sp.diry = 0;
                sp.dirx = 0;
                sp.posy = 0;
                sp.posx = i;
                plateau[i][0] = new Agent(sp, sim, true);

                StartPosition spb = new StartPosition();
                spb.diry = 0;
                spb.dirx = 0;
                spb.posy = gridSizeY-1;
                spb.posx = i;
                plateau[i][gridSizeY-1] = new Agent(spb, sim, true);
            }
            for (int i=0;i<gridSizeY;i++) {
                StartPosition sp = new StartPosition();
                sp.diry = 0;
                sp.dirx = 0;
                sp.posy = i;
                sp.posx = 0;
                plateau[0][i] = new Agent(sp, sim, true);

                StartPosition spb = new StartPosition();
                spb.diry = 0;
                spb.dirx = 0;
                spb.posy = i;
                spb.posx = gridSizeX-1;
                plateau[gridSizeX-1][i] = new Agent(spb, sim, true);
            }
        }


        ArrayList<Agent> agents = new ArrayList<>(nbAgents);
        int colorCounter = colorMode;

        System.out.println("colorMode : " + colorMode);

        // On crée les agents
        for (int i=0; i<nbAgents; i++) {
            StartPosition sp = new StartPosition();

            boolean isShark = false;
            boolean free = true;
            do {
                sp.posx = taken[positionsTaken] % gridSizeX;
                sp.posy = taken[positionsTaken] / gridSizeX;
                positionsTaken++;
                free = (plateau[sp.posx][sp.posy] == null);
            } while (!free);

            do {
                sp.dirx = (int) (Math.random() * (3)) - 2;
                sp.diry = (int) (Math.random() * (3)) - 2;
            } while (sp.dirx != 0 && sp.diry != 0);

            if (colorMode == -2) {
                if (i%nbFishPerShark==0) {
                    isShark = true;
                }
            } else if(colorMode == -1) {
                sp.color = colorArray.get((int) (Math.random() * (colorArray.size() - 1)));
            } else {
                if (colorCounter > 0) {

                    if (colorMode > 6) {
                        sp.color = colorArray.get(0); // rouge
                    } else {
                        sp.color = colorArray.get(colorCounter-1);
                    }
                    colorCounter--;
                } else {
                    sp.color = Color.DARK_GRAY;
                }
            }

            if (colorMode == -2) {

                if (isShark) {
                    sp.color = Color.red;
                    AShark as = new AShark(sp, sim, false, sim.sharkStarveTime, sim.sharkBreedTime);
                    agents.add(as);
                    plateau[sp.posx][sp.posy] = as;
                } else {
                    sp.color = Color.blue;
                    AFish af = new AFish(sp, sim, false, sim.fishBreedTime);
                    agents.add(af);
                    plateau[sp.posx][sp.posy] = af;
                }
            } else {

                System.out.println("new agent ");

                Agent a = new Agent(sp, sim, false);
                agents.add(a);
                plateau[sp.posx][sp.posy] = a;
            }

        }
        return agents;
    }

    public void giveBirth(Simulateur sim, int posx, int posy, int dirx, int diry, boolean isShark, Color color) {

        StartPosition sp = new StartPosition();
        sp.posx = posx;
        sp.posy = posy;
        sp.dirx = dirx;
        sp.diry = diry;
        sp.color = color;

        Agent a;
        if (isShark) {
            a = new AShark(sp, sim, false, sim.sharkStarveTime, sim.sharkBreedTime);
        } else {
            a = new AFish(sp, sim, false, sim.fishBreedTime);
        }
        this.sim.getAgents().add(a);
        this.sim.getPlateau()[posx][posy] = a;
    }

    public class StartPosition {
        public int posx;
        public int posy;
        public int dirx;
        public int diry;
        public Color color;
    }


}
