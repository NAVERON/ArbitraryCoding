package com.eron.snips;


import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * 路径显示的简单案例学习
 * @author eron
 *
 */
public class PathVisualization extends Application {

    private static double SCENE_WIDTH = 400;
    private static double SCENE_HEIGHT = 260;

    Canvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Pane root = new Pane();
        Path path = createPath();
        canvas = new Canvas(SCENE_WIDTH,SCENE_HEIGHT);

        root.getChildren().addAll(path, canvas);

        primaryStage.setScene(new Scene(root, SCENE_WIDTH, SCENE_HEIGHT));
        primaryStage.show();

        Animation animation = createPathAnimation(path, Duration.seconds(5));
        animation.play();
    }

    private Path createPath() {

        Path path = new Path();

        path.setStroke(Color.RED);
        path.setStrokeWidth(10);

        path.getElements().addAll(new MoveTo(20, 20), new CubicCurveTo(380, 0, 380, 120, 200, 120), new CubicCurveTo(0, 120, 0, 240, 380, 240), new LineTo(20,20));

        return path;
    }

    private Animation createPathAnimation(Path path, Duration duration) {

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // move a node along a path. we want its position
        Circle pen = new Circle(0, 0, 4);

        // create path transition
        PathTransition pathTransition = new PathTransition( duration, path, pen);
        pathTransition.currentTimeProperty().addListener( new ChangeListener<Duration>() {

            Location oldLocation = null;

            /**
             * Draw a line from the old location to the new location
             */
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {

                // skip starting at 0/0
                if( oldValue == Duration.ZERO)
                    return;

                // get current location
                double x = pen.getTranslateX();
                double y = pen.getTranslateY();

                // initialize the location
                if( oldLocation == null) {
                    oldLocation = new Location();
                    oldLocation.x = x;
                    oldLocation.y = y;
                    return;
                }

                // draw line
                gc.setStroke(Color.BLUE);
                gc.setFill(Color.YELLOW);
                gc.setLineWidth(4);
                gc.strokeLine(oldLocation.x, oldLocation.y, x, y);

                // update old location with current one
                oldLocation.x = x;
                oldLocation.y = y;
            }
        });

        return pathTransition;
    }

    public static class Location {
        double x;
        double y;
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public static class Launcher{
    	public static void main(String[] args) {
			PathVisualization.main(args);
		}
    }
}





