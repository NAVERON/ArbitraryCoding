package com.eron.attendance.dao;

public interface DAO {

    public void setup() throws Exception;

    public void connect() throws Exception;

    public void close() throws Exception;
}
