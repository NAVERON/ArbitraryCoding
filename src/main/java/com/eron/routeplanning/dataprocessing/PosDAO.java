package com.eron.routeplanning.dataprocessing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PosDAO {  //这个存储数据库不能重复运行，所以到时候应该注释掉
	
	private final String driver = "com.mysql.cj.jdbc.Driver";
	//如果需要自己创建数据库，开始时必须填写一个存在的数据库
	private final String uri = "jdbc:mysql://127.0.0.1:3306/demo";  //?characterEncoding=UTF-8
	private final String username = "wangyulong";
	private final String password = "xxxxxx";
	
	public int rows = 0;  //数据的数量
	public Connection connection = null;
	
	public PosDAO() {  //这样就必须初始化一下，不然没有加载驱动类
		// TODO 加载驱动类，更换数据库时更改此处
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(uri, username, password); 
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addPosReport(PosReport posreport){System.out.println("i'm here posdao add===");
		//添加位置报告
		String[] dataArray = posreport.getAttrArray();  //得到属性值得数组
		String sql = "insert into demo.position_report values(?,?,?,?,?,?,?,?,?,?,?,?)";
		try (
				PreparedStatement statement = connection.prepareStatement(sql);
		){
			statement.setInt(1, Integer.parseInt(dataArray[0]));
			statement.setString(2, dataArray[1]);
			statement.setString(3, dataArray[2]);
			statement.setFloat(4, Float.parseFloat(dataArray[3]));
			statement.setFloat(5, Float.parseFloat(dataArray[4]));
			statement.setDouble(6, Double.parseDouble(dataArray[5]));
			statement.setDouble(7, Double.parseDouble(dataArray[6]));
			statement.setFloat(8, Float.parseFloat(dataArray[7]));
			statement.setFloat(9, Float.parseFloat(dataArray[8]));
			/*//date类型真麻烦。。。
			java.util.Date utilDate = null;
			try {
				utilDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataArray[8]);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			statement.setDate(9, sqlDate);  //new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataArray[8]))
			*/
			statement.setString(10, dataArray[9]);
			statement.setString(11, dataArray[10]);
			statement.setInt(12, Integer.parseInt(dataArray[11]));
			
			statement.executeUpdate();  //执行
		} catch (SQLException e) {
			// TODO: handle exception
			e.getStackTrace();
		}
	}
	public void addNeedposReport(final List<PosReport> posreports){
		//添加一个链表到数据库
		new Thread(new Runnable() {
				//@Override
				public void run() {System.out.println("i'm runnable begin posdao");
					// TODO Auto-generated method stub
					for(int i=0;i<posreports.size();i++){
						addPosReport(posreports.get(i));
					}System.out.println("i'm runnable out posdao");
				}
			}
		).start();
	}
	
	public PosReport getPosReport(String mmsi){
		//获取位置报告的信息
		PosReport posreport = null;
		String sql = "select * from demo.position_report where mmsi=?";
		try (
				PreparedStatement statement = connection.prepareStatement(sql);
		){
			statement.setString(1, mmsi);
			//查询结果
			ResultSet rs = statement.executeQuery();
			while(rs.next()){
				posreport = new PosReport();
				posreport.setMessageid(rs.getInt(1));
				posreport.setMmsi(rs.getString(2));
				posreport.setNavstate(rs.getString(3));
				posreport.setRotatespeed(rs.getFloat(4));
				posreport.setSog(rs.getFloat(5));
				posreport.setLng(rs.getDouble(6));
				posreport.setLat(rs.getDouble(7));
				posreport.setCog(rs.getFloat(8));
				posreport.setTruecourse(rs.getFloat(9));
				posreport.setTimestamp(rs.getString(10));
				posreport.setSpeindicator(rs.getString(11));
				posreport.setPollution(rs.getInt(12));
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.getStackTrace();
		}
		
		return posreport;
	}
	
	public List<PosReport> getNeedposReport(){
		//返回所有的位置报告
		List<PosReport> all = new ArrayList<>();
		PosReport posreport = null;
		String sql = "select * from demo.position_report";
		try (
				Connection connection = DriverManager.getConnection(uri, username, password);
				PreparedStatement statement = connection.prepareStatement(sql);
		){
			//查询结果
			ResultSet rs = statement.executeQuery();
			while(rs.next()){
				posreport = new PosReport();
				posreport.setMessageid(rs.getInt(1));
				posreport.setMmsi(rs.getString(2));
				posreport.setNavstate(rs.getString(3));
				posreport.setRotatespeed(rs.getFloat(4));
				posreport.setSog(rs.getFloat(5));
				posreport.setLng(rs.getDouble(6));
				posreport.setLat(rs.getDouble(7));
				posreport.setCog(rs.getFloat(8));
				posreport.setTruecourse(rs.getFloat(9));
				posreport.setTimestamp(rs.getString(10));
				posreport.setSpeindicator(rs.getString(11));
				posreport.setPollution(rs.getInt(12));
				
				all.add(posreport);
				rows++;
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.getStackTrace();
		}
		return all;
	}
}






