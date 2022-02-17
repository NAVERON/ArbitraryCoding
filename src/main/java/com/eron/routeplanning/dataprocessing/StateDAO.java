package com.eron.routeplanning.dataprocessing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class StateDAO {  //应该运用到具体环境中
	
	private final String driver = "com.mysql.cj.jdbc.Driver";
	//如果需要自己创建数据库，开始时必须填写一个存在的数据库
	private final String uri = "jdbc:mysql://127.0.0.1:3306/demo";  //?characterEncoding=UTF-8
	private final String username = "wangyulong";
	private final String password = "xxxxxx";
	
	public int rows = 0;  //数据量
	
	public StateDAO(){
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addStateReport(StateReport statereport){
		//添加位置报告
		String[] dataArray = statereport.getAttrArray();
		String sql = "insert into demo.ship_report values(?,?,?,?,?,?,?,?,?,?,?,?)";  //需要执行的sql语句
		try (
				Connection connection = DriverManager.getConnection(uri, username, password);
				PreparedStatement statement = connection.prepareStatement(sql);
		){
			//statement.setInt(1, Integer.parseInt(dataArray[0]));
			statement.setString(1, dataArray[0]);
			statement.setInt(2, Integer.parseInt(dataArray[1]));
			statement.setString(3, dataArray[2]);
			statement.setString(4, dataArray[3]);
			statement.setString(5, dataArray[4]);
			statement.setString(6, dataArray[5]);
			statement.setInt(7, Integer.parseInt(dataArray[6]));
			statement.setString(8, dataArray[7]);
			statement.setString(9, dataArray[8]);
			statement.setString(10, dataArray[9]);
			statement.setFloat(11, Float.parseFloat(dataArray[10]));
			statement.setString(12, dataArray[11]);
			
			statement.executeUpdate();  //执行
		} catch (SQLException e) {
			// TODO: handle exception
			e.getStackTrace();
		}
	}
	public void addNeedstateReport(List<StateReport> statereports){
		//添加链表数据，一次添加多个
		new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					for(int i=0;i<statereports.size();i++){
						addStateReport(statereports.get(i));
					}
				}
			}
		).start();
	}
	
	public StateReport getStateReport(int messageid){
		//获取位置报告的信息
		StateReport statereport = null;
		String sql = "select * from demo.ship_report where messageid = ?";  //需要执行的sql语句,后面不能加 ';'
		try (
				Connection connection = DriverManager.getConnection(uri, username, password);
				PreparedStatement statement = connection.prepareStatement(sql);
		){
			statement.setInt(1, messageid);
			//返回数据
			ResultSet rs = statement.executeQuery();
			while(rs.next()){
				statereport = new StateReport();
				statereport.setMmsi(rs.getString(1));
				statereport.setAsiversion(rs.getInt(2));
				statereport.setImonumber(rs.getString(3));
				statereport.setCallnumber(rs.getString(4));
				statereport.setName(rs.getString(5));
				statereport.setType(rs.getString(6));
				statereport.setPosaccuracy(rs.getInt(7));
				statereport.setSize(rs.getString(8));
				statereport.setElectype(rs.getString(9));
				statereport.setEta(rs.getString(10));
				statereport.setDraft(rs.getFloat(11));
				statereport.setDesport(rs.getString(12));
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.getStackTrace();
		}
		//取出的数据生成statereport对象
		return statereport;
	}
	
	public List<StateReport> getNeedposreport(){
		//返回所有的位置报告
		List<StateReport> all = new ArrayList<>();
		StateReport statereport = null;
		String sql = "select * from demo.ship_report";  //需要执行的sql语句,后面不能加 ';'
		try (
				Connection connection = DriverManager.getConnection(uri, username, password);
				PreparedStatement statement = connection.prepareStatement(sql);
		){
			//返回数据
			ResultSet rs = statement.executeQuery();
			while(rs.next()){
				statereport = new StateReport();
				statereport.setMmsi(rs.getString(1));
				statereport.setAsiversion(rs.getInt(2));
				statereport.setImonumber(rs.getString(3));
				statereport.setCallnumber(rs.getString(4));
				statereport.setName(rs.getString(5));
				statereport.setType(rs.getString(6));
				statereport.setPosaccuracy(rs.getInt(7));
				statereport.setSize(rs.getString(8));
				statereport.setElectype(rs.getString(9));
				statereport.setEta(rs.getString(10));
				statereport.setDraft(rs.getFloat(11));
				statereport.setDesport(rs.getString(12));
				
				all.add(statereport);
				rows++;
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.getStackTrace();
		}
		return all;
	}
}



