/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */

package com.eron.solarsystem;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Camera3D;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.almasb.fxgl.scene3d.SkyboxBuilder;
import com.almasb.fxgl.texture.ColoredTexture;
import com.almasb.fxgl.texture.ImagesKt;
import javafx.geometry.Point2D;
import javafx.scene.PointLight;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class SolarSystemApp extends GameApplication {

    private TransformComponent transform;

    private Camera3D camera3D;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.set3D(true);
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.W, () -> {
            camera3D.moveForward();
        });
        onKey(KeyCode.S, () -> {
            camera3D.moveBack();
        });
        onKey(KeyCode.A, () -> {
            camera3D.moveLeft();
        });
        onKey(KeyCode.D, () -> {
            camera3D.moveRight();
        });

        onKey(KeyCode.L, () -> {
            getGameController().exit();
        });
    }

    @Override
    protected void initGame() {
        camera3D = getGameScene().getCamera3D();
        camera3D.setMoveSpeed(40);  // 设置camera 移动速度 
        camera3D.getPerspectiveCamera().setFarClip(500000);
        transform = getGameScene().getCamera3D().getTransform();  // 移动变换 

        transform.translateZ(-10);

        getGameWorld().addEntityFactory(new SolarSystemFactory());  // 设置entity 工厂 

        initLight();  // 灯光 
        initSkybox();  // 外景 包括幕布 其他星球的闪烁影像 
        initBodies();  // 星球    

        getGameScene().setFPSCamera(true);
        getGameScene().setCursorInvisible();  // 设置鼠标指针不可见 
    }

    private void initLight() {
        entityBuilder()
                .at(-5, 0, 0)  // 灯光位置 不设置时为 0 
                .view(new PointLight())
                .buildAndAttach();
    }

    private void initSkybox() {
        var pixels = new ColoredTexture(1024, 1024, Color.RED)
                .pixels()
                .stream()
                .map(p -> p.copy(FXGLMath.randomBoolean(0.02) ? Color.color(1, 1, 1, random(0.0, 1.0)) : Color.BLACK))
                // 很小的几率生成其他颜色 大部分成为黑色 
                .collect(Collectors.toList());

        WritableImage image = (WritableImage) ImagesKt.fromPixels(1024, 1024, pixels);

        var skybox = new SkyboxBuilder(1024)  // 一般游戏中需要一个全局的天空 正方形box  
                .front(image)
                .back(image)
                .left(image)
                .right(image)
                .top(image)
                .bot(image)
                .buildImageSkybox();

        entityBuilder()
                .view(skybox)
                .buildAndAttach();

        var points = new ArrayList<Point2D>();

        for (int i = 0; i < 2000; i++) {
            var x = random(5, 1020);
            var y = random(5, 1020);

            points.add(new Point2D(x, y));
        }

        run(() -> { // 固定间隔时间运行一次 

            points.forEach(p -> {
                var t = getGameTimer().getNow();

                var noise = FXGLMath.noise1D(t * ((p.getX()+p.getY()) / 1024) * 1);

                var c = Color.color(0.67, noise, noise);  // 生成噪点 颜色 

                // image 是 3D box, 修改其中的像素点 
                image.getPixelWriter().setColor((int)p.getX(), (int)p.getY(), c);
                image.getPixelWriter().setColor((int)p.getX()-1, (int)p.getY(), c);
                image.getPixelWriter().setColor((int)p.getX()+2, (int)p.getY(), c);
                image.getPixelWriter().setColor((int)p.getX(), (int)p.getY()-1, c);
                image.getPixelWriter().setColor((int)p.getX(), (int)p.getY()+1, c);
                image.getPixelWriter().setColor((int)p.getX()+1, (int)p.getY()+1, c);
                image.getPixelWriter().setColor((int)p.getX()-1, (int)p.getY()-2, c);
                image.getPixelWriter().setColor((int)p.getX()-2, (int)p.getY()+1, c);
            });

        }, Duration.seconds(0.25));
    }

    private void initBodies() {
        for (CelestialBody body : CelestialBody.values()) {
            spawn(
                "body",
                new SpawnData().put("data", body)  // 在factory 中根据data 设置entity的属性 
            );
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
