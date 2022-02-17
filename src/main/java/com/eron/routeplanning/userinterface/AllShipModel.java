package com.eron.routeplanning.userinterface;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import com.eron.routeplanning.dataprocessing.StateReport;


public class AllShipModel extends AbstractTableModel{  //这个列表显示所有船舶信息
	
	private static final long serialVersionUID = 4791730977815640500L;
	private String[] columnName = {  //列名
			"mmsi",
			"asiversion",
			"imonumber",
			"callnumber",
			"name",
			"type",
			"posaccuracy",
			"size",
			"electype",
			"eta",
			"draft",
			"desport"
	};
	private String[][] shipState = null;  //存储所有数据
	
	public AllShipModel(List<StateReport> statereports) {
		// TODO Auto-generated constructor stub
		shipState = new String[statereports.size()][StateReport.attributeNum];
		for(int i=0;i<statereports.size();i++){
			shipState[i] = statereports.get(i).getAttrArray();
		}
	}

	//@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return StateReport.attributeNum;
	}

	//@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return shipState.length;
	}

	//@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return shipState[rowIndex][columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int arg0) {
		// TODO Auto-generated method stub
		//return super.getColumnClass(arg0);
		return String.class;
	}

	@Override
	public String getColumnName(int index) {
		// TODO Auto-generated method stub
		//return super.getColumnName(arg0);
		return columnName[index];
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int colIndex) {
		// TODO Auto-generated method stub
		//super.setValueAt(arg0, arg1, arg2);
		shipState[rowIndex][colIndex] = (String) value;
	}

}
