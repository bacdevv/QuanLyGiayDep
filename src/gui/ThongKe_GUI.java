package gui;
import dao.ThongKe_DAO;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
public class ThongKe_GUI extends JPanel implements ActionListener {
	private JTextField txtTuNgay, txtDenNgay;
	private JComboBox<String> cboThongKe;
	private JButton bttThongKe, bttLamMoi;
	private JLabel lblTong;
	private JTable tableThongKe;
	private DefaultTableModel modelThongKe;
	private ThongKe_DAO tk_dao = new ThongKe_DAO();
	public ThongKe_GUI() {
		setLayout(new BorderLayout());
		JPanel pnl = new JPanel();
		pnl.add(new JLabel("Từ ngày (yyyy-MM-dd):"));
		pnl.add(txtTuNgay = new JTextField(LocalDate.now().withDayOfMonth(1).toString(), 10));
		pnl.add(new JLabel("Đến ngày:"));
		pnl.add(txtDenNgay = new JTextField(LocalDate.now().toString(), 10));
		pnl.add(cboThongKe = new JComboBox<String>(new String[]{
			"Doanh thu theo ngày", "Sản phẩm bán chạy", "Tồn kho hiện tại"
		}
		));
		pnl.add(bttThongKe = new JButton("Thống kê"));
		pnl.add(bttLamMoi = new JButton("Làm mới"));
		add(pnl, BorderLayout.NORTH);
		tableThongKe = new JTable();
		JScrollPane scroll = new JScrollPane(tableThongKe);
		scroll.setColumnHeaderView(tableThongKe.getTableHeader());
		add(scroll);
		add(lblTong = new JLabel("Chọn loại thống kê và bấm Thống kê."), BorderLayout.SOUTH);
		bttThongKe.addActionListener(this);
		bttLamMoi.addActionListener(this);
		cboThongKe.addActionListener(this);
	}
	@Override public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cboThongKe) {
			boolean locNgay = cboThongKe.getSelectedIndex() != 2;
			txtTuNgay.setEnabled(locNgay);
			txtDenNgay.setEnabled(locNgay);
			tableThongKe.setModel(new DefaultTableModel());
			lblTong.setText("Bấm Thống kê để tải dữ liệu mới.");
			return;
		}
		if (e.getSource() == bttLamMoi) {
			txtTuNgay.setText(LocalDate.now().withDayOfMonth(1).toString());
			txtDenNgay.setText(LocalDate.now().toString());
		}
		try {
			ArrayList<Object[]> dsThongKe;
			String[] colHeader;
			int loai = cboThongKe.getSelectedIndex();
			if (loai == 2) {
				colHeader = new String[]{
					"Mã biến thể", "Tên sản phẩm", "Size", "Màu", "Tồn hiện tại"
				}
				;
				dsThongKe = tk_dao.thongKeTonKho();
			}
			else {
				LocalDate tu = LocalDate.parse(txtTuNgay.getText().trim()), den = LocalDate.parse(txtDenNgay.getText().trim());
				if (loai == 0) {
					colHeader = new String[]{
						"Ngày", "Số hóa đơn", "Doanh thu (VND)"
					}
					;
					dsThongKe = tk_dao.thongKeDoanhThu(tu, den);
				}
				else {
					colHeader = new String[]{
						"Mã SP", "Tên sản phẩm", "Số đôi đã bán", "Doanh thu (VND)"
					}
					;
					dsThongKe = tk_dao.thongKeBanChay(tu, den);
				}
			}
			modelThongKe = new DefaultTableModel(colHeader, 0) {
				@Override public boolean isCellEditable(int row, int column) {
					return false;
				}
			}
			;
			BigDecimal tong = BigDecimal.ZERO;
			for (Object[] dong : dsThongKe) {
				modelThongKe.addRow(dong);
				if (loai != 2) tong = tong.add((BigDecimal) dong[dong.length - 1]);
			}
			tableThongKe.setModel(modelThongKe);
			tableThongKe.setAutoCreateRowSorter(true);
			lblTong.setText(loai == 2 ? "Tồn hiện tại, không lọc theo ngày. " + dsThongKe.size() + " biến thể."
			: "Tổng doanh thu: " + tong.toPlainString() + " VND | " + dsThongKe.size() + " dòng.");
		}
		catch (DateTimeParseException ex) {
			JOptionPane.showMessageDialog(this, "Nhập ngày hợp lệ theo yyyy-MM-dd.");
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Không thống kê được", JOptionPane.ERROR_MESSAGE);
		}
	}
}
