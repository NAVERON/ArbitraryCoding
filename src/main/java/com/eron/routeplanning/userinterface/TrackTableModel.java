package com.eron.routeplanning.userinterface;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.eron.routeplanning.dataprocessing.PosReport;


public class TrackTableModel extends AbstractTableModel{
	
	private static final long serialVersionUID = -6392498587132523448L;
	
	String[] columnName = {  //列名
			"messageid",
			"mmsi",
			"navstate",
			"rotatespeed",
			"sog",
			"lng",
			"lat",
			"cog",
			"truecourse",
			"timestamp",
			"speindicator",
			"pollution"
	};
	//List<PosReport> posreports = null; //存储位置信息
	String[][] trackRecord = null;
	
	public TrackTableModel() {
		super();
	}
	public TrackTableModel(List<PosReport> posreports) {  //将数据导入模型
		super();
		if (posreports == null) {
			return;
		}
		trackRecord = new String[posreports.size()][PosReport.attributeNum];
		for(int i = 0;i<posreports.size();i++){
			trackRecord[i] = posreports.get(i).getAttrArray();
		}
		
	}
	
	//@Override
	public int getColumnCount() {
		// TODO 返回列数
		//return PosReport.attributeNum;
		return PosReport.attributeNum;
	}

	//@Override
	public int getRowCount() {
		// TODO 返回数据的个数，即行数
		if (trackRecord == null) {
			return 0;
		}
		return trackRecord.length;
	}
	
	//@Override
	public Object getValueAt(int rowIndex, int colIndex) {
		// TODO 返回指定行列的数据
		if (trackRecord == null) {
			return null;
		}
		return trackRecord[rowIndex][colIndex];
	}
	@Override
	public Class<?> getColumnClass(int index) {
		// TODO Auto-generated method stub
		return String.class;
	}
	@Override
	public String getColumnName(int colIndex) {
		// TODO Auto-generated method stub
		return columnName[colIndex];
	}
	@Override
	public void setValueAt(Object value, int rowIndex, int colIndex) {
		// TODO Auto-generated method stub
		if (trackRecord == null) {
			return;
		}
		trackRecord[rowIndex][colIndex] = (String) value;
	}

}
