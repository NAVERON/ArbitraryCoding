package com.eron.shipmuticontroller;

import javafx.geometry.Point2D;

/**
 * 基本控制单元 简化版船舶对象, 测试并发控制 
 * @author eron 
 * 这次实现对象自己循环数据, 外部直接获取当前需要的数据即可 
 * 即 外部看ShipLite只是一个静态的数据体 
 */
public class ShipLite implements Runnable { 

	private Point2D position; // 位置 x, y // 自动更新, 一直向前 
	private Point2D vector;  // 方向 vx, vy -> 简介计算当前航向 
	private Double rudder; // 当前舵角  // 改变舵角, 会被中途打断 
	// 舵角的问题, 因为惯性, 舵发生变化时, 会有一定的迟钝, 之前的加速度匀速变化到当前的扭矩(舵角给出的力) 
	private Double angularAcceleration; // 当前角加速度 
	
	private String shipId; // 船舶名称/id 
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// 主循环 
	}
	
	public Double getSpeed() {
		// 获取当前航行速度 
		Double speed = vector.magnitude();
		
		return speed;
	}
	
	public Double getHead() {
		// 获取当前航向 
		Double head = position.angle(position.add(vector));
		
		return head;
	}
	
	public void setRudder() {
		// 设置舵角 是瞬时变化的, 需要时刻更新当前的角加速度和速度矢量变化 
		
	}
	
	public void recordVDR() {
		// voyage data recorder 
		// 自动持久化数据 
	}
	
	public void onUpdate() {
		// 自动航行计算 根据当前的状态计算下一个状态 
	}
	
}









