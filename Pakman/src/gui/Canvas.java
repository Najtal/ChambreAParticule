package gui;
import javax.swing.*;

import agents.ASuperBrick;
import agents.AWall;
import agents.Agent;

import pakman.Simulateur;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by jvdur on 15/01/2016.
 */
public class Canvas extends JPanel implements MouseListener, Observer, KeyListener {


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

        // JPANEL
        this.setSize(canvasWidth, canvasHeight);
        this.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        
        this.setOpaque(true);

    }

    public void paintComponent(Graphics g) {

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
            g.setColor(new Color(205,133,63));
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

        // On dessine la SUPER BRICK
        ASuperBrick asb = model.superBrickAgent;
        g.setColor(asb.getColor());
        g.fillRect(asb.getPosx()*particleSize, asb.getPosy()*particleSize, particleSize, particleSize);


        // Pour chaque agent
        for (Agent agent : model.getAgents()) {
            // On dessine la particule
            g.setColor(agent.getColor());
            if (agent instanceof AWall) {
                g.fillRect(agent.getPosx()*particleSize, agent.getPosy()*particleSize, particleSize, particleSize);
            } else {
                g.fillRoundRect(agent.getPosx()*particleSize, agent.getPosy()*particleSize, particleSize, particleSize, particleArc, particleArc);
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

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {

	    int code = e.getKeyCode();
	    
	    System.out.println("key : " + code);
	    
        if (code == KeyEvent.VK_DOWN) {
        	System.out.println("down");
        }
        if (code == KeyEvent.VK_UP) {
        	System.out.println("up");
        }
        if (code == KeyEvent.VK_LEFT) {
        	System.out.println("left");
        }
        if (code == KeyEvent.VK_RIGHT) {
        	System.out.println("right");
        }
        		
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
