package com.eron.attendance;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.eron.attendance.dao.DerbyWorkRecordDAO;
import com.eron.attendance.dao.SqliteWorkRecodDAO;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;



public class LogerCard extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/attendance_main.fxml"));
        //RecoderModel model = new RecoderModel(new DerbyWorkRecordDAO());
        RecoderModel model = new RecoderModel(new SqliteWorkRecodDAO());
        MainController maincontroller = new MainController(primaryStage, model);  //设置控制器
        loader.setController(maincontroller);
        
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("LogCard");
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/pics/attendance_amarok.png")));  //这种是相对路径
        primaryStage.show();
        
        Platform.setImplicitExit(false);
        primaryStage.setOnCloseRequest(event -> {
            Platform.setImplicitExit(true);
            try {
                model.close();
            } catch (Exception ex) {
                Alert close_database_fault = new Alert(Alert.AlertType.WARNING);
                close_database_fault.setContentText("数据库关闭异常");
                close_database_fault.show();
                Logger.getLogger(LogerCard.class.getName()).log(Level.SEVERE, null, ex);
            }
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
    
    public static class Launcher {
    	public static void main(String[] args) {
			LogerCard.main(args);
		}
    }

}
