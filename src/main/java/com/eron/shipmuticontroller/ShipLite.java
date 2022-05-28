package com.eron.shipmuticontroller;

import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.almasb.fxgl.entity.Entity;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 * 基本控制单元 简化版船舶对象, 测试并发控制
 * 习惯性编成, 内部调用方法使用this前缀, 养成习惯, 这样可以方便的直接观察到代码的调用链结构 
 * @author eron 这次实现对象自己循环数据, 外部直接获取当前需要的数据即可 即 外部看ShipLite只是一个静态的数据体
 * 
 * 今后的设计可以将data独立出来, 外层只做交互, 调度和ui转换 
 */
public class ShipLite implements Callable<String> {
    
    private static final Logger log = LoggerFactory.getLogger(ShipLite.class);

    private Point2D position; // 位置 x, y // 自动更新, 一直向前
    private Point2D vector; // 方向 vx, vy -> 简介计算当前航向
    private Double rudder = 0D; // 当前舵角 // 改变舵角, 会被中途打断
    // 舵角的问题, 因为惯性, 舵发生变化时, 会有一定的迟钝, 之前的加速度匀速变化到当前的扭矩(舵角给出的力)
    private Double angularAcceleration = 0D; // 当前角加速度

    private String shipId; // 船舶名称/id
    private ShipStatusEnum shipStatus = ShipStatusEnum.DEFAULT; // 船舶航行状态 控制整体的运行策略 
    
    // 内部方法线程管理 
    private ThreadPoolExecutor runner = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(1));
    // ExecutorService x = Executors.newSingleThreadExecutor();
    private Future<String> runnableFuture;  // 返回执行状态 
    
    public ShipLite(Point2D pos, Point2D vec, String id) {
        this.position = pos;
        this.vector = vec;
        this.shipId = id;
        
        log.info("初始化...");
        this.init();
    }
    
    public ShipLite(Point2D pos, Point2D vec, String id, ShipStatusEnum status) {
        this.position = pos;
        this.vector = vec;
        this.shipId = id;
        this.shipStatus = status;
        
        log.info("初始化 append ship status ...");
        this.init();
    }
    
    public void init() {
        // 开始执行自己的 run 方法
        log.info("提交自己");
        runnableFuture = this.runner.submit(this);
        
        log.info("ing...");
        log.info("当前runnable status --> {}", runnableFuture.toString());
        log.info("返回结果 --> {}, {}", runnableFuture.isDone(), this.runner.getActiveCount());
    }
    
    @Override
    public String call() throws Exception {
        // 主循环 
        while(this.shipStatus.isMoving()) {
            log.info("循环中...");
            this.goAhead();
            Thread.sleep(1000);
        }
        
        return "running";  // 自定义执行结果
    }
    
    // 终结线程池所有任务 因为单个对象中只允许一个更新循环 
    public void terminalRunner() {
        log.warn("termianl runner !");
        // 使用哪种方式结束上一个运行循环 ? 
        this.runnableFuture.cancel(true);  // 终结当前任务 
        log.info("检查终结状态 --> {}, {}", this.runnableFuture.isDone(), this.runnableFuture.isCancelled());
        
        try {
            log.info("线程池方式终结...");
            // 终结当前运行的线程 
            this.runner.awaitTermination(100, TimeUnit.MILLISECONDS);
            log.info("当前运行数量 --> {}", this.runner.getActiveCount());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    public void goAhead() {  // 由主循环调用 实现自动计算前进/下一个轨迹点的功能 
        // 前进 
        log.info("前进一步");
    }
    
    public void update(ShipStatusEnum status) {
        // 自动航行计算 根据当前的状态计算下一个状态 
        // 更新线程池运行 
        this.shipStatus = status;
        this.terminalRunner();
        // 重启 
        this.init();
        
    }

    public Entity getSelfDescriptionEntity() {
        // 自己调用传进来的句柄  绘制出自己的样貌/图形化自己 
        // 返回Shap 由上层决定是否添加进入真正的界面 
        Entity shipEntity = new Entity();
        log.info("生成组建对象---对外提供对象的缩放旋转接口");
        
        // 应当由内部描述和生成 
        return shipEntity;
    }

    @Override
    public String toString() {
        return "ShipLite [position=" + position + ", vector=" + vector + ", rudder=" + rudder + ", angularAcceleration="
                + angularAcceleration + ", shipId=" + shipId + ", shipStatus=" + shipStatus + ", activeRunner=" + runner.getActiveCount() + "]";
    }
    
    
}









