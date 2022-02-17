package com.eron.snips.demogame;

import javax.swing.JFrame;

/**
 * 不清楚是什么的验证代码
 * 没有图形界面，可能当时需要参考的某一部分代码学习
 * @author eron
 *
 */
public class Game {

	public static void main(String[] args) {

		ShapeFactory shapeFactory = new ShapeFactory();
		Ground ground = new Ground();
		GamePanel gamePanel = new GamePanel();

		Controller controller = new Controller(shapeFactory, ground, gamePanel);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(gamePanel.getSize().width + 10, gamePanel.getSize().height + 35);
		frame.add(gamePanel);
		gamePanel.addKeyListener(controller);
		frame.addKeyListener(controller);
		frame.setVisible(true);
		controller.newGame();
	}

}
