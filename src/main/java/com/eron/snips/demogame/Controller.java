package com.eron.snips.demogame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller extends KeyAdapter implements ShapeListener {

	private Shape shape;
	private Ground ground;
	private ShapeFactory shapeFactory;
	private GamePanel gamePanel;

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		super.keyPressed(e);
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			if (ground.isMovable(shape, Shape.ROTATE))
				shape.rotate();
			break;
		case KeyEvent.VK_DOWN:
			if (isShapeMoveDownable(shape))
				shape.moveDown();
			break;
		case KeyEvent.VK_LEFT:
			if (ground.isMovable(shape, Shape.LEFT))
				shape.moveLeft();
			break;
		case KeyEvent.VK_RIGHT:
			if (ground.isMovable(shape, Shape.RIGHT))
				shape.moveRight();
			break;
		}
		gamePanel.display(ground, shape);
	}

	@Override
	public synchronized boolean isShapeMoveDownable(Shape shape) {
		// TODO Auto-generated method stub
		if (this.shape != shape) {
			return false;
		}

		if (ground.isMovable(shape, Shape.DOWN))
			return true;

		ground.accept(this.shape);

		if (!ground.isFull()) {
			this.shape = shapeFactory.getShape(this);
		}
		return false;
	}

	@Override
	public void shapeMoveDown(Shape shape) {
		// TODO Auto-generated method stub
		gamePanel.display(ground, shape);
	}

	public void newGame() {
		shape = shapeFactory.getShape(this);
	}

	public Controller(ShapeFactory shapeFactory, Ground ground, GamePanel gamePanel) {
		this.shapeFactory = shapeFactory;
		this.ground = ground;
		this.gamePanel = gamePanel;
	}

}
