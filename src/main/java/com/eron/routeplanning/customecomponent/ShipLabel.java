/**
 * 
 */
package com.eron.routeplanning.customecomponent;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JLabel;

/**
 * @author Administrator
 * 为船舶形状的标签对象继承自jpanel
 */
public class ShipLabel extends JLabel {  //尝试去做特殊形状的组件
	
	private static final long serialVersionUID = 132942549165519078L;

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	
	private float course=0;  //组件的方向
	//private float Level=1;  //组件的大小级别
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g.create();
		AffineTransform af = g2d.getTransform();
		g2d.rotate(course, 10, 20);
		//假定船舶大小显示20 20
		int[] xs = {
				0,0,10,20,20
		};
		int[] ys = {
				20,10,0,10,20
		};
		g2d.fillPolygon(xs, ys, 5);
		g2d.setTransform(af);
	}
	
	public void setCourse(float course){
		this.course = course;
	}
}
