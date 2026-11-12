
package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

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

public class ChiTietPhieuNhap_GUI extends JFrame {

	private static final long serialVersionUID = 1L;

	// Thông tin phiếu nhập
	private JTextField txtMaPN;
	private JTextField txtNhaCungCap;
	private JTextField txtNhanVien;
	private JTextField txtNgayNhap;

	// Thông tin sản phẩm
	private JComboBox<String> cboSanPham;
	private JComboBox<String> cboChiTietSanPham;
	private JTextField txtSoLuong;
	private JTextField txtDonGiaNhap;
	private JTextField txtThanhTien;

	// Nút chức năng
	private JButton btnThem;
	private JButton btnXoaRong;
	private JButton btnXoa;
	private JButton btnSua;

	// Bảng và tổng tiền
	private JTextField txtTongTien;
	private JTable table;
	private DefaultTableModel tableModel;

	public ChiTietPhieuNhap_GUI() {
		setTitle("Quản lý chi tiết phiếu nhập");
		setSize(1000, 650);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		buildUI();
	}

	private void buildUI() {

		// ================= PHẦN NORTH =================
		JPanel pnlNorth;
		add(pnlNorth = new JPanel(), BorderLayout.NORTH);

		pnlNorth.setPreferredSize(new Dimension(0, 125));
		pnlNorth.setBorder(
				BorderFactory.createTitledBorder("Thông tin phiếu nhập"));
		pnlNorth.setLayout(null);

		JLabel lblMaPN, lblNhaCungCap, lblNhanVien, lblNgayNhap;

		pnlNorth.add(lblMaPN = new JLabel("Mã phiếu nhập:"));
		pnlNorth.add(lblNhaCungCap = new JLabel("Nhà cung cấp:"));
		pnlNorth.add(lblNhanVien = new JLabel("Nhân viên lập:"));
		pnlNorth.add(lblNgayNhap = new JLabel("Ngày nhập:"));

		pnlNorth.add(txtMaPN = new JTextField());
		pnlNorth.add(txtNhaCungCap = new JTextField());
		pnlNorth.add(txtNhanVien = new JTextField());
		pnlNorth.add(txtNgayNhap = new JTextField());

		txtMaPN.setEditable(false);
		txtNhaCungCap.setEditable(false);
		txtNhanVien.setEditable(false);
		txtNgayNhap.setEditable(false);

		int w1 = 120, h = 25;

		lblMaPN.setBounds(20, 25, w1, h);
		txtMaPN.setBounds(145, 25, 230, h);

		lblNhaCungCap.setBounds(480, 25, w1, h);
		txtNhaCungCap.setBounds(600, 25, 280, h);

		lblNhanVien.setBounds(20, 65, w1, h);
		txtNhanVien.setBounds(145, 65, 230, h);

		lblNgayNhap.setBounds(480, 65, w1, h);
		txtNgayNhap.setBounds(600, 65, 200, h);


		// ================= PHẦN CENTER =================
		JPanel pnlCenter;
		add(pnlCenter = new JPanel(new BorderLayout()), BorderLayout.CENTER);

		// Panel thông tin sản phẩm
		JPanel pnlThongTin;
		pnlCenter.add(pnlThongTin = new JPanel(new GridLayout(3, 4, 10, 12)),
				BorderLayout.CENTER);

		pnlThongTin.setBorder(
				BorderFactory.createTitledBorder("Thông tin sản phẩm nhập"));

		JLabel lblSanPham, lblChiTietSP;
		JLabel lblSoLuong, lblDonGiaNhap;
		JLabel lblThanhTien;

		// Hàng 1
		pnlThongTin.add(lblSanPham = new JLabel("Sản phẩm:"));

		pnlThongTin.add(cboSanPham = new JComboBox<String>());

		pnlThongTin.add(lblChiTietSP = new JLabel("Chi tiết sản phẩm:"));

		pnlThongTin.add(cboChiTietSanPham = new JComboBox<String>());

		// Hàng 2
		pnlThongTin.add(lblSoLuong = new JLabel("Số lượng:"));

		pnlThongTin.add(txtSoLuong = new JTextField());

		pnlThongTin.add(lblDonGiaNhap = new JLabel("Đơn giá nhập:"));

		pnlThongTin.add(txtDonGiaNhap = new JTextField());

		// Hàng 3
		pnlThongTin.add(lblThanhTien = new JLabel("Thành tiền:"));

		pnlThongTin.add(txtThanhTien = new JTextField());
		txtThanhTien.setEditable(false);

		pnlThongTin.add(new JLabel(""));
		pnlThongTin.add(new JLabel(""));

		cboSanPham.setPreferredSize(new Dimension(200, 25));
		cboChiTietSanPham.setPreferredSize(new Dimension(200, 25));

		// Panel nút chức năng
		JPanel pnlButton = new JPanel();

		pnlButton.add(btnThem = new JButton("Thêm"));
		pnlButton.add(btnXoaRong = new JButton("Làm rỗng"));
		pnlButton.add(btnSua = new JButton("Sửa"));
		pnlButton.add(btnXoa = new JButton("Xóa"));

		pnlCenter.add(pnlButton, BorderLayout.SOUTH);


		// ================= PHẦN SOUTH =================
		JPanel pnlSouth;
		add(pnlSouth = new JPanel(new BorderLayout()), BorderLayout.SOUTH);

		String[] headers = {
				"Mã CTSP",
				"Tên sản phẩm",
				"Kích cỡ",
				"Màu sắc",
				"Số lượng",
				"Đơn giá nhập",
				"Thành tiền"
		};

		tableModel = new DefaultTableModel(headers, 0);

		JScrollPane scroll;
		pnlSouth.add(scroll = new JScrollPane(
				table = new JTable(tableModel),
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);

		scroll.setBorder(
				BorderFactory.createTitledBorder("Danh sách chi tiết phiếu nhập"));

		table.setRowHeight(22);

		// Tổng tiền
		JPanel pnlTongTien = new JPanel();

		pnlTongTien.add(new JLabel("Tổng tiền phiếu nhập:"));

		pnlTongTien.add(txtTongTien = new JTextField(15));
		txtTongTien.setEditable(false);

		pnlSouth.add(pnlTongTien, BorderLayout.SOUTH);

		pnlSouth.setPreferredSize(new Dimension(0, 300));
	}

	public static void main(String[] args) {
		new ChiTietPhieuNhap_GUI().setVisible(true);
	}
}
