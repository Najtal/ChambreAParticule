package agents;

import pakman.Simulateur;
import pakman.StartPosition;
import pakman.StartPositionHandler;
import util.PosUtil;

public class ACheep extends Agent {

	public boolean hasASuperBrick;
	private int dirx, diry;
	public int powerOfSuperBlockTimeLeft;

	public ACheep(StartPosition sp, Simulateur sim) {
		super(sp, sim);
		sim.cheep = this;
		this.hasASuperBrick = false;
		this.powerOfSuperBlockTimeLeft = 0;
	}

	public void setDirections(int dirx, int diry) {
		this.dirx = dirx;
		this.diry = diry;
	}

	@Override
	public void doit() throws Exception {

		if (powerOfSuperBlockTimeLeft > 0) {
			powerOfSuperBlockTimeLeft--;
		}

		System.out.println(">>>>>> Cheep POITSION : " + posx + "x" + posy);

		Agent cheep = sim.getAgents().get(0);
		int cpx = PosUtil.correctPositionX(cheep.getPosx()+dirx);
		int cpy = PosUtil.correctPositionY(cheep.getPosy()+diry);
		if (PosUtil.isfree(cpx, cpy)) {
			sim.getPlateau()[posx][posy] = null;
			posx = cpx;
			posy = cpy;
			sim.getPlateau()[cpx][cpy] = this;

			if (sim.superBrickAgent.posx == posx && sim.superBrickAgent.posy == posy) {
				System.out.println(">>>>> BRIIIICK !!!");
				this.hasASuperBrick = true;
				StartPositionHandler.createNewSuperBrick(sim);
				powerOfSuperBlockTimeLeft = sim.powerToken;
			}

		} else {

			throw new Exception("invalid position");

		}
	}
}
