package Frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import TableModel.TableValues;

public class Add_EditFrame extends Abstract_EditFrame {

	public Add_EditFrame(TableValues vls, Abstract_DataFrame f) {
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
				if (checkInfo()) {
					/* Nếu thỏa mãn điều kiện sẽ được hỏi có nhập vào database không */
					int click = JOptionPane.showConfirmDialog(null, "Chắc chắn nhập dữ liệu", "Xác nhận nhập dữ liệu",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					
					/* Nếu đồng ý nhập, lấy dữ liệu từ text, cho vào String data, rồi truyền vào query */
					if (click == JOptionPane.YES_OPTION) {
						String[] data = new String [vls.getColumnCount()];
						/* Lấy dữ liệu từ text */
						for (int i = 0; i < vls.getColumnCount(); i++) {
							if (GetInfoTextArea[i].getText().length() == 0) 
								data[i] = null;
							else data[i] = "'"+GetInfoTextArea[i].getText()+"'";	
						}
						
						if (vls.insertRow(data)) f.resetTableAndInfo();
					}
				} 
				
				/* Nếu không thỏa mãn điều kiện, hiện thông báo yêu cầu nhập lại */
				else {
					JOptionPane.showMessageDialog(null, "Thêm dữ liệu sai ở cột : "+mess , "", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
	}

}
