/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.crawljobs;

import java.time.LocalDate;

/**
 *
 * @author ERON
 */
public class Notice implements Comparable<Notice>{
    
    public String title;
    public String content;
    public LocalDate date;
    public String superlink;
    
    public Notice(){}
    public Notice(String title, String content, LocalDate date, String superlink){
        this.title = title;
        this.content = content;
        this.date = date;
        this.superlink = superlink;
        
    }

    @Override
    public int compareTo(Notice other) {
        if(this.date.isBefore(other.date)){
            return 1;
        }else if(this.date.isAfter(other.date)){
            return -1;
        }
        return 0;
    }
}





