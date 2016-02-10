package gui;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.KeyboardListener;

import pakman.Simulateur;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by jvdur on 13/01/2016.
 */
public class World extends JFrame implements ChangeListener, Observer {

    //rivate static final Color COLOR_GRID = new Color(240, 240, 240);
    //private static final Color COLOR_BACKGROUND = new Color(255, 255, 255);

    private JPanel canvas;
    private Simulateur model;

    /**
     * Constructeur de la frame
     * @param gridSizeX  La taille du tableau de jeu dans sa largeur
     * @param gridSizeY  La taille du tableau de jeu dans sa hauteur
     * @param particleSize La taille d'une bille
     * @param grille Affiche la grille ou pas
     */
    public World(Dimension frameSize, int gridSizeX, int gridSizeY, int particleSize, Simulateur model, boolean grille) {

        super("Pakman");
        this.model = model;

        // Param Frame
        this.setSize(frameSize.width, frameSize.height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        model.addObserver(this);

        // Initialisation du canvas
        this.canvas = new Canvas(gridSizeX, gridSizeY, particleSize, model, grille);

        // Frame construction
        JScrollPane scrPane = new JScrollPane(canvas, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.getContentPane().add(scrPane);

        KeyboardListener keyAdaptator = new KeyboardListener(model);
        this.addKeyListener(keyAdaptator);
        
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        validate();
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
        revalidate();
    }

}
