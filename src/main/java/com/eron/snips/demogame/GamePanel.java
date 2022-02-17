package com.eron.snips.demogame;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GamePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Ground ground;
	private Shape shape;

	public void display(Ground ground, Shape shape) {
		System.out.println("Gamepanel's display");
		this.ground = ground;
		this.shape = shape;
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		// repaint
		super.paintComponents(g);

		g.setColor(new Color(0xcfcfcf));
		g.fillRect(0, 0, Global.WIDTH * Global.CELL_SIZE, Global.HEIGHT * Global.CELL_SIZE);
		if (shape != null && ground != null) {
			shape.drawMe(g);
			ground.drawMe(g);
		}
	}

	public void GamePanel() {
		this.setSize(Global.WIDTH * Global.CELL_SIZE, Global.HEIGHT * Global.CELL_SIZE);
	}

}
