package com.eron.crawljobs;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LogToWeb extends Application {
    
    public static String PROJECT_NAME = "武汉理工通知搜索小工具";
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader( getClass().getResource("/fxml/crawljobs_search.fxml") );
        SearchController searchcontroller = new SearchController(primaryStage);
        loader.setController(searchcontroller);
        
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle(LogToWeb.PROJECT_NAME);
        primaryStage.show();
        
        primaryStage.setOnCloseRequest((event) -> {
            Platform.exit();
            System.exit(0);
        });
    }
    
    public static void main(String[] args) {
        Application.launch(args);
    }
    
    public static class Launcher {
    	public static void main(String[] args) {
			LogToWeb.main(args);
		}
    }
    
}
