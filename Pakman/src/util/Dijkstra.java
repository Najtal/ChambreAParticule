package util;

import java.util.LinkedList;

import agents.ACheep;
import pakman.Simulateur;

public class Dijkstra {

	private static Simulateur sim;
	
	private static int[][] dj;
	private static LinkedList<Pos> ll;
	
	public static void init(Simulateur simul) {
		sim = simul;
	}

	
    public static int[][] computeDijkstra() {


		dj = new int[sim.getGridSizeX()][sim.getGridSizeY()];

    	ll = new LinkedList<Pos>();
    	
    	int x = sim.getAgents().get(0).getPosx();
    	int y = sim.getAgents().get(0).getPosy();
    	Pos startPos = new Pos(x, y);
    	
    	dj[x][y] = 1;
    	ll.add(startPos);
    	
    	while(!ll.isEmpty()) {
        	computeposition(ll.removeFirst());
    	}
    	
    	return dj;
	}

	private static void computeposition(Pos p) {
		int val = dj[p.x][p.y];
		setValueOfPosition(new Pos(p.x, p.y));
	}


	private static void setValueOfPosition(Pos p) {

		// if pos is an empty space
			int minval = Integer.MAX_VALUE;
			// get smallest value arround position
			if (checkPosition(p.x + 1, p.y) && dj[PosUtil.correctPositionX(p.x + 1)][p.y] > 0 && dj[PosUtil.correctPositionX(p.x + 1)][p.y] < minval)
				minval = dj[PosUtil.correctPositionX(p.x + 1)][p.y];
			if (checkPosition(p.x - 1, p.y) && dj[PosUtil.correctPositionX(p.x - 1)][p.y] > 0 && dj[PosUtil.correctPositionX(p.x + 1)][p.y] < minval)
				minval = dj[PosUtil.correctPositionX(p.x - 1)][p.y];
			if (checkPosition(p.x, p.y + 1) && dj[p.x][PosUtil.correctPositionY(p.y + 1)] > 0 && dj[p.x][PosUtil.correctPositionY(p.y+1)] < minval)
				minval = dj[p.x][PosUtil.correctPositionY(p.y + 1)];
			if (checkPosition(p.x, p.y - 1) && dj[p.x][PosUtil.correctPositionY(p.y - 1)] > 0 && dj[p.x][PosUtil.correctPositionY(p.y-1)] < minval)
				minval = dj[p.x][PosUtil.correctPositionY(p.y - 1)];
			if (checkPosition(p.x, p.y) && dj[p.x][p.y] > 0 && dj[p.x][p.y] < minval)
				minval = dj[p.x][p.y];

			if (minval == Integer.MAX_VALUE)
				minval = dj[p.x][p.y];
			else if(minval < dj[p.x][p.y])
				minval += 1;

			// défini sa propre valeur
			dj[p.x][p.y] = minval;

			// propagate smallest value
			propagateOnPosition(p.x + 1, p.y, minval);
			propagateOnPosition(p.x - 1, p.y, minval);
			propagateOnPosition(p.x, p.y + 1, minval);
			propagateOnPosition(p.x, p.y - 1, minval);
	}

	private static void propagateOnPosition(int x, int y, int minval) {
		// if position ok
		if (checkPosition(x, y)) {
			// if value (neighbour)  greater than what they found
			if (dj[PosUtil.correctPositionX(x)][PosUtil.correctPositionY(y)] > minval+1) {
				ll.addLast(new Pos(PosUtil.correctPositionX(x),PosUtil.correctPositionY(y)));

			// if value not initialized
			} else if (dj[PosUtil.correctPositionX(x)][PosUtil.correctPositionY(y)] == 0 ) {
				dj[PosUtil.correctPositionX(x)][PosUtil.correctPositionY(y)] = minval+1;
				ll.addLast(new Pos(PosUtil.correctPositionX(x),PosUtil.correctPositionY(y)));
			}

			//if(dj[PosUtil.correctPositionX(x)][PosUtil.correctPositionY(y)] == 0) {
			//	ll.addLast(new Pos(PosUtil.correctPositionX(x),PosUtil.correctPositionY(y)));
			//}
		} else {
			if(dj[PosUtil.correctPositionX(x)][PosUtil.correctPositionY(y)] != 1)
				dj[PosUtil.correctPositionX(x)][PosUtil.correctPositionY(y)] = -1;
		}
	}

	private static boolean checkPosition(int posx, int posy) {
		if (!PosUtil.isfree(PosUtil.correctPositionX(posx), PosUtil.correctPositionY(posy))) {
			if (sim.getPlateau()[PosUtil.correctPositionX(posx)][PosUtil.correctPositionY(posy)] instanceof ACheep) {
				return true;
			}
			return false;
		} else {
			return true;
		}
	}


	public static void print(int[][] tab) {

		for (int i=sim.getGridSizeX()-1;i>=0 ; i--) {
			for (int j=sim.getGridSizeY()-1;j>=0; j--) {
				if (tab[i][j] < 0)
					System.out.print(" "+ tab[i][j]);
				else if (tab[i][j] <= 9)
					System.out.print("  "+ tab[i][j]);
				else
					System.out.print(" "+ tab[i][j]);

			}
			System.out.println();
		}

	}
}



