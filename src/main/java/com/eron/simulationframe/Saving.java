/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.simulationframe;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ERON
 */
public class Saving {
    
    public static void save(List<History> historys, int id){
        File file = new File(""+String.valueOf(id)+".csv");
        if (!file.exists()) {  //如果文件不存在，则创建这个文件
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Saving.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        FileWriter save;
        try {
             save = new FileWriter(file, true);
             for(History history : historys){
                 save.write(history.toString());
                 save.write("\n");
                 save.flush();
             }
             save.close();
        } catch (IOException ex) {
            Logger.getLogger(Saving.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static boolean deleting(int id){
        boolean isOk = false;
        File file = new File(""+String.valueOf(id)+".csv");
        if ( file.exists() ) {
            file.delete();
            isOk = true;
            //System.out.println("deleted file named " + id + ".csv");
        }else{
            isOk = true;
            System.out.println("this file is not exist!!");
        }
        return isOk;
    }
}









