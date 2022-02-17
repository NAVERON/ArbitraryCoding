package com.eron.snips.customcomponent;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import javax.swing.text.TableView.TableCell;

/**
 * 使用swing自定义表格编辑功能
 */

//****************************************************定义显示表格的Frame
public class CellEditorFrame {
	public static void main(String[] sre) {

		MyTableModel m_TableModel = new MyTableModel();
		JTable m_TableDemo = new JTable(m_TableModel);

		ComBoxEditor m_ComboBoxEditor = new ComBoxEditor();
		MyPicEditor m_PicEditor = new MyPicEditor();

		JFrame m_MyFrame = new JFrame("我的CellEditor，图片位置请双击 选择图片");

		m_TableDemo.getColumnModel().getColumn(0).setCellEditor(m_ComboBoxEditor);
		m_TableDemo.getColumnModel().getColumn(1).setCellEditor(m_PicEditor);
		m_TableDemo.setRowHeight(200);

		JScrollPane m_JScroolPanel = new JScrollPane(m_TableDemo);
		m_JScroolPanel.setViewportView(m_TableDemo);
		m_JScroolPanel.setSize(480, 200);

		m_MyFrame.add(m_JScroolPanel);
		m_MyFrame.setBounds(200, 200, 500, 500);
		m_MyFrame.setDefaultCloseOperation(m_MyFrame.EXIT_ON_CLOSE);
		m_MyFrame.setVisible(true);

	}
}

/**
 * @author 小悠 [][][][][][][][][][][][][][][][][][][][][]表格编辑器
 *         本实例是测试CellEditor的用法 结论 在 Mode中设置 表格为允许编辑的状态，并且要设置 编辑器后才能响应的 编辑器的响应顺序是
 *         1、构造编辑器[调用构造函数] 2、调用 getTableCellEditorComponent（） 返回一个组件，这个组件是 就是编辑器
 *         的状态 例如是一个按钮，下拉框等 3、在编辑器中 响应 对应组件的消息（例如文本框、按钮作为编辑器的时候），
 *         在组件的Action作出自己的处理 4、调用函数fireEditingStopped();告知 编辑完成，例如 Textfield是
 *         Enter时调用的 5、返回编辑器的编辑结果 6、剩下的就 只需要 有 Mode完成了
 */
class MyTableModel extends AbstractTableModel {
	String head[] = { "下拉列表", "图片" };
	// 表格每一列的数据维护类型
	Class[] TypeArr = { String.class, Icon.class };
	// 表格的数据
	Object[][] data = { { "选项A", new ImageIcon("C://I//pic1.png") }, { "选项B", new ImageIcon("C://I//pic2.png") } };

	@Override
	public int getRowCount() {
		return 2;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}

	// 通知Mode每一列的数据类型
	@Override
	public Class getColumnClass(int columnIndex) {
		// TODO Auto-generated method stub
		return TypeArr[columnIndex];// super.getColumnClass(columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	// 表格的列标题
	@Override
	public String getColumnName(int column) {
		return head[column];
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		data[rowIndex][columnIndex] = aValue;
		// 只需要更新对应的位置
		this.fireTableCellUpdated(rowIndex, columnIndex);
	}
}

///////////////////////
//************************************************************************自定义的图片编辑器
//实现 TableCellEditor,ActionListener 接口
class MyPicEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
	/*
	 * ReadMe:当我们点击表格Cell的时候，表格检测点击的消息，检测Cell是否允许编辑， 如果允许编辑 则去调用 表格编辑器 来获取图片，获取完后将图片
	 * 送达给 TableModel 结束编辑器的编辑状态，表格刷新显示 对应的图片
	 */
	// 用于获取图片的变量
	private Icon m_IconPic;
	// 作为 编辑器 ，当我们点击的时候进行图片的选择
	private JButton m_IconButton;
	// 点击按钮的时候 进行文件选择的 Filechooser
	private JFileChooser m_PicFileChooser;
	// 设置当我们 点击2次的时候 编辑器 才起作用
	private static final int clickCountToStart = 2;

	// 构造函数，初始化一些信息
	public MyPicEditor() {
		m_IconButton = new JButton();
		m_IconButton.addActionListener(this);
		m_PicFileChooser = new JFileChooser();
	}

	// 检测鼠标的点击次数，判断编辑器是否起作用
	public boolean isCellEditable(EventObject anEvent) {
		// 如果事件 是 鼠标的事件，大于设定的次数就true,否则false
		if (anEvent instanceof MouseEvent) {
			System.out.println("检测到了鼠标的事件");
			return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
		}
		return false;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		System.out.println("表格Cell获取将要显示的编辑器组件，返回值编辑器包含的控件");
		// 先前的表格Cell的 数据 先保存下来，用于初始化编辑器包含的控件的数据
		m_IconPic = (Icon) value;
		// 返回作为编辑器的组件，这里是一个按钮
		return m_IconButton;
	}

	// 响应编辑器包含的组件的事件
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("编辑器组件事件响应");
		if (e.getSource() == m_IconButton) {
			// 初始化编辑器，显示原始的图片
			m_IconButton.setIcon(m_IconPic);
			// 显示文件选择器，用于选择图片
			m_PicFileChooser.showOpenDialog(m_IconButton);

			if (m_PicFileChooser.getSelectedFile() != null) {
				// 如果选择了新的图片将按钮设置为新的图标
				m_IconPic = new ImageIcon(m_PicFileChooser.getSelectedFile().getAbsolutePath());
			}
			// 数据获取完成，终止编辑器，将数据送达 调用者
			this.fireEditingStopped();
		}

	}

	// 将数据送达调用者，关闭编辑器，表格正常显示
	@Override
	public Object getCellEditorValue() {
		System.out.println("返回结果");
		return m_IconPic;
	}
}

//****************************************************************再写一个Commbox的
class ComBoxEditor extends AbstractCellEditor implements TableCellEditor {
	/*
	 * ReadMe: 这个 ComboBox下拉列表的编辑器 使用一个 JLable 和一个 JComboBox组合的
	 * 将JComboBox放到JLable里，所以只需要将 JLable 作为编辑器组件返回就行了
	 */
	private JComboBox m_ComboBox;
	// 获取 下拉列表的 选择的值
	private String m_SelStr;
	private JLabel m_OutLable;
	// 这里我们设置 鼠标点击 1 次就响应编辑器
	private static final int clickCountToStart = 1;

	// 初始化编辑器包含的控件信息
	public ComBoxEditor() {
		m_ComboBox = new JComboBox();
		m_ComboBox.addItem("选项A");
		m_ComboBox.addItem("选项B");
		m_ComboBox.addItem("选项C");

		m_ComboBox.setSize(100, 30);

		m_OutLable = new JLabel();
		m_OutLable.setLayout(null);
		m_OutLable.setBounds(0, 0, 120, 40);
		m_OutLable.add(m_ComboBox);
		m_ComboBox.setLocation(50, 50);

		// 响应下拉列表的事件
		m_ComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println("下拉列表的选中事件");
				if (e.getStateChange() == e.SELECTED) {
					// 获取选择的值
					m_SelStr = (String) m_ComboBox.getSelectedItem();
					// 结束选择
					fireEditingStopped();
				}
			}
		});
	}

	// 检测鼠标的点击次数，判断编辑器是否起作用
	public boolean isCellEditable(EventObject anEvent) {
		// 如果事件 是 鼠标的事件，大于设定的次数就true,否则false
		if (anEvent instanceof MouseEvent) {
			System.out.println("检测鼠标的点击次数，设置编辑器是否响应");
			return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
		}
		return false;
	}

	// 获取编辑器的组件
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		System.out.println("获取编辑器的组件");
		// 将下拉列表设置为之前的选项
		m_SelStr = (String) value;
		m_ComboBox.setSelectedItem(m_SelStr);
		// 返回值为 null的时候 是空的编辑器，就是说 = =不允许 编辑的
		return m_OutLable;
	}

	// 获取编辑器的 值
	@Override
	public Object getCellEditorValue() {
		return m_SelStr;
	}
}
