
package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class PhieuNhap_GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField txtMaPN;
	private JComboBox<String> cboNhaCungCap;
	private JTextField txtNhanVien;
	private JTextField txtNgayNhap;
	private JTextField txtTongTien;

	private JButton btnThem;
	private JButton btnXoaRong;
	private JButton btnXoa;
	private JButton btnSua;
	private JButton btnLuu;
	private JButton btnXemChiTiet;
	private JButton btnLoc;
	private JButton btnTimKiem;

	private JComboBox<String> cboLocNCC;
	private JComboBox<String> cboTimKiem;
	private JTextField txtTimKiem;

	private JTable table;
	private DefaultTableModel tableModel;

	public PhieuNhap_GUI() {
		setTitle("Quản lý phiếu nhập");
		setSize(900, 600);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		buildUI();
	}

	private void buildUI() {

		// Phần North
		JPanel pnlNorth;
		add(pnlNorth = new JPanel(), BorderLayout.NORTH);
		pnlNorth.setPreferredSize(new Dimension(0, 140));
		pnlNorth.setBorder(BorderFactory.createTitledBorder("Thông tin phiếu nhập"));
		pnlNorth.setLayout(null);

		JLabel lblMaPN, lblNhaCungCap, lblNhanVien, lblNgayNhap, lblTongTien;

		pnlNorth.add(lblMaPN = new JLabel("Mã phiếu nhập: "));
		pnlNorth.add(lblNhaCungCap = new JLabel("Nhà cung cấp: "));
		pnlNorth.add(lblNhanVien = new JLabel("Nhân viên lập: "));
		pnlNorth.add(lblNgayNhap = new JLabel("Ngày nhập: "));
		pnlNorth.add(lblTongTien = new JLabel("Tổng tiền: "));

		pnlNorth.add(txtMaPN = new JTextField());
		pnlNorth.add(cboNhaCungCap = new JComboBox<String>());
		pnlNorth.add(txtNhanVien = new JTextField());
		pnlNorth.add(txtNgayNhap = new JTextField());
		pnlNorth.add(txtTongTien = new JTextField());

		// Không cho chỉnh sửa nhân viên lập và tổng tiền
		txtNhanVien.setEditable(false);
		txtTongTien.setEditable(false);

		int w1 = 120, w2 = 250, h = 20;

		lblMaPN.setBounds(20, 25, w1, h);
		txtMaPN.setBounds(150, 25, 200, h);

		lblNhaCungCap.setBounds(450, 25, w1, h);
		cboNhaCungCap.setBounds(570, 25, w2, h);

		lblNhanVien.setBounds(20, 55, w1, h);
		txtNhanVien.setBounds(150, 55, 200, h);

		lblNgayNhap.setBounds(450, 55, w1, h);
		txtNgayNhap.setBounds(570, 55, 200, h);

		lblTongTien.setBounds(20, 85, w1, h);
		txtTongTien.setBounds(150, 85, 200, h);

		// Phần Center
		JPanel pnlCenter;
		add(pnlCenter = new JPanel(new BorderLayout()), BorderLayout.CENTER);

		// Hàng 1: Các nút chức năng
		JPanel pnlButton = new JPanel();

		pnlButton.add(btnThem = new JButton("Thêm"));
		pnlButton.add(btnXoaRong = new JButton("Làm rỗng"));
		pnlButton.add(btnXoa = new JButton("Xóa"));
		pnlButton.add(btnSua = new JButton("Sửa"));
		pnlButton.add(btnLuu = new JButton("in phiếu nhập"));
		pnlButton.add(btnXemChiTiet = new JButton("Xem chi tiết"));

		// Hàng 2: Tìm kiếm và lọc
		JPanel pnlTimKiem = new JPanel();

		pnlTimKiem.add(new JLabel("Tìm theo: "));

		pnlTimKiem.add(cboTimKiem = new JComboBox<String>(
				new String[] {
						"Mã phiếu nhập",
						"Mã nhân viên",
						"Tên nhân viên"
				}));

		cboTimKiem.setPreferredSize(new Dimension(150, 25));

		pnlTimKiem.add(txtTimKiem = new JTextField(12));

		pnlTimKiem.add(btnTimKiem = new JButton("Tìm kiếm"));

		pnlTimKiem.add(new JLabel("Nhà cung cấp: "));

		pnlTimKiem.add(cboLocNCC = new JComboBox<String>());
		cboLocNCC.setPreferredSize(new Dimension(130, 25));

		pnlTimKiem.add(btnLoc = new JButton("Lọc"));

		pnlCenter.add(pnlButton, BorderLayout.NORTH);
		pnlCenter.add(pnlTimKiem, BorderLayout.CENTER);

		// Phần South
		JScrollPane scroll;

		String[] headers = "Mã PN;Nhà cung cấp;Nhân viên;Ngày nhập;Tổng tiền".split(";");

		tableModel = new DefaultTableModel(headers, 0);

		add(scroll = new JScrollPane(table = new JTable(tableModel),
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.SOUTH);

		scroll.setBorder(BorderFactory.createTitledBorder("Danh sách phiếu nhập"));
		table.setRowHeight(20);
		scroll.setPreferredSize(new Dimension(0, 350));
	}

	public static void main(String[] args) {
		new PhieuNhap_GUI().setVisible(true);
	}
}
