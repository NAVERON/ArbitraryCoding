/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.attendance.dao;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.eron.attendance.user.WorkRecord;

public class SqliteWorkRecodDAO implements WorkRecordDAO{

    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;

    private String protocol = "jdbc:sqlite:";
    private String dbName = "src/main/resources/sqlite/literecords.db";
    private String createString = "CREATE TABLE workrecords ("
            + "id TEXT,"
            + "owner TEXT,"
            + "work_name TEXT,"
            + "system_name TEXT,"
            + "work_acount REAL,"
            + "work_content TEXT,"
            + "record_time TEXT,"
            + "isDraft INTEGER"
            + ");";

    @Override
    public void setup() {
        try {
            connect();
            DatabaseMetaData dbmd = connection.getMetaData();
            ResultSet rs = dbmd.getTables(null, null, null, new String[]{"TABLE"});
            if (!rs.next()) {
                statement = connection.createStatement();
                statement.setQueryTimeout(5);
                statement.executeUpdate(createString);
                close();
                
//                Alert create_table_alert = new Alert(Alert.AlertType.INFORMATION);
//                create_table_alert.setContentText("数据库正在创建表");
//                create_table_alert.show();
            } else {
//                Alert table_exist_alert = new Alert(Alert.AlertType.INFORMATION);
//                table_exist_alert.setContentText("数据库表已经存在");
//                table_exist_alert.show();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DerbyWorkRecordDAO.class.getName()).log(Level.SEVERE, null, ex);
//            Alert create_table_error = new Alert(Alert.AlertType.INFORMATION);
//            create_table_error.setContentText("数据库表已经存在");
//            create_table_error.show();
        }
    }

    @Override
    public void connect() {
        try {
            connection = DriverManager.getConnection(protocol + dbName);
        } catch (SQLException ex) {
            Logger.getLogger(DerbyWorkRecordDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void close() {
        try {
            if (statement != null) {
                statement.close();
                statement = null;
            }
            if(preparedStatement != null){
                preparedStatement.close();
                preparedStatement = null;
            }
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DerbyWorkRecordDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public long insertWorkrecord(WorkRecord workrecord) {
        try {
            long acount = 0L;
            connect();

            String insert = "INSERT INTO workrecords (id, owner, work_name, system_name, work_acount, work_content, record_time, isDraft) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            preparedStatement = connection.prepareStatement(insert);
            preparedStatement.setString(1, workrecord.getId());
            preparedStatement.setString(2, workrecord.getOwner());
            preparedStatement.setString(3, workrecord.getWork_name());
            preparedStatement.setString(4, workrecord.getSystem_name());
            preparedStatement.setDouble(5, workrecord.getWork_acount());
            preparedStatement.setString(6, workrecord.getWork_content());
            preparedStatement.setString(7, workrecord.getRecord_time());
            preparedStatement.setInt(8, workrecord.isDraft);
            acount = preparedStatement.executeUpdate();
            
            //connection.commit();
            close();
            return acount;
        } catch (SQLException ex) {
            Logger.getLogger(DerbyWorkRecordDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1L;
    }

    @Override
    public boolean updateWorkrecord(WorkRecord workrecord) {
        try {
            connect();
            String update = "UPDATE workrecords SET "
                    + "owner = ?, "
                    + "work_name = ?, "
                    + "system_name = ?, "
                    + "work_acount = ?, "
                    + "work_content = ?, "
                    + "record_time = ?, "
                    + "isDraft = ? "
                    + "WHERE id = ?;";
            preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, workrecord.getOwner());
            preparedStatement.setString(2, workrecord.getWork_name());
            preparedStatement.setString(3, workrecord.getSystem_name());
            preparedStatement.setDouble(4, workrecord.getWork_acount());
            preparedStatement.setString(5, workrecord.getWork_content());
            preparedStatement.setString(6, workrecord.getRecord_time());
            preparedStatement.setInt(7, workrecord.isDraft);
            preparedStatement.setString(8, workrecord.getId());
            preparedStatement.executeUpdate();
            
            //connection.commit();
            close();
        } catch (SQLException ex) {
            Logger.getLogger(DerbyWorkRecordDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean deleteWorkrecord(WorkRecord workrecord) {
        try {
            connect();
            String delete = "DELETE FROM workrecords WHERE id = ?;";
            preparedStatement = connection.prepareStatement(delete);
            preparedStatement.setString(1, workrecord.getId());
            preparedStatement.executeUpdate();
            
            //connection.commit();
            close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DerbyWorkRecordDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<WorkRecord> findWorkrecordById(String id) {
        List<WorkRecord> list = new LinkedList<>();
        try {
            connect();

            String findById = "SELECT * FROM workrecords WHERE id = ?;";
            preparedStatement = connection.prepareStatement(findById);
            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date now = sdf.parse(rs.getString("record_time"));
                WorkRecord workrecord = new WorkRecord(
                        rs.getString("owner"),
                        rs.getString("work_name"),
                        rs.getString("system_name"),
                        rs.getDouble("work_acount"),
                        rs.getString("work_content"),
                        now,
                        rs.getInt("isDraft")
                );
                list.add(workrecord);
            }
            //connection.commit();
            close();
        } catch (SQLException ex) {
            Logger.getLogger(DerbyWorkRecordDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(SqliteWorkRecodDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public List<WorkRecord> findWorkrecordisDraft(int isDraft) {
        List<WorkRecord> list = new LinkedList<>();
        try {
            connect();
            String findByisDraft = "SELECT * FROM workrecords WHERE isDraft = ?;";
            preparedStatement = connection.prepareStatement(findByisDraft);
            preparedStatement.setInt(1, isDraft);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date now = sdf.parse(rs.getString("record_time"));
                WorkRecord workrecord = new WorkRecord(
                        rs.getString("owner"),
                        rs.getString("work_name"),
                        rs.getString("system_name"),
                        rs.getDouble("work_acount"),
                        rs.getString("work_content"),
                        now,
                        rs.getInt("isDraft")
                );
                list.add(workrecord);
            }
            //connection.commit();
            close();
        } catch (SQLException ex) {
            Logger.getLogger(DerbyWorkRecordDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(SqliteWorkRecodDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    
}



