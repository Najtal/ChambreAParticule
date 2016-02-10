package agents;

import pakman.StartPosition;
import util.PosUtil;
import pakman.Simulateur;

/**
 * 
 * @author durieu
 *
 */
public class AWolf extends Agent {

	/**
	 * Constructeur de loup
	 * @param sp
	 * @param sim
	 */
	public AWolf(StartPosition sp, Simulateur sim) {
		super(sp, sim);
	}
	
	
	@Override
	public void doit() {		
		// Get the best value arround him from Dijkstra table (sim)

		int[] bestPosition;

		System.out.println("sim.cheep.powerOfSuperBlockTimeLeft : " + sim.cheep.powerOfSuperBlockTimeLeft);

		if (sim.cheep.powerOfSuperBlockTimeLeft > 0) {
			bestPosition = maxValueArroundMe();
		} else {
			bestPosition = minValueArroundMe();
		}
        sim.getPlateau()[bestPosition[0]][bestPosition[1]] = this;
        sim.getPlateau()[posx][posy] = null;
		this.posx = bestPosition[0];
		this.posy = bestPosition[1];
	}


	private int[] minValueArroundMe() {
		
		int[] bestPosition = new int[3];
		bestPosition[2] = Integer.MAX_VALUE;
		
		bestPosition = valueAt(bestPosition, posx, PosUtil.correctPositionY(posy+1));
		bestPosition = valueAt(bestPosition, posx, PosUtil.correctPositionY(posy-1));
		bestPosition = valueAt(bestPosition, PosUtil.correctPositionX(posx+1), posy);
		bestPosition = valueAt(bestPosition, PosUtil.correctPositionX(posx-1), posy);
		
		return bestPosition;
	}
	
	private int[] valueAt(int[] minVal, int posx, int posy) {
	
		if (PosUtil.isfree(posx, posy)) {
			if (minVal[2] > sim.getDijkstra()[posx][posy]) {
				minVal[2] = sim.getDijkstra()[posx][posy];
				minVal[0] = posx;
				minVal[1] = posy;
			}
		} else if (sim.getPlateau()[posx][posy] instanceof ACheep && sim.getDijkstra()[posx][posy] == 1){
			minVal[2] = sim.getDijkstra()[posx][posy];
			minVal[0] = posx;
			minVal[1] = posy;
			sim.isFini(true);
		}
		
		return minVal;
	}


	private int[] maxValueArroundMe() {
		int[] bestPosition = new int[3];
		bestPosition[2] = 0;

		bestPosition = valueAtAsMin(bestPosition, posx, PosUtil.correctPositionY(posy+1));
		bestPosition = valueAtAsMin(bestPosition, posx, PosUtil.correctPositionY(posy-1));
		bestPosition = valueAtAsMin(bestPosition, PosUtil.correctPositionX(posx+1), posy);
		bestPosition = valueAtAsMin(bestPosition, PosUtil.correctPositionX(posx-1), posy);

		return bestPosition;
	}

	private int[] valueAtAsMin(int[] minVal, int posx, int posy) {

		if (PosUtil.isfree(posx, posy)) {
			if (minVal[2] < sim.getDijkstra()[posx][posy]) {
				minVal[2] = sim.getDijkstra()[posx][posy];
				minVal[0] = posx;
				minVal[1] = posy;
			}
		} else if (sim.getPlateau()[posx][posy] instanceof ACheep && sim.getDijkstra()[posx][posy] == 1){
			minVal[2] = sim.getDijkstra()[posx][posy];
			minVal[0] = posx;
			minVal[1] = posy;
			sim.isFini(true);
		}

		return minVal;
	}

}
