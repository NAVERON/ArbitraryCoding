package com.eron.attendance.user;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.concurrent.Task;

/**
 * 异步远程提交数据 
 * @author eron
 *
 */
public class SubmitToRemote extends Task<WorkRecord> {

    private BlockingQueue<WorkRecord> workrecords = new LinkedBlockingQueue<WorkRecord>();
    
    public SubmitToRemote(WorkRecord workrecord) {
        super();
        this.workrecords.add(workrecord);
    }

    public SubmitToRemote(BlockingQueue<WorkRecord> added) {
        this.workrecords.addAll(added);
    }

    @Override
    protected WorkRecord call() throws Exception {
        System.out.println("remote submit");
        return null;
    }


}








