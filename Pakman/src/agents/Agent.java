package agents;
import java.awt.*;

import pakman.Simulateur;
import pakman.StartPosition;
import pakman.StartPositionHandler;


/**
 * Created by jvdur on 13/01/2016.
 */
public class Agent {

    protected final Simulateur sim;
    protected int posx;
    protected int posy;

    private Color color;

    /**
     * Constructeur d'agent
     */
    public Agent(StartPosition sp, Simulateur sim) {
        this.posx = sp.posx;
        this.posy = sp.posy;
        this.sim = sim;
        this.color = sp.color;
    }

    /**
     * Methode qui donne la main a l'agent et lui donne un comportement
     */
    public void doit() throws Exception {
    	
    }



    /*
        GETTERS
     */
    public Color getColor() {
        return color;
    }

    public int getPosy() {
        return posy;
    }

    public int getPosx() {
        return posx;
    }


    /*
        SETTERS
     */
    private void setColor(Color color) {
        this.color = color;
    }

    private void setPosy(int posy) {
        this.posy = posy;
    }

    private void setPosx(int posx) {
        this.posx = posx;
    }
}
