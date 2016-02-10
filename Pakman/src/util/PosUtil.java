package util;

import agents.Agent;
import pakman.Simulateur;

public class PosUtil {

	private static Simulateur sim;
	
	public static void init(Simulateur simul) {
		sim = simul;
	}

	
	
	/*
     * METHODES UTILITAIRES
     */
	public static boolean isfree(int[] data) {
        return isfree(data[0], data[1]);
    }

    public static boolean isfree(int x, int y) {
        return (sim.getPlateau()[x][y] == null);
    }

    public static boolean isfree(int x, int y, Agent[][] tab) {
        return (tab[x][y] == null);
    }

    public static int correctPositionX(int data) {
        if (data > 0 && data < sim.getGridSizeX())
            return data;
        else if (data < 0)
            return (sim.getGridSizeX() - 1);
        else
            return 0;
    }

    public static int correctPositionY(int data) {
        if (data > 0 && data < sim.getGridSizeY())
            return data;
        else if (data < 0)
            return (sim.getGridSizeY() - 1);
        else
            return 0;
    }

    public static int[] correctPosition(int[] data) {
        data[0] = correctPositionX(data[0]);
        data[1] = correctPositionY(data[1]);
        return data;
    }

    public static int[] correctPositions(int x, int y) {
        int[] ret = new int[2];
        ret[0] = correctPositionX(x);
        ret[1] = correctPositionY(y);
        return ret;
    }


	
}
