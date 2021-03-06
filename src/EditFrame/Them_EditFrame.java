package EditFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import DataFrame.Abstract_DataFrame;
import TableModel.DatabaseTable;

public class Them_EditFrame extends Abstract_EditFrame {

	public Them_EditFrame(DatabaseTable vls, Abstract_DataFrame f) {
		super(vls,f);
		// TODO Auto-generated constructor stub
		mainFrame.setTitle("Thêm dữ liệu");
		jButton.setText("Add");
		for (int i = 0; i < vls.getColumnCount(); i++) 
			jLabels[i].setText( "Thêm " + vls.getColumnName(i).toLowerCase() + " : ");
	}

	@Override
	void setAction() {
		// TODO Auto-generated method stub
		jButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isError()) {
					String[] data = new String [vls.getColumnCount()];
					/* Lấy dữ liệu từ text */
					for (int i = 0; i < vls.getColumnCount(); i++) {
						if (GetInfoTextArea[i].getText().length() == 0) 
							data[i] = null;
						else data[i] = "'"+GetInfoTextArea[i].getText()+"'";	
					}
					/* Thêm vào database */
					if (vls.insertSingleRow(data)) 
						f.resetTableAndInfo();			
				} 
		
				/* Nếu không th�?a mãn đi�?u kiện, hiện thông báo yêu cầu nhập lại */
				else {
					JOptionPane.showMessageDialog(null, "Thêm dữ liệu sai ở cột "+mess , "", 
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
	}
}
