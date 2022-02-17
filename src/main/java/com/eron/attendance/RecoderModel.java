package com.eron.attendance;

import java.util.List;

import com.eron.attendance.dao.WorkRecordDAO;
import com.eron.attendance.user.WorkRecord;

/**
 * 连接控制和数据存储的桥接
 *
 * @author ERON
 */
public class RecoderModel {

    private final WorkRecordDAO workrecordDAO;  //存储接口，传入具体实现接口的类

    public RecoderModel(WorkRecordDAO workrecordDAO) throws Exception {
        this.workrecordDAO = workrecordDAO;
        this.workrecordDAO.setup();
    }

    public void addNewRecord(WorkRecord workRecord){
        this.workrecordDAO.insertWorkrecord(workRecord);
    }
    
    public void deleteRecord(WorkRecord workrecord) {
        this.workrecordDAO.deleteWorkrecord(workrecord);
    }

    public List<WorkRecord> getIsDraft(int isDraft) {  //根据输入   确定是要草稿还是已经提交的历史纪录
        List<WorkRecord> workrecords = workrecordDAO.findWorkrecordisDraft(isDraft);
        if (workrecords.isEmpty()) {
            return null;
        }
        return workrecords;
    }

    public void close() throws Exception {
        workrecordDAO.close();
    }
}







