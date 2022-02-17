package com.eron.attendance.user;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.concurrent.Task;

public class SubmitToRemote extends Task<WorkRecord> {

    private BlockingQueue<WorkRecord> workrecords = new LinkedBlockingQueue<WorkRecord>();

    @Override
    protected WorkRecord call() throws Exception {
        System.out.println("remote submit");
        return null;
    }

    public SubmitToRemote(WorkRecord workrecord) {
        super();
        this.workrecords.add(workrecord);
    }

    public SubmitToRemote(BlockingQueue<WorkRecord> added) {
        this.workrecords.addAll(added);
    }

}
