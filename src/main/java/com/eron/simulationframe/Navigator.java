/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eron.simulationframe;

import javafx.geometry.Point2D;
import javafx.scene.Node;


public abstract class Navigator {
    
    private Point2D velocity;  //位置和方向信息已经在node里面了
    private int id;
    
    private Node view;
    private boolean isAlive = true;  //isCollision 这样表示更直观
    
    public Navigator(Node view){
        this.view = view;
    }
    public Node getView(){
        return view;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }
    public Point2D getVelocity() {
        return velocity;
    }
    public void setPosition(Point2D position){
        view.setTranslateX(position.getX());
        view.setTranslateY(position.getY());
    }
    public Point2D getPosition(){
        return new Point2D(view.getTranslateX(), view.getTranslateY());
    }
    public void setAlive(boolean alive){
        this.isAlive = alive;
    }
    public boolean isAlive(){
        return isAlive;
    }
    public boolean isDead(){
        return !isAlive;
    }
    
    public abstract void upDate();
    //视图的方向是向右为0，顺时针旋转  依次增大
    public void turnLeft() {
        view.setRotate(view.getRotate() - 5);
        setVelocity(new Point2D(Math.cos(Math.toRadians(view.getRotate())), Math.sin(Math.toRadians(view.getRotate()))));
    }
    public void turnRight() {
        view.setRotate(view.getRotate() + 5);
        setVelocity(new Point2D(Math.cos(Math.toRadians(view.getRotate())), Math.sin(Math.toRadians(view.getRotate()))));
    }
    
    public boolean isColliding(Navigator other){
        return this.getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
    }
    
    public double calAngle(double dx, double dy) {  //向上是0度角，顺时针旋转
        double theta = Math.atan2(dy, dx);
        //theta += Math.PI/2.0;
        double angle = Math.toDegrees(theta);
        if (angle < 0) {
            angle += 360;
        }
        //System.out.println("create angle : " + angle);
        return angle;
    }
    public double getAngle(){  //向上为0的坐标系
        double angle = view.getRotate();
        if(angle >= 360 || angle < 0){
            if(angle >= 360){
                angle -= 360;
            }else if(angle < 0){
                angle += 360;
            }
        }
        return angle;
    }
    public double getSpeed(){
        return Math.sqrt(velocity.getX()*velocity.getX() + velocity.getY()*velocity.getY());
    }
}
