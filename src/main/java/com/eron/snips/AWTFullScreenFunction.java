package com.eron.snips;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Logger;

import javax.swing.JFrame;


public class AWTFullScreenFunction extends JFrame implements MouseListener{
	
	/**
	 * swing全屏方案
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(AWTFullScreenFunction.class.getName());

	public static void main(String[] args) {
		new AWTFullScreenFunction();
	}
	
	private boolean flag = false;
    //开始时左上角的位置,开始时的高度，宽度
    private Point start = new Point(300,150);
    private int width = 600;
    private int height = 400;
     
     
    public AWTFullScreenFunction() {
        this.addMouseListener(this);        
        this.setLocation(start);
        this.setTitle("全屏程序");
        this.setSize(width, height);
        //this.setUndecorated(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void keyPressed(KeyEvent e) {
        log.info(Character.toString(e.getKeyChar()));
    }
 
    public void keyReleased(KeyEvent e) {
        log.info(Character.toString(e.getKeyChar()));
    }
 
    public void keyTyped(KeyEvent e) {}
	
	@Override
	public void mouseClicked(MouseEvent e) {
        //双击全屏或退出全屏
        if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
            if(flag) {
                normal();
            } else {
                max();
            }
        } 
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

    /**
     * 最大化
     */
    private void max() {
        flag = true;
        this.setLocation(0, 0);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.titled(true);
        this.repaint();
    }
     
    /**
     * 退出全屏
     */
    private void normal() {
        flag = false;
        this.setLocation(start);
        this.setSize(width, height);
        this.titled(false);
        this.repaint();
    }
     
    /**
     * 退去全屏时显示标题，全屏时不显示标题
     * @param title
     */
    private void titled(boolean title) {
        this.dispose();
        this.setUndecorated(title);
        this.setVisible(true);
    }
    
}










