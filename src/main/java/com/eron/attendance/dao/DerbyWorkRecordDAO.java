package com.eron.attendance.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.eron.attendance.user.WorkRecord;

public class DerbyWorkRecordDAO implements WorkRecordDAO {  //不同的数据库，通过实现可以做成不同数据库实现相同的功能

    private Connection connection = null;
    private QueryRunner dbAccess = new QueryRunner();
    private List<WorkRecord> RECORDS = new LinkedList<WorkRecord>();

    private String protocol = "jdbc:derby:";
    private String dbName = "src/main/resources/db/derby_records";
    private String createString = "CREATE TABLE workrecords ("
            + "id VARCHAR(30),"
            + "owner VARCHAR(30),"
            + "work_name VARCHAR(100),"
            + "system_name VARCHAR(100),"
            + "work_acount DOUBLE,"
            + "work_content VARCHAR(500),"
            + "record_time VARCHAR(30),"
            + "isDraft INT"
            + ")";

    /**
     * derby加载内嵌的问题无法解决，暂时搁置 
     */
    @Override
    public void setup() throws InstantiationException {
        try {
        	//Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        	System.out.println("正在获取链接");
            connection = DriverManager.getConnection(protocol + dbName + "; create=true");
            DatabaseMetaData dbmd = connection.getMetaData();
            System.out.println("获取metadata : " + dbmd.toString());
            ResultSet rs = dbmd.getTables(null, null, null, new String[]{"TABLE"});
            if (!rs.next()) {
                Alert close_database_fault = new Alert(Alert.AlertType.INFORMATION);
                close_database_fault.setContentText("数据库正在创建表");
                close_database_fault.showAndWait();
                dbAccess.update(connection, createString);
            } else {
                Alert database_exist_alert = new Alert(Alert.AlertType.INFORMATION);
                database_exist_alert.setContentText("表已经创建");
                database_exist_alert.showAndWait();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DerbyWorkRecordDAO.class.getName()).log(Level.SEVERE, null, ex);
            
            Alert create_database_fault = new Alert(Alert.AlertType.WARNING);
            create_database_fault.setContentText("数据库创建失败");
            create_database_fault.showAndWait();
        }
    }

    @Override
    public void connect() {
        try {
            connection = DriverManager.getConnection(protocol + dbName);
        } catch (SQLException ex) {
            Logger.getLogger(DerbyWorkRecordDAO.class.getName()).log(Level.SEVERE, null, ex);
            
            Alert connect_database_fault = new Alert(Alert.AlertType.WARNING);
            connect_database_fault.setContentText("数据库连接失败");
            connect_database_fault.show();
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
            DriverManager.getConnection(protocol + ";shutdown=true");
        } catch (SQLException ex) {
            Logger.getLogger(DerbyWorkRecordDAO.class.getName()).log(Level.SEVERE, null, ex);

            Alert close_database_fault = new Alert(Alert.AlertType.INFORMATION);
            close_database_fault.setContentText("数据库正在关闭");
            close_database_fault.show();
        }
    }

    @Override
    public long insertWorkrecord(WorkRecord workrecord) {
        try {
            connection = DriverManager.getConnection(protocol + dbName);
            BigDecimal id = dbAccess.insert(connection,
                    "INSERT INTO workrecords (id, owner, work_name, system_name, work_acount, work_content, record_time, isDraft) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    new ScalarHandler<BigDecimal>(),
                    workrecord.getId(),
                    workrecord.getOwner(),
                    workrecord.getWork_name(),
                    workrecord.getSystem_name(),
                    workrecord.getWork_acount(),
                    workrecord.getWork_content(),
                    workrecord.getRecord_time(),
                    workrecord.isDraft
            );// .longValue();
            return id.longValue();
        } catch (SQLException ex) {
            Logger.getLogger(DerbyWorkRecordDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1L;
    }

    @Override
    public boolean updateWorkrecord(WorkRecord workrecord) {
        try {
            connection = DriverManager.getConnection(protocol + dbName);
            dbAccess.update(connection,
                    "UPDATE workrecords SET "
                    + "owner = ?, "
                    + "work_name = ?, "
                    + "system_name = ?, "
                    + "work_acount = ?, "
                    + "work_content = ?, "
                    + "record_time = ?, "
                    + "isDraft = ? "
                    + "WHERE id = ?",
                    workrecord.getOwner(),
                    workrecord.getWork_name(),
                    workrecord.getSystem_name(),
                    workrecord.getWork_acount(),
                    workrecord.getWork_content(),
                    workrecord.getRecord_time(),
                    workrecord.isDraft,
                    workrecord.getId()
            );
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DerbyWorkRecordDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean deleteWorkrecord(WorkRecord workrecord) {
        try {
            connection = DriverManager.getConnection(protocol + dbName);
            dbAccess.update(connection, "DELETE FROM workrecords WHERE id = ?", workrecord.getId());
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DerbyWorkRecordDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<WorkRecord> findWorkrecordById(String id) {
        try {
            connection = DriverManager.getConnection(protocol + dbName);
            RECORDS = dbAccess.query(connection, "SELECT * FROM workrecords WHERE id = ?",
                    new BeanListHandler<WorkRecord>(WorkRecord.class),
                    id);
        } catch (SQLException ex) {
            Logger.getLogger(DerbyWorkRecordDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return RECORDS;
    }
    
    @Override
    public List<WorkRecord> findWorkrecordisDraft(int isDraft) {
        
        try {
            connection = DriverManager.getConnection(protocol + dbName);
            RECORDS =  dbAccess.query(connection, "SELECT * FROM workrecords WHERE isDraft = ?",
                    new BeanListHandler<WorkRecord>(WorkRecord.class),
                    isDraft);
        } catch (SQLException ex) {
            Logger.getLogger(DerbyWorkRecordDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return RECORDS;
    }

}
