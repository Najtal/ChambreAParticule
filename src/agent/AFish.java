package agent;

import model.Simulateur;
import model.StartPositionHandler;

/**
 * Created by jvdur on 24/01/2016.
 */
public class AFish extends Agent {

    public final int breedTime; // The breed time is the number of rounds after which the a
    public int breed;
    /**
     * Constructeur d'agent
     *
     * @param sp
     * @param sim
     * @param isWall
     */
    public AFish(StartPositionHandler.StartPosition sp, Simulateur sim, boolean isWall, int breedTime) {
        super(sp, sim, isWall);

        this.breedTime = breedTime;
        this.breed = 0;
        this.age = 0;
    }


    @Override
    public void doit() {
        //super.doit();

        clearOptions();

        nbValidOptions = 0;
        int posInitx = posx;
        int posInity = posy;
        breed++;
        age++;

        setOptions();

        if (nbValidOptions >= 1 && breed >= breedTime) {
            takeDecision(posInitx, posInity);
            sim.getStartPositionHandler().giveBirth(sim, posInitx, posInity, dirx, diry, false, this.getColor());
            breed = 0;
        } else {
            takeDecision(posInitx, posInity);
        }

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
            }

            // La position d'après la suite de mouvement logique
            int devantx = correctPositionX(posx+dirx);
            int devanty = correctPositionY(posy+diry);

            if (isfree(correctPositions(devantx, devanty))) {
                // Si c'est libre devant on fonce
                addToOptions(devantx, devanty, dirx, diry);
            } else if (dirx != 0 && diry != 0) {
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

        } catch (Throwable t) {
            throw t;
        }
    }

    private boolean isShark(int[] position) {
        return (sim.getPlateau()[position[0]][position[1]] instanceof AShark);
    }

}


