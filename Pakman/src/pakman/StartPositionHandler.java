package pakman;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import agents.*;
import util.PosUtil;
import util.Util;


/**
 * Created by jvdur on 15/01/2016.
 */
public class StartPositionHandler {

    private int[] taken;
    private int positionsTaken;
    private int gridSizeX;
    private int gridSizeY;
    private static Agent[][] plateau;

    private ArrayList<Color> colorArray;

    public StartPositionHandler(int gridSizeX, int gridSizeY) {
        this.taken = Util.getRandomTable(gridSizeX * gridSizeY);
        this.gridSizeX = gridSizeX;
        this.gridSizeY = gridSizeY;

        this.plateau = new Agent[gridSizeX][gridSizeY];
    }

    public Agent[][] getPlateau() {
        return plateau;
    }

    public ArrayList<Agent> getStartPositions(int nbAgents, Simulateur sim, boolean torique, int propWall) {

        Color wallColor = new Color(205,133,63);

        // Add walls if needed
        if (!torique) {
            for (int i=0;i<gridSizeX;i++) {
                StartPosition sp = new StartPosition();
                sp.posy = 0;
                sp.posx = i;
                sp.color = wallColor;
                plateau[i][0] = new AWall(sp, sim);

                StartPosition spb = new StartPosition();
                spb.posy = gridSizeY-1;
                spb.posx = i;
                sp.color = wallColor;
                plateau[i][gridSizeY-1] = new AWall(spb, sim);
            }
            for (int i=0;i<gridSizeY;i++) {
                StartPosition sp = new StartPosition();
                sp.posy = i;
                sp.posx = 0;
                sp.color = wallColor;
                plateau[0][i] = new AWall(sp, sim);

                StartPosition spb = new StartPosition();
                spb.posy = i;
                spb.posx = gridSizeX-1;
                sp.color = wallColor;
                plateau[gridSizeX-1][i] = new AWall(spb, sim);
            }

        }


        // ON crée la SUPER BRICK
        StartPosition spx = new StartPosition();
        boolean free = true;
        do {
            spx.posx = taken[positionsTaken] % gridSizeX;
            spx.posy = taken[positionsTaken] / gridSizeX;
            positionsTaken++;
            free = (plateau[spx.posx][spx.posy] == null);
        } while (!free);
        spx.color = Color.CYAN;

        ASuperBrick s = new ASuperBrick(spx, sim);
        sim.superBrickAgent = s;

        System.out.println(">>> Brick position : " + spx.posx + "x" + spx.posy);


        // ON AJOUTE LES AGENTS
        ArrayList<Agent> agents = new ArrayList<>(nbAgents);
        boolean isCheapCreated = false;
        
        // On crée les agents
        for (int i=0; i<nbAgents; i++) {
            
        	StartPosition sp = new StartPosition();
        	
            free = true;
            do {
                sp.posx = taken[positionsTaken] % gridSizeX;
                sp.posy = taken[positionsTaken] / gridSizeX;
                positionsTaken++;
                free = (plateau[sp.posx][sp.posy] == null);
            } while (!free);

            
            if (!isCheapCreated) {
                sp.color = Color.GREEN;
                Agent a = new ACheep(sp, sim);
                agents.add(a);
                plateau[sp.posx][sp.posy] = a;
                isCheapCreated = true;
            } else {
                sp.color = Color.RED;
                Agent a = new AWolf(sp, sim);
                agents.add(a);
                plateau[sp.posx][sp.posy] = a;
            }

        }



        // Add obstacles
        int rapSize = (int) (gridSizeX/2 * propWall);
        for (int i=0; i<rapSize; i++) {
            int blockPosx;
            int blockPosy;

            do {
                blockPosx = taken[positionsTaken] % gridSizeX;
                blockPosy= taken[positionsTaken] / gridSizeX;
                positionsTaken++;
            } while (plateau[blockPosx][blockPosy] != null);

            AWall block = new AWall(new StartPosition(blockPosx,blockPosy,wallColor), sim);
            plateau[blockPosx][blockPosy] = block;
            agents.add(block);
        }


        return agents;
    }

    private void setBlock(int j, int blockPosx, int blockPosy, Color wallColor, Simulateur sim) {

        if (j == 0) return;

        Random r = new Random();
        if (r.nextInt()%j == 0) {
            if (PosUtil.isfree(PosUtil.correctPositionX(blockPosx), PosUtil.correctPositionY(blockPosy), plateau)) {
                plateau[PosUtil.correctPositionX(blockPosx)][PosUtil.correctPositionY(blockPosx)] = new AWall(new StartPosition(PosUtil.correctPositionX(blockPosx), PosUtil.correctPositionY(blockPosy), wallColor), sim);

                j--;

                setBlock(j, blockPosx+1, blockPosy+1, wallColor, sim);
                setBlock(j, blockPosx+1, blockPosy-1, wallColor, sim);
                setBlock(j, blockPosx+1, blockPosy, wallColor, sim);
                setBlock(j, blockPosx-1, blockPosy+1, wallColor, sim);

                setBlock(j, blockPosx-1, blockPosy-1, wallColor, sim);
                setBlock(j, blockPosx-1, blockPosy, wallColor, sim);
                setBlock(j, blockPosx, blockPosy+1, wallColor, sim);
                setBlock(j, blockPosx, blockPosy-1, wallColor, sim);

            }
        }

        j++;
    }

    public static void createNewSuperBrick(Simulateur sim) {
        // ON crée la SUPER BRICK
        Random r = new Random();
        int rdmX, rdmY;
        do {
            rdmX = r.nextInt(sim.getGridSizeX()-1);
            rdmY = r.nextInt(sim.getGridSizeY()-1);
        } while (plateau[rdmX][rdmY] != null);
        StartPosition spx = new StartPosition(rdmX, rdmY);
        spx.color = Color.CYAN;
        ASuperBrick s = new ASuperBrick(spx, sim);
        sim.superBrickAgent = s;
    }

}
