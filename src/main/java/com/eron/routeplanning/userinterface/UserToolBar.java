package com.eron.routeplanning.userinterface;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.eron.routeplanning.customecomponent.DateChooser;
import com.eron.routeplanning.dataprocessing.PosReport;
import com.eron.routeplanning.dataprocessing.StateReport;

public class UserToolBar extends JPanel{
	
	private static final long serialVersionUID = -4359234720771002287L;
	//数据内容
	public JTable shipInfo = null,
			listTable = null;        //引用table便于操纵表格变换
	public DrawMap drawing = null;
	
	private String[] names = null;  //名字选择框  -》 中内容
	private String[] mmsis = null;  //mmsi选择框 -》 中的选项
	private List<StateReport> statereports = null;
	private List<PosReport> posreports = null;
	private StateReport selectState = null;
	private List<PosReport> selectPos = null;
	//从输入框中获取的数据------------------------------------------------>
	//private String name = null;
	private String mmsi = null;
	private Date startTime = null;
	private Date endTime = null;
	//面板组件 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	private JComboBox<String> shipName = new JComboBox<String>();
	private JComboBox<String> shipMmsi = new JComboBox<String>();
	private JTextField startDate = new JTextField();
	private JTextField endDate = new JTextField();
	private final JButton okBtn = new JButton("OK");
	private final JLabel lblName = new JLabel("Name");
	private final JLabel lblMmsi = new JLabel("Mmsi");
	private final JButton allBtn = new JButton("ShowAll");
	
	public UserToolBar(final List<StateReport> statereports, final List<PosReport> posreports) {
		//System.out.println(this.getLocation());
		this.statereports = statereports;
		this.posreports = posreports;
		names = new String[statereports.size()];
		mmsis = new String[statereports.size()];
		int k=0;
		for(int i=0;i<statereports.size();i++){   //添加选项
			StateReport temp = statereports.get(i);
			mmsis[i] = temp.getMmsi();
			int j=0;
			for(;j<names.length;j++){
				if (temp.getName().equals(names[j])) {
					break;
				}
			}
			if (j==names.length) {
				names[k++] = temp.getName();
			}
		}
		
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		lblName.setFont(new Font("SansSerif", Font.PLAIN, 16));
		add(lblName);
		shipName.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.DESELECTED) {  //这个不加也行
					String tName = (String) shipName.getSelectedItem();
					shipMmsi.removeAllItems();
					for(int i=0;i<statereports.size();i++){
						StateReport tstate = statereports.get(i);
						if (tstate.getName().equalsIgnoreCase(tName)) {
							shipMmsi.addItem(tstate.getMmsi());
						}
					}
				}
			}
		});
		
		shipName.setMaximumRowCount(100);
		add(shipName);
		
		for(int i=0;i<names.length;i++){
			shipName.addItem(names[i]);
		}
		lblMmsi.setFont(new Font("SansSerif", Font.PLAIN, 16));
		add(lblMmsi);
		
		shipMmsi.setMaximumRowCount(100);
		add(shipMmsi);
		
		for(int j=0;j<mmsis.length;j++){
			shipMmsi.addItem(mmsis[j]);
		}
		startDate.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyChar() == KeyEvent.VK_ENTER) {
					endDate.requestFocus();
				}
			}
		});
		final DateChooser kaishi = new DateChooser(this);
		add(kaishi);
		/*startDate.setColumns(20);
		add(startDate);*/
		final DateChooser jieshu = new DateChooser(this);
		add(jieshu);
		/*endDate.setColumns(20);
		add(endDate);*/
		
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//数据提取,处理异常情况
				/*if (startDate.getText().isEmpty() || endDate.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "INPUT TIME REGION", "ERROR", JOptionPane.ERROR_MESSAGE);
					return;
				}*/
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				try {
					//startTime = sdf.parse(startDate.getText().trim());
					//endTime = sdf.parse(endDate.getText().trim());
					
					startTime = sdf.parse(kaishi.getDateField().getText().trim());
					endTime = sdf.parse(jieshu.getDateField().getText().trim());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//System.out.println(startTime+"\n"+endTime);
				mmsi = (String) shipMmsi.getSelectedItem();
				selectPos = getselectPos(mmsi, startTime, endTime);  //将需要的值加入到selectPos里
				selectState = getState(mmsi);
				//执行选择参数后的动作
				//System.out.println(selectState.toString());
				//新建一个数据列表，然后将数据列表传入表格数据模型中，更改信息
				shipInfo.setModel(new ShipTableModel(selectState));
				listTable.setModel(new TrackTableModel(selectPos));
				
				drawing.getPosreports(selectPos);
				drawing.addNodes();
				drawing.repaint();
				
				//将输入框恢复
				shipName.removeAllItems();
				shipMmsi.removeAllItems();
				for(int i=0;i<names.length;i++){
					shipName.addItem(names[i]);
				}
				for(int j=0;j<mmsis.length;j++){
					shipMmsi.addItem(mmsis[j]);
				}
			}
		});
		add(okBtn);
		allBtn.addActionListener(new ActionListener() {  //显示所有数据
			public void actionPerformed(ActionEvent e) {
				shipInfo.setModel(new AllShipModel(statereports));
				listTable.setModel(new TrackTableModel(posreports));
				
				drawing.getPosreports(posreports);
				drawing.addNodes();
				drawing.repaint();
				
				//将输入框恢复
				shipName.removeAllItems();
				shipMmsi.removeAllItems();
				for(int i=0;i<names.length;i++){
					shipName.addItem(names[i]);
				}
				for(int j=0;j<mmsis.length;j++){
					shipMmsi.addItem(mmsis[j]);
				}
			}
		});
		
		add(allBtn);
		
	}
	
	public void pullRefer(JTable shipInfo, JTable listTable, DrawMap drawing){   //传入外界的引用，改变界面数据
		this.shipInfo = shipInfo;
		this.listTable = listTable;
		this.drawing = drawing;
	}
	
	public StateReport getState(String mmsi){  //返回选择的船舶静态信息
		StateReport temp = null;
		for(int i=0;i<statereports.size();i++){
			temp = statereports.get(i);
			if (temp.getMmsi().equalsIgnoreCase(mmsi)) {
				return temp;
			}
		}
		return temp;
	}
	
	public List<PosReport> getselectPos(String mmsi, Date start, Date end){  //根据日期从数据筛选
		List<PosReport> selectPos = new ArrayList<PosReport>();
		PosReport temp = null;
		for(int i=0;i<posreports.size();i++){
			temp = posreports.get(i);
			if (temp.getMmsi().equalsIgnoreCase(mmsi) && temp.getTimestamp().after(start) && temp.getTimestamp().before(end)) {
				selectPos.add(temp);
			}
		}
		//System.out.println("selectPos has-->"+selectPos.size());
		return selectPos;
	}
}
