package agent;

import model.Simulateur;
import model.StartPositionHandler;

import java.awt.*;

/**
 * Created by jvdur on 13/01/2016.
 */
public class Agent {

    protected final Simulateur sim;
    protected int posx;
    protected int posy;
    protected int dirx; // [-1, 0, 1]
    protected int diry; // [-1, 0, 1]

    private Color color;

    protected int[][] options;
    protected int nbValidOptions;
    protected int nboptions = 0;

    public int age;



    /**
     * Constructeur d'agent
     */
    public Agent(StartPositionHandler.StartPosition sp, Simulateur sim) {
        this.posx = sp.posx;
        this.posy = sp.posy;
        this.dirx = sp.dirx;
        this.diry = sp.diry;
        this.color = sp.color;
        this.sim = sim;
    }

    /**
     * Methode qui donne la main a l'agent et lui donne un comportement
     */
    public void doit() {

        clearOptions();

        int posInitx = posx;
        int posInity = posy;

        setOptions();

        takeDecision(posInitx, posInity);
    }

    /*
     *  List all available option
     */
    protected void setOptions() {

        try {

            // Si aucune vitesse
            if (dirx == 0 && diry == 0) {
                // on regarde autour et on se donne une vitesse oposée
                if (!isfree(correctPositions(posx+1, posy))) dirx = -1;
                if (!isfree(correctPositions(posx-1, posy))) dirx = 1;
                if (!isfree(correctPositions(posx+1, posy))) dirx = -1;
                if (!isfree(correctPositions(posx-1, posy))) dirx = 1;
                if (!isfree(correctPositions(posx-1, posy-1))) {
                    dirx = 1;
                    diry = 1;
                }
                if (!isfree(correctPositions(posx+1, posy+1))) {
                    dirx = -1;
                    diry = -1;
                }
                if (!isfree(correctPositions(posx-1, posy+1))) {
                    dirx = 1;
                    diry = -1;
                }
                if (!isfree(correctPositions(posx+1, posy-1))) {
                    dirx = -1;
                    diry = 1;
                }
            } else {

            }

            // La position d'après la suite de mouvement logique
            int devantx = correctPositionX(posx+dirx);
            int devanty = correctPositionY(posy+diry);

            if (isfree(correctPositions(devantx, devanty))) {
                // Si c'est libre devant on fonce
                addToOptions(devantx, devanty, dirx, diry);
            } else {

                if (Simulateur.JEU) {
                    if (!(sim.getPlateau()[correctPositionX(devantx)][correctPositionY(devanty)] instanceof AWall))
                        sim.getPlateau()[correctPositionX(devantx)][correctPositionY(devanty)].setColor(this.color);
                }

                // sinon on regarde autour
                if (dirx != 0 && diry != 0) {
                    // si mouvement en diagonale
                    if (isfree(correctPositionX(posx+dirx),posy)) {
                        addToOptions(posx+dirx, posy, 0, -diry);
                    }
                    if (isfree(posx, correctPositionY(posy+diry))) {
                        addToOptions(posx, posy+diry, -dirx, 0);
                    }

                } else if (diry != 0) {
                    // si mouvement est vertical
                    if (isfree(correctPositions(posx-1,posy+diry))) {
                        addToOptions(posx-1, posy + diry, -1, 0);
                    }
                    if (isfree(correctPositions(posx+1,posy+diry))) {
                        addToOptions(posx+1, posy+diry, +1, 0);
                    }
                    if (isfree(correctPositions(posx-1,posy))) {
                        addToOptions(posx-1, posy, -1, -diry);
                    }
                    if (isfree(correctPositions(posx+1,posy))) {
                        addToOptions(posx+1, posy, +1, -diry);
                    }

                    if (isfree(correctPositions(posx-1,posy))) {
                        addToOptions(posx-1, posy, -1, -diry);
                    }
                    if (isfree(correctPositions(posx,posy+1))) {
                        addToOptions(posx+1, posy, 1, -diry);
                    }
                } else if (dirx != 0) {
                    // si le mouvement est horizontal
                    if (isfree(correctPositions(posx+dirx,posy-1))) {
                        addToOptions(posx+dirx, posy-1, 0, -1);
                    }
                    if (isfree(correctPositions(posx+dirx,posy+1))) {
                        addToOptions(posx+dirx, posy-1, 0, +1);
                    }
                    if (isfree(correctPositions(posx,posy-1))) {
                        addToOptions(posx+dirx, posy-1, -dirx, -1);
                    }
                    if (isfree(correctPositions(posx,posy+1))) {
                        addToOptions(posx+dirx, posy+1, -dirx, +1);
                    }

                    if (isfree(correctPositions(posx,posy-1))) {
                        addToOptions(posx, posy-1, -dirx, -1);
                    }
                    if (isfree(correctPositions(posx,posy+1))) {
                        addToOptions(posx, posy-1, -dirx, +1);
                    }

                }

            }

            nbValidOptions = nboptions;
            if (!Simulateur.IS_TORIQUE) {
                for (int i=0; i<nboptions;i++) {
                    if (options[i][4] == 1 &&
                            (options[i][0] == 0
                            || options[i][1] == 0
                            || options[i][0] == sim.getGridSizeX()-1
                            || options[i][1] == sim.getGridSizeY()-1)
                            ) {
                        options[i][4] = 0;
                        nbValidOptions--;
                    }
                }
            }

            // appele par le simulateur
            //décision en fonction de comportement
            //agents connaissent que les 8 cases environnement

            //! faire attention aux cas speciaux, si ne sait pas bouger
        } catch (Throwable t) {
            throw t;
        }
    }


    /*
     *   Take the best option
     */
    protected void takeDecision(int posInitx, int posInity) {

        if (nbValidOptions == 0) {
            dirx *= -1;
            diry *= -1;
        } else {
            int option = (int) (Math.random() * (nbValidOptions - 1));
            int optionsave = option;
            while(options[option][4] == 0) {
                option = (++option%(nboptions-1));
                if (option == optionsave) {
                    optionsave = -1;
                    break;
                }
            }

            if(optionsave == -1){
                dirx *= -1;
                diry *= -1;
            } else {
                posx = options[option][0];
                posy = options[option][1];
                dirx = options[option][2];
                diry = options[option][3];

                moveTo(posInitx, posInity, posx, posy);
            }
        }

    }


    protected void moveTo(int posInitx, int posInity, int posx, int posy) {
        this.posx = posx;
        this.posy = posy;
        sim.getPlateau()[posInitx][posInity] = null;
        sim.getPlateau()[posx][posy] = this;
    }


    /*
     * METHODES UTILITAIRES
     */
    protected boolean isfree(int[] data) {
        return isfree(data[0], data[1]);
    }

    protected boolean isfree(int x, int y) {
        return (sim.getPlateau()[x][y] == null);
    }

    protected int correctPositionX(int data) {
        if (data > 0 && data < sim.getGridSizeX())
            return data;
        else if (data < 0)
            return (sim.getGridSizeX() - 1);
        else
            return 0;
    }

    protected int correctPositionY(int data) {
        if (data > 0 && data < sim.getGridSizeY())
            return data;
        else if (data < 0)
            return (sim.getGridSizeY() - 1);
        else
            return 0;
    }

    private int[] correctPosition(int[] data) {
        data[0] = correctPositionX(data[0]);
        data[1] = correctPositionY(data[1]);
        return data;
    }

    protected int[] correctPositions(int x, int y) {
        int[] ret = new int[2];
        ret[0] = correctPositionX(x);
        ret[1] = correctPositionY(y);
        return ret;
    }


    private void addToOptions(int[] data) {
        addToOptions(data[0], data[1], data[2], data[3]);
    }

    protected void addToOptions(int x, int y, int dx, int dy) {
        options[nboptions][0] = correctPositionX(x);
        options[nboptions][1] = correctPositionY(y);
        options[nboptions][2] = dx;
        options[nboptions][3] = dy;
        options[nboptions][4] = 1;
        nboptions++;
    }

    protected void clearOptions() {
        options = null;
        options = new int[10][5];
        nboptions = 0;
    }


    /*
        GETTERS
     */
    public Color getColor() {
        return color;
    }

    public int getDiry() {

        return diry;
    }

    public int getDirx() {

        return dirx;
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

    private void setDiry(int diry) {

        this.diry = diry;
    }

    private void setDirx(int dirx) {

        this.dirx = dirx;
    }

    private void setPosy(int posy) {

        this.posy = posy;
    }

    private void setPosx(int posx) {
        this.posx = posx;
    }
}
