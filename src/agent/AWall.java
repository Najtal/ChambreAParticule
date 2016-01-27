package agent;

import model.Simulateur;
import model.StartPositionHandler.StartPosition;

public class AWall extends Agent {

	public final boolean isWall;
	
	public AWall(StartPosition sp, Simulateur sim) {
		super(sp, sim);
		this.isWall = true;
	}
	
}
