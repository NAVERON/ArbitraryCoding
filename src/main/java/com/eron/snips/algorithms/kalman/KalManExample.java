package com.eron.snips.algorithms.kalman;


import java.awt.event.MouseEvent;
import static java.lang.Math.*;
import Jama.Matrix;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseMotionListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class KalManExample extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4629537531434387339L;
	
	private KalmanFilter KF;
    private double mouseX, mouseY;

    public KalManExample() {
        this.addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }

            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
    }

    @Override
    public synchronized void paint(Graphics g) {
        super.paint(g);
        if (KF != null) {
            int x = (int) KF.getX().get(0, 0);
            int y = (int) KF.getX().get(1, 0);
            g.setColor(Color.red);
            g.fillOval(x - 5, y - 5, 10, 10);
        }
    }

    private void test() {

        JFrame frame = new JFrame("Kalman Filter Example");
        frame.getContentPane().add(this);
        this.setSize(400, 400);
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //model parameters
        double x = Math.random(), vx = Math.random(), ax = Math.random();

        //process parameters
        double dt = 50d;
        double processNoiseStdev = 3;
        double measurementNoiseStdev = 5000;
        double m = 0;

        //init filter
        KF = KalmanFilter.buildKF(0, 0, dt, pow(processNoiseStdev, 2) / 2, pow(measurementNoiseStdev, 2));
        KF.setX(new Matrix(new double[][]{{mouseX}, {0}, {mouseY}, {0}}));

        //simulation
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                //filter update
                KF.predict();
                KF.correct(new Matrix(new double[][]{{mouseX}, {mouseY}}));
//                System.out.println("Estimate: ");
//                KF.getX().print(3, 1);
                repaint();
            }
        }, 0, 200);
    }

    public static void main(String[] args) {
        new KalManExample().test();
    }
    
}




