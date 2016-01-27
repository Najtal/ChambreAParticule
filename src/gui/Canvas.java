package gui;

import agent.AFish;
import agent.AShark;
import agent.Agent;
import model.Simulateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by jvdur on 15/01/2016.
 */
public class Canvas extends JPanel implements MouseListener, Observer {


    static int nbt = 0;
    private final boolean grille;
    private final int nbParticleHeight;
    private final int nbParticleWidth;

    private int canvasWidth;
    private int canvasHeight;

    private int particleSize;
    private int particleArc;

    private Simulateur model;


    /**
     * Constructeur de canvas
     */
    public Canvas(int width, int height, int particleSize, Simulateur model, boolean grille) {

        this.canvasHeight = height * particleSize;
        this.canvasWidth = width * particleSize;
        this.nbParticleWidth = width;
        this.nbParticleHeight = height;
        this.grille = grille;

        this.particleSize = particleSize;
        this.particleArc = Math.round(particleSize);

        this.model = model;

        this.addMouseListener(this);
        model.addObserver(this);
        //model.addChangeListener(this);

        // JPANEL
        //this.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        this.setSize(canvasWidth, canvasHeight);
        this.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        //this.setBackground(Color.white);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setOpaque(true);

    }

    public void paintComponent(Graphics g) {

        ArrayList<Agent> agents = (ArrayList<Agent>) model.getAgents().clone();

        if (agents != null && agents.size() >= 1) {

	        if (grille) {
	            g.setColor(Color.lightGray);
	
	            // Lignes horizontales
	            for (int i=1; i<nbParticleWidth; i++) {
	                g.drawLine(i*particleSize,0,i*particleSize,canvasHeight);
	            }
	
	            // Lignes verticales
	            for (int i=0; i<nbParticleHeight; i++) {
	                g.drawLine(0,i*particleSize,canvasWidth,i*particleSize);
	            }
	        }
	
	        if (!Simulateur.IS_TORIQUE) {
	            // On dessine les bords
	            g.setColor(Color.GRAY);
	            int hauteur = (nbParticleHeight-1)*particleSize;
	            int largeur = (nbParticleWidth-1)*particleSize;
	            for (int i=0;i<nbParticleWidth;i++) {
	                // HAUT
	                g.fillRect(i*particleSize,0,particleSize,particleSize);
	                // BAS
	                g.fillRect(i*particleSize,hauteur,particleSize,particleSize);
	            }
	            for (int i=0;i<nbParticleHeight;i++) {
	                // GAUCHE
	                g.fillRect(0,i*particleSize,particleSize,particleSize);
	                // DROIT
	                g.fillRect(largeur,i*particleSize,particleSize,particleSize);
	            }
	        }
	
	
	        // Pour chaque agent
	        for (Agent agent : agents) {
	            // On dessine la particule
	            if (agent instanceof AShark) {
	                //g.setColor(Color.white);
	                //char [] carray = { (((AShark) agent).starve+"").charAt(0) };
	                //g.drawChars(carray, 0, carray.length, agent.getPosx()*particleSize+particleSize/2, agent.getPosy()*particleSize+particleSize/2);
	
	                if(agent.age <= 1) {
	                    g.setColor(Color.PINK);
	                } else {
	                    g.setColor(Color.RED);
	                }
	                //g.fillRect(agent.getPosx()*particleSize, agent.getPosy()*particleSize, particleSize, particleSize);
	                g.fillRoundRect(agent.getPosx()*particleSize, agent.getPosy()*particleSize, particleSize, particleSize, particleArc, particleArc);
	
	            } else {

                    try {
                        if(agent.age <= 1) {
                            g.setColor(Color.GREEN);
                        } else {
                            g.setColor(Color.BLUE);
                        }
                        g.fillRoundRect(agent.getPosx()*particleSize, agent.getPosy()*particleSize, particleSize, particleSize, particleArc, particleArc);
                    } catch (Exception e) {

                    }
	            }
	
	        }
    	
        }

    }


    @Override
    public void update(Observable o, Object arg) {
        this.revalidate();
        this.repaint();
    }



    /* MOUSE LINTENER */
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    // ... other MouseListener methods ... //

}
