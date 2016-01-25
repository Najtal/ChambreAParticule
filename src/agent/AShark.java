package agent;

import model.Simulateur;
import model.StartPositionHandler;

/**
 * Created by jvdur on 24/01/2016.
 */
public class AShark extends Agent {

    public final int starveTime; // It represents the time a predator can survive without eating. After the starve time is exceeded, the agent dies and is removed from the Wa-Tor world
    public int starve;
    public final int breedTime; // The breed time is the number of rounds after which a shark can reproduce
    public int breed;

    /**
     * Constructeur d'agent
     *
     * @param sp
     * @param sim
     * @param isWall
     */
    public AShark(StartPositionHandler.StartPosition sp, Simulateur sim, boolean isWall, int starveTime,int breedTime) {
        super(sp, sim, isWall);
        this.breedTime = breedTime;
        this.starveTime = starveTime;
        this.starve = 0;
        this.breed = 0;
    }

    @Override
    public void doit() {
        //super.doit();

        int posInitx = posx;
        int posInity = posy;
        clearOptions();
        starve++;
        breed++;
        age++;

        // si n'a rien mangé depuis 3 tours, il meurt
        if (starve == starveTime) {
            starve();
            return;
        }

        setOptions();

        if (breed == breedTime) {
            if (this.posx != posInitx || this.posy != posInity){
                sim.getStartPositionHandler().giveBirth(sim, posInitx, posInity, dirx, diry, true, this.getColor());
                this.breed = 0;
            } else {
                breed--;
            }
        }

    }

    private void starve() {
        sim.getAgents().remove(sim.getPlateau()[posx][posy]);
        sim.getPlateau()[posx][posy] = null;
    }

    @Override
    protected void setOptions() {
        super.setOptions();

        int posInitx = posx;
        int posInity = posy;

        // on regarde autuor de sois et on cherche un poisson
        if (isFish(correctPositions(posx+1, posy))) {
            dirx = 1;
            diry = 0;
            eatFish(posInitx, posInity, posx+1, posy);
        }
        if (isFish(correctPositions(posx-1, posy))) {
            dirx = -1;
            diry = 0;
            eatFish(posInitx, posInity, posx-1, posy);
        }
        if (isFish(correctPositions(posx, posy+1))) {
            dirx = 0;
            diry = 1;
            eatFish(posInitx, posInity, posx+1, posy+1);
        }
        if (isFish(correctPositions(posx, posy-1))) {
            dirx = 0;
            diry = -1;
            eatFish(posInitx, posInity, posx, posy-1);
        }
        if (isFish(correctPositions(posx-1, posy-1))) {
            dirx = -1;
            diry = -1;
            eatFish(posInitx, posInity, posx-1, posy);
        }
        if (isFish(correctPositions(posx+1, posy+1))) {
            dirx = 1;
            diry = 1;
            eatFish(posInitx, posInity, posx+1, posy+1);
        }
        if (isFish(correctPositions(posx-1, posy+1))) {
            dirx = -1;
            diry = 1;
            eatFish(posInitx, posInity, posx-1, posy+1);
        }
        if (isFish(correctPositions(posx+1, posy-1))) {
            dirx = 1;
            diry = -1;
            eatFish(posInitx, posInity, posx+1, posy-1);
        }


        if (dirx == 0 && diry == 0) {
            dirx = (int) (Math.random() * (3)) - 2;
            diry = (int) (Math.random() * (3)) - 2;
        }

        // sinon

        /// on continue tout droit
        int devantx = correctPositionX(posx + dirx);
        int devanty = correctPositionY(posy + diry);

        if (isfree(correctPositions(devantx, devanty))) {
            /// Si c'est libre devant on fonce
            moveTo(posInitx, posInity, devantx, devanty);
            return;
        } else {
            /// on choisi une autre option au hasard
            if (dirx != 0 && diry != 0) {
                // si mouvement en diagonale
                if (isfree(correctPositionX(posx + dirx), posy)) {
                    addToOptions(posx + dirx, posy, 0, -diry);
                }
                if (isfree(posx, correctPositionY(posy + diry))) {
                    addToOptions(posx, posy + diry, -dirx, 0);
                }
            } else if (diry != 0) {
                // si mouvement est vertical
                if (isfree(correctPositions(posx - 1, posy + diry))) {
                    addToOptions(posx - 1, posy + diry, -1, 0);
                }
                if (isfree(correctPositions(posx + 1, posy + diry))) {
                    addToOptions(posx + 1, posy + diry, +1, 0);
                }
                if (isfree(correctPositions(posx - 1, posy))) {
                    addToOptions(posx - 1, posy, -1, -diry);
                }
                if (isfree(correctPositions(posx + 1, posy))) {
                    addToOptions(posx + 1, posy, +1, -diry);
                }
                if (isfree(correctPositions(posx - 1, posy))) {
                    addToOptions(posx - 1, posy, -1, -diry);
                }
                if (isfree(correctPositions(posx, posy + 1))) {
                    addToOptions(posx + 1, posy, 1, -diry);
                }
            } else if (dirx != 0) {
                // si le mouvement est horizontal
                if (isfree(correctPositions(posx + dirx, posy - 1))) {
                    addToOptions(posx + dirx, posy - 1, 0, -1);
                }
                if (isfree(correctPositions(posx + dirx, posy + 1))) {
                    addToOptions(posx + dirx, posy - 1, 0, +1);
                }
                if (isfree(correctPositions(posx, posy - 1))) {
                    addToOptions(posx + dirx, posy - 1, -dirx, -1);
                }
                if (isfree(correctPositions(posx, posy + 1))) {
                    addToOptions(posx + dirx, posy + 1, -dirx, +1);

                    if (isfree(correctPositions(posx, posy - 1))) {
                        addToOptions(posx, posy - 1, -dirx, -1);
                    }
                    if (isfree(correctPositions(posx, posy + 1))) {
                        addToOptions(posx, posy - 1, -dirx, +1);
                    }
                }
            }

            nbValidOptions = nboptions;
            if (!Simulateur.IS_TORIQUE) {
                for (int i = 0; i < nboptions; i++) {
                    if (options[i][4] == 1 &&
                            (options[i][0] == 0
                                    || options[i][1] == 0
                                    || options[i][0] == sim.getGridSizeX() - 1
                                    || options[i][1] == sim.getGridSizeY() - 1)
                            ) {
                        options[i][4] = 0;
                        nbValidOptions--;
                    }
                }
            }

            takeDecision(posInitx, posInity);
            return;
        }
    }

    private void eatFish(int posInitx, int posInity, int posx, int posy) {
        if (sim.getPlateau()[posx][posy] != null) {
            sim.getAgents().remove(sim.getPlateau()[posx][posy]);
            sim.getPlateau()[posx][posy] = null;
        }

        //moveTo(correctPositionX(posInitx), correctPositionX(posInity), correctPositionX(posx), correctPositionX(posy));
        this.starve = 0;

        /*if (breed == breedTime) {
            sim.getStartPositionHandler().giveBirth(sim, posInitx, posInity, dirx-1, diry-1, true, this.getColor());
            breed = 0;
        }*/
    }

    private boolean isFish(int[] position) {
        return (sim.getPlateau()[correctPositionX(position[0])][correctPositionX(position[1])] instanceof AFish);
    }

}
