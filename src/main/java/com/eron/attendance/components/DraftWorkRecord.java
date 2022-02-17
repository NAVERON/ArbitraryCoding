/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.attendance.components;

import com.eron.attendance.MainController;
import com.eron.attendance.user.WorkRecord;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 *
 * @author ERON
 */
public class DraftWorkRecord extends HBox{
    
    private WorkRecord workrecord;
    private MainController controller;
    private Button show_btn = new Button();
    private Button delete_btn = new Button("delete");
    
    public DraftWorkRecord(WorkRecord workrecord, MainController controller){
        this.workrecord = workrecord;
        this.controller = controller;
        
        show_btn.setText(this.workrecord.getId());
        this.getChildren().addAll(show_btn, delete_btn);
        
        show_btn.setOnAction(event -> {
            controller.showContent(this.workrecord);
        });
        delete_btn.setOnAction(event -> {
            this.controller .deleteSelected(this.workrecord, this);
        });
        this.styleProperty().bind(Bindings
                .when(hoverProperty())
                .then(new SimpleStringProperty("-fx-background-color: #9ACD32;"))
                .otherwise(new SimpleStringProperty("-fx-background-color: #DDFFA4;"))
        );
    }
    
    public WorkRecord getRecord() {
        return this.workrecord;
    }
    
}
