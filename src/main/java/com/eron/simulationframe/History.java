/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.simulationframe;

import javafx.geometry.Point2D;

/**
 *
 * @author ERON
 */
public class History {  //轨迹点
    
    private double x;
    private double y;
    private double rotation;
    private Point2D velocity;
    private double rudder;
    
    public History(){
    }
    
    public History(double x, double y, double rotation, Point2D velocity, double rudder){
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.velocity = velocity;
        this.rudder = rudder;
    }
    
    public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public Point2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Point2D velocity) {
		this.velocity = velocity;
	}

	public double getRudder() {
		return rudder;
	}

	public void setRudder(double rudder) {
		this.rudder = rudder;
	}

	@Override
    public String toString(){
        return x + "," + y + "," + rotation + "," + velocity.getX() + "," + velocity.getY() + "," + rudder + ","
                + Math.sqrt( velocity.getX()*velocity.getX() + velocity.getY()*velocity.getY() );
    }
}
