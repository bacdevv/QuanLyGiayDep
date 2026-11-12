
package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class NhaCungCap_GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField txtMaNCC;
	private JTextField txtTenNCC;
	private JTextField txtSoDienThoai;
	private JCheckBox chkHoatDong;
	private JButton btnThem;
	private JButton btnXoaRong;
	private JButton btnTimTheoMa;
	private JButton btnTimTheoTen;
	private JButton btnSua;
	private JButton btnLuu;
	private JButton btnLoc;
	private JComboBox<String> cboTrangThai;
	private JTable table;
	private DefaultTableModel tableModel;

	public NhaCungCap_GUI() {
		setTitle("Quản lý nhà cung cấp");
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
		pnlNorth.setBorder(BorderFactory.createTitledBorder("Thông tin nhà cung cấp"));
		pnlNorth.setLayout(null);

		JLabel lblMaNCC, lblTenNCC, lblSoDienThoai;
		pnlNorth.add(lblMaNCC = new JLabel("Mã NCC: "));
		pnlNorth.add(lblTenNCC = new JLabel("Tên nhà cung cấp: "));
		pnlNorth.add(lblSoDienThoai = new JLabel("Số điện thoại: "));

		pnlNorth.add(txtMaNCC = new JTextField());
		pnlNorth.add(txtTenNCC = new JTextField());
		pnlNorth.add(txtSoDienThoai = new JTextField());
		pnlNorth.add(chkHoatDong = new JCheckBox("Đang hoạt động"));

		int w1 = 120, w2 = 300, h = 20;

		lblMaNCC.setBounds(20, 25, w1, h);
		txtMaNCC.setBounds(150, 25, 200, h);

		lblTenNCC.setBounds(20, 55, w1, h);
		txtTenNCC.setBounds(150, 55, w2, h);

		lblSoDienThoai.setBounds(20, 85, w1, h);
		txtSoDienThoai.setBounds(150, 85, 200, h);

		chkHoatDong.setBounds(450, 85, 150, h);

		// Phần Center
		JPanel pnlCenter;
		add(pnlCenter = new JPanel(new BorderLayout()), BorderLayout.CENTER);

		// Hàng 1: Các nút chức năng
		JPanel pnlButton = new JPanel();

		pnlButton.add(btnThem = new JButton("Thêm"));
		pnlButton.add(btnXoaRong = new JButton("Làm rỗng"));
		pnlButton.add(btnTimTheoMa = new JButton("Tìm theo mã NCC"));
		pnlButton.add(btnTimTheoTen = new JButton("Tìm theo tên NCC"));
		pnlButton.add(btnSua = new JButton("Sửa"));
		pnlButton.add(btnLuu = new JButton("In danh sách nhà cung cấp"));

		// Hàng 2: Lọc theo trạng thái
		JPanel pnlLoc = new JPanel();

		pnlLoc.add(new JLabel("Trạng thái: "));

		pnlLoc.add(cboTrangThai = new JComboBox<String>(
				new String[] { "Đang hoạt động", "Ngưng hoạt động" }));

		cboTrangThai.setPreferredSize(new Dimension(130, 25));

		pnlLoc.add(btnLoc = new JButton("Lọc theo trạng thái"));

		pnlCenter.add(pnlButton, BorderLayout.NORTH);
		pnlCenter.add(pnlLoc, BorderLayout.CENTER);

		// Phần South
		JScrollPane scroll;
		String[] headers = "Mã NCC;Tên nhà cung cấp;Số điện thoại;Hoạt động".split(";");

		tableModel = new DefaultTableModel(headers, 0);

		add(scroll = new JScrollPane(table = new JTable(tableModel),
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.SOUTH);

		scroll.setBorder(BorderFactory.createTitledBorder("Danh sách nhà cung cấp"));
		table.setRowHeight(20);
		scroll.setPreferredSize(new Dimension(0, 350));
	}

	public static void main(String[] args) {
		new NhaCungCap_GUI().setVisible(true);
	}
}
