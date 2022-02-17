package com.eron.routeplanning.userinterface;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.eron.routeplanning.dataprocessing.PosReport;


public class PointNode extends JLabel{
	
	private static final long serialVersionUID = -5171531996680388065L;
	
	private String tips = null;  //显示浮动出来的信息
	private String detail = null;
	PosReport posreport = null;
	//传进参数是为了弹出相应的信息
	public PointNode(PosReport posreport){
		//setBackground(Color.ORANGE);
		setOpaque(true);  //当需要显示背景色时需要将不透明设置成true，默认为false
		setBorder(BorderFactory.createEmptyBorder());
		/////////////////////////////////////////////////////////////////////
		//super.setCourse(30);
		setBounds((int)(posreport.getLng()),(int)(posreport.getLat()), 20, 20);   //设置位置大小
		//设置悬浮信息
		tips = "time->"+new SimpleDateFormat("yyyy-MM-dd").format(posreport.getTimestamp())
				+"<>speed->"+posreport.getSog()
				+"<>course->"+posreport.getCog()
				+"<>pollution->"+posreport.getPollution();
		detail = posreport.toString();
		setToolTipText(tips);
		
		mouseEvent();
		this.posreport = posreport;
	}
	private SimpleDateFormat SimpleDateFormat(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	public PointNode() {
		super();
		
	}
	
	public void mouseEvent(){
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				//setBorder(new LineBorder(Color.GREEN, 1));
				setBackground(Color.BLUE);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				//setBorder(BorderFactory.createEmptyBorder());
				setBackground(Color.ORANGE);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(null, detail, "Details", JOptionPane.INFORMATION_MESSAGE);;
			}
		});
	}
	public int getPosPollution(){  //获取该记录点的污染量
		return posreport.getPollution();
	}
	
	@Override
	public String toString() {
		return "PointNode [tips=" + tips + "]";
	}
	
	public void changeLocation(int dx, int dy){  //dx、dy表示相对于原位置的偏移量
		setLocation(getX()+dx, getY()+dy);
	}
	
	public void changeLocation(float level){  //缩放
		int changedX = (int) (getX()*level);
		int changedY = (int) (getY()*level);
		setLocation(changedX, changedY);
	}
}
