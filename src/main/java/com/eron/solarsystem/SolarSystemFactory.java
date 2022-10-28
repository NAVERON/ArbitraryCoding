/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */

package com.eron.solarsystem;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.scene3d.Torus;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.animationBuilder;
import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class SolarSystemFactory implements EntityFactory {

    @Spawns("body")
    public Entity newBody(SpawnData data) {
        // from km to 3D units
        var sizeScale = 0.00005;
        var distanceScale = 0.00000016;

        CelestialBody bodyData = data.get("data");

        // distance between sun and this body, then offset by radius of each body
        var x = (bodyData.getDistanceFromSun() + CelestialBody.SUN.getRadiusScaled(1.0) + bodyData.getRadiusScaled(1.0)) * distanceScale;

        var r = bodyData.getRadiusScaled(sizeScale);  // 球体半径 

        var mat = new PhongMaterial();
        mat.setDiffuseMap(bodyData.getImage());
        var view = new Sphere(r);
        view.setMaterial(mat);  // 设置球体材质 

        var e = entityBuilder(data)
                .at(x, 0, 0)  // 位置 
                .view(view)
                .with(new CelestialBodyComponent(bodyData))  // 赋予 view一些属性 
                .build();

        if (bodyData == CelestialBody.SATURN) {  // 土星 增加土星环 
            var torus = new Torus(r * 1.5, r * 0.3 / 3.0);  // 环  环半径 和环自身的半径 

            e.getViewComponent().addChild(torus);

            animationBuilder()  // 土星环有一个倾角 
                    .duration(Duration.seconds(0.1))
                    .rotate(torus)  // 旋转 
                    .from(new Point3D(0, 0, 0))
                    .to(new Point3D(45, 0, 0))
                    .buildAndPlay();
        }

        if (bodyData == CelestialBody.SUN) {
            e.getViewComponent().addChild(new AmbientLight(Color.rgb(233, 233, 233, 0.2)));  // 太阳 增加一个环境光  
        }

        return e;
    }
    
    
}








