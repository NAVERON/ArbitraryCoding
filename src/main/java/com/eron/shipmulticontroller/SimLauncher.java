package com.eron.shipmulticontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;

import javafx.geometry.Point2D;


/**
 * 关注仿真体系的整体结构和运行逻辑 细节不再这里考虑和实现
 * @author eron
 * 1. 数据和ui分离 数据层上嵌套ui的实现更好一些, 不过实际中并没有功能上的差别, 这里知识设计思路 
 * 2. 外层只能干预对象, 不能决定对象的内部实现 分离思想/低耦合高内聚 
 */
public class SimLauncher extends GameApplication {
    
    private static final Logger log = LoggerFactory.getLogger(SimLauncher.class);

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1000);
        settings.setHeight(800);
        settings.setTitle("ShipSimulator");
        
        settings.setVersion("0.0.1");
        settings.setIntroEnabled(false);
        settings.setGameMenuEnabled(false);
        
    }

    @Override
    protected void initInput() {
        super.initInput();
    }

    @Override
    protected void initGame() {
        // super.initGame();
        
    }

    @Override
    protected void initPhysics() {
        super.initPhysics();
        
    }

    @Override
    protected void initUI() {
        super.initUI();
        
    }

    @Override
    protected void onUpdate(double tpf) {
        super.onUpdate(tpf);

    }

    public static void main(String[] args) {
        
        // 交互式创建  工厂模式创建 包装一层 
        ShipLite ship = new ShipLite(new Point2D(100, 100), new Point2D(1, 2), "eron");
        Entity shipDesc = ship.getSelfDescriptionEntity();
        log.info("当前ship状态 --> {}", ship.toString());
        ship.update(ShipStatusEnum.SAIL);
        log.warn("修改后的状态 --> {}", ship.toString());
        
        GameApplication.launch(args);  // 界面launch之后会阻塞在这里, 否则main方法执行完直接结束进程了 
        
    }

    
}















