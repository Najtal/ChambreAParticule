package util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import pakman.Simulateur;

public class KeyboardListener extends KeyAdapter{

	private Simulateur model;
	
	
	public KeyboardListener(Simulateur model) {
		this.model = model;
	}

	@Override
	public void keyPressed(KeyEvent e) {

		// up
		if (e.getKeyCode() == 38) {
			model.kVertical = -1;
			model.kHorizontal = 0;
		}
		
		// left
		if (e.getKeyCode() == 37) {
			model.kVertical = 0;
			model.kHorizontal = -1;
		}
		
		// right
		if (e.getKeyCode() == 39) {
			model.kVertical = 0;
			model.kHorizontal = 1;
		}
		
		// down
		if (e.getKeyCode() == 40) {
			model.kVertical = 1;
			model.kHorizontal = 0;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
	}

}