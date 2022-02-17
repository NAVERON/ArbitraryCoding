package com.eron.attendance.user;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkRecord {

    private String id;
    private String owner; // 此条记录属于哪个用户？用户名称

    private String work_name;
    private String system_name;
    private double work_acount;
    private String work_content;
    private String record_time;

    public int isDraft;   //是不是草稿，1 是草稿， 0 不是

    public WorkRecord(String owner, String work_name, String system_name, double work_acount, String work_content, Date record_time, int isDraft) {
        this.owner = owner;
        this.work_name = work_name;
        this.system_name = system_name;
        this.work_acount = work_acount;
        this.work_content = work_content;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.record_time = sdf.format(record_time);
        this.isDraft = isDraft;

        setId();  //根据其他参数设置本日志的id
    }

    public String getId() {
        return this.id;
    }

    @Override
    public int hashCode() {  //生成id ?  ---- owner, work_name and record_time
        final int prime = 31;
        int result = 1;
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        result = prime * result + ((work_name == null) ? 0 : work_name.hashCode());
        result = prime * result + record_time.hashCode() + isDraft;
        return result;
    }

    public void setId() {  //生成唯一识别码
        if (owner != null && work_name != null && record_time != null) {
            this.id = String.valueOf(this.hashCode());
        }
    }

    public String getWork_name() {
        return work_name;
    }

    public void setWork_name(String work_name) {
        this.work_name = work_name;
    }

    public String getSystem_name() {
        return system_name;
    }

    public void setSystem_name(String system_name) {
        this.system_name = system_name;
    }

    public double getWork_acount() {
        return work_acount;
    }

    public void setWork_acount(double work_acount) {
        this.work_acount = work_acount;
    }

    public String getWork_content() {
        return work_content;
    }

    public void setWork_content(String work_content) {
        this.work_content = work_content;
    }

    public String getRecord_time() {
        return record_time;
    }

    public void setRecord_time(Date record_time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
        this.record_time = sdf.format(record_time);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object obj) {  //id号是唯一的识别标志
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        WorkRecord other = (WorkRecord) obj;
        if (this.id == null) {
            if (other.getId() != null) {
                return false;
            }
        } else if (!this.id.equals(other.getId())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {  //测试，并没有校验功能
        return getId() +", "+ getOwner() +", "+ getWork_name()+", "+ getSystem_name() +", "+ getWork_acount() +", "+ getWork_content() + ", "+getRecord_time() +", "+isDraft+"\n";
    }

    public void trueStruct() {  //需要从这里提炼出来数据
        //真正上传的数据结构
    }

}




