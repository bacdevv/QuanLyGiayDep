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

public class ChiTietHoaDon_GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField txtMaHD;
	private JComboBox<String> cboChiTietSanPham;
	private JTextField txtSoLuong;
	private JTextField txtDonGia;
	private JTextField txtThanhTien;
	private JTextField txtTongTien;
	private JButton btnThem;
	private JButton btnXoaRong;
	private JButton btnXoa;
	private JButton btnSua;
	private JButton btnLuu;
	private JTable table;
	private DefaultTableModel tableModel;

	public ChiTietHoaDon_GUI() {
		setTitle("Chi tiết hóa đơn");
		setSize(900, 600);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		buildUI();
	}

	private void buildUI() {

		// Phần North
		JPanel pnlNorth;
		add(pnlNorth = new JPanel(), BorderLayout.NORTH);
		pnlNorth.setPreferredSize(new Dimension(0, 140));
		pnlNorth.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết hóa đơn"));
		pnlNorth.setLayout(null); // Absolute layout

		JLabel lblMaHD, lblSanPham, lblSoLuong, lblDonGia, lblThanhTien, lblTongTien;
		pnlNorth.add(lblMaHD = new JLabel("Mã hóa đơn: "));
		pnlNorth.add(lblSanPham = new JLabel("Sản phẩm: "));
		pnlNorth.add(lblSoLuong = new JLabel("Số lượng: "));
		pnlNorth.add(lblDonGia = new JLabel("Đơn giá: "));
		pnlNorth.add(lblThanhTien = new JLabel("Thành tiền: "));
		pnlNorth.add(lblTongTien = new JLabel("Tổng tiền hóa đơn: "));

		pnlNorth.add(txtMaHD = new JTextField());
		pnlNorth.add(cboChiTietSanPham = new JComboBox<String>());
		pnlNorth.add(txtSoLuong = new JTextField());
		pnlNorth.add(txtDonGia = new JTextField());
		pnlNorth.add(txtThanhTien = new JTextField());
		pnlNorth.add(txtTongTien = new JTextField());
		txtMaHD.setEditable(false);
		txtThanhTien.setEditable(false);
		txtTongTien.setEditable(false);

		int w1 = 120, w2 = 250, h = 20;
		lblMaHD.setBounds(20, 25, w1, h);
		txtMaHD.setBounds(150, 25, 200, h);
		lblSanPham.setBounds(450, 25, w1, h);
		cboChiTietSanPham.setBounds(570, 25, w2, h);

		lblSoLuong.setBounds(20, 55, w1, h);
		txtSoLuong.setBounds(150, 55, 200, h);
		lblDonGia.setBounds(450, 55, w1, h);
		txtDonGia.setBounds(570, 55, 200, h);

		lblThanhTien.setBounds(20, 85, w1, h);
		txtThanhTien.setBounds(150, 85, 200, h);
		lblTongTien.setBounds(450, 85, w1, h);
		txtTongTien.setBounds(570, 85, 200, h);

		// Phần Center
		JPanel pnlCenter;
		add(pnlCenter = new JPanel(), BorderLayout.CENTER);
		pnlCenter.add(btnThem = new JButton("Thêm"));
		pnlCenter.add(btnXoaRong = new JButton("Làm rỗng"));
		pnlCenter.add(btnXoa = new JButton("Xóa"));
		pnlCenter.add(btnSua = new JButton("Sửa"));
		pnlCenter.add(btnLuu = new JButton("Lưu"));

		// Phần South
		JScrollPane scroll;
		String[] headers = "Mã CTSP;Tên sản phẩm;Kích cỡ;Màu sắc;Số lượng;Đơn giá;Thành tiền".split(";");

		tableModel = new DefaultTableModel(headers, 0);
		add(scroll = new JScrollPane(table = new JTable(tableModel), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.SOUTH);
		scroll.setBorder(BorderFactory.createTitledBorder("Danh sách chi tiết hóa đơn"));
		table.setRowHeight(20);
		scroll.setPreferredSize(new Dimension(0, 350));
	}

	public static void main(String[] args) {
		new ChiTietHoaDon_GUI().setVisible(true);
	}
}