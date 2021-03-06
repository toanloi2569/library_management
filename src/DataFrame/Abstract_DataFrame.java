package DataFrame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Database.Database;
import TableModel.DatabaseTable;
import TableModel.TemporaryTable;

/*
 *	Frame có 2 phần chính là DisplayDataPanel và InformationPanel
 * 	+ DisplayDataPanel hiển thị database, gồm 1 search_DataPanel và 1 display_DataPanel
 * 		- search_DataPanel gồm : 1 button hiển thị toàn bộ dữ liệu và 1 button tìm kiếm
 * 		- display_DataPanel gồm : 1 table hiển thị data, button addRow
 * 	+ Imformation hiển thị thông tin chi tiết của từng Row ch�?n
 */
public abstract class Abstract_DataFrame extends JPanel{
	final int mainFrameWidth = 1300;
	final int mainFrameHeight = 600;
	final int DataPanelWidth = (int) (0.7 * mainFrameWidth);
	TemporaryTable tvls;
	DatabaseTable vls;
	public static int getRow = -1;
	public static int[] rows;
	JLabel SearchLabel;
	JTable mainTable_Data;

	/* Thành phần DataPanel */
	JPanel DataPanel;
	JPanel SearchPanel_Data, DisplayPanel_Data;
	JPanel DisplayAllPanel_Data;
	JPanel AddPanel_Data;
	JTextField SearchText_Data;
	JButton DisplayAllButton_Data, SearchButton_Data, AddButton_Data;
	JScrollPane TableScrollPanel_Data;

	/* Thành phần InformationPanel */
	JPanel InformationPanel;
	JTextArea TextArea_Information;
	JPanel buttonPanel;
	JButton UpdateButton_Information, DeleteButton_Information;

	/* 
	 * Constructor, 
	 * Tạo 1 editFrame Gồm tableName : hiển thị table đó trong phần displayData 
	 * vls : đối tượng chứa dữ liệu table 
	 * searchName : đặt giá trí cho searchName, hiển thị cho phù hợp với bảng đang xét 
	 * prepareGUI : �?ặt các đối tượng một cách tương đối vào frame 
	 * setupTable : Lấy dữ liệu từ vls đặt vào table
	 */
	public Abstract_DataFrame(DatabaseTable vls) {
		this.vls = vls;
		
		/* �?ặt các thành phần 1 cách tương đối vào frame */
		prepareGUI();

		/* Lấy dữ liệu từ database đặt vào table, hiển thị table */
		setupTable();
		
		/* Cài đặt hiển thị thông tin trên bảng information */
		setupText_Information();
		
		/* Cài đặt các action */
		setupAddButtonAction();
		setupUpdateButtonAction();
		setupDeleteButtonAction();
		setupClickTable();
		setupSearchButtonAction();
		setupSearchTextAction();
		setupDoubleClickAction();
		setupDisplayAllButtonAction();
	}

	/*
	 * �?ặt thành phần 1 cách tương đối vào frame Hiển thị frame addDataPanel :
	 * Thêm các thành phần vào DataPanel addInformation : Thêm các thành phần
	 * vào InformationPanel Thêm 2 panel vào frame
	 */
	public void prepareGUI() {
		/* Cài đặt hiển thị frame */
		this.setSize(mainFrameWidth, mainFrameHeight);
		this.setLocation(20, 50);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		/* Thêm thành phần DataPanel vào frame */
		addDataPanel();

		/* Thành phần InformationPanel */
		addInformationPanel();

		/* Thêm 2 Panel vào frame */
		this.add(DataPanel);
		this.add(InformationPanel);
		this.setVisible(true);
	}

	/*
	 * Thêm vị trí tương đối của DataPanel Cài đặt DataPanel addSearchPanel :
	 * Thêm searchPanel vào DataPanel addDisplayPanel : Thêm displayPanel vào
	 * DataPanel
	 */
	private void addDataPanel() {
		/* Cài đặt DataPanel */
		DataPanel = new JPanel();
		DataPanel.setLayout(new BoxLayout(DataPanel, BoxLayout.Y_AXIS));
		DataPanel.setPreferredSize(new Dimension(DataPanelWidth, mainFrameHeight));
		DataPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));

		/* Thêm searchPanel vào DataPanel */
		addSearchPanel_Data();
		DataPanel.add(SearchPanel_Data);

		/* Thêm displayPanel vào DataPanel */
		addDisplayPanel_Data();
		DataPanel.add(DisplayPanel_Data);
	}

	/*
	 * Thêm vị trí tương đối của DisplayPanel_Data Cài đặt hiển thị displayPanel
	 * (Hiển thị dữ liệu trong bảng) Gồm : 1 button hiển thị toàn bộ dữ liệu 1
	 * table hiển thị data 1 button để thêm dữ liệu
	 */
	private void addDisplayPanel_Data() {
		
		/* button hiển thị toàn bộ dữ liệu */
		DisplayAllButton_Data = new JButton("Hiển thị toàn bộ dữ liệu");
		DisplayAllButton_Data.setPreferredSize(new Dimension(150, 22));
		DisplayAllButton_Data.setMargin(new Insets(0, 0, 0, 0));
		DisplayAllButton_Data.setAlignmentX(Component.LEFT_ALIGNMENT);
		DisplayAllPanel_Data = new JPanel();
		DisplayAllPanel_Data.setLayout(new BorderLayout());
		DisplayAllPanel_Data.add(DisplayAllButton_Data,BorderLayout.WEST);
		DisplayAllPanel_Data.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		/* Cài đặt hiển thị displayPanel */
		DisplayPanel_Data = new JPanel();
		DisplayPanel_Data.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		DisplayPanel_Data.setLayout(new BoxLayout(DisplayPanel_Data, BoxLayout.Y_AXIS));
		DisplayPanel_Data.setAlignmentX(Component.LEFT_ALIGNMENT);

		/* hiển thị table */
		mainTable_Data = new JTable();
		mainTable_Data.setModel(vls);
		TableScrollPanel_Data = new JScrollPane(mainTable_Data);
		TableScrollPanel_Data.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
				"Dữ liệu", TitledBorder.CENTER, TitledBorder.TOP));
		TableScrollPanel_Data.setAlignmentX(Component.LEFT_ALIGNMENT);
		TableScrollPanel_Data.setPreferredSize(new Dimension(DataPanelWidth, mainFrameHeight - 60));

		/* button thêm dữ liệu */
		AddButton_Data = new JButton();
		AddPanel_Data = new JPanel(new FlowLayout(FlowLayout.CENTER));
		AddPanel_Data.add(AddButton_Data);
		AddPanel_Data.setAlignmentX(Component.LEFT_ALIGNMENT);

		/* add các com vào displayPanel */
		DisplayPanel_Data.add(DisplayAllPanel_Data);
		DisplayPanel_Data.add(TableScrollPanel_Data);
		DisplayPanel_Data.add(AddPanel_Data);
	}

	/*
	 * Thêm vị trí tương đối searchPanel 
	 * Gồm  1 button tìm kiếm, 1 thanh tìm kiếm 
	 */
	private void addSearchPanel_Data() {
		/* Cài đặt hiển thị searchPanel */
		SearchPanel_Data = new JPanel(new FlowLayout(FlowLayout.LEFT));
		SearchPanel_Data.setPreferredSize(new Dimension(DataPanelWidth, 30));
		SearchPanel_Data.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		SearchPanel_Data.setAlignmentX(Component.LEFT_ALIGNMENT);

		/* button tìm kiếm */
		SearchButton_Data = new JButton("Tìm kiếm");
		SearchButton_Data.setPreferredSize(new Dimension(100, 22));
		SearchButton_Data.setAlignmentX(Component.LEFT_ALIGNMENT);

		/* Thanh tìm kiếm */
		SearchText_Data = new JTextField();
		SearchText_Data.setPreferredSize(new Dimension(240, 22));
		
		/* add com vào searchPanel */
		SearchPanel_Data.add(new JLabel("Tìm kiếm : "));
		SearchPanel_Data.add(SearchText_Data);
		SearchPanel_Data.add(SearchButton_Data);	
	}

	/* Thêm vị trí tương đối của InformationPanel 
	 * 		Gồm : 	1 text area hiển thị thông tin row đang được ch�?n
	 * 				1 update button
	 * 				1 delete button
	 */
	private void addInformationPanel() {
		/* Cài đặt hiện thị InformationPanel */
		InformationPanel = new JPanel();
		InformationPanel.setLayout(new BoxLayout(InformationPanel, BoxLayout.Y_AXIS));
		InformationPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20),
				BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Thông tin chi tiết",
						TitledBorder.LEFT, TitledBorder.TOP)));
		InformationPanel.setPreferredSize(new Dimension(mainFrameWidth-DataPanelWidth-20,mainFrameHeight));

		/* Cài đặt text hiển thị thông tin, tự động xuống dòng và không bẻ chữ khi xuống dòng */
		TextArea_Information = new JTextArea();
		TextArea_Information.setLineWrap(true);
		TextArea_Information.setWrapStyleWord(true);
		TextArea_Information.setEditable(false);
		TextArea_Information.setBackground(null);
		JScrollPane jScrollPane = new JScrollPane(TextArea_Information);
		jScrollPane.setPreferredSize(new Dimension(180, 600));
		jScrollPane.setBorder(BorderFactory.createCompoundBorder(
				 BorderFactory.createEmptyBorder(5, 5, 5, 5),BorderFactory.createEtchedBorder()));
		
		/* Cài đặt 2 button sửa, xóa */
		UpdateButton_Information = new JButton();
		DeleteButton_Information = new JButton();

		/* Thêm 2 button vào cùng 1 dòng, sử dụng 1 JPanel */
		buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(UpdateButton_Information);
		buttonPanel.add(DeleteButton_Information);

		/* Thêm com vào InformationPanel */
		InformationPanel.add(jScrollPane);
		InformationPanel.add(buttonPanel);
	}

	/* Cài đặt hiển thị thông tin Row lên panel Information */
	public void setupText_Information() {
		/* Duyệt các cột của table để lấy tên các row, sau đó in vào text area */
		TextArea_Information.setText("");
		if (getRow != -1 && getRow < vls.getRowCount() ) {
			for (int i = 0; i < vls.getColumnCount(); i++) 
				if (vls.getValueAt(getRow, i) != null) 
					TextArea_Information.append(vls.getColumnName(i) +" : "+ vls.getValueAt(getRow, i)+"\n\n");	
				else TextArea_Information.append(vls.getColumnName(i) +" : "+"\n\n");
		} else 
			for (int i = 0; i < vls.getColumnCount(); i++)
				TextArea_Information.append(vls.getColumnName(i) + " : \n\n");
	}
	
	/* Reset lại trạng thái của bảng và panel Information*/
	public void resetTableAndInfo() {
		vls.fireTableDataChanged();
		setupText_Information();
	}
	
	/* Cài đặt sự kiện khi double click vào bảng */
	public void setupDoubleClickAction(){
		mainTable_Data.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable)e.getSource();
				Point point = e.getPoint();
				getRow = table.rowAtPoint(point);
				if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
					displayDuLieu();
				}
			}
		});
	}
	
	/* Khi ấn enter, sẽ tìm kiếm text đã nhập trong database */
	void setupSearchTextAction(){
		SearchText_Data.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayResultSearch();
			}
		});
	}
	
	/* Khi ấn tìm kiếm, sẽ tìm kiếm text đã nhập trong database */
	void setupSearchButtonAction(){
		SearchButton_Data.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				displayResultSearch();
			}
		});		
	}
	
	/* Hiển thị toàn bộ dữ liệu trong database */
	void setupDisplayAllButtonAction(){
		DisplayAllButton_Data.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vls.deleteAllValues();
				vls.display(vls.getAllData());
				vls.fireTableDataChanged();
			}
		});
	}
	
	/* Hiển thị giá trị tìm kiếm được trong bảng */
	void displayResultSearch(){
		/* Xóa tất cả các hiển thị trong bảng */
		vls.deleteAllValues();
		
		/* Tìm kiếm text trong database và hiển thị */
		String text = SearchText_Data.getText();
		ResultSet result = vls.searchOnDatabase(text);
		if (result != null) vls.display(result);
		vls.fireTableDataChanged();
	}
	
	/* Hành động khi double click vào table */
	abstract void displayDuLieu();
	
	/* Thay đổi độ rộng bảng cho phù hợp */
	abstract void setupTable();
	
	/* Thêm sự kiện khi ấn addButton */
	abstract void setupAddButtonAction();
	
	/* Thêm sự kiện khi ấn updateButton */
	abstract void setupUpdateButtonAction();
	
	/* Thêm sự kiện khi ấn deleteButotn */
	abstract void setupDeleteButtonAction();
	
	/* Thêm sự kiện khi nhấn vào 1 row trong table */
	abstract void setupClickTable();
}