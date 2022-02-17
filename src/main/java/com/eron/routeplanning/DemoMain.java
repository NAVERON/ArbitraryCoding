package com.eron.routeplanning;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import com.eron.routeplanning.dataprocessing.AccessData;
import com.eron.routeplanning.dataprocessing.PosDAO;
import com.eron.routeplanning.dataprocessing.PosReport;
import com.eron.routeplanning.dataprocessing.StateDAO;
import com.eron.routeplanning.dataprocessing.StateReport;
import com.eron.routeplanning.userinterface.AllShipModel;
import com.eron.routeplanning.userinterface.DrawMap;
import com.eron.routeplanning.userinterface.TrackTableModel;
import com.eron.routeplanning.userinterface.UserToolBar;

@SuppressWarnings("serial")
public class DemoMain extends JFrame {

	List<StateReport> statereports = null;
	List<PosReport> posreports = null;

	public DemoMain() {
		super();
	}

	public DemoMain(List<StateReport> statereports, List<PosReport> posreports) {
		this.statereports = statereports;
		this.posreports = posreports;

		initialInterface(); // 放置界面组件
		// pack();
		setVisible(true);
	}

	public static void main(String[] args) {
		// 从外界获取数据
		AccessData access = new AccessData();
		// 从文件中/或者其他网络资源中获取数据
		final List<StateReport> statereports = access.getStateReport();
		final List<PosReport> posreports = access.getPosReport();
		if (statereports.isEmpty() || posreports.isEmpty()) {
			JOptionPane.showMessageDialog(null, "NO DATA", "File Warning", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		// 已经将数据按每行 存储到了data数组中，可以通过access.getdata()获得
		// 可以将存储数据库的动作新建线程完成
		StateDAO statedao = new StateDAO();
		PosDAO posdao = new PosDAO();
		// statedao.addNeedstateReport(statereports);
		// posdao.addNeedposReport(posreports);

		// List<StateReport> getstateFromDB = statedao.getNeedposreport();
		// List<PosReport> getposFromDB = posdao.getNeedposReport();
		// 最后显示界面,先导入数据
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				DemoMain demoMain = new DemoMain(statereports, posreports);
			}
		});
	}

	private void initialInterface() {
		setTitle("DataProcessClient");
		setBounds(50, 50, 1200, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/*
		 * try {
		 * UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		 * } catch (ClassNotFoundException | InstantiationException |
		 * IllegalAccessException | UnsupportedLookAndFeelException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		getContentPane().setLayout(new BorderLayout(0, 0));
		/*********************** 顶部菜单选择 ****************************/
		// 顶部选择栏
		UserToolBar top = new UserToolBar(statereports, posreports);
		getContentPane().add(top, BorderLayout.NORTH);
		/************************* 左边相应的船舶信息 ************************************/
		// 左边船舶信息栏
		// ShipTableModel infoData = new ShipTableModel(statereports.get(1));
		// //数据需要通过这个继承类传入，并更新表格
		AllShipModel allships = new AllShipModel(statereports);
		JTable shipInfo = new JTable(allships);
		// 调整表格的列宽
		// FitTableColumns(shipInfo);
		JScrollPane shipScroll = new JScrollPane(shipInfo);
		getContentPane().add(shipScroll, BorderLayout.WEST);
		/***************************
		 * 右边相应的显示面板，一个表格，一个图画
		 *************************************/
		// 右边绘图栏，分为两个形式，一个是表格，一个是画图
		final DrawMap drawing = new DrawMap(); // 绘画面板>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		drawing.getPosreports(null); // 导入数据
		drawing.addNodes(); // 添加组件显示
		// 路径轨迹点---表格>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		TrackTableModel trackData = new TrackTableModel(null);
		final JTable listTable = new JTable(trackData);
		JScrollPane tracksTable = new JScrollPane(listTable);
		// 标签化窗口
		JTabbedPane details = new JTabbedPane();
		// 添加标题
		details.add("drawing", drawing);
		details.add("tracks", tracksTable);
		// 这里需要添加usertoolbar对两个表格的操纵
		getContentPane().add(details, BorderLayout.CENTER);

		top.pullRefer(shipInfo, listTable, drawing);

		shipInfo.addMouseListener(new MouseAdapter() {
			// 这里有一点问题，当在船舶静态信息界面时，会出现越界的情况,所以应当在表格内部实现点击的侦听
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);
				int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint());
				String getMmsi = (String) new AllShipModel(statereports).getValueAt(row, 0); // 界面层叠问题导致索引越界

				List<PosReport> selected = new ArrayList<PosReport>();
				for (int i = 0; i < posreports.size(); i++) {
					PosReport tPosReport = posreports.get(i);
					if (tPosReport.getMmsi().equals(getMmsi)) {
						selected.add(tPosReport);
					}
				}

				listTable.setModel(new TrackTableModel(selected));
				drawing.getPosreports(selected);
				drawing.addNodes();
				drawing.repaint();
			}

		});
	}

	public void FitTableColumns(JTable myTable) { // 令表格大小适应内容
		JTableHeader header = myTable.getTableHeader();
		int rowCount = myTable.getRowCount();
		Enumeration columns = myTable.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn column = (TableColumn) columns.nextElement();
			int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
			int width = (int) myTable.getTableHeader().getDefaultRenderer()
					.getTableCellRendererComponent(myTable, column.getIdentifier(), false, false, -1, col)
					.getPreferredSize().getWidth();
			for (int row = 0; row < rowCount; row++) {
				int preferedWidth = (int) myTable.getCellRenderer(row, col)
						.getTableCellRendererComponent(myTable, myTable.getValueAt(row, col), false, false, row, col)
						.getPreferredSize().getWidth();
				width = Math.max(width, preferedWidth);
			}
			header.setResizingColumn(column); // 此行很重要
			column.setWidth(width + myTable.getIntercellSpacing().width);
		}
	}
}
