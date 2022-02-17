package com.eron.routeplanning.userinterface;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

import com.eron.routeplanning.dataprocessing.StateReport;


public class ShipTableModel extends AbstractTableModel{
	
	private static final long serialVersionUID = -3750702284489171206L;
	//private List<StateReport> statereports = new ArrayList<>();  //存储船舶静态数据
	String[][] nowShip = null;  //显示当前船舶静态数据
	String[] columnName = {  //列名
			"Attribute",
			"Value"
	};
	//>>>>>>>>>>>>>>>>>>>>>>>>>>谨记本次教训，要初始化字符串数组，不然会很麻烦，现在前面null，后面赋值时初始化也可以
	public ShipTableModel() {
		super();
	}
	public ShipTableModel(StateReport statereport) {
		super();
		if (statereport == null) {  //如果空值，就直接返回
			return;
		}
		String[] attribute = {
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
		String[] values = statereport.getAttrArray();
		nowShip = new String[StateReport.attributeNum][2];
		for(int i = 0;i<StateReport.attributeNum;i++){
			nowShip[i][0] = attribute[i];
			nowShip[i][1] = values[i];
		}
	}
	
	//@Override
	public int getColumnCount() {
		// TODO 返回列直木先伐
		return 2;
	}

	//@Override
	public int getRowCount() {
		// TODO 返回行数
		return StateReport.attributeNum;
	}
	
	//@Override
	public Object getValueAt(int rowIndex, int colIndex) {
		// TODO rowinde  colindex
		//return statereports.get(rowIndex).getAttrArray()[colIndex];
		if (nowShip == null) {
			return null;
		}
		return nowShip[rowIndex][colIndex];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return columnName[column];
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		//super.setValueAt(aValue, rowIndex, columnIndex);
		if (nowShip == null) {
			return;
		}
		nowShip[rowIndex][columnIndex] = (String) value;
	}

}
