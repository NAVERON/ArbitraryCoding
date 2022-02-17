package com.eron.routeplanning.userinterface;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.eron.routeplanning.dataprocessing.PosReport;

import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DrawMap extends JPanel {
	
	private static final long serialVersionUID = 6515159857267295490L;
	
	private Point start = null, end = null, draging = null;
	List<PointNode> nodes = new ArrayList<PointNode>();  //存储轨迹对象
	private boolean aeraSet = false;
	
	private float level = 1;  //倍数，当前界面的缩放比例，1:1
	private float shiftX = 0, shiftY = 0;
	/**
	 * 点之间的连线只能画出来了，seperator只能画水平或垂直线
	 */
	public DrawMap() {
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent wheelEvent) {
				if (wheelEvent.getWheelRotation() == 1) {
					level /= 2;  // 记录缩放比例
					nodeScale(0.5F);  //参数表示相对于现在缩放多少
				}
				else if(wheelEvent.getWheelRotation() == -1){
					level *= 2;
					nodeScale(2);
				}
				repaint();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (!aeraSet) {
					//界面上的组件整体移动
					int dx = e.getX()-start.x;
					int dy = e.getY()-start.y;
					//////////////////记录偏移量
					shiftX += dx;
					shiftY += dy;
					////////////////
					nodeMove(dx, dy);
					start = e.getPoint();
					repaint();
				}
				else if (aeraSet) {
					//选择区域
					draging = new Point(e.getX(), e.getY());
					repaint();
					//System.out.println("aera set function");
				}
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				start = e.getPoint();
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				//System.out.println(start);
				if (e.getButton() == MouseEvent.BUTTON3) {
					aeraSet = true;
					//System.out.println("you jian pressed"+e.getPoint());
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				end = e.getPoint();
				if (e.getButton() == MouseEvent.BUTTON3) {
					aeraSet = false;
					repaint();
					double area = (e.getX()-start.x)*(e.getY()-start.y);
					double allPollution = 0;
					List<PointNode> selectedNodes = new ArrayList<PointNode>();
					for(int i=0;i<nodes.size();i++){  //只能从左向右选取
						PointNode temp = nodes.get(i);
						if (temp.getX()>start.x&&temp.getX()<end.x&&temp.getY()>start.y&&temp.getY()<end.y) {
							selectedNodes.add(temp);
							allPollution += temp.getPosPollution();
						}
					}
					//求得选取的点之后进行计算
					double avePoLevel = allPollution/area;
					String info = "area->"+area
							+"\nnodes->"+selectedNodes.size()
							+"\nallpollutiom"+allPollution
							+"\navepollution->"+avePoLevel*1000;
					//System.out.println(info);
					JOptionPane.showMessageDialog(null, info, "Calcu Resault", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		setLayout(null);
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createEmptyBorder());
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		if (!aeraSet) {
			if (nodes.isEmpty()) {
				return;
			}
			int startX = nodes.get(0).getX();
			int startY = nodes.get(0).getY();
			int endX = 0, endY = 0;
			PointNode end = null;
			for(int i=1;i<nodes.size();i++){
				end = nodes.get(i);
				endX = end.getX();
				endY = end.getY();
				g.drawLine(startX, startY, endX, endY);
				startX = endX;
				startY = endY;
				
				g.setColor(Color.BLUE);
				if(level>20){
					g.fillRect(startX, startY, (int) (end.getPosPollution()*level), 20);
				}
				else if (level>10&& level <20) {
					g.fillOval(startX, startY, 20, 20);
				}
				else {
					g.setColor(Color.DARK_GRAY);
					g.fillOval(startX, startY, 10, 20);
				}
			}
		}
		//画选择的区域
		if (aeraSet) {  //选择的时候显示，之后消失
			paintRect(g);
		}
	}
	
	public void paintRect(Graphics g){
		//System.out.println("paint rect...");
		g.drawRect(start.x, start.y, draging.x-start.x, draging.y-start.y);
	}
	
	public void getPosreports(List<PosReport> posreports){  //从外界获取数据,生成jlabel节点数组
		nodes.clear();  //先将先前的数据清除
		if (posreports ==null) {
			return;
		}
		for(int i=0;i<posreports.size();i++){  //将数据存入jlabel
			nodes.add(new PointNode(posreports.get(i)));
		}
	}
	
	public void addNodes(){
		this.removeAll();  //先将所有组件移出
		for(int i=0;i<nodes.size();i++){  //添加节点
			this.add(nodes.get(i));
		}
	}
	
	public void nodeMove(int dx, int dy){
		for(int i=0;i<nodes.size();i++){
			nodes.get(i).changeLocation(dx, dy);
		}
	}
	
	public void nodeScale(float level){
		for(int i=0;i<nodes.size();i++){
			nodes.get(i).changeLocation(level);
		}
	}
}
