package com.eron.attendance.dao;

import java.util.List;

import com.eron.attendance.user.WorkRecord;

public interface WorkRecordDAO extends DAO {

    public long insertWorkrecord(WorkRecord workrecord);

    public boolean updateWorkrecord(WorkRecord workrecord);

    public boolean deleteWorkrecord(WorkRecord workrecord);

    public List<WorkRecord> findWorkrecordById(String id);

    public List<WorkRecord> findWorkrecordisDraft(int isDraft);
}
