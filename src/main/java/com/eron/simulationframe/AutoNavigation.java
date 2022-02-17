/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.simulationframe;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AutoNavigation extends Application {
    
    private Group things = new Group();  //放置新建的运动物体
    private Pane show;
    private Group tracks = new Group();
    public static List<Ship> ships = new LinkedList<Ship>();
    
    private AnimationTimer timer = null;
    private boolean isPlaying = false, needSave = false;
    private int id = 0;  //自增的id号
    private IntegerProperty number = new SimpleIntegerProperty(0);  //显示创建了多少运动对象
    private FloatProperty rudder = new SimpleFloatProperty();  //显示舵角
    private FloatProperty velocityX = new SimpleFloatProperty();  //x水平
    private FloatProperty velocityY = new SimpleFloatProperty();  //y垂直分量
    
    private double pressedX, pressedY, dragedX, dragedY,releasedX, releasedY;
    private boolean drawing = false;
    private Line line, route;
    
    private Parent createContent(){
        VBox root = new VBox();
        root.setPrefSize(800, 650);
        
        show = new Pane();
        show.setPrefSize(800, 600);
        show.getChildren().add(things);
        show.getChildren().add(tracks);
        
        HBox command = new HBox();
        Button save = new Button("save");
        Button quit = new Button("quit");
        Button clear = new Button("clearAll");
        command.getChildren().addAll(save, quit, clear);
        
        save.setOnAction(e -> { saveFunction(ships); });
        quit.setOnAction(e -> {
            
            Platform.exit();
            System.exit(0);
        });
        clear.setOnAction(e -> {
            ships.clear();
            things.getChildren().clear();
            tracks.getChildren().clear();
            show.getChildren().removeAll(line, route);  line = route = null;
            id = 0;
            number.set(id);
            rudder.set(0);
            velocityX.set(0);
            velocityY.set(0);
        });
        
        HBox info = new HBox();
        Text amount = new Text(), rudderAngle = new Text(), vX = new Text(), vY = new Text();
        amount.textProperty().bind(number.asString("number : [%d]  "));
        rudderAngle.textProperty().bind(rudder.asString("rudderAngle : [%f]  "));
        vX.textProperty().bind(velocityX.asString("vX : [%f]  "));
        vY.textProperty().bind(velocityY.asString("vY : [%f]  "));
        info.getChildren().addAll(amount, rudderAngle, vX, vY);
        
        root.getChildren().addAll(command, show, info);
        
        show.setOnMousePressed(e ->{
            pressedX = e.getX();
            pressedY = e.getY();
            drawing = true;
            show.requestFocus();
        });
        show.setOnMouseDragged(e ->{
            this.dragedX = e.getX();
            this.dragedY = e.getY();
            //System.out.println("autonavigation.AutoNavigation.Draged");
            if(drawing){
                show.getChildren().remove(line); //这里不移除也可以更新界面，什么原理？上面代码统一把界面移除了，这样这不用重复写了
                line = new Line(pressedX, pressedY, dragedX, dragedY);
                show.getChildren().add(line);
            }
        });
        
        show.setOnMouseReleased(e -> {
            this.releasedX = e.getX();
            this.releasedY = e.getY();
            drawing = false;  //删除过程线
            show.getChildren().remove(line);
            double dx = releasedX - pressedX;
            double dy = releasedY - pressedY;
            
            Ship ship = new Ship(id++, new Point2D(dx/10, dy/10));
            ship.setDestination(new Point2D(pressedX + ship.getVelocity().getX()*1000, pressedY + ship.getVelocity().getY()*1000));
            //System.out.println("计算之后的角度 ： " + (ship.getAngle(dx, dy)-90) );
            ship.getView().setRotate(ship.calAngle(dx, dy));
            
            addShip(ship, pressedX, pressedY);
            
            number.set(id);
        });
        
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();
        
        return root;
    }
    
    public void onUpdate(){
        for(int i = 0; i < ships.size(); i++){
            for(int j = 0; j < ships.size(); j++){
                if(ships.get(i) != ships.get(j)){
                    if(ships.get(i).isColliding(ships.get(j))){
                        ships.get(i).setAlive(false);
                        ships.get(j).setAlive(false);
                    }
                }
            }
        }
        tracks.getChildren().clear();  // 清空再重新画, 以后实现增量绘制，会多判断
        for(int k = 0; k < ships.size(); k++){  // 绘制轨迹
            Ship temp = ships.get(k);
            List<Circle> shipTracks = new ArrayList<>();
            for (History s : temp.getHistorys()) {
            	shipTracks.add(new Circle(s.getX(), s.getY(), 2, Color.BLUE));
            }
            tracks.getChildren().addAll(shipTracks);
        }
        ships.forEach(Navigator::upDate);
        
        if(!ships.isEmpty()){
            rudder.set((float) ships.get(0).getRudder());
            velocityX.set((float) ships.get(0).getVelocity().getX());
            velocityY.set((float) ships.get(0).getVelocity().getY());
            
            show.getChildren().remove(route);
            route = new Line(ships.get(0).getPosition().getX(), ships.get(0).getPosition().getY(), ships.get(0).destination.getX(), ships.get(0).destination.getY());
            show.getChildren().add(route);
        }
    }
    
    public void addShip(Ship ship, double x, double y){
        ships.add(ship);
        
        addNavigator(ship, x, y);
    }
    
    public void addNavigator(Navigator navigator, double x, double y){  //视图上的添加
        navigator.getView().setTranslateX(x);
        navigator.getView().setTranslateY(y);
        
        things.getChildren().add(navigator.getView());
    }
    
    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene( createContent() );
        
        primaryStage.setTitle( "CollisionAvoidance" );
        primaryStage.setScene(scene);
        primaryStage.getScene().setOnKeyPressed(e -> {
            if (!ships.isEmpty()) {
                Ship temp = ships.get(0);
                if (e.getCode() == KeyCode.LEFT) {
                    //System.out.println("turn left");
                    temp.turnLeft();
                } else if (e.getCode() == KeyCode.RIGHT) {
                    //System.out.println("turnRight");
                    temp.turnRight();
                }else if(e.getCode() == KeyCode.UP){
                    temp.setVelocity( temp.getVelocity().add(
                        new Point2D( 
                        			Math.abs(Math.cos(Math.toRadians(temp.getAngle()))), 
                        			Math.abs(Math.sin(Math.toRadians(temp.getAngle()))) 
                        		)
                    ) );
                } else if(e.getCode() == KeyCode.DOWN){  //减速无法减速至停止, 会反向变大, 因为角度在360范围内正负情况不一致
                    temp.setVelocity( temp.getVelocity().subtract(
                        new Point2D( 
	                        		-Math.abs(Math.cos(Math.toRadians(temp.getAngle()))), 
	                        		-Math.abs(Math.sin(Math.toRadians(temp.getAngle()))) 
                        		)
                    ) );
                }else if (e.getCode() == KeyCode.SPACE) {
                    if(isPlaying){
                        isPlaying = !isPlaying;
                        timer.stop();
                    }else{
                        isPlaying = !isPlaying;
                        timer.start();
                    }
                }
            }
            
        });
        
        //primaryStage.getScene().getRoot().requestFocus();  // 获取焦点
        show.requestFocus();
        
        primaryStage.show();
    }
    public void saveFunction(List<Ship> ships){
        //System.out.println("saving");
        for(int i = 0; i < ships.size(); i++){
            List<History> shipsHis = new ArrayList<History>(ships.get(i).getHistorys());
        	Saving.save(shipsHis, ships.get(i).getId());
        }
        for(int i = 0; i < ships.size(); i++){
            ships.get(i).clearHistory();
        }
    }
    public void deleteFunction() {
        if (!needSave) {
            File dir = new File("data");
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                for (File file : files) {
                    file.delete();
                    System.out.println("delete file : " + file.getName());
                }
            }
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public static class Launcher {
    	public static void main(String[] args) {
			AutoNavigation auto = new AutoNavigation();
			auto.main(args);
		}
    }
    
}









